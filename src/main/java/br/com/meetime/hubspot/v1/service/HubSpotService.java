package br.com.meetime.hubspot.v1.service;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HubSpotService {

    //url
    @Value("${hubspot.url.base}")
    private String baseUrl;

    @Value("${hubspot.url.redirect}")
    private String redirectUrl;

    @Value("${hubspot.url.auth}")
    private String authRoute;

    @Value("${hubspot.url.token}")
    private String tokenRoute;

    @Value("${hubspot.url.contacts}")
    private String contactsRoute;

    @Value("${hubspot.url.webhook}")
    private String webhookRoute;

    @Value("${hubspot.url.webhook.subscriptions}")
    private String subscriptionsRoute;

    //ETC
    @Value("${hubspot.auth.scope}")
    private String scope;

    @Value("${hubspot.token.grantType}")
    private String grantType;

    @Value("${hubspot.key.appId}")
    private String appId;

    public String getUrl(String clientId) {
        return String.format("%s%s/?client_id=%s&redirect_uri=%s&scope=%s", baseUrl, authRoute, clientId, redirectUrl, scope);
    }

    public HttpResponse<String> postTokenAccess(String clientId, String clientSecret, String code) {
        String url = String.format("%s%s", baseUrl, tokenRoute);
        return Unirest.post(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("grant_type", grantType)
                .field("client_id", clientId)
                .field("client_secret", clientSecret)
                .field("redirect_uri", redirectUrl)
                .field("code", code)
                .asString();

    }

    public HttpResponse<String> postCreateAccount(String token, String jsonBody) {
        String url = String.format("%s%s", baseUrl, tokenRoute);
        return Unirest.post(url)
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .asString();
    }

    public HttpResponse<String> getSubscriptions(String hapikey) {
        String url = String.format("%s%s/%s%s?hapikey=%s", baseUrl, webhookRoute, appId, subscriptionsRoute,hapikey);
        return Unirest.get(url)
                .asString();
    }

    public HttpResponse<String> patchUpdateSubscriptions(String idSubscription, String hapikey, String jsonBody) {
        String url = String.format("%s%s/%s/subscriptions/%s?hapikey=%s", baseUrl, webhookRoute, appId, idSubscription, hapikey);
        return Unirest.patch(url)
                .header("content-type", "application/json")
                .body(jsonBody)
                .asString();
    }
}
