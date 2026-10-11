package com.farmconnect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.farmconnect.dto.BuyerRegistrationRequest;
import com.farmconnect.dto.FarmerRegistrationRequest;
import com.farmconnect.dto.OrderItemRequest;
import com.farmconnect.dto.OrderPlacementRequest;
import com.farmconnect.dto.ProductCreateUpdateRequest;
import com.farmconnect.dto.SupportAdviceReplyRequest;
import com.farmconnect.dto.SupportTicketRequest;
import com.farmconnect.entity.ProductCategory;
import com.farmconnect.entity.SupportTopic;
import com.farmconnect.entity.SupportUrgency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FarmConnectIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("API INTEGRATION: Farmer registration & privacy protection layer check")
    void testFarmerRegistrationAndCatalogPrivacy() throws Exception {
        FarmerRegistrationRequest reg = new FarmerRegistrationRequest();
        reg.setEmail("integration.farmer" + System.currentTimeMillis() + "@test.org");
        reg.setPassword("secretPass123");
        reg.setFullName("Anand Sharma");
        reg.setPhone("+91 98333 44444");
        reg.setFarmName("Sharma Biodynamic Estate");
        reg.setRegionCode("Zone-Central-Hills-09");
        reg.setExactAddress("Private Street 88, Confidential Hamlet, MH");
        reg.setFarmingExperienceYears(15);
        reg.setIsOrganicCertified(true);
        reg.setBio("Organic coffee, black pepper and mountain fruits grower.");

        mockMvc.perform(post("/api/auth/register/farmer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reg)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.farmerProfileId").exists())
                .andExpect(jsonPath("$.anonymizedRegionCode").value("Zone-Central-Hills-09"));

        // Verify that public catalog endpoint does not leak farmer's legal name, phone or address
        mockMvc.perform(get("/api/catalog/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(content().string(not(containsString("Private Street 88"))))
                .andExpect(content().string(not(containsString("+91 98333 44444"))));
    }

    @Test
    @DisplayName("API INTEGRATION: Cultivation Support Ticket & Expert Agronomist Response flow")
    void testCultivationSupportTicketLifecycle() throws Exception {
        SupportTicketRequest ticketReq = new SupportTicketRequest();
        ticketReq.setFarmerId(1L);
        ticketReq.setTopic(SupportTopic.PEST_CONTROL);
        ticketReq.setTitle("Spotted stem borer in sweet corn plot");
        ticketReq.setDescription("Found pinholes on whorl leaves and frass near leaf axils.");
        ticketReq.setUrgency(SupportUrgency.HIGH);
        ticketReq.setCropType("Sweet Corn");

        String ticketResponseJson = mockMvc.perform(post("/api/support/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andReturn().getResponse().getContentAsString();

        Long ticketId = objectMapper.readTree(ticketResponseJson).get("id").asLong();

        // Agronomist (Expert ID 6) replies to the ticket
        SupportAdviceReplyRequest replyReq = new SupportAdviceReplyRequest();
        replyReq.setResponderId(6L);
        replyReq.setGuidanceText("Release Trichogramma chilonis egg parasitoid cards @ 100,000/ha. Apply Bacillus thuringiensis (Bt kurstaki) @ 2g/liter in evening.");
        replyReq.setRecommendedResources("IPM Factsheet: Biological Management of Corn Stem Borer.");

        mockMvc.perform(post("/api/support/requests/" + ticketId + "/reply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(replyReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RESOLVED"))
                .andExpect(jsonPath("$.responses", hasSize(1)))
                .andExpect(jsonPath("$.responses[0].guidanceText").value(org.hamcrest.Matchers.containsString("Trichogramma")));
    }

    @Test
    @DisplayName("API INTEGRATION: Browse Seasonal Agricultural Tips")
    void testSeasonalTipsRetrieval() throws Exception {
        mockMvc.perform(get("/api/support/tips"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].content").exists());
    }
}
