<%@ page import="ru.yamshikov.servlet.rest_servlet_example.logic.Model" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Задание №1.1 (Servlet/REST/Web)</title>
</head>
<body>

    <h1>
        <%= "Страница по работе с пользователями" %>
    </h1>

    <hr/>

    <h2>
        <% Model model = Model.getInstance(); %>
        <%= "Всего пользователей: " + model.getCountUsers() %>
    </h2>

    <h4>Поиск пользователей (0 - для получения всех пользователей)</h4>
    <form method="get" action="get-users">
        <label>ID пользователя:
            <input type="text" name="userId">
        </label>
        <button type="submit">Поиск</button>
    </form>

    <br/>
    <h4>Удаление пользователей</h4>
    <form method="get" action="delete-users">
        <label>ID пользователя:
            <input type="text" name="userId">
        </label>
        <button type="submit">Удалить</button>
    </form>

    <br/>
    <a href="add-user.html">Создать пользователя</a>
    <br/>
    <a href="update-user.html">Изменить пользователя</a>
    </body>

</html>