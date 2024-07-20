package burgermap.members;

import burgermap.members.dto.MemberJoinDto;
import burgermap.members.dto.MemberMapper;
import burgermap.members.dto.MemberResponseDto;
import burgermap.members.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("members")
public class MemberController {

    private final MemberRepository repository;

    /**
     * 회원 추가
     */
    @PostMapping
    public MemberResponseDto addMember(@RequestBody MemberJoinDto memberJoinDto) {
        // 검증 로직

        // 회원 가입용 DTO -> entity
        Member member = new Member();
        MemberMapper.memberJoinDto2Member(memberJoinDto, member);

        // 회원 추가
        repository.addMember(member);
        log.info("new member = {}", member);

        // entity -> 응답용 DTO
        MemberResponseDto memberResponseDto = new MemberResponseDto();
        MemberMapper.member2MemberResponseDto(member, memberResponseDto);

        return memberResponseDto;
    }

    /**
     * 회원 식별 번호를 통한 회원 조회
     */
    @GetMapping("/{memberId}")
    public MemberResponseDto findMemberByMemberID(@PathVariable("memberId") Long memberId, HttpServletResponse response) {
        Member member = repository.findMember(memberId);

        if (member == null) {  // 해당 식별 번호를 가진 회원이 없는 경우
            log.info("member(memberId = {}) not found", memberId);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }

        log.info("found member = {}", member);
        MemberResponseDto memberResponseDto = new MemberResponseDto();
        MemberMapper.member2MemberResponseDto(member, memberResponseDto);

        return memberResponseDto;
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
