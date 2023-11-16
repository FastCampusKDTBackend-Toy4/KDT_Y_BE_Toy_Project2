# 코드 컨벤션

## 디렉토리 구조

> ⚠️ 디렉토리 안에 `.gitkeep` 파일 외에 **다른 파일이 있다면** `.gitkeep`은 지우셔도 됩니다.

```
Project
├── HELP.md
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── readme.md
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── kdt_y_be_toy_project2
    │   │           ├── domain
    │   │           │   ├── itinerary
    │   │           │   │   ├── api
    │   │           │   │   ├── controller
    │   │           │   │   ├── domain
    │   │           │   │   ├── dto
    │   │           │   │   ├── exception
    │   │           │   │   ├── repository
    │   │           │   │   └── service
    │   │           │   ├── member
    │   │           │   │   ├── controller
    │   │           │   │   ├── domain
    │   │           │   │   ├── dto
    │   │           │   │   │   └── request
    │   │           │   │   ├── exception
    │   │           │   │   ├── repository
    │   │           │   │   └── service
    │   │           │   ├── model
    │   │           │   │   └── exception
    │   │           │   └── trip
    │   │           │       ├── controller
    │   │           │       ├── domain
    │   │           │       ├── dto
    │   │           │       ├── exception
    │   │           │       ├── repository
    │   │           │       └── service
    │   │           └── global
    │   │               ├── config
    │   │               ├── error
    │   │               ├── jwt
    │   │               ├── resolver
    │   │               ├── security
    │   │               └── util
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       └── templates
    └── test
        └── java
            └── com
                └── kdt_y_be_toy_project2
                    ├── domain
                    │   └── trip
                    │   │   ├── controller
                    │   │   ├── repository
                    │   │   └── service
                    │   └── itinerary
                    │   │   ├── controller
                    │   │   ├── repository
                    │   │   └── service
                    │   └── model
                    ├── global
                    │   ├── factory
                    │   ├── helper
                    │   └── jwt
                    │       └── service
                    │  
                    └── integration
```

## 컨트롤러

```java

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ExampleApi {
	private final ExampleRepository exampleRepository;
	private final ExampleService exampleService;

	@GetMapping("/example/{example_id}")
	public ResponseEntity<GetResponse> getExample(
		@PathVariable(name = "example_id") final Long id
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(exampleService.getExample(id));
	}

	@PostMapping("/example")
	public ResponseEntity<AddResponse> addExample(
		@RequestBody final ExampleDTORequest request
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(exampleService.addExamples(ExampleDTO.of(request)));
	}
}
```

## 서비스

```java

@Service
@Transactional
@RequiredArgsConstructor
public class ExampleService {

	private final ExampleRepository exampleRepository;

	@Transactional(readOnly = true)
	public GetResponse getExample(final long exampleId) {
		final ExampleEntity exampleEntity = exampleRepository.findById(memberId);
		return GetResponse.from(exampleEntity);
	}

	public AddResponse add(final long memberId, final MemberProfileUpdate dto) {
		final ExampleEntity exampleEntity = exampleRepository.save(memberId);
		return AddResponse.from(exampleEntity);
	}
}
```

- 클래스 어노테이션에 `Transactional`을 붙인다.

## DTO

- `record` 를 사용합니다.

### RequestDTO

```java

@Builder
public record ExampleRequest(
	@NotNull int a,
	@NotNull String b
) {
	public static ExampleEntity toEntity(final ExampleResponse response) {
		return ExampleEntity.builder()
			.a(response.a)
			.b(response.b)
			.build();
	}
}
```

### ResponseDTO

```java

@Builder
public record ExampleResponse(
	int a,
	String b
) {
	public static ExampleResponse from(final ExampleEntity entity) {
		return ExampleResponse.builder()
			.a(a)
			.b(b)
			.build();
	}
}
```

## Entity

```java

@Entity
@Table(name = "example")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Example {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Long id;

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true, updatable = false, length = 50))
	private Email email;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "first", column = @Column(name = "first_name", nullable = false)),
		@AttributeOverride(name = "middle", column = @Column(name = "middle_name")),
		@AttributeOverride(name = "last", column = @Column(name = "last_name", nullable = false))
	})
	private Name name;

	@Column(name = "used", nullable = false)
	private Boolean used;

	@Column(name = "discount", nullable = false)
	private Double discount;

	@Builder
	private Example(Email email, Name name, boolean used, double discount) {
		this.email = email;
		this.name = name;
		this.used = used;
		this.discount = discount;
	}

	// 상황에 따라 적절하게 메소드 만들어서 사용
	public void updateProfile(final Name name) {
		this.name = name;
	}

	public void verifyUsed() {
		if (used)
			throw new CouponAlreadyUseException();
	}
}
```

## 에러처리

### Exception

- 공통 Exception 코드

```java

@Getter
public abstract class ApplicationException extends RuntimeException {

	private final ErrorCode errorCode;

	protected ApplicationException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
}
```

- Exception 사용 코드

```java
// 방법 1
public class ExampleErrorException extends ApplicationException {

	public ExampleErrorException(String message) {
		super(message, ErrorCode.EXAMPLE_ERROR_CODE);
	}
}

// 방법 2
public class ExampleErrorException extends ApplicationException {
	private static final ErrorCode ERROR_CODE = ErrorCode.EXAMPLE_ERROR;

	public ExampleErrorException() {
		super(ERROR_CODE.getSimpleMessage(), ERROR_CODE);
	}
}
```

### ErrorCode

```java
public enum ErrorCode {

	// Common
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력 형식입니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러입니다."),

	EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "중복된 이메일주소입니다.");

	private final String code;
	private final String message;
	private int status;
	private final HttpStatus httpStatus;
	private final String simpleMessage;

	ErrorCode(HttpStatus httpStatus, String simpleMessage) {
		this.httpStatus = httpStatus;
		this.simpleMessage = simpleMessage;
	}
}
```