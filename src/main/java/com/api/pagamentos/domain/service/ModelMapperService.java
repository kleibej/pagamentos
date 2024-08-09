package com.api.pagamentos.domain.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelMapperService {

    private final ModelMapper modelMapper;

    public ModelMapperService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <T> T toObject(Class<T> clazz, Object item){
        if(item == null) return null;
        return modelMapper.map(item, clazz);
    }

    public <T> List<T> toList(Class<T> clazz, List<?> items) {

        return items.stream()
                .map(item -> modelMapper.map(item, clazz))
                .toList();
    }
}
