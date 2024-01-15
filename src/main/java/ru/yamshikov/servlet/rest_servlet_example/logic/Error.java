package ru.yamshikov.servlet.rest_servlet_example.logic;

import lombok.Builder;

@Builder
public class Error {

    String code;
    String message;
    String time;

}
