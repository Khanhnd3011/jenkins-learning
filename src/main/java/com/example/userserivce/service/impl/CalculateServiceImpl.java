package com.example.userserivce.service.impl;

import com.example.userserivce.service.CalculateService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
public class CalculateServiceImpl implements CalculateService {



    @Override
    public Integer sum(Integer a, Integer b) {
        return a+b;
    }
}
