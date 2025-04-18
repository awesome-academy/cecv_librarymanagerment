package com.sun.librarymanagement.exception;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@JsonTypeName("errors")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@Getter
public class AppErrorMessages {

    private final List<String> body;

    public AppErrorMessages() {
        body = new ArrayList<>();
    }

    public void append(String message) {
        body.add(message);
    }
}
