package com.snm.security.dto.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BaseConverter {

    @Autowired
    private ConversionService conversionService;

    @Nullable
    public <T> T convert(@Nullable Object source, Class<T> targetType) {
        return conversionService.convert(source, targetType);
    }

    public <T, S> List<T> convertList(@Nullable List<S> source, Class<T> targetClass) {

        TypeDescriptor sourceType = TypeDescriptor.collection(List.class, TypeDescriptor.forObject(source));
        TypeDescriptor targetType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(targetClass));
        return  (List<T>) conversionService.convert(source, sourceType, targetType);
    }
}
