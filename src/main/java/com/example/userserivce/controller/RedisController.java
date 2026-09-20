package com.example.userserivce.controller;

import com.example.userserivce.dto.ApiResponse;
import com.example.userserivce.service.RedisTestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/redis")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class RedisController {

   RedisTestService redisTestService;

   @PostMapping("/{key}/{value}")
    public ApiResponse<String> save(@PathVariable String key,
                                    @PathVariable String value){
       redisTestService.save(key,value);

       return ApiResponse.success("saved");
   }

   @GetMapping("/{key}")
    public ApiResponse<String> get(@PathVariable String key){
       return ApiResponse.success(redisTestService.get(key));
   }
}
