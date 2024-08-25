package burgermap.controller;

import burgermap.TestcontainersMySqlTest;
import burgermap.dto.member.MemberJoinDto;
import burgermap.dto.member.MemberResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberControllerTest extends TestcontainersMySqlTest {

    @LocalServerPort
    private int port;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TestRestTemplate restTemplate;

    private String urlForm = "http://localhost:%d%s";

    private String validId = "length10Id";
    private String validPw = "length10Pw";
    private String validEmail = "id@gmail.com";

    MemberJoinDto addTestMember() throws Exception {
        MemberJoinDto memberJoinDto = new MemberJoinDto();
        memberJoinDto.setLoginId(validId);
        memberJoinDto.setEmail(validEmail);
        memberJoinDto.setPassword(validPw);

        // add 1 member
        restTemplate.postForObject(urlForm.formatted(port, "/members"), memberJoinDto, MemberResponseDto.class);

        return memberJoinDto;
    }

    @Test
    @DisplayName("회원 추가")
    void addMemberTest() throws Exception {
        // given
        MemberJoinDto memberJoinDto = new MemberJoinDto();
        memberJoinDto.setLoginId(validId);
        memberJoinDto.setEmail(validEmail);
        memberJoinDto.setPassword(validPw);

        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setLoginId(memberJoinDto.getLoginId());
        memberResponseDto.setEmail(memberJoinDto.getEmail());

        // when
        MemberResponseDto memberResponseDtoResult = restTemplate.postForObject(
                urlForm.formatted(port, "/members"), memberJoinDto, MemberResponseDto.class);

        // then
        assertThat(memberResponseDtoResult).isEqualTo(memberResponseDto);
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

        MemberResponseDto memberResponseDtoResult = restTemplate.getForObject(
                urlForm.formatted(port, "/members/1"), MemberResponseDto.class);

        // then
        assertThat(memberResponseDtoResult).isEqualTo(memberResponseDto);
    }

    @Test
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("존재하지 않는 회원 조회")
    void findUnregisteredMemberTest() throws Exception {
        // given
        Long unregisteredMemberId = -1L;

        // when
        ResponseEntity<MemberResponseDto> responseEntity= restTemplate.getForEntity(
                urlForm.formatted(port, "/members/" + unregisteredMemberId), MemberResponseDto.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("회원 삭제")
    void deleteMemberTest() throws Exception {
        // given
        addTestMember();
        Long deletedMemberId = 1L;

        // when
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                urlForm.formatted(port, "/members/" + deletedMemberId), HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("존재하지 않는 회원 삭제")
    void deleteUnregisteredMemberTest() throws Exception {
        // given
        Long unregisteredMemberId = -1L;
        // when
        // then
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                urlForm.formatted(port, "/members/" + unregisteredMemberId), HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
