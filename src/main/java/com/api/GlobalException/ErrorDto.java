package com.api.GlobalException;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
