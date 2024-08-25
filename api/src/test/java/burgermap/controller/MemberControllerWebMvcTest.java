package burgermap.controller;

import burgermap.dto.member.MemberJoinDto;
import burgermap.dto.member.MemberResponseDto;
import burgermap.dto.member.MemberUpdateDto;
import burgermap.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
public class MemberControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private String validId = "length10Id";
    private String validPw = "length10Pw";
    private String validEmail = "id@gmail.com";

    @Test
    @DisplayName("회원 추가")
    void addMemberTest() throws Exception {
        // 요청으로부터 MemberJoinDto 객체를 MemberService로 전달 후 MemberResponseDto 객체를 반환받아 응답으로 전송

        // given
        // 가입 요청 정보 및 반환 정보 정의
        MemberJoinDto newMember = new MemberJoinDto();
        newMember.setLoginId(validId);
        newMember.setPassword(validPw);
        newMember.setEmail(validEmail);
        String newMemberString = objectMapper.writeValueAsString(newMember);

        MemberResponseDto expectedMemberResponse = new MemberResponseDto();
        expectedMemberResponse.setLoginId(newMember.getLoginId());
        expectedMemberResponse.setEmail(newMember.getEmail());

        // service: MemberJoinDto -> MemberResponseDto
        given(memberService.addMember(any(MemberJoinDto.class))).willReturn(expectedMemberResponse);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                        post("/members")
                                .content(newMemberString)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        String body = response.getContentAsString();
        MemberResponseDto addMemberResponse = objectMapper.readValue(body, MemberResponseDto.class);

        // then
        assertThat(addMemberResponse).isEqualTo(expectedMemberResponse);
    }

    @Test
    @DisplayName("제약조건을 어기는 값으로 회원 추가")
    void addNonValidInfoMemberTest() throws Exception {
        // MemberJoinDto에 적용한 필드 제약조건을 만족하지 않으면 문제의 필드와 메세지를 응답으로 반환
        // 제약:
        //   id: @Size(min = 8, max = 12), @NotBlank
        //   password: @Size(min = 8, max = 12), @NotBlank
        //   email: @Email, @NotBlank

        // Non-Valid case
        String shortId = "shortId";
        String shortPw = "shortPw";
        String longId = "tooLoooooongId";
        String longPw = "tooLoooooongPw";
        String wrongEmail = "nonValidEmail@";

        // given - 제약조건보다 짧은 id & password
        MemberJoinDto memberJoinDtoTooShort = new MemberJoinDto();
        memberJoinDtoTooShort.setLoginId(shortId);
        memberJoinDtoTooShort.setPassword(shortPw);
        memberJoinDtoTooShort.setEmail(validEmail);
        String memberInfo = objectMapper.writeValueAsString(memberJoinDtoTooShort);

        // when & then
        mockMvc.perform(post("/members")
                        .content(memberInfo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].fieldName", hasItems("loginId", "password")))
                .andExpect(jsonPath("$.errors[*].fieldName", not(hasItems("email"))));

        // given - 제약조건보다 긴 id & password
        MemberJoinDto memberJoinDtoTooLong = new MemberJoinDto();
        memberJoinDtoTooLong.setLoginId(longId);
        memberJoinDtoTooLong.setPassword(longPw);
        memberJoinDtoTooLong.setEmail(validEmail);
        memberInfo = objectMapper.writeValueAsString(memberJoinDtoTooLong);

        // when & then
        mockMvc.perform(post("/members")
                        .content(memberInfo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].fieldName", hasItems("loginId", "password")))
                .andExpect(jsonPath("$.errors[*].fieldName", not(hasItems("email"))));

        // given - 잘못된 email 형식
        MemberJoinDto memberJoinDtoNonValidEmail = new MemberJoinDto();
        memberJoinDtoNonValidEmail.setLoginId(validId);
        memberJoinDtoNonValidEmail.setPassword(validPw);
        memberJoinDtoNonValidEmail.setEmail(wrongEmail);
        memberInfo = objectMapper.writeValueAsString(memberJoinDtoNonValidEmail);

        // when & then
        mockMvc.perform(post("/members")
                        .content(memberInfo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].fieldName", hasItems("email")))
                .andExpect(jsonPath("$.errors[*].fieldName", not(hasItems("loginId", "password"))));

        // given - blank
        MemberJoinDto memberJoinDtoBlank = new MemberJoinDto();
        memberJoinDtoBlank.setLoginId("");
        memberJoinDtoBlank.setPassword("");
        memberJoinDtoBlank.setEmail("");
        memberInfo = objectMapper.writeValueAsString(memberJoinDtoBlank);

        // when & then
        mockMvc.perform(post("/members")
                        .content(memberInfo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].fieldName", hasItems("loginId", "password", "email")));
    }

    @Test
    @DisplayName("회원 식별 번호로 회원 조회")
    void findMemberByMemberIdTest() throws Exception {
        // 회원 식별 번호에 해당하는 회원 조회, 응답으로 전송

        // given
        long existMemberId = 1L;
        MemberResponseDto expectedFoundMember = new MemberResponseDto();
        expectedFoundMember.setLoginId(validId);
        expectedFoundMember.setEmail(validEmail);
        given(memberService.findMemberByMemberId(existMemberId)).willReturn(expectedFoundMember);

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/members/" + existMemberId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        MemberResponseDto foundMember = objectMapper.readValue(response.getContentAsString(), MemberResponseDto.class);

        // then
        assertThat(foundMember).isEqualTo(expectedFoundMember);
    }

    @Test
    @DisplayName("존재하지 않는 회원 식별 번호로 회원 조회")
    void findMemberByNonExistIdTest() throws Exception {
        // MemberSerivce의 회원 조회 결과가 null인 경우 NotFound 상태 설정

        // given
        long nonExistMemberId = 99L;
        given(memberService.findMemberByMemberId(nonExistMemberId)).willReturn(null);

        // when & then
        mockMvc.perform(get("/members/" + nonExistMemberId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("회원 식별 번호로 회원 삭제")
    void deleteMemberTest() throws Exception {
        // MemberService에서 반환한 삭제된 회원 정보를 응답에 담아 전송

        // given
        long existMemberId = 1L;
        MemberResponseDto expectedDeletedMember = new MemberResponseDto();
        expectedDeletedMember.setLoginId(validId);
        expectedDeletedMember.setEmail(validEmail);
        given(memberService.deleteMember(existMemberId)).willReturn(expectedDeletedMember);

        // when
        MockHttpServletResponse response = mockMvc.perform(delete("/members/" + existMemberId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        MemberResponseDto deletedMember = objectMapper.readValue(response.getContentAsString(), MemberResponseDto.class);

        // then
        assertThat(deletedMember).isEqualTo(expectedDeletedMember);
    }

    @Test
    @DisplayName("존재하지 않는 회원 식별 번호로 회원 삭제")
    void deleteMemberByNonExistMemberIdTest() throws Exception {
        // MemberService의 회원 삭제 결과가 null인 경우 NotFound 상태 설정

        // given
        long nonExistMemberId = 99L;
        given(memberService.findMemberByMemberId(nonExistMemberId)).willReturn(null);

        // when & then
        mockMvc.perform(delete("/members/" + nonExistMemberId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("회원 정보 수정")
    void updateMemberTest() throws Exception {
        // given
        long existMemberId = 1L;

        MemberUpdateDto memberUpdateDto = new MemberUpdateDto();
        memberUpdateDto.setPassword(validPw);
        memberUpdateDto.setEmail(validEmail);
        String updateInfo = objectMapper.writeValueAsString(memberUpdateDto);

        MemberResponseDto expectedMemberResponseDto = new MemberResponseDto();
        expectedMemberResponseDto.setLoginId(validId);
        expectedMemberResponseDto.setEmail(validEmail);

        given(memberService.updateMember(any(Long.class), any(MemberUpdateDto.class))).willReturn(expectedMemberResponseDto);

        // when
        MockHttpServletResponse response = mockMvc.perform(patch("/members/" + existMemberId)
                .content(updateInfo)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        MemberResponseDto updatedMember = objectMapper.readValue(response.getContentAsString(), MemberResponseDto.class);

        // then
        assertThat(updatedMember).isEqualTo(expectedMemberResponseDto);
    }

    @Test
    @DisplayName("존재하지 않는 회원 정보 수정")
    void updateUnregisteredMemberTest() throws Exception {
        // given
        long NonExistMemberId = -1L;

        MemberUpdateDto memberUpdateDto = new MemberUpdateDto();
        memberUpdateDto.setPassword(validPw);
        memberUpdateDto.setEmail(validEmail);
        String updateInfo = objectMapper.writeValueAsString(memberUpdateDto);

        // when & then
        mockMvc.perform(patch("/members/" + NonExistMemberId)
                        .content(updateInfo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("제약조건을 어기는 값으로 회원 정보 수정")
    void updateMemberWithNonValidInfo() throws Exception {
        // given
        long existMemberId = 1L;

        // Non-Valid case
        String shortPw = "shortPw";
        String longPw = "tooLoooooongPw";
        String wrongEmail = "nonValidEmail@";

        // given - 제약조건보다 짧은 password
        MemberUpdateDto memberUpdateDtoShortPw = new MemberUpdateDto();
        memberUpdateDtoShortPw.setPassword(shortPw);

        String updateInfoShortPw = objectMapper.writeValueAsString(memberUpdateDtoShortPw);

        // when & then
        mockMvc.perform(patch("/members/" + existMemberId)
                        .content(updateInfoShortPw)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].fieldName", hasItems("password")));

        // given - 제약조건보다 긴 password
        MemberUpdateDto memberUpdateDtoLongPw = new MemberUpdateDto();
        memberUpdateDtoLongPw.setPassword(longPw);

        String updateInfoLongPw = objectMapper.writeValueAsString(memberUpdateDtoLongPw);

        // when & then
        mockMvc.perform(patch("/members/" + existMemberId)
                        .content(updateInfoLongPw)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].fieldName", hasItems("password")));

        // given - 잘못된 이메일 형식
        MemberUpdateDto memberUpdateDtoWrongEmail = new MemberUpdateDto();
        memberUpdateDtoWrongEmail.setEmail(wrongEmail);

        String updateInfoWrongEmail = objectMapper.writeValueAsString(memberUpdateDtoWrongEmail);

        // when & then
        mockMvc.perform(patch("/members/" + existMemberId)
                        .content(updateInfoWrongEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].fieldName", hasItems("email")));

        // given - password, email 모두 null
        MemberUpdateDto memberUpdateDtoAllNull = new MemberUpdateDto();

        String updateInfoAllNull = objectMapper.writeValueAsString(memberUpdateDtoAllNull);

        // when & then
        mockMvc.perform(patch("/members/" + existMemberId)
                        .content(updateInfoAllNull)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].objectError", hasItems("allFieldsNull")));
    }
}
