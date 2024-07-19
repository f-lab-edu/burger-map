package burgermap;

import burgermap.members.dto.MemberJoinDTO;
import burgermap.members.dto.MemberResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mvc;

    @AfterEach
    void afterEach() throws Exception {
        mvc.perform((delete("/members")));  // HashMapRepository 초기화
    }

    MemberJoinDTO addTestMember() throws Exception {
        MemberJoinDTO memberJoinDTO = new MemberJoinDTO();
        memberJoinDTO.setLoginId("testId");
        memberJoinDTO.setEmail("test@gmail.com");
        memberJoinDTO.setPassword("testPw");

        // when
        String reqMessageBody = objectMapper.writeValueAsString(memberJoinDTO);

        // then
        mvc.perform(post("/members")
                .content(reqMessageBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        return memberJoinDTO;
    }

    @Test
    @DisplayName("회원 추가")
    void addMemberTest() throws Exception {
        // given
        MemberJoinDTO memberJoinDTO = new MemberJoinDTO();
        memberJoinDTO.setLoginId("testId");
        memberJoinDTO.setEmail("test@gmail.com");
        memberJoinDTO.setPassword("testPw");

        MemberResponseDTO memberResponseDTO = new MemberResponseDTO();
        memberResponseDTO.setLoginId(memberJoinDTO.getLoginId());
        memberResponseDTO.setEmail(memberJoinDTO.getEmail());

        // when
        String reqMessageBody = objectMapper.writeValueAsString(memberJoinDTO);
        String respMessageBody = objectMapper.writeValueAsString(memberResponseDTO);

        // then
        mvc.perform(post("/members")
                .content(reqMessageBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(respMessageBody)));
    }

    @Test
    @DisplayName("회원 조회")
    void findMemberByMemberIdTest() throws Exception {
        // given
        // 회원 등록
        MemberJoinDTO memberJoinDTO = addTestMember();

        // when
        MemberResponseDTO memberResponseDTO = new MemberResponseDTO();
        memberResponseDTO.setLoginId(memberJoinDTO.getLoginId());
        memberResponseDTO.setEmail(memberJoinDTO.getEmail());
        String respMessageBody = objectMapper.writeValueAsString(memberResponseDTO);

        // then
        // 회원 조회
        mvc.perform(get("/members/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(respMessageBody)));
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회")
    void findUnregisteredMemberTest() throws Exception {
        // given
        Long unregisteredMemberId = 1L;

        // when
        // then
        mvc.perform(get("/members/" + unregisteredMemberId)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("회원 삭제")
    void deleteMemberTest() throws Exception {
        // given
        addTestMember();

        // when
        // then
        mvc.perform(delete("/members/1")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("존재하지 않는 회원 삭제")
    void deleteUnregisteredMemberTest() throws Exception {
        // given
        // when
        // then
        mvc.perform(delete("/members/1")).andExpect(status().isNotFound());
    }
}