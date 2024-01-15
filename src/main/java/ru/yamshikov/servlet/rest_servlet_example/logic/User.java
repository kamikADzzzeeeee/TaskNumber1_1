package ru.yamshikov.servlet.rest_servlet_example.logic;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class User {
    private String name;
    private String surname;
    private Double salary;
}
