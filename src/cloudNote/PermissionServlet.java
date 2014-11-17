package cloudNote;

import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bwplo_000 on 2014-11-17.
 */
public class PermissionServlet extends NoteHttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF8");

        String path = request.getPathInfo();
        if (path.equals("/list")){
            this.list(request, response);
        }
        else
        if (path.equals("/delete")){
            this.delete(request, response);
        }
        else
        if (path.equals("/add")){
            this.save(request, response);
        }
        else{
            PrintWriter out = response.getWriter();
            ApiHelper.Status status = ApiHelper.Status.ERROR;
            Map<String, String> return_fields = new HashMap<String, String>();
            return_fields.put("msg","Invalid url");
            out.println(ApiHelper.returnJson(status, return_fields));
        }

    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Permission call: start list Permissions");

        ApiHelper.Status status = ApiHelper.Status.OK;
        Map<String, String> return_fields = new HashMap<String, String>();
        PrintWriter out = response.getWriter();
        //TODO: Permissions array to fill!
        List<UserNoteRelationsEntity> rights = new ArrayList<UserNoteRelationsEntity>();

        Session session = null;
        try {
            String token = request.getParameter("token");
            String note_id = request.getParameter("note_id");
            if ((token == null)||(note_id == null)){
                throw new Exception("No token or note id specified in request");
            }
            session = DbHelper.getCreatedSession();

            //TODO: Check token and list permission for that USER and NOTE
            UserEntity user = DbHelper.getUserByToken(session, token);

            if(user != null){
                rights = DbHelper.getUserNoteRelationsByUserId(session, user.getId());
            }

        } catch (Exception ex) {
            status = ApiHelper.Status.ERROR;
            return_fields.put("msg", ex.getMessage());
        }
        finally {
            DbHelper.closeSession(session);
        }
        String ret;
        if (return_fields.isEmpty()) {
            ret = ApiHelper.permissionJson(rights);
        }else{
            ret = ApiHelper.returnJson(status, return_fields);
        }
        System.out.println(ret);
        out.println(ret);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Permission call: start save Permission");

        ApiHelper.Status status = ApiHelper.Status.OK;
        Map<String, String> return_fields = new HashMap<String, String>();
        PrintWriter out = response.getWriter();
        Session session = null;
        try {
            String token = request.getParameter("token");
            String note_id = request.getParameter("note_id");
            String permission = request.getParameter("permission");
            String login = request.getParameter("user_login");

            if ((token == null)||(note_id == null)||(permission == null)||(login == null)){
                throw new Exception("No token or note id or permission or user specified in request");
            }

            session = DbHelper.getCreatedSession();
            //TODO: Check token

            //TODO: Get User from token, update permission for the given user in(login)
            UserEntity user = DbHelper.getUserByToken(session, token);
            if(user != null){
                DbHelper.addPermission(session,user.getId(), login, permission, note_id);
            }

            System.out.println("Note call:: SUCCESS ");
            return_fields.put("msg", "Permission saved");
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

    private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Permission call: start delete Permission");

        ApiHelper.Status status = ApiHelper.Status.OK;
        Map<String, String> return_fields = new HashMap<String, String>();
        PrintWriter out = response.getWriter();
        Session session = null;
        try {
            String token = request.getParameter("token");
            String note_id = request.getParameter("note_id");
            String login = request.getParameter("user_login");

            if ((token == null)||(note_id == null)||(login == null)){
                throw new Exception("No token or note id or user specified in request");
            }

            session = DbHelper.getCreatedSession();
            //TODO: Check token
            //TODO: remove permission for the given user in(login) and note
            TokenEntity tokenEntity = DbHelper.getToken(session, token);
            if(tokenEntity != null){
                DbHelper.removePermission(session, login, note_id);
            }

            System.out.println("Note call:: SUCCESS ");
            return_fields.put("msg", "Permission deleted");
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
