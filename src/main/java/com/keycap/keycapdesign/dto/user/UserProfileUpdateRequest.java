package com.keycap.keycapdesign.dto.user;

import jakarta.validation.constraints.Pattern;

public class UserProfileUpdateRequest {
    private String fullName;
    @Pattern(regexp = "^$|^(03|05|08|09)\\d{8}$", message = "Phone must be 10 digits and start with 03, 05, 08 or 09")
    private String phone;
    private String avatarUrl;
    private String bankAccount;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getBankAccount() { return bankAccount; }
    public void setBankAccount(String bankAccount) { this.bankAccount = bankAccount; }
}
