package burgermap.members;

import burgermap.members.dto.MemberJoinDTO;
import burgermap.members.dto.MemberMapper;
import burgermap.members.dto.MemberResponseDTO;
import burgermap.members.repository.HashMapMemberRepository;
import burgermap.members.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("members")
public class MemberController {

//    private final MemberRepository repository;
    private final HashMapMemberRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 회원 추가
     */
    @PostMapping
    public MemberResponseDTO addMember(@RequestBody String messageBody) throws IOException {
        MemberJoinDTO memberJoinDTO = objectMapper.readValue(messageBody, MemberJoinDTO.class);

        // 검증 로직

        // 회원 가입용 DTO -> entity
        Member member = new Member();
        MemberMapper.memberJoinDTO2Member(memberJoinDTO, member);

        // 회원 추가
        repository.addMember(member);
        log.info("new member = {}", member);

        // entity -> 응답용 DTO
        MemberResponseDTO memberResponseDTO = new MemberResponseDTO();
        MemberMapper.member2MemberResponseDTO(member, memberResponseDTO);

        return memberResponseDTO;
    }

    /**
     * 회원 식별 번호를 통한 회원 조회
     */
    @GetMapping("/{memberId}")
    public MemberResponseDTO findMemberByMemberID(@PathVariable("memberId") Long memberId, HttpServletResponse response) {
        repository.printAllMembers();
        Member member = repository.findMember(memberId);

        if (member == null) {  // 해당 식별 번호를 가진 회원이 없는 경우
            log.info("member(memberId = {}) not found", memberId);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }

        log.info("found member = {}", member);
        MemberResponseDTO memberResponseDTO = new MemberResponseDTO();
        MemberMapper.member2MemberResponseDTO(member, memberResponseDTO);

        return memberResponseDTO;
    }

    /**
     * 회원 식별 번호를 통한 회원 삭제
     */
    @DeleteMapping("/{memberId}")
    public void deleteMember(@PathVariable("memberId") Long memberId, HttpServletResponse response) {
        Member member = repository.findMember(memberId);

        if (member == null) {
            log.info("member(memberId = {}) not found", memberId);
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            Member deletedMember = repository.deleteMember(memberId);
            log.info("member deleted = {}", deletedMember);
        }
    }

    /**
     * 모든 회원 삭제
     */
    @DeleteMapping
    public void deleteAllMembers() {
        repository.clear();
    }

}
