package br.com.meetime.hubspot.dto.response.webhook;

import java.util.List;

public class WebhookEventResponse {
    private List<WebhookEvent> results;

    public List<WebhookEvent> getResults() {
        return results;
    }

    public void setResults(List<WebhookEvent> results) {
        this.results = results;
    }
}
