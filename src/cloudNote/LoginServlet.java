package cloudNote;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by bwplo_000 on 2014-11-01.
 */
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF8");
        PrintWriter out = response.getWriter();
        String token = "null";
        String msg = "authorized";
        out.println("{ " +
                "'status':1, 'response': " +
                    "{ " +
                        " 'token':  " + token +
                        ", 'message': " + msg  +
                    " }" +
                "}");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF8");
        PrintWriter out = response.getWriter();

        String token = "null";
        String msg = "authorized";
        out.println("{ " +
                "'status':1, 'response': " +
                "{ " +
                " 'token':  " + token +
                ", 'message': " + msg  +
                " }" +
                "}");

    }
}
