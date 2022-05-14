package com.hms.service.dto;

import com.hms.domain.User;
import com.hms.domain.Wallet;

public class WalletUserDTO {
    private Wallet wallet;
    private User user;

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
