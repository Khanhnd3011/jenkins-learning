package com.example.userserivce.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse <T> {
    int code;
    String message;
    T result;

    public static<T> ApiResponse<T> success(T result){
        return ApiResponse.<T>builder()
                .code(1000)
                .result(result)
                .build();
    }

    public static <T> ApiResponse<T> error(int code,String message){
        return ApiResponse.<T> builder()
                .code(code)
                .message(message)
                .build();
    }

}
