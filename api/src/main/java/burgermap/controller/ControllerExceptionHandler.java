package burgermap.controller;

import burgermap.dto.common.NotValidArgumentDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    /**
     * DTO의 필드 validation 예외를 응답으로 전송
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        HashMap<String, Object> bodyMap = new HashMap<>();
        List<NotValidArgumentDto> notValidArgList = new ArrayList<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            NotValidArgumentDto errorDtoFromError = createNotValidArgumentDto(fieldError, request);
            notValidArgList.add(errorDtoFromError);
        });

        bindingResult.getGlobalErrors().forEach(objectError -> {
            NotValidArgumentDto errorDtoFromError = createNotValidArgumentDto(objectError, request);
            notValidArgList.add(errorDtoFromError);
        });

        bodyMap.put("errors", notValidArgList);
        return bodyMap;
    }

    public NotValidArgumentDto createNotValidArgumentDto(ObjectError error, HttpServletRequest request) {
        NotValidArgumentDto notValidArgumentDto = new NotValidArgumentDto();
        if (error instanceof FieldError) {
            String fieldName = ((FieldError) error).getField();
            notValidArgumentDto.setFieldName(fieldName);
        } else {
            notValidArgumentDto.setObjectError(error.getCode());
        }

        String message = messageSource.getMessage(error, request.getLocale());
        notValidArgumentDto.setMessage(message);

        return notValidArgumentDto;
    }
}
