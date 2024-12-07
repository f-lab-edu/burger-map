package burgermap.advice;

import burgermap.dto.common.ExceptionMessageDto;
import burgermap.exception.store.StoreNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class StoreExceptionAdvice {
    /**
     * 에외 처리: 주어진 storeId에 해당하는 가게가 없을 때 404 NOT_FOUND 반환
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StoreNotExistException.class)
    public ExceptionMessageDto handleStoreNotExistException(StoreNotExistException e) {
        return new ExceptionMessageDto(e.getMessage());
    }
}
