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
 * Created by bwplo_000 on 2014-11-16.
 */
public class NoteHttpServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF8");
        PrintWriter out = response.getWriter();
        ApiHelper.Status status = ApiHelper.Status.ERROR;
        Map<String, String> return_fields = new HashMap<String, String>();
        return_fields.put("msg","GET method not possible for this service");
        out.println(ApiHelper.returnJson(status, return_fields));
    }
}
