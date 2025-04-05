package br.com.meetime.hubspot.v1.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RateLimitConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRateLimiting() throws Exception {
        for (int i = 0; i < 100; i++) {
            mockMvc.perform(get("/api/v1/hubspot/auth-url")
                            .param("clientId", "test-client-" + i))
                    .andExpect(status().isOk());
        }

        MvcResult result = mockMvc.perform(get("/api/v1/hubspot/auth-url")
                        .param("clientId", "test-client-101"))
                .andExpect(status().isTooManyRequests())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Too many requests"));
        assertTrue(content.contains("Rate limit exceeded"));

        System.out.println(content);
    }
}