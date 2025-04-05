package br.com.meetime.hubspot.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventSubscriptionProcessDTO {

    @JsonProperty("active")
    private boolean active;

    public EventSubscriptionProcessDTO() {
    }

    public EventSubscriptionProcessDTO(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "EventSubscriptionProcessDTO{" +
                "active=" + active +
                '}';
    }
}
