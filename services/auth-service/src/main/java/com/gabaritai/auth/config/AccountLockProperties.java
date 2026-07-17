package com.gabaritai.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.account-lock")
public class AccountLockProperties {

    /** Numero de tentativas de login invalidas antes de bloquear temporariamente a conta (UC-002). */
    private int maxFailedAttempts = 5;

    /** Duracao do bloqueio temporario, em minutos. */
    private long lockDurationMinutes = 15;

    public int getMaxFailedAttempts() {
        return maxFailedAttempts;
    }

    public void setMaxFailedAttempts(int maxFailedAttempts) {
        this.maxFailedAttempts = maxFailedAttempts;
    }

    public long getLockDurationMinutes() {
        return lockDurationMinutes;
    }

    public void setLockDurationMinutes(long lockDurationMinutes) {
        this.lockDurationMinutes = lockDurationMinutes;
    }
}
