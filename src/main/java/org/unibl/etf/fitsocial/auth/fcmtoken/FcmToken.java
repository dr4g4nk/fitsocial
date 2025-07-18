package org.unibl.etf.fitsocial.auth.fcmtoken;

import jakarta.persistence.*;
import org.unibl.etf.fitsocial.auth.user.User;

import java.time.Instant;

@Entity
@Table(name = "fcm_token", schema = "auth")
public class FcmToken {

    @Id
    @Column(name = "token")
    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "timestamp")
    private Instant timestamp;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
