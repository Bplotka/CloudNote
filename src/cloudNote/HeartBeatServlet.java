package cloudNote;

import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bwplo_000 on 2014-11-03.
 */
public class HeartBeatServlet extends NoteHttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Hb call: heartbeat");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF8");
        PrintWriter out = response.getWriter();
        ApiHelper.Status status = ApiHelper.Status.OK;
        Map<String, String> return_fields = new HashMap<String, String>();
        Session session = null;
        try {
            String mail = request.getParameter("login");
            String token = request.getParameter("token");
            if ((mail == null)||(token == null)){
                throw new Exception("No token or login specified in request");
            }

            System.out.println("Hb call:: heartbeat from user " + mail);
            session = DbHelper.getCreatedSession();

            if (DbHelper.isTokenValid(session, mail, token, true)) {
                return_fields.put("msg", "Updated session");
            }else{
                status = ApiHelper.Status.NO_AUTH;
                return_fields.put("msg", "Session not active, must log in!");
            }
        } catch (Exception ex) {
            status = ApiHelper.Status.ERROR;
            return_fields.put("msg", ex.getMessage());
        }
        finally {
            DbHelper.closeSession(session);
        }
        String ret = ApiHelper.returnJson(status, return_fields);
        System.out.println(ret);
        out.println(ret);
    }
}
