package cloudNote;

import org.hibernate.Session;
import org.json.JSONObject;
import sun.org.mozilla.javascript.internal.json.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by bwplo_000 on 2014-11-16.
 */
public class NoteServlet extends NoteHttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF8");

        String path = request.getPathInfo();
        if (path.equals("/list")){
            this.list(request, response);
        }
        else
        if (path.equals("/save")){
            this.save(request, response);
        }
        else
        if (path.equals("/delete")){
            this.delete(request, response);
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
        System.out.println("Note call: start list Notes");

        ApiHelper.Status status = ApiHelper.Status.OK;
        Map<String, String> return_fields = new HashMap<String, String>();
        PrintWriter out = response.getWriter();
        //Notes array to fill!
        List<NoteEntity> notes = new ArrayList<NoteEntity>();
        Session session = null;
        try {
            String token = request.getParameter("token");

            if ((token == null)){
                throw new Exception("No token specified in request");
            }

            session = DbHelper.getCreatedSession();

            //Check token and list notes for that USER! ( for user who has rights to read)
            UserEntity user = DbHelper.getUserByToken(session, token);
            if(user != null){
                notes = user.Notes;
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
            ret = ApiHelper.noteJson(notes);
        }else{
            ret = ApiHelper.returnJson(status, return_fields);
        }
        System.out.println(ret);
        out.println(ret);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Note call: start save Notes");

        ApiHelper.Status status = ApiHelper.Status.OK;
        Map<String, String> return_fields = new HashMap<String, String>();
        PrintWriter out = response.getWriter();
        Session session = null;
        try {
            String token = request.getParameter("token");
            String note = request.getParameter("note");

            if ((token == null)||(note == null)){
                throw new Exception("No token or note specified in request");
            }

            session = DbHelper.getCreatedSession();
            // Check token
            // Parse note from json, get ID and find if exists in db.
            // If exists check if user have RW rights. If not just create note.
            TokenEntity tokenEntity = DbHelper.getToken(session, token, true);

            JSONObject obj = new JSONObject(note);
            int noteId = obj.getInt("id");
            String noteTitle = obj.getString("title");
            String noteContent = obj.getString("content");
            Byte isPublic = Byte.parseByte(obj.getString("is_public"));

            UserEntity user = DbHelper.getUserByToken(session, tokenEntity);
            NoteEntity noteEntity = null;
            for(NoteEntity user_note : user.Notes)
            {
                if(user_note.getId() == noteId)
                {
                    noteEntity = user_note;
                }
            }

            if(noteEntity == null){
                //NEW NOTE
                System.out.println("Note call:: New note ");
                noteEntity = new NoteEntity();
            }else{
                if (noteEntity.getRight() == RightEnum.READ) {
                    throw new Exception("User has no WRITE permission for that note");
                }
            }
            noteEntity.setContent(noteContent);
            noteEntity.setTitle(noteTitle);
            noteEntity.setCreateBy(tokenEntity.getId());
            noteEntity.setIsPublic(isPublic);
            DbHelper.saveNote(session, tokenEntity.getId(), noteEntity);

            System.out.println("Note call:: SUCCESS ");
            return_fields.put("msg", "Note saved");
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
        System.out.println("Note call: start delete Notes");

        ApiHelper.Status status = ApiHelper.Status.OK;
        Map<String, String> return_fields = new HashMap<String, String>();
        PrintWriter out = response.getWriter();
        Session session = null;
        try {
            String token = request.getParameter("token");
            String note_id = request.getParameter("note_id");

            if ((token == null)||(note_id == null)){
                throw new Exception("No token or note_id specified in request");
            }

            session = DbHelper.getCreatedSession();
            // Check token
            // delete note with given note id
            UserEntity user = DbHelper.getUserByToken(session, token);
            NoteEntity noteEntity = null;
            int noteId = Integer.parseInt(note_id);
            for(NoteEntity user_note : user.Notes)
            {
                if(user_note.getId() == noteId)
                {
                    if (user_note.getRight() == RightEnum.READ_WRITE)  noteEntity = user_note;
                }
            }
            if(noteEntity==null){
                throw new Exception("User has no permission to remove that note");
            }
            DbHelper.removeNote(session, noteEntity);

            System.out.println("Note call:: SUCCESS ");
            return_fields.put("msg", "Note deleted");
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
