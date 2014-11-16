package cloudNote;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistryBuilder;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by bwplo_000 on 2014-11-05.
 */
public class DbHelper {

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
        user.Notes = DbHelper.getUserNotes(session,user.getId());

        if (user == null){
            throw new Exception("There is no such user");
        }
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

    public static List<NoteEntity> getUserNotes(Session session, int userId) {
        Criteria criteria = session.createCriteria(UserNoteRelationsEntity.class);
        criteria.add(Restrictions.eq("userId", userId));

        List<UserNoteRelationsEntity> userNoteRelations = (List<UserNoteRelationsEntity>) criteria;

        List<NoteEntity> notes = null;

        for(UserNoteRelationsEntity relation :  userNoteRelations){
            NoteEntity note = getNoteById(session, relation.getNoteId());

            note.setRight(RightProvider.getRightFromInt(relation.getNoteRight()));
            notes.add(note);
        }

        return notes;
    }

    public static NoteEntity getNoteById(Session session, int id) {
        NoteEntity note;
        Criteria criteria = session.createCriteria(NoteEntity.class);
        criteria.add(Restrictions.eq("id", id));

        note = (NoteEntity) criteria.uniqueResult();
        return note;
    }

    public static void SaveNote(Session session, int userId, NoteEntity note)  {
        session.beginTransaction();

        UserNoteRelationsEntity relation = new UserNoteRelationsEntity();
        relation.setNoteId(note.getId());
        relation.setUserId(userId);
        relation.setNoteRight(RightProvider.getRightFromEnum(note.getRight()));

        session.save(relation);
        session.save(note);

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
