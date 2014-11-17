package cloudNote;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistryBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bwplo_000 on 2014-11-05.
 */
public class DbHelper {
    public static int LOGIN_TIME = 200000000; //200000
    public static Session getCreatedSession() throws Exception {
        Session session;
        try {
            if (sessionFactory == null) {
                Configuration configuration = new Configuration().configure();

                sessionFactory = configuration.buildSessionFactory(
                        new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry());
            }

            session = sessionFactory.openSession();

        } catch (HibernateException ex) {
            System.out.println(ex.getMessage());
            throw new Exception("Cannot open DB connection");
        }
        return session;
    }
    private static SessionFactory sessionFactory;

    public static void closeSession(Session session){
        if (session != null) {
            session.close();
        }
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }

    }

    public static UserEntity getUserByLogin(Session session, String login) throws Exception{
        Criteria criteria = session.createCriteria(UserEntity.class);
        criteria.add(Restrictions.eq("login", login));
        UserEntity user = (UserEntity) criteria.uniqueResult();
        if (user == null){
            throw new Exception("There is no such user");
        }
        user.Notes = DbHelper.getUserNotes(session,user.getId());
        return user;
    }

    public static TokenEntity getTokenByUser(Session session, UserEntity user, Boolean check_last_hb)  throws Exception {

        Criteria criteria = session.createCriteria(TokenEntity.class);
        criteria.add(Restrictions.eq("userId", user.getId()));
        if (check_last_hb){
            java.util.Date date = new java.util.Date();
            Timestamp t = new Timestamp(date.getTime() - 15000);
            System.out.println(t.toString());
            criteria.add(Restrictions.ge("updateTime", t));
        }
        TokenEntity t  = (TokenEntity) criteria.uniqueResult();
        if (check_last_hb){
            if (t==null){
                t = DbHelper.getTokenByUser(session,user,false);
                if (t!=null){
                    DbHelper.removeToken(session,t);
                    System.out.println("Removing token because it is outdated");
                    return null;
                }
            }
        }

        return t;
    }

    public static void removeToken(Session session, TokenEntity token)  throws Exception {
        session.beginTransaction();
        session.delete(token);
        session.getTransaction().commit();
    }

    public static void createUserSession(Session session, UserEntity user, String token, Boolean uniq)  throws Exception {
        java.util.Date date = new java.util.Date();
        if (uniq) {

            if (DbHelper.getTokenByUser(session, user, true) != null){
                throw new Exception("There is a session for that user! Cannot login");
            }
        }
        session.beginTransaction();
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUserId(user.getId());

        tokenEntity.setCreateTime(new Timestamp(date.getTime()));
        tokenEntity.setUpdateTime(new Timestamp(date.getTime()));
        tokenEntity.setToken(token);
        session.save(tokenEntity);

        session.getTransaction().commit();
    }

    public static Boolean isTokenValid(Session session, String mail, String token, Boolean update_session_time) throws Exception{
        java.util.Date date = new java.util.Date();
        UserEntity user = DbHelper.getUserByLogin(session, mail);

        TokenEntity token_from_db = DbHelper.getTokenByUser(session, user, true);
        if (token_from_db != null){
            if (token_from_db.getToken().equals(token)){
                if (update_session_time){
                    session.beginTransaction();
                    token_from_db.setUpdateTime(new Timestamp(date.getTime()));
                    session.update(token_from_db);
                    session.getTransaction().commit();
                }
                return true;
            }
        }
        return false;
    }

    public static void addNewUser(Session session, String login, String password)
    {
        session.beginTransaction();

        UserEntity user = new UserEntity();
        user.setLogin(login);
        user.setPassword(password);

        session.save(user);

        session.getTransaction().commit();
    }

    public static Boolean isCurrentLoginAvailable(Session session, String login) {

        Criteria criteria = session.createCriteria(UserEntity.class);
        criteria.add(Restrictions.eq("login", login));

        UserEntity user = (UserEntity) criteria.uniqueResult();

        if(user != null && !user.equals(null))
        {
            return true;
        }
        return false;
    }

    public static List<UserNoteRelationsEntity> getUserNoteRelationsByUserId(Session session, int userId) throws Exception{
        Criteria criteria = session.createCriteria(UserNoteRelationsEntity.class);
        criteria.add(Restrictions.eq("userId", userId));
        List<UserNoteRelationsEntity> userNoteRelations = (List<UserNoteRelationsEntity>) criteria.list();

        return userNoteRelations;
    }

    public static List<NoteEntity> getUserNotes(Session session, int userId) throws Exception {
        Criteria criteria = session.createCriteria(UserNoteRelationsEntity.class);
        criteria.add(Restrictions.eq("userId", userId));

        List<UserNoteRelationsEntity> userNoteRelations = (List<UserNoteRelationsEntity>) criteria.list();

        List<NoteEntity> notes = new ArrayList<NoteEntity>();;

        for(UserNoteRelationsEntity relation :  userNoteRelations){
            NoteEntity note = getNoteById(session, relation.getNoteId());

            note.setRight(RightProvider.getRightFromInt(relation.getNoteRight()));
            notes.add(note);
        }

        return notes;
    }

    public static NoteEntity getNoteById(Session session, int id) throws Exception{
        NoteEntity note;
        Criteria criteria = session.createCriteria(NoteEntity.class);
        criteria.add(Restrictions.eq("id", id));

        note = (NoteEntity) criteria.uniqueResult();
        return note;
    }

    public static void saveNote(Session session, int userId, NoteEntity note)  {
        session.beginTransaction();

        UserNoteRelationsEntity relation = new UserNoteRelationsEntity();
        relation.setNoteId(note.getId());
        relation.setUserId(userId);
        relation.setNoteRight(RightProvider.getRightFromEnum(note.getRight()));

        session.save(relation);
        session.save(note);

        session.getTransaction().commit();
    }

    public static TokenEntity getToken(Session session, String token, Boolean check_last_hb) throws Exception {
        TokenEntity tokenEntity;
        Criteria criteria = session.createCriteria(TokenEntity.class);
        criteria.add(Restrictions.eq("token", token));

        if (check_last_hb){
            java.util.Date date = new java.util.Date();
            Timestamp t = new Timestamp(date.getTime() - LOGIN_TIME);
            System.out.println(t.toString());
            criteria.add(Restrictions.ge("updateTime", t));
        }
        tokenEntity = (TokenEntity) criteria.uniqueResult();
        if (check_last_hb){
            if (tokenEntity==null){
                tokenEntity = DbHelper.getToken(session,token,false);
                if (tokenEntity!=null){
                    DbHelper.removeToken(session,tokenEntity);
                    System.out.println("Removing token because it is outdated");
                }
            }
        }
        if(tokenEntity==null) throw new Exception("Token not valid");

        return tokenEntity;
    }

    public static UserEntity getUserByToken(Session session, String token) throws Exception{
        TokenEntity tokenEntity = getToken(session, token, true);

        UserEntity user = DbHelper.getUserById(session, tokenEntity.getUserId());
        return user;
    }

    public static UserEntity getUserByToken(Session session, TokenEntity token) throws Exception{
        UserEntity user = DbHelper.getUserById(session, token.getUserId());
        return user;
    }

    private static UserEntity getUserById(Session session, int id) throws Exception{
        UserEntity user;
        Criteria criteria = session.createCriteria(UserEntity.class);
        criteria.add(Restrictions.eq("id", id));

        user = (UserEntity) criteria.uniqueResult();

        user.Notes = DbHelper.getUserNotes(session, user.getId());
        return user;
    }

    public static void addPermission(Session session, int userId, String grantedUserLogin, String permission, String note_id) throws Exception {
        session.beginTransaction();
        int right = RightProvider.getRightFromString(permission);

        UserNoteRelationsEntity relation = new UserNoteRelationsEntity();
        relation.setNoteId(Integer.parseInt(note_id));

        UserEntity grantedUser = DbHelper.getUserByLogin(session, grantedUserLogin);
        relation.setUserId(grantedUser.getId());

        relation.setNoteRight(right);
        session.save(relation);

        session.getTransaction().commit();
    }

    public static void removePermission(Session session, String revokeUserLogin, String note_id) throws Exception{

        session.beginTransaction();
        UserEntity user = DbHelper.getUserByLogin(session, revokeUserLogin);
        Criteria criteria = session.createCriteria(UserNoteRelationsEntity.class);
        criteria.add(Restrictions.eq("note_id", note_id));
        criteria.add(Restrictions.eq("user_id", user.getId()));

        UserNoteRelationsEntity relation = (UserNoteRelationsEntity) criteria.uniqueResult();

        session.delete(relation);
        session.getTransaction().commit();
    }

    public static void removeNote(Session session, NoteEntity note) throws Exception{
        session.beginTransaction();
        session.delete(note);
        session.getTransaction().commit();
    }

//    public static void createUserSession(Session session, UserEntity user, String token)  throws Exception {
//
//    session.beginTransaction();
//    TokenEntity tokenEntity = new TokenEntity();
//    tokenEntity.setUserId(user.getId());
//    java.util.Date date = new java.util.Date();
//    tokenEntity.setCreateTime(new Timestamp(date.getTime()));
//    tokenEntity.setUpdateTime(Timestamp.valueOf("2015-10-10 10:10:10"));
//    tokenEntity.setToken(token);
//    session.save(tokenEntity);
//
//    session.getTransaction().commit();


//    criteria = session.createCriteria(TokenEntity.class);
//    criteria.add(Restrictions.eq("userId", user.getId()));
//    tokenEntity = (TokenEntity) criteria.uniqueResult();
}
