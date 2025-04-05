package br.com.meetime.hubspot.v1.enums;

public enum EventTypeSubscriptionEnum {

    CONTACT_CREATION("contact.creation");

    private final String type;

    EventTypeSubscriptionEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
