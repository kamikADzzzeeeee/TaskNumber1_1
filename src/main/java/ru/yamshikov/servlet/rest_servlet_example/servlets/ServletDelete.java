package ru.yamshikov.servlet.rest_servlet_example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.yamshikov.servlet.rest_servlet_example.logic.Model;
import ru.yamshikov.servlet.rest_servlet_example.logic.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(urlPatterns = "/delete-users")
public class ServletDelete extends HttpServlet {
    Model model = Model.getInstance();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=utf-8");
        PrintWriter out = resp.getWriter();
        Integer userId = null;
        try {
            userId = Integer.valueOf(req.getParameter("userId"));
        } catch (NumberFormatException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(htmlErrorPageResponse("Неверный формат id пользователя"));
            return;
        }


        if (!model.findUserById(userId)){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(htmlErrorPageResponse("Польователя с таким id не существует"));
            return;
        }

        out.println(htmlFindUsersPage(Map.of(userId,model.getUserById(userId))));
        model.removeUserById(userId);

    }


    private String htmlFindUsersPage(Map<Integer, User> userMap){
        String s ="";
        int i = 0;
        for (Map.Entry<Integer, User> element : userMap.entrySet()){
            i++;
            s = s + "<p> Удален пользователь с ID = " + element.getKey()+ ":</p>\n" +
                    "<ul>\n" +
                    "\t     <li>Имя:" + element.getValue().getName() +  "</li>\n" +
                    "       <li>Фамилия:" + element.getValue().getSurname() + "</li>\n" +
                    "       <li>Зарплата:" + element.getValue().getSalary() + "</li>\n" +
                    "</ul>\n";
        }

        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Page Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Страница удаленных пользователей</h1>\n" +
                "<hr/>\n" +
                "\n" +
                "\n" +
                "\n" +
                s +
                "\n" +
                "\n" +
                "</br>" +
                "<a href=\"index.jsp\">Домой</a>\n" +
                "</body>\n" +
                "</html>";


    }

    private String htmlErrorPageResponse(String error){
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Page Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Страница найденных пользователей</h1>\n" +
                "<hr/>\n" +
                "\n" +
                "<h4>" + error + "</h4>" +
                "</br>" +
                "<a href=\"index.jsp\">Домой</a>\n" +
                "</body>\n" +
                "</html>";
    }




}
