package burgermap;

import burgermap.members.dto.MemberJoinDto;
import burgermap.members.dto.MemberResponseDto;
import burgermap.members.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MockMvc mvc;

    @AfterEach
    void afterEach() throws Exception {
        memberRepository.clear();  // HashMapRepository 초기화
    }

    MemberJoinDto addTestMember() throws Exception {
        MemberJoinDto memberJoinDto = new MemberJoinDto();
        memberJoinDto.setLoginId("testId");
        memberJoinDto.setEmail("test@gmail.com");
        memberJoinDto.setPassword("testPw");

        // when
        String reqMessageBody = objectMapper.writeValueAsString(memberJoinDto);

        // then
        mvc.perform(post("/members")
                .content(reqMessageBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        return memberJoinDto;
    }

    @Test
    @DisplayName("회원 추가")
    void addMemberTest() throws Exception {
        // given
        MemberJoinDto memberJoinDto = new MemberJoinDto();
        memberJoinDto.setLoginId("testId");
        memberJoinDto.setEmail("test@gmail.com");
        memberJoinDto.setPassword("testPw");

        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setLoginId(memberJoinDto.getLoginId());
        memberResponseDto.setEmail(memberJoinDto.getEmail());

        // when
        String reqMessageBody = objectMapper.writeValueAsString(memberJoinDto);
        String respMessageBody = objectMapper.writeValueAsString(memberResponseDto);

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
        MemberJoinDto memberJoinDto = addTestMember();

        // when
        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setLoginId(memberJoinDto.getLoginId());
        memberResponseDto.setEmail(memberJoinDto.getEmail());
        String respMessageBody = objectMapper.writeValueAsString(memberResponseDto);

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
