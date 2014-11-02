package cloudNote;

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
public class HeartBeatServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF8");
        PrintWriter out = response.getWriter();
        ApiHelper.Status status = ApiHelper.Status.OK;
        Map<String, String> return_fields = new HashMap<String, String>();
        try {
            String mail = request.getParameter("login");
            String token = request.getParameter("token");
            boolean auth = true;
            if ((mail == null)||(token == null)){
                throw new Exception("No token or login specified in request");
            }
            else {
            /* TODO: DB Check token */
            }

            if (auth) {
                /* TODO: Update last hb in user session DB*/
                return_fields.put("msg", "Updated session");
                out.println(ApiHelper.returnJson(status, return_fields));
            }else{
                status = ApiHelper.Status.NO_AUTH;
                return_fields.put("msg", "Session not active, must log in!");
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
        return_fields.put("msg","GET method not possible for hb");
        out.println(ApiHelper.returnJson(status, return_fields));
    }
}
