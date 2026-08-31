package com.example.userserivce.controller;


import com.example.userserivce.dto.ApiResponse;
import com.example.userserivce.service.CalculateService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RequestMapping("/api/calculate")
public class CalculateController {

    CalculateService calculateService;

    @PostMapping("/sum")
    public ApiResponse<Integer> calculateSum(@RequestParam Integer a,
                                             @RequestParam Integer b){
        return ApiResponse.success(calculateService.sum(a,b));
    }


}
