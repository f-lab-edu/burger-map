package burgermap.advice;

import burgermap.dto.common.ExceptionMessageDto;
import burgermap.exception.store.NotOwnerMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class MemberTypeCheckAdvice {
    /**
     * 예외 처리: OWNER가 아닌 회원이 OWNER 권한이 필요한 작업을 시도하면 401 UNAUTHORIZED 반환
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NotOwnerMemberException.class)
    public ExceptionMessageDto handleNotOwnerMemberException(NotOwnerMemberException e) {
        return new ExceptionMessageDto(e.getMessage());
    }
}
