package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.ai.AiChatRequest;
import com.keycap.keycapdesign.dto.ai.AiChatResponse;
import com.keycap.keycapdesign.dto.ai.AiRecommendation;
import com.keycap.keycapdesign.entity.Conversation;
import com.keycap.keycapdesign.entity.Message;
import com.keycap.keycapdesign.entity.Product;
import com.keycap.keycapdesign.entity.Review;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.enums.AuthProvider;
import com.keycap.keycapdesign.enums.ConversationStatus;
import com.keycap.keycapdesign.enums.KeyProfile;
import com.keycap.keycapdesign.enums.LayoutType;
import com.keycap.keycapdesign.enums.MessageType;
import com.keycap.keycapdesign.enums.ProductStatus;
import com.keycap.keycapdesign.enums.ProductTheme;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.enums.UserStatus;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.ConversationRepository;
import com.keycap.keycapdesign.repository.MessageRepository;
import com.keycap.keycapdesign.repository.ProductRepository;
import com.keycap.keycapdesign.repository.ReviewRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import com.keycap.keycapdesign.util.JsonUtil;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AiChatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AiChatService.class);
    private static final int MAX_HISTORY_MESSAGES = 10;
    private static final int MAX_CANDIDATE_PRODUCTS = 12;
    private static final int MAX_FAQ_CHARS = 12000;
    private static final int MAX_LOCAL_REPLY_ITEMS = 3;
    private static final List<String> KNOWLEDGE_FILES = List.of("classpath:ai/faq.txt", "classpath:ai/data.md");
    private static final Pattern PRICE_PATTERN = Pattern.compile(
            "(\\d+(?:[\\.,]\\d+)?)\\s*(trieu|tr|m|k|nghin|ngan)?",
            Pattern.CASE_INSENSITIVE);
    private static final List<String> DOMAIN_KEYWORDS = List.of(
            "keycap", "keycaps", "keyboard", "ban phim", "phim co", "switch", "switches",
            "stab", "stabilizer", "pcb", "plate", "case", "layout", "profile", "key profile",
            "gmk", "cherry", "oem", "sa", "xda", "dsa", "mt3", "pbt", "abs", "hotswap",
            "socket", "gasket", "foam", "stem", "linear", "tactile", "clicky", "ansi", "iso",
            "60", "65", "75", "tkl", "full", "custom", "design", "thiet ke", "mau",
            "gia", "bao gia", "ngan sach", "budget", "dich vu", "tu van", "de xuat",
            "recommend", "gaming", "van phong", "go code", "typing");

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.endpoint:https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent}")
    private String endpointTemplate;

    @Value("${gemini.model:gemini-1.5-flash}")
    private String model;

    @Value("${gemini.temperature:0.4}")
    private double temperature;

    @Value("${gemini.max-output-tokens:2048}")
    private int maxOutputTokens;

    @Value("${gemini.thinking-budget:0}")
    private int thinkingBudget;

    @Value("${gemini.timeout-ms:10000}")
    private long timeoutMs;

    @Value("${ai.system.email:ai-assistant@keycap.local}")
    private String systemEmail;

    @Value("${ai.system.name:Keycap AI Assistant}")
    private String systemName;

    @Value("${ai.system.password:change-me}")
    private String systemPassword;

    @Value("${ai.response.max-recommendations:5}")
    private int defaultMaxRecommendations;

    @Value("${ai.provider.debug:false}")
    private boolean providerDebug;

    private final ConversationService conversationService;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResourceLoader resourceLoader;

    private RestTemplate restTemplate;

    public AiChatService(ConversationService conversationService,
            ConversationRepository conversationRepository,
            MessageRepository messageRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            ReviewRepository reviewRepository,
            PasswordEncoder passwordEncoder,
            ResourceLoader resourceLoader) {
        this.conversationService = conversationService;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = passwordEncoder;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofMillis(timeoutMs));
        requestFactory.setReadTimeout(Duration.ofMillis(timeoutMs));
        this.restTemplate = new RestTemplate(requestFactory);
    }

    @Transactional
    public AiChatResponse chat(AiChatRequest request, Long userId) {
        if (request == null || request.getMessage() == null || request.getMessage().isBlank()) {
            throw new BadRequestException("Message is required");
        }

        Conversation conversation = resolveConversation(request.getConversationId(), userId);
        if (conversation.getStatus() == ConversationStatus.CLOSED) {
            throw new BadRequestException("Conversation is closed");
        }

        User customer = getUser(userId);
        User aiUser = getOrCreateAiUser();
        saveMessage(conversation, customer, request.getMessage());

        List<Message> history = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversation.getId());
        List<Message> recent = tail(history, MAX_HISTORY_MESSAGES);

        boolean inScope = isInScope(request.getMessage())
                || recent.stream().anyMatch(message -> isInScope(message.getContent()));
        if (!inScope) {
            String refusal = "Xin lỗi, mình chỉ hỗ trợ tư vấn về keycap, keyboard, switch, layout, profile và dịch vụ custom liên quan.";
            saveMessage(conversation, aiUser, refusal);
            return new AiChatResponse(conversation.getId(), refusal, List.of(),
                    List.of("Bạn đang tìm keycap cho layout nào?"), hasProviderConfig());
        }

        ProductIntent intent = ProductIntent.from(request);
        List<ProductMatch> candidates = selectCandidateProducts(intent);
        List<AiRecommendation> recommendations = buildRecommendations(candidates, intent, request.getMaxRecommendations());
        List<String> followUpQuestions = buildFollowUpQuestions(intent, recommendations);

        boolean providerAvailable = hasProviderConfig();
        String providerError = null;
        String reply;
        if (providerAvailable) {
            try {
                String prompt = buildPrompt(request.getMessage(), recent, recommendations, intent, loadFaqContent());
                reply = callGemini(prompt);
            } catch (RestClientException | IllegalArgumentException | BadRequestException ex) {
                providerAvailable = false;
                providerError = providerError(ex);
                LOGGER.warn("Gemini provider failed. model={}, endpointTemplate={}, error={}",
                        model, endpointTemplate, providerError, ex);
                reply = buildLocalReply(intent, recommendations, followUpQuestions, true, providerError);
            }
        } else {
            providerError = "Missing Gemini configuration. Check GEMINI_API_KEY, GEMINI_MODEL, and GEMINI_ENDPOINT.";
            LOGGER.warn("Gemini provider disabled. {}", providerError);
            reply = buildLocalReply(intent, recommendations, followUpQuestions, false, providerError);
        }

        saveMessage(conversation, aiUser, reply);
        return new AiChatResponse(conversation.getId(), reply, recommendations, followUpQuestions, providerAvailable);
    }

    private Conversation resolveConversation(Long conversationId, Long userId) {
        if (conversationId != null) {
            conversationService.validateUserCanAccess(conversationId, userId);
            Conversation conversation = conversationService.getConversation(conversationId);
            User aiUser = getOrCreateAiUser();
            if (conversation.getStaff() != null
                    && !Objects.equals(conversation.getStaff().getId(), aiUser.getId())) {
                throw new BadRequestException("Conversation is assigned to staff. Open a new AI conversation.");
            }
            if (conversation.getStaff() == null) {
                conversation.setStaff(aiUser);
                conversationRepository.save(conversation);
            }
            return conversation;
        }

        User customer = getUser(userId);
        User aiUser = getOrCreateAiUser();
        Conversation conversation = new Conversation();
        conversation.setCustomer(customer);
        conversation.setStaff(aiUser);
        conversation.setStatus(ConversationStatus.OPEN);
        return conversationRepository.save(conversation);
    }

    private void saveMessage(Conversation conversation, User sender, String content) {
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(content);
        message.setMessageType(MessageType.TEXT);
        message.setIsRead(false);
        messageRepository.save(message);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private User getOrCreateAiUser() {
        return userRepository.findByEmail(systemEmail)
                .orElseGet(() -> {
                    User ai = new User();
                    ai.setEmail(systemEmail);
                    String rawPassword = systemPassword == null || systemPassword.isBlank()
                            ? systemEmail
                            : systemPassword;
                    ai.setPassword(passwordEncoder.encode(rawPassword));
                    ai.setFullName(systemName);
                    ai.setRole(Role.STAFF);
                    ai.setProvider(AuthProvider.LOCAL);
                    ai.setStatus(UserStatus.ACTIVE);
                    ai.setEmailVerified(true);
                    return userRepository.save(ai);
                });
    }

    private boolean isInScope(String text) {
        String normalized = normalize(text);
        for (String keyword : DOMAIN_KEYWORDS) {
            if (normalized.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private List<ProductMatch> selectCandidateProducts(ProductIntent intent) {
        List<Product> activeProducts = productRepository.findAll().stream()
                .filter(product -> product.getStatus() == ProductStatus.ACTIVE)
                .collect(Collectors.toList());

        List<ProductMatch> scored = activeProducts.stream()
                .map(product -> new ProductMatch(product, scoreProduct(product, intent)))
                .filter(match -> match.score() > 0 || intent.isBroadAdvice())
                .sorted(Comparator.comparing(ProductMatch::score).reversed()
                        .thenComparing(match -> inStock(match.product()) ? 0 : 1)
                        .thenComparing(match -> match.product().getCreatedAt(),
                                Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(MAX_CANDIDATE_PRODUCTS)
                .collect(Collectors.toList());

        if (!scored.isEmpty()) {
            return scored;
        }

        return activeProducts.stream()
                .map(product -> new ProductMatch(product, scoreProduct(product, intent)))
                .sorted(Comparator.comparing((ProductMatch match) -> inStock(match.product()) ? 0 : 1)
                        .thenComparing(match -> match.product().getCreatedAt(),
                                Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(MAX_CANDIDATE_PRODUCTS)
                .collect(Collectors.toList());
    }

    private int scoreProduct(Product product, ProductIntent intent) {
        int score = 0;
        String haystack = normalize(String.format("%s %s %s %s %s",
                safeText(product.getName()),
                safeText(product.getDescription()),
                product.getTheme() == null ? "" : product.getTheme().name().replace("_", " "),
                product.getLayoutType() == null ? "" : product.getLayoutType().name().replace("_", " "),
                product.getKeyProfile() == null ? "" : product.getKeyProfile().name().replace("_", " ")));

        for (String token : intent.tokens()) {
            if (haystack.contains(token)) {
                score += 2;
            }
        }
        if (product.getTheme() != null && intent.themes().contains(product.getTheme())) {
            score += 6;
        }
        if (product.getLayoutType() != null && intent.layouts().contains(product.getLayoutType())) {
            score += 6;
        }
        if (product.getKeyProfile() != null && intent.profiles().contains(product.getKeyProfile())) {
            score += 6;
        }
        if (matchesBudget(product, intent)) {
            score += 5;
        } else if (intent.hasBudget()) {
            score -= 3;
        }
        if (inStock(product)) {
            score += 2;
        }
        BigDecimal rating = averageRating(product);
        if (rating != null && rating.compareTo(BigDecimal.valueOf(4)) >= 0) {
            score += 2;
        }
        return score;
    }

    private boolean matchesBudget(Product product, ProductIntent intent) {
        if (!intent.hasBudget() || product.getPrice() == null) {
            return false;
        }
        if (intent.minBudget() != null && product.getPrice().compareTo(intent.minBudget()) < 0) {
            return false;
        }
        return intent.maxBudget() == null || product.getPrice().compareTo(intent.maxBudget()) <= 0;
    }

    private boolean inStock(Product product) {
        return product.getStockQuantity() != null && product.getStockQuantity() > 0;
    }

    private List<AiRecommendation> buildRecommendations(List<ProductMatch> matches, ProductIntent intent, Integer max) {
        int limit = max == null ? defaultMaxRecommendations : Math.max(1, Math.min(max, 10));
        return matches.stream()
                .limit(limit)
                .map(match -> toRecommendation(match.product(), intent))
                .collect(Collectors.toList());
    }

    private AiRecommendation toRecommendation(Product product, ProductIntent intent) {
        return new AiRecommendation(
                product.getId(),
                product.getName(),
                limitText(safeText(product.getDescription()), 220),
                product.getPrice(),
                product.getStockQuantity(),
                firstImage(product),
                product.getTheme() == null ? null : product.getTheme().name(),
                product.getLayoutType() == null ? null : product.getLayoutType().name(),
                product.getKeyProfile() == null ? null : product.getKeyProfile().name(),
                averageRating(product),
                buildReason(product, intent));
    }

    private String buildReason(Product product, ProductIntent intent) {
        List<String> reasons = new ArrayList<>();
        if (product.getTheme() != null && intent.themes().contains(product.getTheme())) {
            reasons.add("đúng theme " + product.getTheme().name());
        }
        if (product.getLayoutType() != null && intent.layouts().contains(product.getLayoutType())) {
            reasons.add("khớp layout " + product.getLayoutType().name());
        }
        if (product.getKeyProfile() != null && intent.profiles().contains(product.getKeyProfile())) {
            reasons.add("profile " + product.getKeyProfile().name());
        }
        if (matchesBudget(product, intent)) {
            reasons.add("nằm trong ngân sách");
        }
        if (inStock(product)) {
            reasons.add("còn hàng");
        }
        BigDecimal rating = averageRating(product);
        if (rating != null && rating.compareTo(BigDecimal.valueOf(4)) >= 0) {
            reasons.add("đánh giá tốt");
        }
        if (reasons.isEmpty()) {
            return "Phù hợp với nhu cầu tư vấn keycap/keyboard";
        }
        return "Phù hợp vì " + String.join(", ", reasons);
    }

    private List<String> buildFollowUpQuestions(ProductIntent intent, List<AiRecommendation> recommendations) {
        List<String> questions = new ArrayList<>();
        if (intent.layouts().isEmpty()) {
            questions.add("Bạn đang dùng layout nào: 60%, 65%, 75%, TKL hay full-size?");
        }
        if (intent.profiles().isEmpty()) {
            questions.add("Bạn thích profile nào: Cherry/OEM để gõ hằng ngày hay SA/XDA cao và retro hơn?");
        }
        if (!intent.hasBudget()) {
            questions.add("Ngân sách dự kiến của bạn là bao nhiêu?");
        }
        if (recommendations.isEmpty()) {
            questions.add("Bạn có thể mô tả thêm về màu sắc, theme hoặc switch đang dùng không?");
        }
        return questions.stream().limit(3).collect(Collectors.toList());
    }

    private String buildLocalReply(ProductIntent intent, List<AiRecommendation> recommendations,
            List<String> followUpQuestions, boolean providerFailed, String providerError) {
        StringBuilder builder = new StringBuilder();
        if (providerFailed) {
            if (providerDebug && providerError != null && !providerError.isBlank()) {
                builder.append("Debug provider: ").append(providerError).append("\n\n");
            }
            builder.append("Mình chưa kết nối được AI provider, nên đang tư vấn bằng bộ gợi ý nội bộ.\n\n");
        }

        if (intent.wantsServiceInfo() || intent.wantsCustomDesign()) {
            builder.append("Về dịch vụ: shop hỗ trợ tư vấn layout/profile/theme, gợi ý sản phẩm có sẵn, ")
                    .append("và luồng custom request nếu bạn muốn thiết kế riêng. ")
                    .append("Với custom keycap, bạn nên chuẩn bị layout, theme, reference image và ghi chú về màu sắc/chất liệu.\n\n");
        }

        if (intent.hasBudget()) {
            builder.append("Về ngân sách: mình sẽ ưu tiên sản phẩm");
            if (intent.minBudget() != null) {
                builder.append(" từ ").append(formatPrice(intent.minBudget())).append(" VND");
            }
            if (intent.maxBudget() != null) {
                builder.append(" đến ").append(formatPrice(intent.maxBudget())).append(" VND");
            }
            builder.append(".\n\n");
        }

        if (recommendations.isEmpty()) {
            builder.append("Hiện mình chưa thấy sản phẩm nào khớp rõ với yêu cầu. ")
                    .append("Bạn hãy cho mình thêm layout, profile, theme và ngân sách để lọc chính xác hơn.");
        } else {
            builder.append("Mình gợi ý các mẫu sau:\n");
            recommendations.stream().limit(MAX_LOCAL_REPLY_ITEMS).forEach(item -> builder
                    .append("- ID ").append(item.getProductId())
                    .append(" | ").append(item.getName())
                    .append(" | ").append(formatPrice(item.getPrice())).append(" VND")
                    .append(" | ").append(item.getReason())
                    .append("\n"));
        }

        if (!followUpQuestions.isEmpty()) {
            builder.append("\nĐể chốt gợi ý tốt hơn, mình cần thêm: ")
                    .append(String.join(" ", followUpQuestions));
        }
        return builder.toString();
    }

    private String providerError(Exception ex) {
        if (ex == null) {
            return "Unknown provider error";
        }
        String message = ex.getMessage();
        if (message == null || message.isBlank()) {
            return ex.getClass().getSimpleName();
        }
        return ex.getClass().getSimpleName() + ": " + limitText(message, 500);
    }

    private String buildPrompt(String userMessage, List<Message> recent, List<AiRecommendation> recommendations,
            ProductIntent intent, String faqContent) {
        StringBuilder builder = new StringBuilder();
        builder.append("Bạn là trợ lý AI của shop keycap/keyboard. ")
                .append("Chỉ trả lời trong phạm vi keycap, keyboard, switch, layout, key profile, giá, dịch vụ custom và đặt hàng. ")
                .append("Luôn trả lời bằng tiếng Việt có dấu, tự nhiên, thân thiện và đúng chính tả. ")
                .append("Không bịa giá ngoài dữ liệu sản phẩm/FAQ. Nếu thiếu thông tin thì hỏi lại 1-2 câu ngắn. ")
                .append("Có thể đề xuất combo keycap + switch + layout nếu hợp lý.\n\n");

        builder.append("Yêu cầu hiện tại:\n").append(userMessage).append("\n\n");
        builder.append("Thông tin đã suy luận:\n")
                .append("- Layout: ").append(intent.layouts()).append("\n")
                .append("- Profile: ").append(intent.profiles()).append("\n")
                .append("- Theme: ").append(intent.themes()).append("\n")
                .append("- Budget min: ").append(formatPrice(intent.minBudget())).append("\n")
                .append("- Budget max: ").append(formatPrice(intent.maxBudget())).append("\n\n");

        if (!recent.isEmpty()) {
            builder.append("Lịch sử gần đây:\n");
            for (Message message : recent) {
                String role = message.getSender() == null ? "USER" : message.getSender().getRole().name();
                builder.append("- ").append(role).append(": ")
                        .append(limitText(message.getContent(), 300)).append("\n");
            }
            builder.append("\n");
        } else {
            builder.append("Không có sản phẩm cụ thể nào trong database đang khớp rõ với yêu cầu. ")
                    .append("Vẫn phải tư vấn đầy đủ theo kiến thức keyboard/keycap: giải thích profile, layout, switch phù hợp, ")
                    .append("chất liệu/độ êm/cảm giác gõ, và nói rõ rằng cần kiểm tra kho hoặc staff hỗ trợ để chốt mã sản phẩm cụ thể.\n\n");
        }

        if (!recommendations.isEmpty()) {
            builder.append("Sản phẩm nên ưu tiên để gợi ý:\n");
            for (AiRecommendation item : recommendations) {
                builder.append("- ID ").append(item.getProductId())
                        .append(" | ").append(item.getName())
                        .append(" | price ").append(formatPrice(item.getPrice()))
                        .append(" | stock ").append(item.getStockQuantity())
                        .append(" | theme ").append(item.getTheme())
                        .append(" | layout ").append(item.getLayoutType())
                        .append(" | profile ").append(item.getKeyProfile())
                        .append(" | rating ").append(formatRating(item.getAverageRating()))
                        .append(" | reason ").append(item.getReason())
                        .append(" | desc ").append(limitText(safeText(item.getDescription()), 160))
                        .append("\n");
            }
            builder.append("\n");
        }

        if (faqContent != null && !faqContent.isBlank()) {
            builder.append("FAQ/Hướng dẫn dịch vụ:\n");
            builder.append(limitText(faqContent, MAX_FAQ_CHARS)).append("\n\n");
        }

        builder.append("Hãy trả lời trực tiếp câu hỏi của khách, sau đó đưa 2-4 gợi ý cụ thể nếu có sản phẩm phù hợp. ")
                .append("Nếu nhắc sản phẩm, ghi ID sản phẩm.");
        builder.append("\nCâu trả lời phải hoàn chỉnh, không dừng giữa câu. ")
                .append("Nên gồm: nhận xét nhu cầu, gợi ý profile/switch/layout, lưu ý về độ êm, và bước tiếp theo để chốt sản phẩm.");
        return builder.toString();
    }

    private String callGemini(String prompt) {
        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);
        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));
        Map<String, Object> body = new HashMap<>();
        body.put("contents", List.of(content));

        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", temperature);
        generationConfig.put("maxOutputTokens", maxOutputTokens);
        if (thinkingBudget >= 0) {
            generationConfig.put("thinkingConfig", Map.of("thinkingBudget", thinkingBudget));
        }
        body.put("generationConfig", generationConfig);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        String url = String.format(endpointTemplate, model) + "?key=" + apiKey;
        Map response = restTemplate.postForObject(url, entity, Map.class);
        return extractText(response);
    }

    private String extractText(Map response) {
        if (response == null) {
            throw new BadRequestException("Gemini response is empty");
        }
        Object candidatesObj = response.get("candidates");
        if (!(candidatesObj instanceof List candidates) || candidates.isEmpty()) {
            throw new BadRequestException("Gemini response has no candidates");
        }
        Object firstCandidate = candidates.get(0);
        if (!(firstCandidate instanceof Map candidateMap)) {
            throw new BadRequestException("Gemini response is invalid");
        }
        Object finishReasonObj = candidateMap.get("finishReason");
        String finishReason = finishReasonObj == null ? "" : String.valueOf(finishReasonObj);
        Object contentObj = candidateMap.get("content");
        if (!(contentObj instanceof Map contentMap)) {
            throw new BadRequestException("Gemini response content is invalid");
        }
        Object partsObj = contentMap.get("parts");
        if (!(partsObj instanceof List parts) || parts.isEmpty()) {
            throw new BadRequestException("Gemini response parts are empty");
        }
        StringBuilder text = new StringBuilder();
        for (Object partObj : parts) {
            if (!(partObj instanceof Map partMap)) {
                continue;
            }
            Object textObj = partMap.get("text");
            if (textObj != null) {
                text.append(String.valueOf(textObj));
            }
        }
        if (text.isEmpty()) {
            throw new BadRequestException("Gemini response text is missing");
        }
        String result = text.toString().trim();
        if (result.isBlank()) {
            throw new BadRequestException("Gemini response text is empty");
        }
        if ("MAX_TOKENS".equalsIgnoreCase(finishReason)) {
            throw new BadRequestException("Gemini response was truncated by maxOutputTokens");
        }
        return result;
    }

    private boolean hasProviderConfig() {
        return apiKey != null && !apiKey.isBlank()
                && model != null && !model.isBlank()
                && endpointTemplate != null && !endpointTemplate.isBlank();
    }

    private String loadFaqContent() {
        StringBuilder builder = new StringBuilder();
        for (String location : KNOWLEDGE_FILES) {
            String content = loadKnowledgeFile(location);
            if (!content.isBlank()) {
                if (!builder.isEmpty()) {
                    builder.append("\n\n---\n\n");
                }
                builder.append("# ").append(location.replace("classpath:ai/", "")).append("\n\n")
                        .append(content);
            }
        }
        return builder.toString();
    }

    private String loadKnowledgeFile(String location) {
        try {
            Resource resource = resourceLoader.getResource(location);
            if (!resource.exists()) {
                return "";
            }
            try (InputStream inputStream = resource.getInputStream()) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (Exception ex) {
            LOGGER.warn("Could not load AI knowledge file: {}", location, ex);
            return "";
        }
    }

    private BigDecimal averageRating(Product product) {
        if (product == null || product.getId() == null) {
            return null;
        }
        List<Review> reviews = reviewRepository.findByProductId(product.getId());
        if (reviews == null || reviews.isEmpty()) {
            return null;
        }
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;
        for (Review review : reviews) {
            if (review.getRating() != null) {
                sum = sum.add(BigDecimal.valueOf(review.getRating()));
                count++;
            }
        }
        if (count == 0) {
            return null;
        }
        return sum.divide(BigDecimal.valueOf(count), 1, RoundingMode.HALF_UP);
    }

    private String firstImage(Product product) {
        try {
            List<String> images = JsonUtil.fromJson(product.getImagesJson());
            return images.isEmpty() ? null : images.get(0);
        } catch (BadRequestException ex) {
            return null;
        }
    }

    private List<Message> tail(List<Message> messages, int max) {
        if (messages == null || messages.isEmpty()) {
            return List.of();
        }
        int start = Math.max(0, messages.size() - max);
        return messages.subList(start, messages.size());
    }

    private String limitText(String text, int maxChars) {
        if (text == null) {
            return "";
        }
        if (text.length() <= maxChars) {
            return text;
        }
        return text.substring(0, maxChars) + "...";
    }

    private String formatPrice(BigDecimal price) {
        if (price == null) {
            return "n/a";
        }
        return price.setScale(0, RoundingMode.HALF_UP).toPlainString();
    }

    private String formatRating(BigDecimal rating) {
        return rating == null ? "n/a" : rating + "/5";
    }

    private String normalize(String text) {
        if (text == null) {
            return "";
        }
        String lower = text.toLowerCase(Locale.ROOT);
        String normalized = Normalizer.normalize(lower, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalized.replace("_", " ");
    }

    private String safeText(String text) {
        return text == null ? "" : text;
    }

    private record ProductMatch(Product product, int score) {
    }

    private record ProductIntent(String normalizedMessage, List<String> tokens, Set<ProductTheme> themes,
            Set<LayoutType> layouts, Set<KeyProfile> profiles, BigDecimal minBudget, BigDecimal maxBudget,
            boolean wantsPriceInfo, boolean wantsServiceInfo, boolean wantsCustomDesign) {

        static ProductIntent from(AiChatRequest request) {
            String normalized = normalizeStatic(request.getMessage());
            Set<ProductTheme> themes = detectThemes(normalized);
            Set<LayoutType> layouts = detectLayouts(normalized);
            Set<KeyProfile> profiles = detectProfiles(normalized);
            Budget budget = detectBudget(normalized, request.getMinBudget(), request.getMaxBudget());
            return new ProductIntent(
                    normalized,
                    extractKeywordsStatic(normalized),
                    themes,
                    layouts,
                    profiles,
                    budget.min(),
                    budget.max(),
                    containsAny(normalized, "gia", "bao gia", "price", "cost", "bao nhieu", "ngan sach", "budget"),
                    containsAny(normalized, "dich vu", "tu van", "bao hanh", "ship", "dat hang", "service"),
                    containsAny(normalized, "custom", "thiet ke", "design", "reference", "mockup"));
        }

        boolean hasBudget() {
            return minBudget != null || maxBudget != null;
        }

        boolean isBroadAdvice() {
            return tokens.isEmpty() || wantsPriceInfo || wantsServiceInfo || wantsCustomDesign
                    || (!themes.isEmpty() || !layouts.isEmpty() || !profiles.isEmpty());
        }

        private static boolean containsAny(String text, String... keywords) {
            for (String keyword : keywords) {
                if (text.contains(keyword)) {
                    return true;
                }
            }
            return false;
        }

        private static Set<ProductTheme> detectThemes(String text) {
            Set<ProductTheme> themes = new HashSet<>();
            if (containsAny(text, "colorful", "sac mau", "nhieu mau", "mau sac")) {
                themes.add(ProductTheme.COLORFUL);
            }
            if (containsAny(text, "rgb", "led")) {
                themes.add(ProductTheme.RGB);
            }
            if (containsAny(text, "minimal", "toi gian", "don gian")) {
                themes.add(ProductTheme.MINIMAL);
            }
            if (containsAny(text, "retro", "co dien", "vintage")) {
                themes.add(ProductTheme.RETRO);
            }
            if (containsAny(text, "pastel", "cute", "nhe nhang")) {
                themes.add(ProductTheme.PASTEL);
            }
            if (containsAny(text, "dark", "den", "toi", "black")) {
                themes.add(ProductTheme.DARK);
            }
            return themes;
        }

        private static Set<LayoutType> detectLayouts(String text) {
            Set<LayoutType> layouts = new HashSet<>();
            if (containsAny(text, "60%", "layout 60", "60")) {
                layouts.add(LayoutType.LAYOUT_60);
            }
            if (containsAny(text, "65%", "layout 65", "65")) {
                layouts.add(LayoutType.LAYOUT_65);
            }
            if (containsAny(text, "75%", "layout 75", "75")) {
                layouts.add(LayoutType.LAYOUT_75);
            }
            if (containsAny(text, "tkl", "tenkeyless", "87")) {
                layouts.add(LayoutType.TKL);
            }
            if (containsAny(text, "full size", "full-size", "fullsize", "100%", "104")) {
                layouts.add(LayoutType.FULL);
            }
            if (containsAny(text, "ansi")) {
                layouts.add(LayoutType.ANSI);
            }
            if (containsAny(text, "iso")) {
                layouts.add(LayoutType.ISO);
            }
            if (containsAny(text, "custom layout", "layout rieng")) {
                layouts.add(LayoutType.CUSTOM);
            }
            return layouts;
        }

        private static Set<KeyProfile> detectProfiles(String text) {
            Set<KeyProfile> profiles = new HashSet<>();
            if (containsAny(text, "cherry")) {
                profiles.add(KeyProfile.CHERRY);
            }
            if (containsAny(text, "oem")) {
                profiles.add(KeyProfile.OEM);
            }
            if (containsAny(text, " sa ", "profile sa")) {
                profiles.add(KeyProfile.SA);
            }
            if (containsAny(text, "dsa")) {
                profiles.add(KeyProfile.DSA);
            }
            if (containsAny(text, "xda")) {
                profiles.add(KeyProfile.XDA);
            }
            if (containsAny(text, "mt3")) {
                profiles.add(KeyProfile.MT3);
            }
            return profiles;
        }

        private static Budget detectBudget(String text, BigDecimal requestMin, BigDecimal requestMax) {
            List<BigDecimal> values = new ArrayList<>();
            Matcher matcher = PRICE_PATTERN.matcher(text);
            while (matcher.find()) {
                BigDecimal value = toVnd(matcher.group(1), matcher.group(2));
                if (value != null && value.compareTo(BigDecimal.valueOf(20000)) >= 0) {
                    values.add(value);
                }
            }

            BigDecimal min = requestMin;
            BigDecimal max = requestMax;
            if (values.size() >= 2) {
                BigDecimal first = values.get(0);
                BigDecimal second = values.get(1);
                min = min == null ? first.min(second) : min;
                max = max == null ? first.max(second) : max;
            } else if (values.size() == 1) {
                BigDecimal value = values.get(0);
                if (containsAny(text, "duoi", "toi da", "max", "under", "<")) {
                    max = max == null ? value : max;
                } else if (containsAny(text, "tren", "it nhat", "min", "from", ">")) {
                    min = min == null ? value : min;
                } else {
                    max = max == null ? value : max;
                }
            }

            if (min != null && max != null && min.compareTo(max) > 0) {
                BigDecimal temp = min;
                min = max;
                max = temp;
            }
            return new Budget(min, max);
        }

        private static BigDecimal toVnd(String rawNumber, String rawUnit) {
            if (rawNumber == null || rawNumber.isBlank()) {
                return null;
            }
            BigDecimal number = new BigDecimal(rawNumber.replace(",", "."));
            String unit = rawUnit == null ? "" : rawUnit.toLowerCase(Locale.ROOT);
            if (unit.equals("trieu") || unit.equals("tr") || unit.equals("m")) {
                return number.multiply(BigDecimal.valueOf(1_000_000));
            }
            if (unit.equals("k") || unit.equals("nghin") || unit.equals("ngan")) {
                return number.multiply(BigDecimal.valueOf(1_000));
            }
            if (number.compareTo(BigDecimal.valueOf(20)) < 0) {
                return number.multiply(BigDecimal.valueOf(1_000_000));
            }
            if (number.compareTo(BigDecimal.valueOf(10_000)) < 0) {
                return number.multiply(BigDecimal.valueOf(1_000));
            }
            return number;
        }

        private static List<String> extractKeywordsStatic(String normalized) {
            return Arrays.stream(normalized.split("[^a-z0-9]+"))
                    .filter(token -> token.length() >= 3)
                    .filter(token -> !DOMAIN_KEYWORDS.contains(token))
                    .distinct()
                    .collect(Collectors.toList());
        }

        private static String normalizeStatic(String text) {
            if (text == null) {
                return "";
            }
            String lower = text.toLowerCase(Locale.ROOT);
            String normalized = Normalizer.normalize(lower, Normalizer.Form.NFD)
                    .replaceAll("\\p{M}", "");
            return normalized.replace("_", " ");
        }
    }

    private record Budget(BigDecimal min, BigDecimal max) {
    }
}
