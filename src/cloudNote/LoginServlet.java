package cloudNote;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bwplo_000 on 2014-11-01.
 */
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ApiHelper.Status status = ApiHelper.Status.OK;
        Map<String, String> return_fields = new HashMap<String, String>();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF8");
        PrintWriter out = response.getWriter();
        try {
            String mail = request.getParameter("login");
            String pass = request.getParameter("password");
            boolean auth = true;
            if ((mail == null)||(pass == null)){
                throw new Exception("No password or login specified in request");
            }
            else {
            /* TODO: DB Check password */
            }

            if (auth) {
                /* TODO: Generate Token and save in DB. Create user session in DB*/
                return_fields.put("msg", "Authorized");
                return_fields.put("token", "1234");
                out.println(ApiHelper.returnJson(status, return_fields));
            }else{
                status = ApiHelper.Status.NO_AUTH;
                return_fields.put("msg", "Bad login or password");
                out.println(ApiHelper.returnJson(status, return_fields));
            }
        } catch (Exception ex) {
            status = ApiHelper.Status.ERROR;
            return_fields.put("msg", ex.getMessage());
            out.println(ApiHelper.returnJson(status, return_fields));
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF8");
        PrintWriter out = response.getWriter();
        ApiHelper.Status status = ApiHelper.Status.ERROR;
        Map<String, String> return_fields = new HashMap<String, String>();
        return_fields.put("msg","GET method not possible for login");
        out.println(ApiHelper.returnJson(status, return_fields));

    }

}
