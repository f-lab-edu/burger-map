package burgermap.advice;

import burgermap.exception.member.MemberNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class MemberExceptionAdvice {
    /**
     * 예외 처리: 회원 식별 번호로 회원 조회 시 회원이 존재하지 않는 경우 404 NOT_FOUND 반환
     * 예외 발생 지점: MemberService, StoreService
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MemberNotExistException.class)
    public Map<String, String> handleMemberNotExistException(MemberNotExistException e) {
        return Map.of("message", e.getMessage());
    }
}
