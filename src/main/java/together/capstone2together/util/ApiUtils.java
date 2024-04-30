package together.capstone2together.util;

import org.springframework.http.HttpStatus;
import together.capstone2together.dto.api.ApiError;
import together.capstone2together.dto.api.ApiResult;

public class ApiUtils {

    public static <T>ApiResult<T> success(T response){
        return new ApiResult<>(true, response, null);
    }
    public static ApiResult<?> error(String msg, int status){
        return new ApiResult<>(false, null, new ApiError(msg, status));
    }
    public static <T>ApiResult<?> error(String msg, T response, int status){
        return new ApiResult<>(false, response, new ApiError(msg, status));
    }
}
