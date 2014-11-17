package cloudNote;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by bwplo_000 on 2014-11-01.
 */
public class LoginServlet extends NoteHttpServlet {

    protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Login call: start login");

        ApiHelper.Status status = ApiHelper.Status.OK;
        Map<String, String> return_fields = new HashMap<String, String>();
        PrintWriter out = response.getWriter();

        Session session = null;
        try {
            String mail = request.getParameter("login");
            String pass = request.getParameter("password");

            if ((mail == null)||(pass == null)){
                throw new Exception("No password or login specified in request");
            }

            System.out.println("Login call:: login user " + mail);
            session = DbHelper.getCreatedSession();

            UserEntity user = DbHelper.getUserByLogin(session, mail);

            if(user.getPassword().equals(pass)) {

                UUID uuid = UUID.randomUUID();
                DbHelper.createUserSession(session, user, uuid.toString(), true);
                System.out.println("Login call:: SUCCESS ");
                return_fields.put("msg", "Authorized");
                return_fields.put("token", uuid.toString());
            }else{
                status = ApiHelper.Status.NO_AUTH;
                return_fields.put("msg", "Bad login or password");
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

    protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Logout call: logout user");

        ApiHelper.Status status = ApiHelper.Status.OK;
        Map<String, String> return_fields = new HashMap<String, String>();
        PrintWriter out = response.getWriter();
        Session session = null;
        try {
            String mail = request.getParameter("login");
            String token = request.getParameter("token");
            if ((mail == null)||(token == null)){
                throw new Exception("No token or login specified in request");
            }

            System.out.println("Logout call:: logout user " + mail);
            session = DbHelper.getCreatedSession();

            if (DbHelper.isTokenValid(session, mail, token, true)) {
                UserEntity user = DbHelper.getUserByLogin(session, mail);
                TokenEntity token_from_db = DbHelper.getTokenByUser(session, user, false);
                DbHelper.removeToken(session, token_from_db);

                status = ApiHelper.Status.OK;
                return_fields.put("msg", "User logged out!");
            }else{
                status = ApiHelper.Status.NO_AUTH;
                return_fields.put("msg", "Such session does not exists!");
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF8");
        String logout = request.getParameter("logout");
        if ((logout != null )&&( logout.equals("true"))){
            this.logout(request,response);
        }
        else{
            this.login(request,response);
        }

    }
}
