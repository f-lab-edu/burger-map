package burgermap.service;

import burgermap.dto.member.MemberJoinDto;
import burgermap.dto.member.MemberResponseDto;
import burgermap.dto.member.MemberUpdateDto;
import burgermap.entity.Member;
import burgermap.mapper.MemberMapper;
import burgermap.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.lang.reflect.Field;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;
    public MemberResponseDto addMember(MemberJoinDto memberJoinDto) {
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

    public MemberResponseDto findMemberByMemberId(Long memberId) {
        Member member = repository.findMember(memberId);
        if (member == null) {  // 해당 식별 번호를 가진 회원이 없는 경우
            log.info("member(memberId = {}) not found", memberId);
            return null;
        }
        log.info("found member = {}", member);
        MemberResponseDto memberResponseDto = new MemberResponseDto();
        MemberMapper.member2MemberResponseDto(member, memberResponseDto);

        return memberResponseDto;
    }

    public MemberResponseDto deleteMember(Long memberId) {
        Member member = repository.findMember(memberId);
        if (member == null) {  // 해당 식별 번호를 가진 회원이 없는 경우
            log.info("member(memberId = {}) not found", memberId);
            return null;
        } else {
            Member deletedMember = repository.deleteMember(memberId);
            log.info("member deleted = {}", deletedMember);
            MemberResponseDto memberResponseDto = new MemberResponseDto();
            MemberMapper.member2MemberResponseDto(deletedMember, memberResponseDto);
            return memberResponseDto;
        }
    }

    public MemberResponseDto updateMember(Long memberId, MemberUpdateDto memberUpdateDto) {
        Member member = repository.updateMember(memberId, memberUpdateDto);
        if (member == null) {  // 해당 식별 번호를 가진 회원이 없는 경우
            log.info("member(memberId = {}) not found", memberId);
            return null;
        }

        MemberResponseDto memberResponseDto = new MemberResponseDto();
        MemberMapper.member2MemberResponseDto(member, memberResponseDto);

        return memberResponseDto;
    }
}
