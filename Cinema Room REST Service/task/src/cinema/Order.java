package cinema;

import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Label;
import jdk.jfr.Name;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.UUID;

public class Order {
    UUID token;
    Seat ticket;

    public Order(UUID token, Seat ticket) {
        this.token = token;
        this.ticket = ticket;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Seat getTicket() {
        return ticket;
    }

    public void setTicket(Seat ticket) {
        this.ticket = ticket;
    }
}
