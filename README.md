# User-Registration-Api

> [!NOTE]
> ### In this Api we make Crud opertion usiing Spring Boot RestApi.
> 1. Create User Registration 
> 2. Get User By Id
> 3. Get All User Details
> 4. Delete User By Id
> 5. Update User By Id
> 6. implementing filed validation and pagination. 

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
> 3. Configure MYSQL database configuration in application.yml file.
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

## Configure MYSQL database configuration in application.yml file.
```yaml
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

## Create User Repository class inside Repository package.

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

    List<User> findByFullNameContainingIgnoreCase(String name);
}
```

## Create User Service interface and User Service Class inside the service package.

### *UserService*
```java
public interface UserService {

    public UserDto createUser(User user);

    public Page<UserDto> getAllUser(Pageable pageable);

    UserDto updateUser(Long id, UserDto dto);

    UserDto getUserById(Long id);

    String deleteUserById(Long id);

    List<UserDto> searchByName(String name);

}
```

### *UserServiceImpl*
```java
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDto createUser(User user) {
        if (repository.existsByEmail(user.getEmail()))
            throw new  IllegalArgumentException("User email already exist.");
        if (repository.existsByMobile(user.getMobile()))
            throw new  IllegalArgumentException("User Mobile no. already exist.");
        User saveUser = repository.save(user);
        UserDto dto = mapToDto(saveUser);
        return dto;
    }

    @Override
    public Page<UserDto> getAllUser(Pageable pageable) {
        Page<User> userPage = repository.findAll(pageable);
        List<UserDto> userDtoList = userPage.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(userDtoList, pageable, userPage.getTotalPages());
    }

    @Override
    public UserDto updateUser(Long id, UserDto dto) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundError("Record Not found"));
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setMobile(dto.getMobile());
        user.setGender(dto.getGender());
        user.setAge(dto.getAge());
        repository.save(user);

        UserDto userdto = mapToDto(user);
        return userdto;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundError("Record not found"));
        UserDto dto = mapToDto(user);
        return dto;
    }

    @Override
    public String deleteUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundError("Record Not found"));
        repository.delete(user);
        return "User Delete Successfully";
    }

    @Override
    public List<UserDto> searchByName(String name) {
        List<User> byFullNameContainingIgnoreCase = repository.findByFullNameContainingIgnoreCase(name);
        List<UserDto> userDtoList = byFullNameContainingIgnoreCase.stream().map(u -> mapToDto(u)).collect(Collectors.toList());
        return userDtoList;
    }

    // dto to entity
    private UserDto mapToDto(User user){
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setMobile(user.getMobile());
        dto.setAge(user.getAge());
        dto.setGender(user.getGender());
        return dto;
    }

    // entity to dto
    private User mapToEntity(UserDto dto){
        User user = new User();
        user.setId(dto.getId());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setMobile(dto.getMobile());
        user.setAge(dto.getAge());
        user.setGender(dto.getGender());
        return user;
    }
    
}
```

### Create User Controller class inside the Controller Package.
### *User Controller*
```java
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService service;

    // url pattern: localhost:8081/api/user
    @PostMapping
    public ResponseEntity<?> CreateUser(@Valid @RequestBody User user) {
        try {
            UserDto dto = service.createUser(user);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    // url pattern: localhost:8081/api/user
    // url pattern: localhost:8081/api/user?page=0
    // url pattern: http://localhost:8081/api/user?page=0&size=2
    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllStates(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        Page<UserDto> states = service.getAllUser(PageRequest.of(page, size));
        return ResponseEntity.ok(states);
    }

    // url pattern: localhost:8081/api/user/8
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDto dto) {

        UserDto updatedto = service.updateUser(id, dto);
        return new ResponseEntity<>(updatedto, HttpStatus.CREATED);
    }

    // url pattern: localhost:8081/api/user/8
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        UserDto userById = service.getUserById(id);
        return new ResponseEntity<>(userById, HttpStatus.FOUND);
    }

    // url pattern: localhost:8081/api/user/8
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id){
        String msg = service.deleteUserById(id);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    // url pattern: localhost:8081/api/user/search?name=s
    // url pattern: localhost:8081/api/user/search?name=abc
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchByName (@RequestParam(required = false) String name){
        List<UserDto> userDtos = service.searchByName(name);
        if (userDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(userDtos, HttpStatus.FOUND);
    }
}
```

##  Create User Dto and Error Dto inside the payload package.

### *UserDto* 
```java
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private  Long id;
    private String fullName;
    private String email;
    private String mobile;
    private Long age;
    private String gender;
}
```

### *ErrorDto* 
```java
@Setter
@Getter
public class ErrorDto {
    private String msg;
    private LocalDate time;
    private String uri;

    public ErrorDto(String msg, LocalDate time, String uri) {
        this.msg = msg;
        this.time = time;
        this.uri = uri;
    }

    public ErrorDto() {
    }
}
```

### *Create ApplicationException and ResourceNotFoundError inside the GloableException package.* 

### *ApplicationException* 

```java
@RestControllerAdvice
public class ApplicationException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handlerInvalidArgument(MethodArgumentNotValidException ex) {

        Map<String, String> errorMsg = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errorMsg.put(error.getField(), error.getDefaultMessage()));
        return errorMsg;
    }


    @ExceptionHandler(ResourceNotFoundError.class)
    public ResponseEntity<ErrorDto> ResourceNotFoundHandler(ResourceNotFoundError r, WebRequest request){
        ErrorDto errorDto = new ErrorDto(r.getMessage(), LocalDate.now(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> ResourceNotFoundHandler(Exception e, WebRequest request){
        ErrorDto errorDto = new ErrorDto(e.getMessage(), LocalDate.now(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

### *ResourceNotFoundError* 
```java
public class ResourceNotFoundError extends  RuntimeException{
    public ResourceNotFoundError() {
    }

    public ResourceNotFoundError(String message) {
        super(message);
    }
}
```


### Following pictures will help to understand flow of API

### *Swagger*

![image](https://github.com/user-attachments/assets/8f997cab-d21e-4c84-825c-467d7fe0bd02)

### *PostMan Test Cases*

Url - http://localhost:8081/api/user
![image](https://github.com/user-attachments/assets/35db1777-1eb9-47d9-ba07-ca6f12601a9d)

Url - http://localhost:8081/api/user
![image](https://github.com/user-attachments/assets/dbde9f53-ef56-4a95-975b-07fd9c2dae6a)

Url - http://localhost:8081/api/user/8
![image](https://github.com/user-attachments/assets/22146f47-9696-487d-a94c-5b23c6ecca69)

Url - http://localhost:8081/api/user/2
![image](https://github.com/user-attachments/assets/14e3d454-d25f-47ca-ac08-cb219766bfdb)

Url - http://localhost:8081/api/user/4
![image](https://github.com/user-attachments/assets/f9dab458-a545-40d8-a1f1-606335554f17)


Url - http://localhost:8081/api/user/search?name=s

Url - http://localhost:8081/api/user/search?name=sunny

Url - http://localhost:8081/api/user/search?name=abc
![image](https://github.com/user-attachments/assets/e7de500e-f0e2-4fe6-aed6-5b8fc2d5bb1e)


URL :  http://localhost:8080/student/allstudent

URL :  http://localhost:8080/student/allstudent?page=0

URL : http://localhost:8080/student/allstudent?page=0&size=2
![image](https://github.com/user-attachments/assets/aac515c4-826f-4243-a0fc-135dfa8ff5aa)

### Filed validation

Url - http://localhost:8081/api/user

![image](https://github.com/user-attachments/assets/6d7fe528-99bd-4e78-8224-7146939f3b5a)


 

