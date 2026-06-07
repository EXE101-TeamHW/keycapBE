# 📚 DATA.MD — Knowledge Base AI Tư Vấn Bàn Phím Cơ & Custom

> **Phiên bản:** 1.0 | **Ngôn ngữ:** Tiếng Việt
> Tài liệu này là nguồn tri thức RAG cho AI tư vấn bàn phím cơ, keycap custom, phụ kiện và dịch vụ liên quan.

---

## MỤC LỤC

1. [Kiến thức nền — Bàn phím cơ là gì?](#1-kiến-thức-nền)
2. [Layout — Chọn kích thước bàn phím](#2-layout)
3. [Switch — Cơ chế kích hoạt phím](#3-switch)
4. [Keycap Profile — Hình dạng & chiều cao keycap](#4-keycap-profile)
5. [Keycap Material — Chất liệu](#5-keycap-material)
6. [PCB — Hotswap vs Soldered](#6-pcb)
7. [Case & Mounting Style](#7-case--mounting-style)
8. [Stabilizer — Stab](#8-stabilizer)
9. [Foam & Mod âm thanh](#9-foam--mod-âm-thanh)
10. [Lube — Bôi trơn](#10-lube)
11. [Âm thanh & Sound Profile](#11-âm-thanh--sound-profile)
12. [Tư vấn theo mục đích sử dụng](#12-tư-vấn-theo-mục-đích-sử-dụng)
13. [Tư vấn theo ngân sách](#13-tư-vấn-theo-ngân-sách)
14. [Quy trình Custom Keycap](#14-quy-trình-custom-keycap)
15. [Thương hiệu & Kit phổ biến](#15-thương-hiệu--kit-phổ-biến)
16. [Mod phổ biến](#16-mod-phổ-biến)
17. [Bảo trì & Vệ sinh bàn phím](#17-bảo-trì--vệ-sinh-bàn-phím)
18. [Chính sách báo giá, bảo hành, giao hàng](#18-chính-sách-báo-giá-bảo-hành-giao-hàng)
19. [Câu trả lời mẫu — Khách chưa biết gì](#19-câu-trả-lời-mẫu)
20. [Thuật ngữ bàn phím cơ (Glossary)](#20-glossary)
21. [FAQ Tổng hợp](#21-faq-tổng-hợp)

---

## TÓM TẮT NHANH CHO AI

- Nếu khách chưa biết gì về bàn phím cơ/custom: dùng chương 1, 12 và 19; hỏi ngắn về mục đích dùng, layout, ngân sách và mức ồn mong muốn.
- Nếu khách hỏi chọn layout 60/65/75/TKL/full-size/96%: dùng chương 2; giải thích ưu/nhược điểm theo nhu cầu thực tế như văn phòng, gaming, code, nhập liệu số hoặc bàn nhỏ.
- Nếu khách hỏi switch, độ êm, cảm giác gõ, tiếng thocky/clacky/poppy: dùng chương 3, 9, 10 và 11; ưu tiên switch linear/silent cho văn phòng yên tĩnh, tactile nếu khách muốn có phản hồi rõ, tránh clicky khi cần ít ồn.
- Nếu khách hỏi profile/keycap cao thấp, Cherry/OEM/SA/XDA/MT3/KAT/DSA: dùng chương 4; nhắc profile Cherry/OEM dễ làm quen, SA/MT3 cao và đã tay hơn nhưng có thể mỏi nếu gõ lâu hoặc chưa quen.
- Nếu khách hỏi chất liệu keycap, ABS/PBT, doubleshot/dye-sub: dùng chương 5; giải thích độ bền, cảm giác bề mặt, âm thanh và khả năng bóng dầu.
- Nếu khách hỏi PCB, hotswap, soldered, 3-pin/5-pin, QMK/VIA, RGB: dùng chương 6; ưu tiên hotswap cho người mới vì dễ thay switch.
- Nếu khách hỏi case, plate, mounting style, gasket/tray/top/bottom mount: dùng chương 7; liên hệ với cảm giác gõ, độ flex và âm thanh.
- Nếu khách hỏi stab, phím dài bị lọc cọc/rattle: dùng chương 8; gợi ý lube/tune stabilizer.
- Nếu khách hỏi mod âm thanh, foam, tape mod, PE foam, switch film: dùng chương 9 và 16; nhắc mod ảnh hưởng âm thanh nhưng cần làm đúng để tránh rủi ro.
- Nếu khách hỏi lube switch/stab: dùng chương 10; phân biệt lube switch và lube stab, nhắc người mới nên bắt đầu từ stab trước.
- Nếu khách hỏi theo mục đích sử dụng như văn phòng/gaming/code/viết/học sinh-sinh viên: dùng chương 12; đưa cấu hình gợi ý theo nhu cầu.
- Nếu khách hỏi theo ngân sách: dùng chương 13; chia tier ngân sách và không bịa giá nếu không có sản phẩm cụ thể trong database.
- Nếu khách hỏi custom keycap, group buy, quy trình đặt thiết kế riêng: dùng chương 14 và 18; giải thích flow, thông tin khách cần chuẩn bị và nhắc giá cuối cùng cần staff xác nhận.
- Nếu khách hỏi thương hiệu switch/keycap/bàn phím: dùng chương 15; ưu tiên tư vấn theo nhu cầu thay vì khẳng định một thương hiệu luôn tốt nhất.
- Nếu khách hỏi bảo trì, vệ sinh, đổ nước, lỗi dùng lâu: dùng chương 17; đưa bước xử lý an toàn trước.
- Nếu khách hỏi chính sách báo giá, bảo hành, giao hàng: dùng chương 18; dùng template chính sách, không tự cam kết ngoài nội dung shop đã cung cấp.
- Nếu khách hỏi thuật ngữ như hotswap, gasket, thock, stab, lube, group buy: dùng chương 20; giải thích ngắn gọn, dễ hiểu.
- Nếu khách hỏi câu phổ biến hoặc cần trả lời nhanh: dùng chương 21 và các câu mẫu ở chương 19.
- Luôn ưu tiên dữ liệu sản phẩm từ database khi cần recommend sản phẩm cụ thể; nếu database không có sản phẩm khớp, vẫn tư vấn theo kiến thức trong tài liệu và nói rõ cần kiểm tra kho/staff để chốt mã sản phẩm.
- Trả lời bằng tiếng Việt có dấu, thân thiện, không quá dài; nếu thiếu thông tin, hỏi tối đa 1-2 câu tiếp theo.

---

## 1. KIẾN THỨC NỀN

### Bàn phím cơ là gì?
Bàn phím cơ (mechanical keyboard) là loại bàn phím sử dụng **switch cơ học riêng biệt cho từng phím**, khác với bàn phím membrane (màng) thông thường. Mỗi switch là một cơ cấu vật lý độc lập gồm: thân switch, stem (cần gạt), lò xo, và tiếp điểm điện.

**Ưu điểm bàn phím cơ:**
- Tuổi thọ cao hơn (50–100 triệu lần bấm/switch)
- Cảm giác bấm rõ ràng, chính xác hơn
- Tùy biến cao: thay switch, keycap, mod âm thanh
- Âm thanh đặc trưng (thocky, clacky, tùy chỉnh được)
- Phù hợp gõ nhiều giờ, giảm mỏi tay

**Nhược điểm:**
- Giá cao hơn membrane
- Tiếng ồn (với switch clicky) — cần cân nhắc môi trường làm việc
- Kích thước thường lớn hơn (trừ layout compact)

### Bàn phím custom là gì?
Bàn phím custom là bàn phím được **lắp ráp tùy chỉnh theo yêu cầu** từ các linh kiện riêng lẻ: case, PCB, plate, switch, keycap, stab. Người dùng có toàn quyền kiểm soát:
- Âm thanh (thock, clack, muted)
- Cảm giác bấm (lực, độ nảy, độ chắc)
- Ngoại hình (màu sắc, chất liệu, layout)
- Tính năng (RGB, knob, screen)

---

## 2. LAYOUT

### Tổng quan các layout phổ biến

| Layout           | Số phím   | Kích thước  | Đặc điểm chính           | Phù hợp                |
| ---------------- | --------- | ----------- | ------------------------ | ---------------------- |
| Full-size (100%) | ~104 phím | Lớn nhất    | Đủ Numpad, F-row, arrow  | Kế toán, dữ liệu số    |
| TKL (80%)        | ~87 phím  | Bỏ Numpad   | Phổ biến nhất, cân bằng  | Gaming, văn phòng      |
| 75%              | ~84 phím  | Nhỏ hơn TKL | Giữ arrow, F-row, Delete | Lập trình, portable    |
| 65%              | ~68 phím  | Compact     | Giữ arrow, bỏ F-row      | Gaming, di động        |
| 60%              | ~61 phím  | Rất nhỏ     | Bỏ arrow, bỏ F-row       | Minimalist, desk space |
| 40%              | ~40 phím  | Cực nhỏ     | Nhiều layer, niche       | Enthusiast nâng cao    |
| 96% / 1800       | ~96 phím  | Gần full    | Numpad nhỏ gọn hơn       | Cần Numpad, bàn nhỏ    |

---

### 2.1 Full-size (100%)

**Số phím:** ~104 phím (ANSI) / ~105 phím (ISO)
**Kích thước điển hình:** ~44cm × 14cm

**Bao gồm:**
- Alphas (chữ cái, số)
- Function row (F1–F12)
- Navigation cluster (Insert, Delete, Home, End, PgUp, PgDn)
- Arrow keys
- Numpad đầy đủ (17 phím)

**Ưu điểm:**
- Đầy đủ tính năng nhất
- Không cần ghi nhớ layer
- Numpad tiện nhập số

**Nhược điểm:**
- Chiếm nhiều diện tích bàn
- Chuột phải đặt xa hơn → mỏi vai khi gaming
- Nặng, ít portable

**Phù hợp:** Kế toán, kho vận, nhập liệu số nhiều, người dùng phổ thông không muốn thay đổi thói quen.

**Không phù hợp:** Bàn nhỏ, gaming FPS cạnh tranh, người cần portable.

---

### 2.2 TKL — Tenkeyless (80%)

**Số phím:** ~87 phím
**Kích thước điển hình:** ~36cm × 14cm

**Bao gồm:** Alphas + F-row + Navigation cluster + Arrow keys. **Bỏ Numpad.**

**Ưu điểm:**
- Cân bằng nhất giữa đầy đủ và compact
- Chuột đặt gần hơn → ít mỏi vai
- Vẫn có F-row tiện cho shortcut, gaming
- Phổ biến nhất → nhiều lựa chọn case, keycap

**Nhược điểm:**
- Vẫn khá to nếu bàn nhỏ
- Bỏ Numpad → không tiện nhập số nhiều

**Phù hợp:** Gaming, văn phòng đa năng, người chuyển từ full-size lần đầu.

---

### 2.3 Layout 75%

**Số phím:** ~84 phím
**Kích thước điển hình:** ~32cm × 14cm

**Bao gồm:** Alphas + F-row + Arrow keys + một số nav keys (Delete, PgUp, PgDn...). **Bố cục nén chặt hơn TKL**, các phím nav sát nhau hơn.

**Ưu điểm:**
- Nhỏ gọn hơn TKL ~15%
- Vẫn đủ F-row, arrow keys
- Phù hợp lập trình (F-keys, arrow dùng nhiều)
- Portable tốt

**Nhược điểm:**
- Layout không chuẩn → một số phím có vị trí khác thường (Backspace nhỏ, Delete sát)
- Keycap set đôi khi thiếu key cho 75% (cần chú ý khi mua)

**Phù hợp:** Developer/lập trình viên, người dùng laptop quen tay, bàn vừa, vừa muốn đủ phím vừa tiết kiệm không gian.

---

### 2.4 Layout 65%

**Số phím:** ~68 phím
**Kích thước điển hình:** ~31cm × 11cm

**Bao gồm:** Alphas + Arrow keys + một số nav (Delete, PgUp, PgDn, Home, End). **Bỏ hoàn toàn F-row.**

**Ưu điểm:**
- Rất compact, gọn bàn
- Vẫn có arrow keys (khác biệt vs 60%)
- Phổ biến trong cộng đồng custom
- Dễ mang đi

**Nhược điểm:**
- Bỏ F-row → cần dùng Fn layer cho F1–F12 và media keys
- Học lại một số thao tác

**Phù hợp:** Gaming (nhiều gamer ưa 65% vì bỏ Fn-row ít dùng), minimalist, người dùng bàn nhỏ.

**Layout 65% phổ biến:** Tofu65, Bakeneko65, Mode 65, KBD67 Lite

---

### 2.5 Layout 60%

**Số phím:** ~61 phím
**Kích thước điển hình:** ~29cm × 10cm

**Bao gồm:** Chỉ có Alphas + modifier keys. **Bỏ F-row, bỏ Arrow keys, bỏ nav cluster.**

**Ưu điểm:**
- Nhỏ nhất trong các layout phổ biến
- Tối giản tuyệt đối
- Nhiều case đẹp, giá rẻ (GK61, Anne Pro 2...)
- Cộng đồng mod lớn

**Nhược điểm:**
- Không có arrow keys → phải dùng layer (Fn + WASD hoặc HJKL)
- Không có F-row → gaming/shortcut cần học lại
- Không phù hợp người mới hoặc cần nhiều phím chức năng

**Phù hợp:** Enthusiast, minimalist, bàn rất nhỏ, người đã quen layout layer.

**Lưu ý:** 60% có nhiều biến thể bottom row (WKL, HHKB, standard) → cần chú ý khi mua keycap.

---

### 2.6 Layout 96% / 1800

**Số phím:** ~96–98 phím
**Đặc điểm:** Gần như full-size nhưng **nén numpad sát vào phần main**, bỏ khoảng trống giữa arrow và numpad.

**Phù hợp:** Cần Numpad nhưng muốn tiết kiệm không gian, bàn vừa.

---

### Tóm tắt tư vấn layout theo nhu cầu

| Nhu cầu                | Layout đề xuất         |
| ---------------------- | ---------------------- |
| Nhập số nhiều, kế toán | Full-size hoặc 96%     |
| Gaming FPS/tối ưu      | 65% hoặc TKL           |
| Lập trình/code         | 75% hoặc TKL           |
| Văn phòng phổ thông    | TKL hoặc Full-size     |
| Minimalist, bàn nhỏ    | 60% hoặc 65%           |
| Portable, đi làm/cafe  | 65% hoặc 60%           |
| Mới chuyển từ membrane | TKL (ít thay đổi nhất) |

---

## 3. SWITCH

Switch là linh hồn của bàn phím cơ. Có 3 loại chính:

### 3.1 Linear Switch (Switch thẳng)

**Cơ chế:** Stem đi thẳng xuống, **không có bump hay click** trong quá trình bấm. Cảm giác mượt mà, đều đặn từ trên xuống dưới.

**Đặc điểm:**
- Không có tactile bump
- Êm nhất trong 3 loại
- Âm thanh: thường thocky, poppy hoặc clacky tùy case/mod
- Lực bấm: tùy switch (35g–80g)

**Phù hợp:**
- Gaming (thao tác nhanh, ít bị "vướng" bump)
- Văn phòng yêu cầu im lặng
- Người thích cảm giác mượt, không muốn feedback cơ học

**Switch Linear phổ biến:**

| Switch                    | Actuation | Lực cuối | Ghi chú                    |
| ------------------------- | --------- | -------- | -------------------------- |
| Cherry MX Red             | 2.0mm     | 45g      | Phổ biến nhất, light, mượt |
| Cherry MX Black           | 2.0mm     | 60g      | Nặng hơn Red, chắc tay     |
| Gateron Yellow            | 2.0mm     | 35g      | Rất nhẹ, mượt hơn Cherry   |
| Gateron Red               | 2.0mm     | 45g      | Mượt hơn Cherry Red        |
| Gateron Black             | 2.0mm     | 50g      | Nặng nhẹ vừa, smooth       |
| Akko V3 Cream Yellow      | 2.0mm     | 35g      | Rẻ, mượt, phổ biến VN      |
| Novelkeys Cream           | 2.0mm     | 55g      | Thocky nổi tiếng           |
| Gateron Oil King          | 2.0mm     | 55g      | Smooth cao, pre-lubed      |
| SP Star Meteor White      | 2.0mm     | 35g      | Nhẹ, affordable            |
| JWK Linear (Alpaca, etc.) | 2.0mm     | 62g      | Chất lượng cao             |

---

### 3.2 Tactile Switch (Switch có phản hồi xúc giác)

**Cơ chế:** Có một **bump (điểm cộm)** rõ ràng khi stem đi qua điểm actuation, không có tiếng click.

**Đặc điểm:**
- Có bump xúc giác — biết khi nào phím kích hoạt
- Không có tiếng click to
- Tốt cho gõ văn bản nhiều
- Giúp giảm bottoming-out không cần thiết

**Phân loại bump:**
- **Light tactile:** bump nhẹ, gần giống linear (Cherry MX Brown)
- **Medium tactile:** bump rõ, phổ biến (Gateron Brown, Boba U4)
- **Heavy tactile:** bump mạnh, rõ nét (Holy Pandas, Topre)

**Switch Tactile phổ biến:**

| Switch                       | Actuation          | Bump      | Ghi chú                                  |
| ---------------------------- | ------------------ | --------- | ---------------------------------------- |
| Cherry MX Brown              | 2.0mm              | Nhẹ       | Phổ biến nhất, nhưng bị chê bump quá nhẹ |
| Gateron Brown                | 2.0mm              | Nhẹ       | Mượt hơn Cherry Brown                    |
| Akko V3 Cream Blue (tactile) | 2.0mm              | Vừa       | Giá rẻ, bump khá rõ                      |
| Boba U4                      | 2.0mm              | Vừa-mạnh  | Im lặng, bump rõ, nổi tiếng              |
| Boba U4T                     | 2.0mm              | Vừa-mạnh  | Có tiếng, bump như U4                    |
| Holy Panda / Holy Panda X    | 2.0mm              | Mạnh      | Iconic tactile, premium                  |
| Topre (EC)                   | Electro-capacitive | Đặc trưng | Khác hoàn toàn, cao cấp                  |
| Durock POM T1                | 2.0mm              | Mạnh      | Smooth, bump rõ                          |

---

### 3.3 Clicky Switch (Switch có tiếng click)

**Cơ chế:** Có bump + **tiếng click** cơ học khi kích hoạt phím. Hai loại cơ chế:
- **Click jacket** (Cherry Blue): tiếng click từ jacket bao quanh stem
- **Click bar** (Kailh Box): thanh click bar tạo tiếng, ổn định hơn trong môi trường ẩm

**Đặc điểm:**
- Âm thanh click to, rõ ràng
- Feedback xúc giác + thính giác mạnh
- **KHÔNG phù hợp văn phòng** hoặc môi trường yên tĩnh
- Cảm giác gõ thỏa mãn nhất

**Switch Clicky phổ biến:**

| Switch                      | Actuation | Tiếng       | Ghi chú               |
| --------------------------- | --------- | ----------- | --------------------- |
| Cherry MX Blue              | 2.2mm     | To, rõ      | Iconic, phổ biến nhất |
| Cherry MX Green             | 2.2mm     | To hơn Blue | Nặng hơn Blue         |
| Kailh Box White             | 1.8mm     | To, sắc     | Click bar, ổn định    |
| Kailh Box Jade              | 1.8mm     | Rất to      | Thick click bar, nặng |
| Gateron Blue                | 2.2mm     | To          | Phổ biến, giá tốt     |
| Akko V3 Cream Blue (clicky) | 2.0mm     | Vừa         | Affordable            |

---

### 3.4 So sánh 3 loại switch

| Tiêu chí          | Linear       | Tactile           | Clicky       |
| ----------------- | ------------ | ----------------- | ------------ |
| Cảm giác          | Mượt, đều    | Có bump, phản hồi | Bump + click |
| Âm thanh          | Im / thocky  | Im / thocky nhẹ   | Clicky, to   |
| Gaming            | ⭐⭐⭐⭐⭐        | ⭐⭐⭐               | ⭐⭐           |
| Gõ văn bản        | ⭐⭐⭐          | ⭐⭐⭐⭐⭐             | ⭐⭐⭐⭐         |
| Văn phòng im lặng | ⭐⭐⭐⭐⭐        | ⭐⭐⭐⭐              | ⭐            |
| Độ thỏa mãn       | ⭐⭐⭐          | ⭐⭐⭐⭐              | ⭐⭐⭐⭐⭐        |
| Phổ biến VN       | Red / Yellow | Brown             | Blue         |

---

### 3.5 Switch Silent (Switch im lặng)

Một số switch có **foam/silicone damper** tích hợp để giảm tiếng khi bottoming-out và upstroke:
- **Cherry MX Silent Red / Black**
- **Gateron Silent Red / Yellow / Black**
- **Boba U4** (tactile silent)
- **Outemu Silent Sky**

**Phù hợp:** Văn phòng, phòng họp, ban đêm, ở chung.

---

### 3.6 Lực bấm — Actuation Force

| Phân loại | Lực    | Cảm giác                     |
| --------- | ------ | ---------------------------- |
| Rất nhẹ   | <40g   | Dễ bấm nhầm, ngón tay mỏi ít |
| Nhẹ       | 40–50g | Phổ biến gaming              |
| Vừa       | 50–60g | Cân bằng                     |
| Nặng      | 60–70g | Ít bấm nhầm, chắc tay        |
| Rất nặng  | >70g   | Niche, gõ ít/đặc biệt        |

---

## 4. KEYCAP PROFILE

Profile là **hình dạng và chiều cao của keycap**, ảnh hưởng lớn đến cảm giác gõ và thẩm mỹ.

### 4.1 Cherry Profile

**Chiều cao:** Thấp-vừa (~9.4mm hàng R1, ~8.1mm hàng R3)
**Hình dạng mặt:** Hơi cong lõm (sculpted)
**Góc nghiêng:** Mỗi hàng có góc nghiêng khác nhau (row 1-4 khác nhau)

**Đặc điểm:**
- Compact, không quá cao
- Cảm giác gõ tự nhiên, thoải mái
- Được sản xuất bởi Cherry (Đức), chuẩn phổ biến nhất
- Sau này nhiều hãng clone: GMK (Đức), Drop Signature, ePBT...
- **GMK là Cherry profile cao cấp nhất** — ABS doubleshot, legends sắc nét

**Phù hợp:** Người muốn cảm giác truyền thống, gõ nhiều giờ, chuẩn mực nhất.

---

### 4.2 OEM Profile

**Chiều cao:** Cao hơn Cherry (~11.2mm)
**Hình dạng mặt:** Cong lõm (sculpted)
**Đặc điểm đặc trưng:** Profile sculpted, mỗi hàng khác nhau, nhưng cao hơn Cherry

**Đặc điểm:**
- Phổ biến nhất trên bàn phím gaming/prebuilt (Razer, Corsair...)
- Cảm giác "quen thuộc" với đa số người dùng
- Giá rẻ hơn Cherry/GMK
- Không premium bằng Cherry nhưng đủ dùng

**Phù hợp:** Người mới, budget thấp, muốn cảm giác quen thuộc.

---

### 4.3 SA Profile

**Chiều cao:** Cao nhất trong các profile phổ biến (~16.5mm)
**Hình dạng mặt:** Cong lõm sâu, spherical (cầu)
**Nhà sản xuất chính:** Signature Plastics (SP), Kibo, Melgeek

**Đặc điểm:**
- Rất cao → cảm giác "lướt" tay trên phím, cần kê tay
- Tiếng gõ thường rất thocky
- Thẩm mỹ vintage, retro cực đẹp
- Nặng hơn các profile khác
- ABS thường (doubleshot) → bóng hơn theo thời gian

**Phù hợp:** Người thích thẩm mỹ vintage, không gaming nhiều, muốn âm thanh độc đáo.
**Không phù hợp:** Gaming, người cần gõ nhanh, bàn không có wrist rest.

---

### 4.4 XDA Profile

**Chiều cao:** Thấp-vừa (~9mm)
**Hình dạng mặt:** Phẳng, spherical nhẹ, **uniform** (tất cả hàng cùng chiều cao)
**Nhà sản xuất:** Kibo, nhiều hãng Trung Quốc

**Đặc điểm:**
- Uniform profile → có thể xoay keycap bất kỳ hướng nào
- Thoải mái với người gõ không nhìn phím (touch typist)
- Mặt phím rộng hơn Cherry
- Thường PBT dye-sub → bền màu

**Phù hợp:** Touch typist, thích thẩm mỹ phẳng hiện đại, thiết kế thấp.
**Không phù hợp:** Người cần sculpted rõ ràng để định hướng ngón tay.

---

### 4.5 MT3 Profile

**Chiều cao:** Cao (~15mm tương đương SA)
**Hình dạng mặt:** Cong lõm sâu, sculpted mạnh, **spherical**
**Nhà sản xuất:** Drop (ban đầu thiết kế bởi Matt3o)

**Đặc điểm:**
- Cảm giác "ôm" ngón tay rõ nhất trong các profile
- Tiếng gõ thocky, dày
- Thẩm mỹ vintage nhưng hiện đại hơn SA
- ABS doubleshot
- Được đánh giá rất cao về comfort khi gõ lâu

**Phù hợp:** Người gõ nhiều, thích vintage, muốn comfort tối đa.
**Không phù hợp:** Gaming nhanh, người thích thấp.

---

### 4.6 KAT Profile

**Chiều cao:** Vừa (~12mm — giữa Cherry và SA)
**Hình dạng mặt:** Sculpted nhẹ, cylindrical
**Nhà sản xuất:** Keyreative (Trung Quốc)

**Đặc điểm:**
- Kết hợp độ cao vừa phải, mặt phím thoải mái
- PBT dye-sub → bền màu
- Giá vừa phải
- Đang ngày càng phổ biến

---

### 4.7 DSA Profile

**Chiều cao:** Thấp (~7.6mm)
**Hình dạng mặt:** Phẳng, spherical, **uniform**
**Nhà sản xuất:** Signature Plastics

**Đặc điểm:**
- Uniform như XDA nhưng nhỏ hơn
- Dễ tùy biến vì mọi phím đều dùng được ở mọi vị trí
- Ít phổ biến hơn XDA

---

### Bảng so sánh profile

| Profile    | Chiều cao | Sculpted? | Âm thanh | Comfort | Phổ biến |
| ---------- | --------- | --------- | -------- | ------- | -------- |
| Cherry/GMK | Thấp-vừa  | Có        | Vừa      | ⭐⭐⭐⭐⭐   | ⭐⭐⭐⭐⭐    |
| OEM        | Cao-vừa   | Có        | Vừa      | ⭐⭐⭐     | ⭐⭐⭐⭐⭐    |
| SA         | Rất cao   | Có        | Thocky   | ⭐⭐⭐⭐    | ⭐⭐⭐      |
| MT3        | Cao       | Có (mạnh) | Thocky   | ⭐⭐⭐⭐⭐   | ⭐⭐⭐      |
| XDA        | Thấp-vừa  | Không     | Nhẹ      | ⭐⭐⭐     | ⭐⭐⭐      |
| KAT        | Vừa       | Nhẹ       | Vừa      | ⭐⭐⭐⭐    | ⭐⭐⭐      |
| DSA        | Thấp      | Không     | Nhẹ      | ⭐⭐      | ⭐⭐       |

---

## 5. KEYCAP MATERIAL

### 5.1 ABS (Acrylonitrile Butadiene Styrene)

**Đặc điểm:**
- Phổ biến hơn
- Âm thanh thường "clacky", trong hơn
- **Bóng dầu theo thời gian** (shine) — nhìn bẩn, không ưa thích
- Màu sắc sống động hơn PBT
- In legends sắc nét hơn (phù hợp doubleshot)

**Loại in:**
- **Doubleshot ABS:** 2 lớp nhựa đúc lồng nhau → legend không bao giờ phai
- **Pad printed ABS:** in mực → phai theo thời gian (rẻ tiền)

**Thương hiệu ABS nổi tiếng:** GMK (Đức — chuẩn vàng), Signature Plastics (SA/DSA)

---

### 5.2 PBT (Polybutylene Terephthalate)

**Đặc điểm:**
- Cứng hơn, dày hơn ABS
- **Không bóng dầu** theo thời gian — bền hơn nhiều
- Âm thanh thường "thocky", trầm hơn
- Màu sắc hơi đục, tự nhiên hơn
- Giá vừa phải

**Loại in:**
- **Dye-sublimation (dye-sub) PBT:** nhuộm màu vào trong nhựa → legend không phai, chỉ làm được màu tối trên nền sáng
- **Doubleshot PBT:** 2 lớp PBT → bền nhất, có thể làm màu sáng trên nền tối
- **Laser etched:** khắc laser → có thể phai dần

**Thương hiệu PBT nổi tiếng:** ePBT, Akko (PBT dye-sub), Infinikey (PBT), Domikey (PBT doubleshot)

---

### 5.3 So sánh ABS vs PBT

| Tiêu chí          | ABS                     | PBT                 |
| ----------------- | ----------------------- | ------------------- |
| Độ bền surface    | Bóng dầu theo thời gian | Không bóng, bền hơn |
| Âm thanh          | Clacky, trong           | Thocky, trầm        |
| Màu sắc           | Sống động               | Hơi đục             |
| Legend chất lượng | Doubleshot rất sắc      | Dye-sub rất bền màu |
| Giá               | Biến động (GMK = đắt)   | Thường rẻ hơn GMK   |
| Phổ biến          | Rất phổ biến            | Rất phổ biến        |

---

## 6. PCB

### 6.1 Hotswap PCB

**Định nghĩa:** PCB có **socket hotswap** (Kailh hoặc Millmax) → có thể rút/cắm switch mà **không cần hàn**.

**Ưu điểm:**
- Thay switch dễ dàng, không cần kỹ năng hàn
- Thử nhiều loại switch
- Sửa lỗi switch hỏng dễ

**Nhược điểm:**
- Đắt hơn PCB thường
- Socket có thể bị lỏng theo thời gian nếu thay quá nhiều lần
- Một số socket (Kailh) chỉ chịu được ~100 lần rút/cắm

**Socket phổ biến:**
- **Kailh hotswap socket:** Phổ biến nhất, rẻ, chắc đủ dùng
- **Millmax socket (0305, 7305):** Cao cấp hơn, bền hơn, có thể dùng switch 3-pin và 5-pin

---

### 6.2 Soldered PCB (PCB hàn)

**Định nghĩa:** Switch được **hàn cố định** vào PCB.

**Ưu điểm:**
- Kết nối ổn định nhất
- Giá PCB rẻ hơn
- Phổ biến ở bàn phím prebuilt

**Nhược điểm:**
- Cần kỹ năng và dụng cụ hàn để thay switch
- Sai lầm có thể hỏng PCB

---

### 6.3 Switch 3-pin vs 5-pin

- **3-pin (PCB mount):** 2 chân điện + 1 chân giữa. Phổ biến hơn, tương thích rộng hơn.
- **5-pin (plate mount):** 2 chân điện + 3 chân nhựa. Ổn định hơn, cần plate hoặc PCB hỗ trợ 5-pin. Có thể clip bỏ 2 chân nhựa để dùng với PCB 3-pin.

---

### 6.4 Tính năng PCB đặc biệt

- **Per-key RGB:** LED riêng từng phím, hỗ trợ hiệu ứng đa dạng
- **South-facing / North-facing LED:** Ảnh hưởng tương thích với Cherry profile (north-facing gây "interference" với Cherry — xem chi tiết section mod)
- **VIA/VIAL compatible:** Remapping phím không cần flash firmware
- **QMK firmware:** Custom sâu, macro, layer không giới hạn

---

## 7. CASE & MOUNTING STYLE

### 7.1 Chất liệu Case

| Chất liệu          | Âm thanh        | Cảm giác   | Giá     | Ghi chú             |
| ------------------ | --------------- | ---------- | ------- | ------------------- |
| Nhôm (Aluminum)    | Pingy, bright   | Chắc, nặng | Cao     | Premium nhất        |
| Polycarbonate (PC) | Thocky, muffled | Nhẹ, flex  | Vừa     | Trong suốt đẹp      |
| ABS Plastic        | Hollow, loud    | Nhẹ        | Thấp    | Entry level         |
| Acrylic            | Sáng, clacky    | Cứng       | Thấp    | Trong suốt rẻ       |
| Brass              | Nặng, thocky    | Rất nặng   | Cao     | Thường plate/weight |
| Wood               | Ấm, đặc trưng   | Trung bình | Vừa-cao | Niche, độc đáo      |
| Carbon Fiber       | Sắc, stiff      | Nhẹ        | Cao     | Niche               |

---

### 7.2 Mounting Style (Kiểu gắn Plate/PCB)

Mounting style quyết định **flex** (độ đàn hồi) và **âm thanh** của bàn phím.

**Tray Mount:**
- PCB/plate gắn thẳng vào đáy case
- Cứng nhất, ít flex
- Âm thanh thường không hay
- Phổ biến trên bàn phím giá rẻ

**Top Mount:**
- Plate gắn vào nắp trên của case
- Phổ biến, âm thanh tốt
- Flex vừa phải

**Bottom Mount:**
- Plate gắn vào đáy case
- Flex tốt hơn top mount
- Âm thanh thường sâu hơn

**Gasket Mount:**
- Plate/PCB được giữ bởi **gasket (đệm silicon/foam)** thay vì vít cứng
- **Flex nhiều nhất** → cảm giác bấm mềm mại, "bouncy"
- Âm thanh thocky, ít pingy
- Phổ biến trong bàn phím custom cao cấp

**Top Mount + O-ring:**
- Biến thể giữa top mount và gasket
- Thêm o-ring vào điểm gắn để tăng flex

**Sandwich Mount:**
- Plate kẹp giữa top và bottom case
- Ổn định, cứng

**Bảng tóm tắt:**

| Mount  | Flex      | Âm thanh | Phổ biến    |
| ------ | --------- | -------- | ----------- |
| Tray   | Thấp nhất | Kém nhất | Entry level |
| Top    | Vừa       | Tốt      | Phổ biến    |
| Bottom | Vừa-cao   | Tốt      | Mid-range   |
| Gasket | Cao nhất  | Tốt nhất | Premium     |

---

### 7.3 Plate Material

| Material           | Âm thanh        | Flex | Ghi chú             |
| ------------------ | --------------- | ---- | ------------------- |
| Aluminum           | Bright, pingy   | Thấp | Phổ biến            |
| Brass              | Thocky, nặng    | Thấp | Nặng, premium feel  |
| Polycarbonate (PC) | Thocky, muffled | Cao  | Flex tốt            |
| FR4 (PCB material) | Clacky          | Vừa  | Rẻ, phổ biến budget |
| Carbon Fiber       | Sắc, stiff      | Thấp | Niche               |
| POM                | Muffled, thocky | Vừa  | Trending            |

---

## 8. STABILIZER

### 8.1 Stab là gì?
Stabilizer là cơ cấu giữ cho **phím lớn (2u+) không bị lắc** khi bấm không đúng tâm. Cần cho: Spacebar, Shift trái/phải, Enter, Backspace, Numpad Enter, Numpad 0.

### 8.2 Loại Stab

**Plate-mounted stab:**
- Gắn vào plate
- Không tháo rời khi bàn phím đã lắp
- Phổ biến ở bàn phím prebuilt

**PCB-mounted stab (screw-in và snap-in):**
- Gắn trực tiếp vào PCB
- **Screw-in:** vặn vít → ổn định nhất, không lỏng
- **Snap-in:** bấm vào lỗ PCB → tiện nhưng có thể lỏng
- Tháo rời được để lube/mod

### 8.3 Thương hiệu Stab

| Thương hiệu          | Chất lượng | Ghi chú               |
| -------------------- | ---------- | --------------------- |
| Durock V2            | ⭐⭐⭐⭐⭐      | Chuẩn vàng, screw-in  |
| TX Stab              | ⭐⭐⭐⭐⭐      | Rất smooth, premium   |
| Zeal Stabs           | ⭐⭐⭐⭐⭐      | Đắt, tốt              |
| C3 Equalz            | ⭐⭐⭐⭐       | Tốt, phổ biến         |
| Gateron Ink Stab     | ⭐⭐⭐⭐       | Phổ biến, giá tốt     |
| Cherry Stab          | ⭐⭐⭐        | Chuẩn, cần lube nhiều |
| Stock (kèm bàn phím) | ⭐⭐         | Cần mod/lube          |

### 8.4 Lube Stab

Stab **bắt buộc phải lube** để không bị rattle (lắc) và distaff (tiếng "ọp ẹp"):
- **Wire:** Dielectric grease hoặc XHT-BDZ
- **Housing/stem:** Krytox 205g0, Krytox 106, hoặc Permatex Dielectric Grease
- **Band-aid mod:** dán miếng foam nhỏ dưới vị trí stab trên PCB

---

## 9. FOAM & MOD ÂM THANH

### 9.1 PE Foam Mod

**Định nghĩa:** Đặt lớp PE foam (xốp PE mỏng) giữa switch và PCB trước khi gắn switch.

**Tác dụng:**
- Muffled âm thanh đáng kể
- Tăng âm thanh "poppy", thocky
- Thay đổi cảm giác bấm nhẹ

**Cách làm:** Đặt PE foam lên PCB → đục lỗ tại vị trí switch → gắn switch lên.

---

### 9.2 Case Foam

**Định nghĩa:** Foam đặt dưới PCB/plate trong case để hấp thụ âm thanh hollow.

**Tác dụng:** Giảm tiếng rỗng của case, âm thanh đặc hơn.

**Loại:** Neoprene, EVA foam, Poron foam.

---

### 9.3 PCB Foam

**Định nghĩa:** Foam đặt giữa PCB và plate.

**Tác dụng:** Giảm tiếng "ping" của plate, muffled âm thanh.

---

### 9.4 Tempest Mod (Gasket Mod DIY)

**Định nghĩa:** Nhét foam/tissue vào khoảng trống trong case để giảm hollow sound.

**Rẻ nhất** trong các mod âm thanh, hiệu quả tốt.

---

### 9.5 Tape Mod

**Định nghĩa:** Dán masking tape hoặc painter's tape lên mặt dưới PCB.

**Tác dụng:** Thay đổi âm thanh: thường thocky hơn, ít clacky hơn. Hiệu quả thay đổi tùy case.

**Chi phí:** Gần như miễn phí.

---

### 9.6 Switch Foam

Foam nhỏ đặt trong từng switch housing để giảm tiếng bottom-out.

---

### 9.7 Tóm tắt ưu tiên mod âm thanh

1. **Lube switch + stab** → Ảnh hưởng lớn nhất
2. **PE foam mod** → Thay đổi âm thanh rõ
3. **Tape mod** → Nhanh, rẻ, hiệu quả
4. **Case foam** → Giảm hollow
5. **PCB foam** → Giảm ping
6. **Gasket mount** → Ảnh hưởng hệ thống toàn bộ

---

## 10. LUBE

### 10.1 Lube Switch

Bôi trơn switch để **giảm scratchiness, tăng độ mượt, giảm tiếng ồn**.

**Loại lube phổ biến:**

| Lube            | Độ nhớt | Dùng cho                       | Ghi chú                     |
| --------------- | ------- | ------------------------------ | --------------------------- |
| Krytox 205g0    | Dày     | Linear switch (housing + stem) | Chuẩn vàng cho linear       |
| Krytox 105      | Loãng   | Linear switch lò xo            | Oil, không dùng cho tactile |
| Tribosys 3203   | Vừa     | Tactile switch                 | Không làm mất bump          |
| Tribosys 3204   | Vừa-dày | Tactile/linear                 | Đa dụng                     |
| Krytox GPL 205  | Dày     | Linear                         | Tương đương 205g0           |
| Superlube 51004 | Loãng   | Switch                         | Alternative rẻ              |

**Lưu ý quan trọng:**
- **KHÔNG lube legs (chân bump)** của tactile switch → mất bump
- Lube quá nhiều → mất feedback, âm thanh muffled
- Linear switch có thể lube nhiều hơn tactile

### 10.2 Lube Stab

Xem mục 8.4.

### 10.3 Phương pháp lube

**Hand lube (lube tay):** Tháo từng switch, dùng cọ nhỏ lube từng bộ phận. Chất lượng tốt nhất nhưng tốn thời gian (50–100 switch × vài phút/switch).

**Bag lube (lube túi):** Cho switch + lube vào túi, lắc đều. Nhanh hơn nhưng không đều, không kiểm soát được.

**Switch roller:** Dụng cụ lube nhanh, vừa nhanh vừa đều.

---

## 11. ÂM THANH & SOUND PROFILE

### 11.1 Các thuật ngữ mô tả âm thanh

| Thuật ngữ        | Mô tả                            | Ví dụ                       |
| ---------------- | -------------------------------- | --------------------------- |
| **Thocky**       | Âm trầm, đục, ấm — "thock thock" | Creams + gasket mount       |
| **Clacky**       | Âm cao, sắc, rõ — "clack clack"  | ABS keycap + Cherry MX Blue |
| **Poppy**        | Âm tươi, vui, "pop pop"          | PE foam mod + linear        |
| **Muted/Silent** | Im lặng, muffled                 | Silent switch + foam        |
| **Pingy**        | Tiếng kim loại cao vít, khó chịu | Aluminum plate không foam   |
| **Hollow/Empty** | Tiếng rỗng trong case            | Case nhựa không foam        |
| **Creamy**       | Mượt, êm, dễ chịu                | Lubed linear                |
| **Bassy**        | Âm trầm, nặng                    | PC case + brass plate       |

---

### 11.2 Yếu tố ảnh hưởng âm thanh (theo mức độ)

1. **Case material & mounting** — Ảnh hưởng lớn nhất tổng thể
2. **Switch type & lube** — Âm thanh từng phím
3. **Keycap profile & material** — ABS vs PBT, cao vs thấp
4. **Plate material** — Aluminum pingy, PC thocky
5. **Foam mods** — Fine-tuning
6. **Stabilizer quality** — Rattle = tệ

---

### 11.3 Build recipe theo sound profile mong muốn

**Muốn thocky nhất:**
→ Gasket mount + PC plate + PBT keycap + NK Cream / JWK Linear + Durock stab lubed + case foam + PE foam mod

**Muốn clacky:**
→ Aluminum case + top mount + ABS keycap (GMK) + clicky switch hoặc unlubed linear

**Muốn silent/muted:**
→ PC case + gasket mount + silent switch + PBT keycap + heavy foam mod

**Muốn poppy:**
→ PE foam mod + NK Cream + ABS keycap + mid flex case

---

## 12. TƯ VẤN THEO MỤC ĐÍCH SỬ DỤNG

### 12.1 Văn phòng

**Yêu cầu:**
- Im lặng (không làm phiền đồng nghiệp)
- Thoải mái gõ nhiều giờ
- Gọn gàng, chuyên nghiệp về ngoại hình

**Đề xuất:**

| Tiêu chí | Đề xuất                                                                 |
| -------- | ----------------------------------------------------------------------- |
| Layout   | TKL hoặc 75%                                                            |
| Switch   | Linear silent (Gateron Silent Red/Yellow) hoặc Tactile silent (Boba U4) |
| Keycap   | PBT dye-sub, màu trung tính (trắng, xám, đen)                           |
| Profile  | Cherry hoặc OEM                                                         |
| Case     | Nhựa hoặc nhôm, không RGB lòe loẹt                                      |

**Kit đề xuất văn phòng (ngân sách vừa):**
- Keychron K2 (hotswap) + switch Boba U4 hoặc Gateron Silent
- Akko 5075B + switch Akko Cream Yellow V3

---

### 12.2 Gaming

**Yêu cầu:**
- Thao tác nhanh, phản hồi nhanh
- Actuation lực nhẹ
- Polling rate cao (1000Hz+)
- Anti-ghosting / N-key rollover

**Đề xuất:**

| Tiêu chí        | Đề xuất                                                             |
| --------------- | ------------------------------------------------------------------- |
| Layout          | 65% hoặc TKL (chuột đặt gần)                                        |
| Switch          | Linear nhẹ (Gateron Yellow, Akko Cream Yellow, Cherry Speed Silver) |
| Actuation Force | 35–45g                                                              |
| Keycap          | ABS hoặc PBT, không quá cao (Cherry hoặc OEM)                       |
| PCB             | Hotswap để thử switch                                               |
| Polling rate    | 1000Hz (phổ biến) hoặc 8000Hz (high-end)                            |

**Kit đề xuất gaming:**
- Wooting 60HE (analog switch — đỉnh cao gaming)
- Lamzu Atlantis / Keychron Q Series + Gateron Yellow
- Akko MOD Series + Akko Cream Yellow

**Lưu ý gaming:**
- Rapid trigger (Wooting, nhiều bàn phím gaming mới) → cải thiện input timing
- Magnetic switch (Hall Effect): không mài mòn, tuổi thọ cao hơn

---

### 12.3 Lập trình / Coding

**Yêu cầu:**
- Gõ nhiều giờ không mỏi
- Phím tắt, F-keys, arrow keys đủ
- Ergonomics tốt
- Có thể cần macro/programmable keys

**Đề xuất:**

| Tiêu chí   | Đề xuất                                                              |
| ---------- | -------------------------------------------------------------------- |
| Layout     | 75% hoặc TKL (cần F-row, arrow)                                      |
| Switch     | Tactile vừa (Boba U4T, Holy Panda) hoặc Linear mượt (Gateron Yellow) |
| Firmware   | QMK/VIA — remapping tự do                                            |
| Keycap     | PBT, Cherry profile, màu tối cho contrast                            |
| Ergonomics | Cân nhắc split keyboard nếu làm việc nhiều giờ                       |

**Kit đề xuất coding:**
- Keychron Q2 (75%) + QMK + Holy Panda
- Akko MOD007B + Boba U4T
- Tofu65 custom build

**Cân nhắc thêm:**
- **Split keyboard** (Sofle, Lily58, Dactyl): giảm mỏi vai, wrist, cần DIY
- **Programmable macro pad** bổ sung nếu cần nhiều shortcut

---

### 12.4 Viết văn / Content Creator

**Đề xuất:**
- Tactile switch có bump rõ (Boba U4T, Holy Panda) → biết chính xác khi phím kích hoạt
- Profile SA hoặc MT3 → thocky, ấm, thoải mái
- TKL hoặc Full-size (ít bỏ phím)

---

### 12.5 Học sinh / Sinh viên

**Đề xuất:**
- Budget thấp: Akko 3068B / 5075B + switch Akko Cream
- Nếu dùng thư viện: switch silent
- TKL hoặc 65% → linh hoạt

---

## 13. TƯ VẤN THEO NGÂN SÁCH

### 13.1 Entry Level — Dưới 1.000.000 VNĐ

**Mục tiêu:** Trải nghiệm bàn phím cơ lần đầu, không quá đầu tư.

**Gợi ý:**
- Akko 3068B (BT + USB-C, hotswap) ~700–900K
- Royal Kludge RK61 / RK68 ~400–600K
- Redragon K552 ~300–500K

**Lưu ý:** Ở mức này, switch/stab thường cần lube để cải thiện. Không nên kỳ vọng âm thanh premium.

---

### 13.2 Mid-range — 1.000.000 – 3.000.000 VNĐ

**Mục tiêu:** Cân bằng chất lượng và giá, có thể mod.

**Gợi ý:**
- Akko MOD Series (MOD007B, MOD005, MOD007PC) ~1.5–2.5M
- Keychron V Series (V1, V2, V3) ~1.5–2M
- Cidoo V65 / QK65 ~2–3M
- GMMK Pro ~2.5–3M

---

### 13.3 High-end — 3.000.000 – 8.000.000 VNĐ

**Mục tiêu:** Chất lượng build cao, âm thanh tốt, gasket mount.

**Gợi ý:**
- Keychron Q Series (Q1, Q2, Q3) ~3–4M
- Mode 65 / Mode 80 ~4–6M
- KBD75v3 / KBD67 Lite ~3–5M
- Bakeneko65 / Frog65 ~4–7M

---

### 13.4 Premium / Custom Group Buy — 8.000.000 VNĐ+

**Mục tiêu:** Collector, audiophile keyboard, premium feel.

**Gợi ý:**
- Hineybush H60/H65/H87 ~8–15M
- Mammoth75 / Mammoth65 ~10–20M
- Brutal v2 ~6–10M
- Ikki68 Aurora ~5–8M
- Tofu60/65 (entry custom) ~4–7M

---

### 13.5 Budget keycap

| Phân khúc | Tên              | Giá ước tính |
| --------- | ---------------- | ------------ |
| Budget    | Akko keycap PBT  | 200–400K     |
| Budget    | Domikey keycap   | 300–600K     |
| Mid       | ePBT keycap      | 500–1.2M     |
| Mid       | Infinikey keycap | 800–1.5M     |
| Premium   | GMK (group buy)  | 3–6M         |
| Premium   | SA set           | 2–5M         |

---

## 14. QUY TRÌNH CUSTOM KEYCAP

### 14.1 Hai loại mua keycap

**In-stock (hàng có sẵn):**
- Mua ngay, nhận ngay
- Thường là PBT dye-sub, KAT, ePBT
- Ít độc quyền hơn

**Group Buy (GB):**
- Đặt hàng tập thể, sản xuất theo đơn
- Thường 6–12 tháng chờ (hoặc lâu hơn)
- Giá thường tốt hơn IC
- Rủi ro delay, cancel
- Thường là GMK, SA, MT3, KAT exclusive

---

### 14.2 Quy trình Group Buy

```
Interest Check (IC)
      ↓
      Người thiết kế đăng concept, hỏi thăm dò nhu cầu
      Cộng đồng vote/comment
      ↓
Group Buy (GB) mở
      ↓
      Đặt cọc hoặc thanh toán toàn phần
      Hạn đặt thường 2–4 tuần
      ↓
Sản xuất
      ↓
      Nhà máy sản xuất (thường GMK, Keyreative, Signature Plastics)
      Thời gian: 3–12 tháng
      ↓
QC (Quality Control)
      ↓
      Proxy/store kiểm tra chất lượng
      ↓
Shipping
      ↓
      Vận chuyển về kho VN (nếu qua proxy)
      ↓
Nhận hàng
```

---

### 14.3 Proxy Group Buy tại Việt Nam

Vì hầu hết GB tổ chức quốc tế (Themekey, Novelkeys, Drop...), người Việt cần **mua qua proxy**:
- Proxy đặt số lượng lớn, người mua đặt qua proxy
- Proxy chịu rủi ro vận chuyển, thuế nhập khẩu
- Proxy thường thu phí service + shipping

**Proxy VN phổ biến:** Tùy cửa hàng bạn làm việc.

---

### 14.4 Thuật ngữ Group Buy

| Thuật ngữ         | Ý nghĩa                                                      |
| ----------------- | ------------------------------------------------------------ |
| **IC**            | Interest Check — thăm dò quan tâm                            |
| **GB**            | Group Buy — mở đặt hàng                                      |
| **MOQ**           | Minimum Order Quantity — số lượng tối thiểu để GB thành công |
| **Extra/Extras**  | Số lượng dư sau GB, bán sau với giá cao hơn                  |
| **Vendor**        | Cửa hàng/đại lý GB tại quốc gia                              |
| **Proxy**         | Đại diện mua hàng quốc tế cho người ở VN                     |
| **Designer**      | Người thiết kế keycap set                                    |
| **Render**        | Hình ảnh 3D của keycap set                                   |
| **Colorway**      | Phối màu của keycap set                                      |
| **Base kit**      | Bộ keycap cơ bản (đủ cho ANSI TKL/65/75)                     |
| **Novelties kit** | Phím decorative, không có chức năng                          |
| **Spacebar kit**  | Bộ spacebar thêm                                             |
| **Extension kit** | Thêm phím cho layout đặc biệt (ISO, HHKB, Alice...)          |

---

### 14.5 Quy trình custom keycap in-house (nếu cửa hàng cung cấp dịch vụ)

**Nếu cửa hàng nhận in keycap custom (in pad/UV in):**

```
Khách cung cấp:
- Thiết kế legend (font, icon, màu sắc)
- Chọn keycap nền (PBT/ABS, màu)
- Chọn profile
      ↓
Kiểm duyệt thiết kế
      ↓
Báo giá + xác nhận
      ↓
Đặt cọc
      ↓
Sản xuất (3–14 ngày tùy số lượng)
      ↓
QC + chụp ảnh gửi khách
      ↓
Thanh toán phần còn lại
      ↓
Giao hàng
```

---

## 15. THƯƠNG HIỆU & KIT PHỔ BIẾN

### 15.1 Thương hiệu Switch

| Thương hiệu       | Xuất xứ    | Nổi tiếng với                      |
| ----------------- | ---------- | ---------------------------------- |
| **Cherry**        | Đức        | MX Red/Blue/Brown — chuẩn gốc      |
| **Gateron**       | Trung Quốc | Mượt hơn Cherry, giá tốt           |
| **Kailh**         | Trung Quốc | Box switch, Speed switch           |
| **Akko**          | Trung Quốc | Giá tốt, phổ biến VN               |
| **JWK/Durock**    | Trung Quốc | Smooth linear cao cấp (Alpaca, T1) |
| **TTC**           | Trung Quốc | Speed Silver, Gold Pink            |
| **Boba (Gazzew)** | Trung Quốc | Tactile U4/U4T nổi tiếng           |
| **Novelkeys**     | Mỹ         | NK Cream — iconic thocky           |
| **SP Star**       | Trung Quốc | Giá tốt, smooth                    |

---

### 15.2 Thương hiệu Keycap

| Thương hiệu            | Profile          | Material        | Phân khúc   |
| ---------------------- | ---------------- | --------------- | ----------- |
| **GMK**                | Cherry           | ABS doubleshot  | Premium     |
| **Signature Plastics** | SA/DSA           | ABS/PBT         | Premium     |
| **Drop**               | MT3, others      | ABS doubleshot  | Mid-Premium |
| **ePBT**               | Cherry           | PBT dye-sub     | Mid         |
| **Infinikey**          | Cherry           | PBT dye-sub     | Mid         |
| **KAT (Keyreative)**   | KAT              | PBT dye-sub     | Mid         |
| **Akko**               | OEM, Cherry-like | PBT dye-sub     | Budget-Mid  |
| **Domikey**            | Cherry, SA       | PBT dye-sub/ABS | Budget-Mid  |
| **NicePBT / ePBT**     | Cherry           | PBT dye-sub     | Mid         |

---

### 15.3 Thương hiệu Bàn phím Custom phổ biến

| Thương hiệu  | Xuất xứ | Phân khúc                |
| ------------ | ------- | ------------------------ |
| **Keychron** | HK      | Mid — Q series, V series |
| **Akko**     | CN      | Budget-Mid — MOD series  |
| **KBDfans**  | CN      | Mid-Premium              |
| **Mode**     | US      | Premium                  |
| **Bakeneko** | CN      | Mid-Premium              |
| **Frog**     | US/CN   | Mid-Premium              |
| **Ikki68**   | CN      | Mid                      |
| **GMMK Pro** | US      | Mid                      |
| **Wooting**  | NL      | Gaming premium           |
| **Lamzu**    | CN      | Gaming Mid-Premium       |

---

## 16. MOD PHỔ BIẾN

### 16.1 Band-aid Mod

**Mô tả:** Dán miếng foam nhỏ lên PCB tại vị trí tiếp xúc wire của stab.
**Tác dụng:** Giảm tiếng rattle của stab.
**Chi phí:** ~0 (dùng band-aid hoặc foam thừa)

---

### 16.2 Tempest Mod / Potato Mod

**Mô tả:** Nhét foam/tissue vào không gian rỗng trong case.
**Tác dụng:** Giảm hollow sound của case.
**Chi phí:** ~0

---

### 16.3 Tape Mod

**Mô tả:** Dán masking tape lên mặt dưới PCB.
**Tác dụng:** Thay đổi âm thanh, thường thocky hơn.
**Chi phí:** ~0

---

### 16.4 PE Foam Mod

**Mô tả:** Lớp PE foam giữa switch và PCB.
**Tác dụng:** Poppy, muffled, đặc âm thanh.
**Chi phí:** 20–50K

---

### 16.5 North-facing vs South-facing LED Fix

Khi PCB có LED north-facing (LED phía trên switch), Cherry profile keycap bị interference (ánh sáng yếu, không đều do leg keycap che LED).
**Fix:** Dùng PCB south-facing hoặc chọn keycap profile không bị ảnh hưởng (GMK R4, XDA, SA).

---

### 16.6 Switch Film

**Mô tả:** Miếng film mỏng kẹp giữa top và bottom housing của switch.
**Tác dụng:** Giảm wobble (lắc) của stem, cải thiện âm thanh và cảm giác.
**Chi phí:** 50–100K/100 film

---

### 16.7 Spring Swap

Thay lò xo trong switch để thay đổi lực bấm (nhẹ hơn hoặc nặng hơn) mà không cần đổi switch.

---

### 16.8 Stab Holee Mod

Thêm miếng foam nhỏ vào stab để giảm rattle và cải thiện sound.

---

## 17. BẢO TRÌ & VỆ SINH BÀN PHÍM

### 17.1 Vệ sinh hàng ngày

- Dùng **compressed air** xịt bụi giữa các phím mỗi tuần
- Lau mặt keycap bằng khăn microfiber khô
- Không ăn uống trên bàn phím

### 17.2 Vệ sinh sâu (1–3 tháng/lần hoặc khi cần)

**Keycap:**
1. Tháo toàn bộ keycap (dùng keycap puller)
2. Ngâm trong nước ấm + một chút xà phòng (30–60 phút)
3. Khuấy nhẹ, tráng nước sạch
4. Để khô hoàn toàn trước khi gắn lại (**quan trọng — không để ẩm**)

**Switch:**
- Nếu hotswap: rút switch ra, dùng compressed air và/hoặc tăm bông + isopropyl alcohol
- Nếu switch lọt nước/bụi nhiều: ngâm trong IPA (nếu switch không có PCB-mounted LED)

**Case và plate:**
- Lau bằng IPA + khăn microfiber
- Với case nhôm: có thể dùng polish nhẹ

### 17.3 Xử lý khi đổ nước

1. **Tắt nguồn ngay lập tức / rút cáp**
2. Lật ngược bàn phím để thoát nước
3. Tháo keycap, tháo switch nếu hotswap
4. Để khô hoàn toàn 24–48 giờ
5. Dùng IPA lau bảng mạch nếu có
6. **Không cắm điện khi còn ẩm**

---

## 18. CHÍNH SÁCH BÁO GIÁ, BẢO HÀNH, GIAO HÀNG

> Nguồn chính sách: đồng bộ theo trang Policies của Keycap Shop. Khi tư vấn, AI chỉ được cam kết trong phạm vi nội dung dưới đây. Nếu khách hỏi mức giá dịch vụ custom/lube/mod cụ thể chưa có trong dữ liệu, hãy nói cần staff xác nhận báo giá cuối cùng.

### 18.1 Điều khoản dịch vụ

**Khi khách truy cập và mua sắm trên website:**
- Khách hàng đồng ý tuân thủ các điều khoản và điều kiện của Keycap Shop.
- Shop có quyền thay đổi, chỉnh sửa hoặc cập nhật điều khoản vào bất kỳ lúc nào mà không cần thông báo trước.
- Khách hàng cần cung cấp thông tin cá nhân chính xác như họ tên, số điện thoại và địa chỉ để phục vụ giao hàng.
- Không sử dụng website cho mục đích gian lận, phá hoại hoặc phát tán mã độc.
- Hình ảnh và nội dung trên website thuộc bản quyền Keycap Shop; việc sao chép cần có sự đồng ý bằng văn bản.

---

### 18.2 Chính sách bảo mật

**Cam kết bảo vệ dữ liệu khách hàng:**
- Shop chỉ thu thập thông tin cần thiết cho xử lý đơn hàng và chăm sóc khách hàng, bao gồm tên, số điện thoại, địa chỉ và email.
- Mật khẩu và thông tin thẻ thanh toán được mã hóa.
- Shop không lưu trữ thông tin thẻ tín dụng của khách hàng trên máy chủ.
- Cam kết không mua bán, trao đổi hoặc chia sẻ dữ liệu khách hàng cho bên thứ ba vì mục đích thương mại.

---

### 18.3 Chính sách thanh toán

**Phương thức thanh toán:**
- Hỗ trợ thanh toán trực tuyến bằng mã QR, chuyển khoản nhanh qua thẻ ATM nội địa, Visa/Mastercard quốc tế thông qua cổng thanh toán bảo mật VNPAY hoặc PayOS.
- Với PayOS, khách hàng thanh toán qua mã QR được cấp tự động sau khi đặt hàng.
- Đối với đơn custom keycap riêng, khách hàng cần đặt cọc trước 50% giá trị đơn hàng để shop chuẩn bị vật liệu và sản xuất.
- Phần còn lại của đơn custom được thanh toán khi nhận hàng.

---

### 18.4 Chính sách bảo hành

**Áp dụng cho sản phẩm keycap bán ra:**
- Bảo hành 1 đổi 1 trong 30 ngày nếu phát hiện lỗi từ nhà sản xuất.
- Các lỗi được hỗ trợ gồm: nứt, gãy stem, thiếu phím hoặc sai màu so với mô tả.
- Bảo hành phai màu trọn đời chỉ áp dụng với keycap dùng công nghệ in Dye-sub hoặc Double-shot.

**Không bảo hành trong các trường hợp:**
- Sản phẩm bị rơi vỡ, trầy xước do ngoại lực.
- Mài mòn hoặc hư hỏng do sử dụng sai cách.
- Tự ý dùng hóa chất tẩy rửa mạnh làm hỏng bề mặt keycap.

---

### 18.5 Chính sách đổi trả

**Thời hạn và điều kiện đổi trả:**
- Shop hỗ trợ đổi trả trong vòng 7 ngày kể từ ngày khách nhận hàng.
- Sản phẩm phải còn nguyên vẹn, chưa qua sử dụng, không trầy xước và không dính bẩn.
- Còn đầy đủ hộp nguyên bản, vỏ bọc và phụ kiện đi kèm như keypuller, nhãn dán hoặc switch tặng kèm nếu có.

**Phí vận chuyển đổi trả:**
- Nếu lỗi do shop, ví dụ giao sai hàng hoặc hàng lỗi, shop chịu 100% phí vận chuyển.
- Nếu khách đổi trả do thay đổi ý định, khách chịu toàn bộ phí vận chuyển 2 chiều.

---

### 18.6 Chính sách vận chuyển

**Phí vận chuyển:**
- Miễn phí vận chuyển cho mọi đơn hàng từ 1.250.000đ trở lên.
- Miễn phí vận chuyển cho các đơn đặt làm custom keycap đặc biệt.
- Phí tiêu chuẩn toàn quốc: 30.000đ.
- Khu vực nội thành TP.HCM hoặc khu vực lân cận chi nhánh shop: 15.000đ.

**Thời gian giao hàng:**
- Nội thành TP.HCM/Hà Nội: 1-2 ngày.
- Các tỉnh/thành phố khác: 3-5 ngày.
- Đơn pre-order tuân theo thời gian thông báo riêng trên trang sản phẩm.

---

### 18.7 Chính sách báo giá và tư vấn custom

**Khi tư vấn giá cho khách:**
- Giá sản phẩm có sẵn dựa trên giá hiển thị trên website tại thời điểm đặt hàng.
- Với custom keycap, AI cần hỏi rõ layout, theme/concept, ảnh reference nếu có, chất liệu hoặc yêu cầu đặc biệt.
- Báo giá custom cuối cùng cần staff xác nhận sau khi kiểm tra độ phức tạp thiết kế, vật liệu và thời gian sản xuất.
- Không tự đưa ra phí lube, phí mod, phí thiết kế hoặc phí lắp ráp cụ thể nếu dữ liệu sản phẩm/dịch vụ chưa cung cấp con số chính thức.

---

## 19. CÂU TRẢ LỜI MẪU

### 19.1 Khách chưa biết gì — Giới thiệu tổng quát

**Q: Bàn phím cơ là gì? Có đáng mua không?**

> "Bàn phím cơ khác bàn phím bình thường ở chỗ mỗi phím có một switch riêng — bạn có thể cảm nhận rõ 'cảm giác click' khi bấm, ngón tay biết chính xác khi nào phím được kích hoạt.
>
> Đáng mua hay không tùy nhu cầu:
> - Gõ nhiều (lập trình, viết lách) → rất đáng, tay ít mỏi hơn
> - Gaming → đáng nếu muốn response tốt hơn
> - Văn phòng bình thường → tùy sở thích và ngân sách
>
> Hiện tại bàn phím cơ có giá từ ~500K đến vài triệu, mình có thể tư vấn theo ngân sách của bạn nhé?"

---

**Q: Mình mới dùng lần đầu, không biết chọn gì?**

> "Không sao, mình sẽ hỏi 3 câu thôi:
> 1. **Bạn dùng để làm gì?** (gaming, gõ văn bản, lập trình, hay đa năng?)
> 2. **Ngân sách khoảng bao nhiêu?** (dưới 1tr, 1–3tr, hay trên 3tr?)
> 3. **Bàn làm việc có rộng không?** (ảnh hưởng đến kích thước bàn phím nên chọn)
>
> Trả lời 3 câu này là mình có thể gợi ý ngay phù hợp nhất cho bạn!"

---

### 19.2 Khách hỏi về switch

**Q: Dùng switch gì tốt? Red, Blue hay Brown?**

> "Ba loại này khác nhau về cảm giác:
> - **Red (Linear):** Mượt, êm, không có điểm cộm. Phổ biến cho gaming.
> - **Blue (Clicky):** Có tiếng 'click' to khi bấm. Thỏa mãn khi gõ nhưng ồn.
> - **Brown (Tactile):** Có điểm cộm nhẹ khi bấm nhưng không click. Cân bằng.
>
> Nếu bạn ngồi làm việc nơi có người khác → nên tránh Blue.
> Nếu gaming → Red hoặc Yellow nhẹ hơn.
> Nếu muốn cảm giác gõ rõ ràng, không muốn ồn → Brown hoặc Boba U4.
>
> Bạn hay dùng bàn phím ở đâu và làm gì nhỉ?"

---

### 19.3 Khách hỏi về layout

**Q: Mua 60%, 75% hay TKL?**

> "Tùy nhu cầu bạn nhé:
> - **TKL (bỏ Numpad):** Phổ biến nhất, đủ phím, chuột đặt gần hơn. Lý tưởng cho hầu hết mọi người.
> - **75%:** Nhỏ gọn hơn TKL, vẫn có F1–F12 và arrow. Phù hợp bàn nhỏ hoặc hay di chuyển.
> - **60%:** Rất nhỏ gọn nhưng bỏ cả arrow key và F-row. Cần học dùng layer. Hợp minimalist.
>
> Bạn có thường dùng phím mũi tên và F1–F12 không? Đó là câu hỏi quan trọng nhất."

---

### 19.4 Khách hỏi về ngân sách

**Q: Budget 2 triệu mua được gì tốt?**

> "Với 2 triệu bạn có nhiều lựa chọn tốt:
> - **Akko MOD007B:** Gasket mount, hotswap, âm thanh rất hay cho tầm giá. ~1.8–2.2M tùy phiên bản.
> - **Keychron V2 (TKL):** QMK firmware, hotswap, build chắc. ~1.8M.
> - **Cidoo V65:** 65%, gasket, hotswap. ~2–2.5M.
>
> Cả ba đều dùng được ngay, có thể nâng cấp switch sau nếu muốn. Bạn thích layout nào?"

---

### 19.5 Khách hỏi về âm thanh

**Q: Làm sao để bàn phím kêu 'thock thock' nghe hay vậy?**

> "Âm thanh 'thock' đến từ nhiều yếu tố kết hợp:
> 1. **Gasket mount case** — kiểu gắn đàn hồi cho âm trầm, ít ping
> 2. **Switch NK Cream hoặc Gateron lubed** — thocky nổi tiếng
> 3. **PBT keycap** — âm trầm hơn ABS
> 4. **PE foam mod + case foam** — loại bỏ hollow sound
> 5. **Lube switch và stab đúng cách** — không rattle, không scratch
>
> Bàn phím thocky tốt trong tầm 2–3M: **Akko MOD007B** hoặc **Keychron Q series**. Bạn muốn nghe thử video demo không?"

---

### 19.6 Khách hỏi về keycap

**Q: PBT và ABS khác nhau thế nào?**

> "PBT bền hơn, không bị bóng dầu sau vài tháng dùng, âm thanh thường trầm hơn. ABS thì màu sắc đẹp hơn, legend sắc nét hơn (đặc biệt GMK), nhưng dễ bóng dầu.
>
> Dùng hàng ngày → nên chọn PBT dye-sub (như Akko PBT, ePBT).
> Muốn bộ keycap đẹp, premium → GMK (ABS doubleshot) nhưng giá cao hơn.
>
> Ngân sách của bạn cho keycap khoảng bao nhiêu?"

---

### 19.7 Khách hỏi về custom / mod

**Q: Mình muốn tự lắp bàn phím, bắt đầu từ đâu?**

> "Custom keyboard nghe có vẻ phức tạp nhưng thực ra khá vui! Trình tự cơ bản:
> 1. **Chọn case** (xác định layout, budget, chất liệu)
> 2. **Chọn PCB** (hotswap cho người mới — dễ đổi switch)
> 3. **Chọn plate** (nhôm/PC/FR4)
> 4. **Chọn switch** (linear/tactile/clicky theo sở thích)
> 5. **Chọn stab** (Durock V2 là safe choice)
> 6. **Chọn keycap** (profile và màu sắc)
> 7. **Lube switch + stab** (bước quan trọng nhất)
> 8. **Lắp ráp**
>
> Ngân sách bắt đầu từ khoảng 3–4M cho một build decent. Mình có thể tư vấn từng bước cho bạn nhé — bạn muốn bắt đầu từ đâu?"

---

### 19.8 Khách phân vân giữa 2 sản phẩm

**Q: Akko MOD007B hay Keychron Q3 thì tốt hơn?**

> "Cả hai đều rất tốt ở tầm giá này, nhưng khác nhau một chút:
>
> | | Akko MOD007B | Keychron Q3 |
> |--|--|--|
> | Layout | 75% | TKL (80%) |
> | Mount | Gasket | Gasket |
> | Firmware | Tùy phiên bản | QMK/VIA |
> | Giá | ~1.8–2.2M | ~3–3.5M |
> | Âm thanh | Rất tốt | Rất tốt |
>
> Nếu budget ~2M và muốn compact → MOD007B.
> Nếu muốn TKL đầy đủ, cần QMK remap tự do → Q3 xứng đáng thêm tiền.
>
> Bạn cần TKL hay 75% phù hợp hơn?"

---

### 19.9 Khách hỏi giao hàng/bảo hành

**Q: Shop bảo hành bao lâu, giao hàng mất mấy ngày?**

> "Bạn ơi:
> - **Bảo hành keycap:** 1 đổi 1 trong 30 ngày nếu lỗi từ nhà sản xuất như nứt, gãy stem, thiếu phím hoặc sai màu so với mô tả.
> - **Bảo hành phai màu:** trọn đời với keycap Dye-sub hoặc Double-shot.
> - **Giao nội thành TP.HCM/Hà Nội:** khoảng 1-2 ngày.
> - **Tỉnh/thành khác:** khoảng 3-5 ngày.
> - **Phí ship:** 30.000đ toàn quốc, 15.000đ nội thành TP.HCM/khu vực gần chi nhánh; miễn phí ship cho đơn từ 1.250.000đ hoặc đơn custom keycap đặc biệt.
>
> Bạn ở khu vực nào để mình ước tính phí ship chính xác hơn nhé?"

---

## 20. GLOSSARY

| Thuật ngữ                         | Giải thích                                                  |
| --------------------------------- | ----------------------------------------------------------- |
| **Actuation point**               | Điểm kích hoạt phím (khi tín hiệu được gửi đến máy tính)    |
| **Bottom-out**                    | Bấm phím chạm đến đáy hoàn toàn                             |
| **Pre-travel**                    | Khoảng cách từ trên xuống đến điểm actuation                |
| **Total travel**                  | Tổng khoảng di chuyển từ trên xuống đáy                     |
| **Bump**                          | Điểm cộm trong tactile switch                               |
| **Clicky jacket**                 | Cơ chế tạo tiếng click bằng vỏ bọc quanh stem (Cherry Blue) |
| **Click bar**                     | Thanh kim loại tạo tiếng click (Kailh Box)                  |
| **Stem**                          | Cần gạt bên trong switch                                    |
| **Housing**                       | Vỏ của switch                                               |
| **Spring**                        | Lò xo bên trong switch                                      |
| **Leaf spring**                   | Tiếp điểm điện trong switch                                 |
| **PCB**                           | Printed Circuit Board — bảng mạch in                        |
| **Plate**                         | Tấm kim loại/nhựa giữ switch                                |
| **Hotswap**                       | Tính năng thay switch không cần hàn                         |
| **QMK**                           | Firmware mã nguồn mở — tùy biến bàn phím sâu nhất           |
| **VIA/VIAL**                      | Giao diện GUI để remap phím với QMK                         |
| **Layer**                         | Lớp phím ảo — bấm Fn để switch layer                        |
| **N-key rollover (NKRO)**         | Nhận tất cả phím bấm đồng thời không mất phím               |
| **Anti-ghosting**                 | Không nhầm phím khi bấm nhiều phím cùng lúc                 |
| **Polling rate**                  | Tần suất bàn phím báo cáo trạng thái (Hz) — 1000Hz = 1ms    |
| **Lube**                          | Mỡ bôi trơn switch/stab                                     |
| **Stab**                          | Stabilizer — cơ cấu giữ phím lớn không lắc                  |
| **Rattle**                        | Tiếng lắc cọc cạch của stab tệ                              |
| **Ping**                          | Tiếng kim loại cao tần khó chịu trong bàn phím nhôm         |
| **Thock**                         | Âm thanh trầm, đục, được ưa chuộng                          |
| **Clack**                         | Âm thanh cao, sắc                                           |
| **WPM**                           | Words Per Minute — tốc độ gõ                                |
| **Endgame**                       | Bàn phím/setup không cần nâng cấp thêm (cảm giác hoàn hảo)  |
| **GMK**                           | Nhà sản xuất keycap ABS Cherry profile cao cấp tại Đức      |
| **ePBT**                          | Nhà sản xuất keycap PBT dye-sub phổ biến Trung Quốc         |
| **SA**                            | Keycap profile cao, vintage, sculpted spherical             |
| **MT3**                           | Keycap profile của Drop, cao, sculpted spherical            |
| **Cherry profile**                | Keycap profile thấp-vừa, sculpted, phổ biến nhất            |
| **Group buy (GB)**                | Đặt hàng tập thể để sản xuất keycap/bàn phím                |
| **IC**                            | Interest Check — thăm dò nhu cầu trước GB                   |
| **Proxy**                         | Đại diện mua hàng quốc tế                                   |
| **Gasket mount**                  | Kiểu gắn plate/PCB bằng đệm mềm, flex nhiều nhất            |
| **Tray mount**                    | Kiểu gắn cứng nhắc nhất                                     |
| **Hall Effect / Magnetic switch** | Switch dùng cảm biến từ, không mài mòn                      |
| **Analog switch**                 | Switch có thể nhận nhiều mức lực khác nhau (Wooting)        |
| **Rapid trigger**                 | Tính năng actuation lại ngay khi phím nhả ra một khoảng nhỏ |
| **Dye-sub**                       | Kỹ thuật in legend bằng nhuộm nhiệt vào PBT                 |
| **Doubleshot**                    | Kỹ thuật đúc 2 lớp nhựa để legend không phai                |
| **Shine-through**                 | Keycap/switch trong suốt để LED chiếu qua                   |
| **Encoder/Knob**                  | Núm xoay tích hợp trên một số bàn phím custom               |
| **Daughterboard**                 | PCB phụ chứa cổng USB                                       |
| **Flashing**                      | Nạp firmware vào PCB                                        |
| **Bootloader**                    | Chế độ đặc biệt để flash firmware                           |
| **WKL**                           | Winkey-less — layout không có phím Windows                  |
| **HHKB layout**                   | Happy Hacking Keyboard layout — biến thể 60% đặc biệt       |
| **Alice layout**                  | Layout ergonomic split-angle phổ biến                       |
| **Ergo layout**                   | Bất kỳ layout nào được thiết kế theo ergonomics             |
| **Knob**                          | Núm xoay chỉnh âm lượng/chức năng                           |
| **Bespoke**                       | Custom theo đặt hàng riêng                                  |
| **Desoldering**                   | Tháo switch khỏi PCB hàn bằng cách rút chì                  |

---

## 21. FAQ TỔNG HỢP

**Q: Bàn phím cơ có tốt cho gaming hơn membrane không?**
A: Có một số lợi thế: actuation point rõ ràng hơn, N-key rollover, polling rate cao. Tuy nhiên, với gaming casual thì membrane đủ dùng. Gaming cạnh tranh (esport) thì linear switch nhẹ trên bàn phím cơ có lợi thế nhất định.

**Q: Cherry MX hay Gateron tốt hơn?**
A: Gateron thường được đánh giá mượt hơn Cherry ở cùng loại (Red, Yellow, Black). Cherry bền hơn về thương hiệu và lịch sử. Với cùng giá, Gateron thường là lựa chọn tốt hơn về cảm giác.

**Q: Có cần lube switch không?**
A: Không bắt buộc nhưng rất đáng làm. Lube switch làm bàn phím mượt hơn, ít scratch, âm thanh hay hơn đáng kể. Stab thì **bắt buộc lube** để không bị rattle.

**Q: Bàn phím cơ có dùng được cho laptop không?**
A: Có, kết nối qua USB hoặc Bluetooth. Hầu hết bàn phím cơ hiện đại đều có cổng USB-C.

**Q: Wireless hay có dây tốt hơn cho gaming?**
A: Có dây vẫn được ưa thích cho gaming cạnh tranh (polling rate ổn định hơn, không lo pin). Wireless OK cho gaming casual và văn phòng.

**Q: Keycap GMK có gì đặc biệt mà đắt vậy?**
A: GMK sản xuất tại Đức bằng ABS doubleshot Cherry profile với chất lượng legend sắc nét nhất, màu sắc chính xác nhất, và texture đặc trưng. Đắt vì: sản xuất châu Âu, số lượng có hạn qua group buy, phí thiết kế.

**Q: Bàn phím cơ có dùng được trên Mac không?**
A: Có. Cần remap một số phím (Command/Option/Windows key) qua firmware hoặc phần mềm. Nhiều bàn phím như Keychron có chế độ switch Mac/Windows.

**Q: Mua bàn phím ở đâu uy tín?**
A: Cửa hàng chuyên bàn phím cơ, trang Shopee/Lazada shop uy tín (xem đánh giá), hoặc trực tiếp qua cộng đồng keyboard VN (Facebook groups, Voz forum).

**Q: 60% có phím arrow không?**
A: Không có arrow keys mặc định. Cần bấm Fn + tổ hợp (thường Fn+WASD hoặc Fn+IJKL) để dùng arrow.

**Q: Mình bị đổ nước vào bàn phím, phải làm sao?**
A: Tắt nguồn ngay, rút cáp, lật ngược, tháo keycap. Để khô 24–48 giờ. Không cắm điện khi còn ẩm. Nếu cần, dùng IPA (cồn y tế) làm sạch PCB.

**Q: Cần wrist rest không?**
A: Không bắt buộc, nhưng nên dùng nếu gõ nhiều giờ. Đặc biệt quan trọng với profile cao (SA, MT3). Chọn wrist rest foam/leather/wood tùy sở thích.

**Q: Tại sao switch giống nhau nhưng âm thanh khác nhau giữa các bàn phím?**
A: Vì âm thanh bị ảnh hưởng bởi rất nhiều yếu tố: case material, mounting style, plate material, foam mods, keycap profile/material, stab chất lượng. Switch chỉ là một phần.

---

*Tài liệu này được cập nhật bởi team tư vấn bàn phím cơ. Giá và thông tin sản phẩm có thể thay đổi theo thời gian thị trường.*

*Phiên bản: 1.0 | Ngày tạo: 2024*
