package com.approval.document.documentapproval.config;

import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViolationsMessageGenerator<T> {
    public List<String> getMessage(List<ConstraintViolation<T>> t) {
        List<String> messageList = new ArrayList<>();

        for(ConstraintViolation<T> item : t.stream().collect(Collectors.toList())) {
            PathImpl path = (PathImpl)item.getPropertyPath();
            String propertyName = path.getLeafNode().getName();
            messageList.add(String.format("%s : %s", propertyName, item.getMessage()));
        }

        return  messageList;
    }
}
