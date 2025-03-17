package com.api.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
