package com.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

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
