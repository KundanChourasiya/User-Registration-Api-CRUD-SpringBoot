# User-Registration-Api

> [!NOTE]
> ### In this Api we make Crud opertion usiing Spring Boot RestApi.
> 1. Create User Registration 
> 2. Get User By Id
> 3. Get All User Details
> 4. Delete User By Id
> 5. Update User By Id
> 6. All input field validation

## Tech Stack
- Java -17
- Spring Boot- 3
- Spring Data JPA
- lombok
- Validation
- MySQL
- Postman
- Swagger UI

## Modules
* User Modules

## Documentation
Swagger UI Documentation - http://localhost:8080/swagger-ui/

## Installation & Run
Before running the API server, you should update the database config inside the application.properties file.
Update the port number, username and password as per your local database config.
    
```yaml
server:
  port: 8081

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/mydb
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

```

## API Root Endpoint
```
https://localhost:8080/
http://localhost:8080/swagger-ui/
user this data for checking purpose.
```
## Step To Be Followed
> 1. Create Rest Api will return to User Details.
>    
>    **Project Documentation**
>    - **Entity** - User (class)
>    - **Payload** - UserDto, ErrorDto (class)
>    - **Repository** - UserRepository (interface)
>    - **Service** - UserService (interface),UserServiceImpl (class)
>    - **Controller** - UserController (Class)
>    - **Global Exception** - ApplicationException, ResourceNotFoundError (class)
>      
> 2. add jpa, Swagger, mysql connector etc dependency in pom.xml file.
> 3. Configure mysql database configuration in application.yml file.
> 4. Create User class inside entity Package.
> 5. Create User Repository class inside Repository package.
> 6. Create User Service interface and User Service Class inside the service package.
> 7. Create User Controller class inside the Controller Package.
> 8. Create User Dto and Error Dto inside the payload package.
> 9. Create ApplicationException and ResourceNotFoundError inside the GloableException package.

## Important Dependency to be used
```xml
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
			<version>2.3.0</version> <!-- Latest version -->
		</dependency>
		
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
```

## Configure mysql database configuration in application.yml file
```ymal
server:
  port: 8081

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/regdbapi1
    username: root
    password: test
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  application:
    name: Registration-api
```

## Create User class inside entity Package.
```java
@Entity
@Table(name = "registration")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private  Long id;

    @NotBlank(message = "This Filed is required")
    @Column(name = "FullName", nullable = false)
    private String fullName;

    @NotBlank(message = "This Filed is required")
    @Column(name = "Email", nullable = false, unique = true)
    @Email(message = "Enter the Valid mail id")
    private String email;

    @NotBlank(message = "This Filed is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Password must have of minimum 8 Characters and at least one uppercase letter, one lowercase letter, one number and one special character")
    @Column(name = "Password")
    private String password;

    @NotNull(message = "This Filed is required")
    @Size(min = 10, max = 10, message = "size must be 10 digits.")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits Only.")
    @Column(name = "Mobile", nullable = false, unique = true)
    private String mobile;

    @NotNull(message = "This Filed is required")
    @Min(18)
    @Max(60)
    @Column(name = "Age", nullable = false)
    private Long age;

    @NotBlank(message = "This Filed is required")
    @Column(name = "Gender", nullable = false)
    private String gender;

    @CreationTimestamp
    @Column(name = "CreateDate", updatable = false)
    private LocalDate createDate;

    @UpdateTimestamp
    @Column(name = "UpdateDate", insertable = false)
    private LocalDate updateDate;

}
```

## Create StudentRepository interface in repository package.

```
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);
    boolean existsByMobile(String mobile);
    List<Student> findByFullNameContainingIgnoreCase(String fullname);

}
```

## Create StudentService interface and StudentServiceImpl class in Service package.

### *StudentService*
```
public interface StudentService {

    // create Student
    public StudentDto createStudent(StudentDto dto);

    // get All student Details
    public List<StudentDto> getAllStudent();

    // get All student in page Format
    public Page<StudentDto> getAllStudentPage(Pageable pageable);

    // get single Student details
    public StudentDto getStudent(Long id);

    // Update Student Details
    public StudentDto updateStudent(Long id, StudentDto dto);

    // Delete Student Details
    public StudentDto deleteStudent(Long id);

    // Search Student details using name
    public List<StudentDto> searchByName(String name);

}
```

### *StudentServiceImpl*
```
@Service
public class StudentServiceImpl implements StudentService {
    private StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // map to dto
    private StudentDto mapTODto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setFullName(student.getFullName());
        dto.setEmail(student.getEmail());
        dto.setPassword(student.getPassword());
        dto.setMobile(student.getMobile());
        dto.setGender(student.getGender());
        dto.setAge(student.getAge());
        return dto;
    }

    // map to Entity
    private Student mapToEntity(StudentDto dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setPassword(dto.getPassword());
        student.setMobile(dto.getMobile());
        student.setGender(dto.getGender());
        student.setAge(dto.getAge());
        return student;
    }

    @Override
    public StudentDto createStudent(StudentDto dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setPassword(dto.getPassword());
        student.setGender(dto.getGender());
        student.setAge(dto.getAge());
        student.setMobile(dto.getMobile());

        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("User email already exist.");
        }
        if (studentRepository.existsByMobile(dto.getMobile())) {
            throw new IllegalArgumentException("User Mobile no. already exist.");
        }

        Student saveStudent = studentRepository.save(student);
        StudentDto studentDto = mapTODto(saveStudent);
        return studentDto;
    }

    @Override
    public List<StudentDto> getAllStudent() {
        List<Student> studentList = studentRepository.findAll();
        List<StudentDto> studentDtoList = studentList.stream().map(this::mapTODto).collect(Collectors.toUnmodifiableList());
        return studentDtoList;
    }

    @Override
    public Page<StudentDto> getAllStudentPage(Pageable pageable) {
        Page<Student> studentPage = studentRepository.findAll(pageable);
        List<StudentDto> studentDtoList = studentPage.getContent().stream().map(this::mapTODto).collect(Collectors.toList());
        return new PageImpl<>(studentDtoList, pageable, studentPage.getTotalPages());
    }

    @Override
    public StudentDto getStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Student Not Found By Id"));
        StudentDto studentDto = mapTODto(student);
        return studentDto;
    }

    @Override
    public StudentDto updateStudent(Long id, StudentDto dto) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Student Not Found By Id"));
        student.setId(id);
        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setPassword(dto.getPassword());
        student.setGender(dto.getGender());
        student.setAge(dto.getAge());
        student.setMobile(dto.getMobile());
        Student save = studentRepository.save(student);
        StudentDto studentDto = mapTODto(save);
        return studentDto;
    }

    @Override
    public StudentDto deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Student Not Found By Id"));
        studentRepository.deleteById(student.getId());
        StudentDto studentDto = mapTODto(student);
        return studentDto;
    }

    @Override
    public List<StudentDto> searchByName(String name) {
        List<Student> studentList = studentRepository.findByFullNameContainingIgnoreCase(name);
        List<StudentDto> studentDtoList = studentList.stream().map(this::mapTODto).collect(Collectors.toUnmodifiableList());
        return studentDtoList;
    }
}
```

##  Create ApiResponse and StudentDto class inside the Payload Package.

### *StudentDto* 
```
@Data
public class StudentDto {

    private Long id;

    @NotBlank(message = "This Filed is required")
    private String fullName;

    @NotBlank(message = "This Filed is required")
    @Email(message = "Enter the Valid mail id")
    private String email;

    @NotBlank(message = "This Filed is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Password must have of minimum 8 Characters and at least one uppercase letter, one lowercase letter, one number and one special character")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "This Filed is required")
    @Size(min = 10, max = 10, message = "size must be 10 digits.")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits Only.")
    private String mobile;

    @NotNull(message = "This Filed is required")
    @Min(18)
    @Max(60)
    private Long age;

    @NotBlank(message = "This Filed is required")
    private String gender;
}

```
### *ApiResponseDto* 
```
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String massage;
    private T Data;

}
```


### *Create GlobalException class and UserNotFoundException class inside the GlobalException Package.* 

### *GlobalException* 

```
@RestControllerAdvice
public class GlobalExceptionHandler {

    // General error handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneral(Exception ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // UserNotFoundException handler
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UserNotFoundException ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // IllegalArgumentException Handler
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> IllegalArgumentExceptionHandle(IllegalArgumentException ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // MethodArgumentNotValidException handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handlerInvalidArgument(MethodArgumentNotValidException ex) {

        Map<String, String> errorMsg = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errorMsg.put(error.getField(), error.getDefaultMessage()));
        ApiResponse<Object> response = new ApiResponse<>(false, "Something went wrong", errorMsg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
```

### *UserNotFoundException* 
```
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
```

### *Create StudentController class inside the Controller Package.* 

```
@RestController
@RequestMapping("/student")
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    //URL : http://localhost:8080/student/create
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createUser(@Valid @RequestBody StudentDto dto) {
        StudentDto student = studentService.createStudent(dto);
        ApiResponse<?> apiResponse = new ApiResponse<>(true, "Student saved!!!", student);
        return ResponseEntity.status(200).body(apiResponse);
    }

    //URL : http://localhost:8080/student/all-student
    @GetMapping("/all-student")
    public ResponseEntity<ApiResponse<?>> getAllStudent() {
        List<StudentDto> allStudent = studentService.getAllStudent();
        ApiResponse<List<StudentDto>> allStudentDetails = new ApiResponse<>(true, "All student details", allStudent);
        return ResponseEntity.status(200).body(allStudentDetails);
    }

    //URL :  http://localhost:8080/student/allstudent
    //URL :  http://localhost:8080/student/allstudent?page=0
    //URL : http://localhost:8080/student/allstudent?page=0&size=2
    @GetMapping("/allstudent")
    public ResponseEntity<ApiResponse<?>> getAllStudentPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        Page<StudentDto> allStudentPage = studentService.getAllStudentPage(PageRequest.of(page, size));
        if (!allStudentPage.isEmpty()){
            ApiResponse<Page<StudentDto>> allStudentDetailsInPage = new ApiResponse<>(true, "All student details in page", allStudentPage);
            return ResponseEntity.status(200).body(allStudentDetailsInPage);
        }
        ApiResponse<Object> noStudentDetailsInPage  = new ApiResponse<>(false, "No student Found ", null);
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value())).body(noStudentDetailsInPage);
    }

    //URL : http://localhost:8080/student/get/3
    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<?>> getStudent(@PathVariable Long id) {
        StudentDto student = studentService.getStudent(id);
        ApiResponse<StudentDto> studentDtoApiResponse = new ApiResponse<>(true, "Student record by Id", student);
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.ACCEPTED.value())).body(studentDtoApiResponse);
    }

    //URL : http://localhost:8080/student/update/1
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<?>> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDto dto) {
        StudentDto studentDto = studentService.updateStudent(id, dto);
        ApiResponse<StudentDto> studentDtoApiResponse = new ApiResponse<>(true, "Student record update Successfully", studentDto);
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.OK.value())).body(studentDtoApiResponse);
    }

    //URL : http://localhost:8080/student/delete/6
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteStudent(@PathVariable Long id) {
        StudentDto student = studentService.deleteStudent(id);
        ApiResponse<StudentDto> studentDtoApiResponse = new ApiResponse<>(true, "Student id " + student.getId() + " Delete Successfully", null);
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.OK.value())).body(studentDtoApiResponse);
    }

    //URL : http://localhost:8080/student/search?name=k
    //URL : http://localhost:8080/student/search?name=sdghs
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchByName(@RequestParam(required = false) String name) {

        List<StudentDto> studentDtos = studentService.searchByName(name);
        if (!studentDtos.isEmpty()) {
            ApiResponse<List<StudentDto>> studentFoundByName = new ApiResponse<>(true, "Student Found by name", studentDtos);
            return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.FOUND.value())).body(studentFoundByName);
        }
        ApiResponse<Object> noStudentFoundByName = new ApiResponse<>(false, "No student Found by name", null);
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value())).body(noStudentFoundByName);
    }
}

```

## Configure **_Swagger Definition_** to use Api Documentation and all Controller Documentation.

### *Swegger Defination*
```
// configure swagger OpenAPIDefinition
@OpenAPIDefinition(
		info = @Info(
				title = "Crud Operations with custom Api Response",
				version = "1.0",
				description = "In this Api we customize Api Response.",
				contact = @Contact(
						name = "Kundan Kumar Chourasiya",
						email = "mailmekundanchourasiya@gmail.com"
				)
		),
		servers = @Server(
				url = "http://localhost:8080",
				description = "Crud Operations with custom Api Response url"
		)
)
```


### Following pictures will help to understand flow of API

### *Swagger*

![image](https://github.com/user-attachments/assets/ab1b5567-08de-4994-b51a-704b97401a3f)

### *PostMan Test Cases*

Url - http://localhost:8080/student/create
![image](https://github.com/user-attachments/assets/23227c38-af02-4a09-ae6f-b5f7a58a9720)

Url - http://localhost:8080/student/search?name=g

Url - http://localhost:8080/student/search?name=Armando
![image](https://github.com/user-attachments/assets/bae60f1d-eb55-45f1-849c-db2e78fee760)

Url - http://localhost:8080/student/all-student
![image](https://github.com/user-attachments/assets/f65d7d34-8d3c-48a4-9699-524741de0bee)

Url - http://localhost:8080/student/get/4
![image](https://github.com/user-attachments/assets/b6f69bc6-8a69-470c-9298-34cf62bb68d5)

Url - http://localhost:8080/student/update/6
![image](https://github.com/user-attachments/assets/f094ac96-78bb-495a-a857-32906a3be89c)

Url - http://localhost:8080/student/delete/6
![image](https://github.com/user-attachments/assets/aa86df71-5145-45fd-8ad6-11eb81a176db)

URL :  http://localhost:8080/student/allstudent

URL :  http://localhost:8080/student/allstudent?page=0

URL : http://localhost:8080/student/allstudent?page=0&size=2
![image](https://github.com/user-attachments/assets/965fa88d-de0d-4c9f-a323-2b73d9190cc4)


 
![image](https://github.com/user-attachments/assets/8f997cab-d21e-4c84-825c-467d7fe0bd02)
