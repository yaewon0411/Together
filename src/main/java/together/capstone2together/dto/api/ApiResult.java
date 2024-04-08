package together.capstone2together.dto.api;

import lombok.Getter;
import lombok.Setter;
import together.capstone2together.dto.api.ApiError;

@Getter
@Setter
public class ApiResult<T> {

    private final boolean success; //성공 호출 여부
    private final T response; //반환 데이터
    private final ApiError apiError; //api 호출 실패 시 반환할 오류 메시지와 상태 코드 객체

    public ApiResult(boolean success, T response, ApiError apiError) {
        this.success = success;
        this.response = response;
        this.apiError = apiError;
    }
}
/*
1. 정상, 오류처리 모두 success 필드를 포함
 - 정상 처리라면 true, 오류 처리라면 false 값을 출력
2. 정상 처리는 response 필드를 포함하고 error 필드는 null
 - 응답 데이터는 객체로 표현
3. 오류 처리는 error 필드를 포함하고 response 필드는 null.  error 필드는 status, message 필드를 포함
 - status : HTTP Response status code 값과 동일한 값을 출력
 - message : 오류 메시지가 출력

 */
