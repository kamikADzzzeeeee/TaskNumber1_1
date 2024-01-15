package ru.yamshikov.servlet.rest_servlet_example.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.yamshikov.servlet.rest_servlet_example.logic.Model;
import ru.yamshikov.servlet.rest_servlet_example.logic.User;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/update-user")
public class ServletPut extends HttpServlet {
    Model model = Model.getInstance();


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String salary = request.getParameter("salary");
        String userId = request.getParameter("userId");
        if (name.isEmpty() || surname.isEmpty() || salary.isEmpty() || userId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(htmlErrorPageResponse("Данные отсутствуют"));
            return;
        }
        Double doubleSalary = null;
        try {
            doubleSalary = Double.valueOf(salary);
        } catch (NumberFormatException exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(htmlErrorPageResponse("Неверный формат зарплаты"));
            return;
        }

        Integer id = null;
        try {
            id = Integer.valueOf(userId);
        } catch (NumberFormatException exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(htmlErrorPageResponse("Неверный формат ID пользователя"));
            return;
        }

        if (!model.findUserById(id)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(htmlErrorPageResponse("Пользователя с таким ID не существует"));
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        model.updateUserById(id, new User(name, surname, doubleSalary));
        out.println(htmlPageResponse(name, surname, salary));
    }

    private String htmlPageResponse(String name, String surname, String salary) {
        return "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>\n" +
                "    Страница для изменения пользователя\n" +
                "</h1>\n" +
                "<hr/>\n" +
                "\n" +
                "<h3>Пользователь" + name + " " + surname + " " + salary + " " + "изменен</h3>\n" +
                "\n" +
                "<br/>\n" +
                "<a href=\"index.jsp\">Домой</a>\n" +
                "<a href=\"update-user.html\">Изменить пользователя</a>\n" +
                "<br/>\n" +
                "\n" +
                "\n" +
                "</body>";
    }

    private String htmlErrorPageResponse(String error) {
        return "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>\n" +
                "    Страница для изменение пользователя\n" +
                "</h1>\n" +
                "<hr/>\n" +
                "\n" +
                "<h3>Ошибка. " + error + "</h3>\n" +
                "\n" +
                "<br/>\n" +
                "<a href=\"index.jsp\">Домой</a>\n" +
                "<br/>\n" +
                "<a href=\"update-user.html\">Изменить пользователя</a>\n" +
                "\n" +
                "\n" +
                "</body>\n";
    }


}