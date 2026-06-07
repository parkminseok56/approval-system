package com.example.approval.approval;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class ApprovalApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void approvalFlow_createApproveComplete() throws Exception {
        signup("user1", "1234", "홍길동");
        signup("user2", "1234", "김철수");
        signup("user3", "1234", "이영희");

        String token1 = login("user1", "1234");
        String token2 = login("user2", "1234");
        String token3 = login("user3", "1234");

        Long approver2Id = getUserId(token2);
        Long approver3Id = getUserId(token3);

        Long approvalId = createApproval(token1, approver2Id, approver3Id);

        mockMvc.perform(get("/api/approvals/" + approvalId)
                        .header("Authorization", "Bearer " + token1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.lines[0].status").value("PENDING"));

        mockMvc.perform(post("/api/approvals/" + approvalId + "/approve")
                        .header("Authorization", "Bearer " + token2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"1차 승인\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.nextApprover").value("이영희"));

        mockMvc.perform(post("/api/approvals/" + approvalId + "/approve")
                        .header("Authorization", "Bearer " + token3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"최종 승인\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));

        mockMvc.perform(get("/api/approvals/" + approvalId)
                        .header("Authorization", "Bearer " + token1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void approvalFlow_reject() throws Exception {
        signup("rejecter1", "1234", "신청자");
        signup("rejecter2", "1234", "결재자");

        String requesterToken = login("rejecter1", "1234");
        String approverToken = login("rejecter2", "1234");
        Long approverId = getUserId(approverToken);

        Long approvalId = createApproval(requesterToken, approverId);

        mockMvc.perform(post("/api/approvals/" + approvalId + "/reject")
                        .header("Authorization", "Bearer " + approverToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"반려합니다\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));

        mockMvc.perform(get("/api/approvals/" + approvalId)
                        .header("Authorization", "Bearer " + requesterToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    void authFlow_signupLoginMe() throws Exception {
        signup("meuser", "1234", "테스트");

        String token = login("meuser", "1234");

        mockMvc.perform(get("/api/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("meuser"))
                .andExpect(jsonPath("$.name").value("테스트"));
    }

    private void signup(String username, String password, String name) throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"%s","password":"%s","name":"%s"}
                                """.formatted(username, password, name)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SIGNUP_SUCCESS"));
    }

    private String login(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"%s","password":"%s"}
                                """.formatted(username, password)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(json.get("accessToken").asText()).isNotBlank();
        return json.get("accessToken").asText();
    }

    private Long getUserId(String token) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    private Long createApproval(String token, Long... approverIds) throws Exception {
        StringBuilder ids = new StringBuilder("[");
        for (int i = 0; i < approverIds.length; i++) {
            if (i > 0) {
                ids.append(',');
            }
            ids.append(approverIds[i]);
        }
        ids.append(']');

        MvcResult result = mockMvc.perform(post("/api/approvals")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"휴가 신청","content":"3일 휴가","approverIds":%s}
                                """.formatted(ids)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.message").value("APPROVAL_CREATED"))
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }
}
