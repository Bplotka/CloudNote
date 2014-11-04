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

/**
 * Created by bwplo_000 on 2014-11-01.
 */
public class LoginServlet extends HttpServlet {

    protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Call: login user");
        ApiHelper.Status status = ApiHelper.Status.OK;
        Map<String, String> return_fields = new HashMap<String, String>();
        PrintWriter out = response.getWriter();

        SessionFactory sessionFactory = HibernateUtilities.getSessionFactory();
        Session session = sessionFactory.openSession();



        try {
            String mail = request.getParameter("login");
            String pass = request.getParameter("password");
            boolean auth = false;

            Criteria criteria = session.createCriteria(UserEntity.class);
            criteria.add(Restrictions.eq("login", mail));
            UserEntity user = (UserEntity) criteria.uniqueResult();

            if ((mail == null)||(pass == null)){
                throw new Exception("No password or login specified in request");
            }
            else {

                if(user.getPassword().equals(pass))
                {
                    auth = true;
                }
            }

            if (auth) {

                session.beginTransaction();

                TokenEntity tokenEntity = new TokenEntity();
                tokenEntity.setUserId(user.getId());
                java.util.Date date = new java.util.Date();
                tokenEntity.setCreateTime(new Timestamp(date.getTime()));
                tokenEntity.setUpdateTime(Timestamp.valueOf("2015-10-10 10:10:10"));

                session.save(tokenEntity);

                session.getTransaction().commit();

                criteria = session.createCriteria(TokenEntity.class);
                criteria.add(Restrictions.eq("userId", user.getId()));
                tokenEntity = (TokenEntity) criteria.uniqueResult();

                return_fields.put("msg", "Authorized");
                return_fields.put("token", String.valueOf(tokenEntity.getId()));
            }else{
                status = ApiHelper.Status.NO_AUTH;
                return_fields.put("msg", "Bad login or password");
            }
        } catch (Exception ex) {
            status = ApiHelper.Status.ERROR;
            return_fields.put("msg", ex.getMessage());
        }
        finally {
            session.close();
            sessionFactory.close();
        }
        out.println(ApiHelper.returnJson(status, return_fields));
    }

    protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Call: logout user");

        ApiHelper.Status status = ApiHelper.Status.OK;
        Map<String, String> return_fields = new HashMap<String, String>();
        PrintWriter out = response.getWriter();
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
                /* TODO: Delete session from db! For our user in <login>*/
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
        out.println(ApiHelper.returnJson(status, return_fields));
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
