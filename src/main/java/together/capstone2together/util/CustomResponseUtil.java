package together.capstone2together.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import together.capstone2together.dto.ResponseDto;

public class CustomResponseUtil {

    private final static Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

    public static void unAuthentication(HttpServletResponse response, String msg){
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(-1, msg, null);
            String responseBody = om.writeValueAsString(responseDto);

            response.setContentType("application/json; charset=utf-8");
            response.setStatus(401);
            response.getWriter().println(responseBody); //만약 response status가 403이면, 그 response를 가로채고 내용을 "error"로 바꿈

        }catch (Exception e){
            log.error("서버 파싱 에러");
        }
    }

    public static void fail(HttpServletResponse response, String msg, HttpStatus httpStatus){
        try {
            ObjectMapper om = new ObjectMapper();

            ResponseDto<?> responseDto = new ResponseDto<>(-1, msg, null);
            String responseBody = om.writeValueAsString(ApiUtils.error(msg, HttpStatus.UNAUTHORIZED.value()));


            response.setContentType("application/json; charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody); //만약 response status가 403이면, 그 response를 가로채고 내용을 "error"로 바꿈

        }catch (Exception e){
            log.error("서버 파싱 에러");
        }
    }

    public static void success(HttpServletResponse response, Object dto){
        try {
            ObjectMapper om = new ObjectMapper();
            String responseBody = om.writeValueAsString(ApiUtils.success("로그인 성공"));

            response.setContentType("application/json; charset=utf-8");
            response.setStatus(200);
            response.getWriter().println(responseBody); //만약 response status가 403이면, 그 response를 가로채고 내용을 "error"로 바꿈

        }catch (Exception e){
            log.error("서버 파싱 에러");
        }
    }
}
