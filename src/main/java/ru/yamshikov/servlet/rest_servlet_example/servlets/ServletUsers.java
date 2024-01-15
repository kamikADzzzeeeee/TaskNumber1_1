package ru.yamshikov.servlet.rest_servlet_example.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.yamshikov.servlet.rest_servlet_example.logic.Error;
import ru.yamshikov.servlet.rest_servlet_example.logic.Model;
import ru.yamshikov.servlet.rest_servlet_example.logic.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/*
*
* Сервлет для работы с JSON
*
* */
@WebServlet(urlPatterns = {"/users", "/users/*"})
public class ServletUsers extends HttpServlet {
    private final AtomicInteger counter = new AtomicInteger(6);
    private final Model model = Model.getInstance();
    private BufferedReader reader;
    private Gson gson;


    //Добавить нового пользователя
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("UTF-8");                              //Считывать кирилицу
        response.setContentType("application/json");                        //Устанавливаем тип ответа - json
        PrintWriter out = response.getWriter();                             //Получаем PrintWriter для установки ответа
        gson = new Gson();                                                  //Инициализируем объект gson для работы с json
        try {
            reader = request.getReader();                                   //Получаем из запроса json в формате BufferedReader
            User user = gson.fromJson(reader, User.class);                  //маппим reader в объкт типа User
            model.add(counter.getAndIncrement(), user);                     //добавляем в модель нового user'a
            response.setStatus(HttpServletResponse.SC_CREATED);             //Формируем response. Устанавливаем статус ответа
            out.println(gson.toJson(user));                                 //маппим созданного пользователя в json и отправляем обратно

        } catch (JsonSyntaxException |
                 JsonIOException exception) {         //В случае если формат json неверный, выдается ошибка пользователя 404
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Error error = Error
                    .builder()
                    .code(String.valueOf(HttpServletResponse.SC_BAD_REQUEST))
                    .message("Неверный формат json'a")
                    .time(LocalDateTime.now().toString())
                    .build();
            out.println(gson.toJson(error));

        } catch (
                Exception exception) {                                      //В случае если ошибка неизвеста, выдается ошибка сервера 500
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Error error = Error.builder()
                    .code(String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR))
                    .message("Неизвестная ошибка")
                    .time(LocalDateTime.now().toString())
                    .build();
            out.println(gson.toJson(error));
        }

    }

    //Получить пользователей/пользователя
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");                                  //Считывать кирилицу
        response.setContentType("application/json");                            //Устанавливаем тип ответа - json
        PrintWriter out = response.getWriter();                                 //Получаем PrintWriter для установки ответа
        gson = new Gson();                                                      //Инициализируем объект gson для работы с json
        String str = request.getPathInfo();                                     //Получаем путь по которому пришел запрос (/user или /user/{id})

        if (str == null) {                                                      //Если путь = "/user" тогда str = null
            response.setStatus(HttpServletResponse.SC_OK);                      //Устанавливаем статус ответа
            out.println(gson.toJson(model.getAllUser()));                       //Отправляем всех пользователй в формате json
            return;                                                             //Выход из метода doGet
        }

        int userId;                                                         //Получаем из пути запроса userId
        try {
            String sub = str.substring(1);
            userId = Integer.parseInt(sub);
        } catch (
                NumberFormatException exception) {                             //В случае, если путь запроса содержит знаки, выдается ошибка в формате json
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Error error = Error
                    .builder()
                    .code(String.valueOf(HttpServletResponse.SC_BAD_REQUEST))
                    .message("Неверный формат пути")
                    .time(LocalDateTime.now().toString())
                    .build();
            out.println(gson.toJson(error));
            return;
        }

        if (!model.findUserById(userId)) {                                      //Проверяем, что пользователь с таким id существует. Если нет - выдаем ошибку
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Error error = Error
                    .builder()
                    .code(String.valueOf(HttpServletResponse.SC_BAD_REQUEST))
                    .message("Пользователя с таким ID не существует")
                    .time(LocalDateTime.now().toString())
                    .build();
            out.println(gson.toJson(error));
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);                          //Устанавливаем статус ответа
        out.println(gson.toJson(model.getUserById(userId)));                    //В ответ устанавливаем найденного пользователя по id в формате json
    }

    //Удалить пользователей/пользователя
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");                                  //Считывать кирилицу
        response.setContentType("application/json");                            //Устанавливаем тип ответа - json
        PrintWriter out = response.getWriter();                                 //Получаем PrintWriter для установки ответа
        gson = new Gson();                                                      //Инициализируем объект gson для работы с json

        String str = request.getPathInfo();                                     //Получаем путь по которому пришел запрос (/user или /user/{id})

        if (str == null) {                                                      //Если путь = "/user" тогда str = null
            response.setStatus(HttpServletResponse.SC_OK);                      //Устанавливаем статус ответа
            model.removeAllUsers();                                             //Удаляем всех пользоватлей
            return;                                                             //Выход из метода doDelete
        }

        int userId;                                                             //Получаем из пути запроса userId
        try {
            String sub = str.substring(1);
            userId = Integer.parseInt(sub);
        } catch (
                NumberFormatException exception) {                              //В случае, если путь запроса содержит знаки, выдается ошибка в формате json
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Error error = Error
                    .builder()
                    .code(String.valueOf(HttpServletResponse.SC_BAD_REQUEST))
                    .message("Неверный формат пути")
                    .time(LocalDateTime.now().toString())
                    .build();
            out.println(gson.toJson(error));
            return;
        }

        if (!model.findUserById(userId)) {                                      //Проверяем, что пользователь с таким id существует. Если нет - выдаем ошибку
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Error error = Error
                    .builder()
                    .code(String.valueOf(HttpServletResponse.SC_BAD_REQUEST))
                    .message("Пользователя с таким ID не существует")
                    .time(LocalDateTime.now().toString())
                    .build();
            out.println(gson.toJson(error));
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);                          //Устанавливаем статус ответа
        model.removeUserById(userId);                                           //Удаляем пользователя по id

    }


    //Изменить пользователя
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");                                  //Считывать кирилицу
        response.setContentType("application/json");                            //Устанавливаем тип ответа - json
        PrintWriter out = response.getWriter();                                 //Получаем PrintWriter для установки ответа
        gson = new Gson();                                                      //Инициализируем объект gson для работы с json

        String str = request.getPathInfo();                                     //Получаем путь по которому пришел запрос (/user или /user/{id})

        if (str == null) {                                                      //Если путь = "/user" тогда str = null
            response.setStatus(HttpServletResponse.SC_OK);                      //Устанавливаем статус ответа
            model.removeAllUsers();                                             //Удаляем всех пользоватлей
            return;                                                             //Выход из метода doPut
        }

        int userId;                                                             //Получаем из пути запроса userId
        try {
            String sub = str.substring(1);
            userId = Integer.parseInt(sub);
        } catch (
                NumberFormatException exception) {                              //В случае, если путь запроса содержит знаки, выдается ошибка в формате json
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Error error = Error
                    .builder()
                    .code(String.valueOf(HttpServletResponse.SC_BAD_REQUEST))
                    .message("Неверный формат пути")
                    .time(LocalDateTime.now().toString())
                    .build();
            out.println(gson.toJson(error));
            return;
        }

        if (!model.findUserById(userId)) {                                      //Проверяем, что пользователь с таким id существует. Если нет - выдаем ошибку
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Error error = Error
                    .builder()
                    .code(String.valueOf(HttpServletResponse.SC_BAD_REQUEST))
                    .message("Пользователя с таким ID не существует")
                    .time(LocalDateTime.now().toString())
                    .build();
            out.println(gson.toJson(error));
            return;
        }

        try {
            reader = request.getReader();                                       //Получаем из запроса json в формате BufferedReader
            User newUser = gson.fromJson(reader, User.class);                   //маппим reader в объкт типа User
            newUser = model.updateUserById(userId, newUser);                    //обновляем в модели нового user'a
            response.setStatus(HttpServletResponse.SC_OK);                      //Формируем response. Устанавливаем статус ответа
            out.println(gson.toJson(newUser));                                  //маппим созданного пользователя в json и отправляем обратно
        } catch (JsonSyntaxException |
                 JsonIOException exception) {                                   //В случае если формат json неверный, выдается ошибка пользователя 404
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Error error = Error
                    .builder()
                    .code(String.valueOf(HttpServletResponse.SC_BAD_REQUEST))
                    .message("Неверный формат json'a")
                    .time(LocalDateTime.now().toString())
                    .build();
            out.println(gson.toJson(error));

        } catch (
                Exception exception) {                                      //В случае если ошибка неизвеста, выдается ошибка сервера 500
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Error error = Error.builder()
                    .code(String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR))
                    .message("Неизвестная ошибка")
                    .time(LocalDateTime.now().toString())
                    .build();
            out.println(gson.toJson(error));
        }


    }


}
