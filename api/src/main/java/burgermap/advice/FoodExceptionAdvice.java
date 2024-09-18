package burgermap.advice;

import burgermap.exception.food.FoodAttributeNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class FoodExceptionAdvice {
    /**
     * 음식의 Menu Type, Menu Category, Ingredient가 등록되지 않는 요소인 경우 404 NOT_FOUND 반환
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FoodAttributeNotExistException.class)
    public Map<String, String> handleFoodAttributeNotExistException(FoodAttributeNotExistException e) {
        return Map.of("message", e.getMessage());
    }
}
