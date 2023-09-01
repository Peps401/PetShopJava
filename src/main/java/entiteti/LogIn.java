package entiteti;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LogIn {

    private Long id;
    private User user;
    private LocalDateTime time;

    private BigDecimal discount;

    public LogIn(Long id, User user, LocalDateTime time, BigDecimal discount) {
        this.id = id;
        this.user = user;
        this.time = time;
        this.discount = discount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
