package together.capstone2together.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import together.capstone2together.dto.ResponseDto;
import together.capstone2together.dto.api.ApiError;
import together.capstone2together.dto.api.ApiResult;
import together.capstone2together.ex.CustomApiException;
import together.capstone2together.ex.CustomValidationException;
import together.capstone2together.util.ApiUtils;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(CustomApiException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(ApiUtils.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<?>validationException(CustomValidationException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(ApiUtils.error(e.getMessage(), e.getErrorMap(), HttpStatus.BAD_REQUEST.value()),HttpStatus.BAD_REQUEST);
    }

}
