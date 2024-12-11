package burgermap.advice;

import burgermap.dto.common.ExceptionMessageDto;
import burgermap.exception.food.FoodNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class FoodExceptionAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FoodNotExistException.class)
    public ExceptionMessageDto handleFoodNotExistException(FoodNotExistException e) {
        return new ExceptionMessageDto(e.getMessage());
    }
}
