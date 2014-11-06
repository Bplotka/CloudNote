package cloudNote;

import org.hibernate.Session;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bwplo_000 on 2014-11-03.
 */
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Boolean validation_success = true;
        String param_query = "";

        request.setCharacterEncoding("UTF8");

        Session session = null;
        try{
            session = DbHelper.getCreatedSession();

            String login = request.getParameter("login");

            Boolean isCurrentLoginAvailable = DbHelper.isCurrentLoginAvailable(session, login);
            if (login != null){
                param_query = param_query.concat("&login=" + login);
            }

            String password = request.getParameter("password");

            String password2 = request.getParameter("password2");

            if (login.equals("")){
                param_query = param_query.concat("&err-login=Nie podano loginu.");
                validation_success = false;
            }
            else if(isCurrentLoginAvailable)
            {
                param_query = param_query.concat("&err-login=Login istnieje w bazie.");
                validation_success = false;
            }
            else{
                if (!login.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z0-9-]+$")){
                    param_query = param_query.concat("&err-login=Login nie jest poprawnym adresem e-mail.");
                    validation_success = false;
                }
            }


            if (password.equals("")){
                param_query = param_query.concat("&err-password=Nie podano hasła");
                validation_success = false;
            }
            else{
                if (password.length() < 8){
                    param_query = param_query.concat("&err-password=Hasło musi mieć więcej niż 8 znaków.");
                    validation_success = false;
                }
                else {
                    if (password2.equals("")) {
                        param_query = param_query.concat("&err-password=Hasła nie pasują do siebie.");
                        validation_success = false;
                    } else {
                        if (!password.equals(password2)) {
                            param_query = param_query.concat("&err-password=Hasła nie pasują do siebie.");
                            validation_success = false;
                        }

                    }
                }
            }

            if (validation_success){
                DbHelper.addNewUser(session,login,password);

                RequestDispatcher view = request.getRequestDispatcher("register_success.jsp?login="+login);
                view.forward(request, response);
                return;
            }
        }
        catch (Exception ex) {
            RequestDispatcher view = request.getRequestDispatcher("index.jsp?error=true");
            view.forward(request, response);
        }


        RequestDispatcher view = request.getRequestDispatcher("index.jsp?error=true" + param_query);
        view.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF8");
        PrintWriter out = response.getWriter();
        ApiHelper.Status status = ApiHelper.Status.ERROR;
        Map<String, String> return_fields = new HashMap<String, String>();
        return_fields.put("msg","GET method not possible for register");
        out.println(ApiHelper.returnJson(status, return_fields));
    }
}
