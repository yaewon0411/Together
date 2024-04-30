# Together 


### 전역 예외 처리기: CustomExceptionHandler
- @RestControllerAdvice : 여러 컨트롤러에서 발생 가능한 예외를 전역적으로 처리할 수 있는 핸들러를 포함함을 나타내는 어노테이션
- 예외 처리 자동화 : 특정 예외 유형을 자동으로 캐치하고 적절한 HTTP 응답 반환
- 응답 커스터마이징
- 처리하는 예외
  - API 예외
  - 유효성 검사 실패 예외

### AOP를 활용한 유효성 검사: CustomValidationAdvice
- 유효성 검사 자동화 : @PostMapping과 @PutMapping이 달린 메서드에 대해 자동으로 유효성 검사
- 에러 처리 : 유효성 검사 실패 시 오류 메시지가 담긴 map 생성 후 CustomValidationException으로 예외 전달
- 작동 방식
  - 1) Pointcut 정의 : @PostMapping과 @PutMapping 메서드를 대상으로 하는 포인트컷 정의
  - 2) Advice 실행 : 대상 메서드 실행 전후(@Around)에 validationAdvice 실행
  - 3) 예외 생성과 처리 : 유효성 검사에 실패한 필드와 메시지 수집 => CustomValidationException으로 예외 전달 => 전역 예외 처리기에서 관리
```java
@Component
@Aspect
public class CustomValidationAdvice {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping(){}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping(){}

    @Around("postMapping() || putMapping()") //joinPoint 전후 제어
    public Object validationAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        ....
    }
```

