package chat.dao;

import chat.model.Message;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * Hibernate MessageDao implementation
 * 
 * @author Igor
 */
public class MessageDaoHibernate implements MessageDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Message> listMessages(Date dateFrom, Date dateTo) {
        Session session = sessionFactory.openSession();
        try {
            Transaction tx = session.beginTransaction();
            Query query = session.getNamedQuery("messagesByDate");
            query.setParameter("dateFrom", dateFrom);
            query.setParameter("dateTo", dateTo);
            List<Message> list = query.list();
            tx.commit();
            return list;
        } finally {
            session.close();
        }
    }

    public List<Message> listMessagesRecent(int fromMessageId) {
        Session session = sessionFactory.openSession();
        try {
            Transaction tx = session.beginTransaction();
            Query query = session.getNamedQuery("messagesRecentById");
            query.setParameter("messageId", fromMessageId);
            List<Message> list = query.list();
            tx.commit();
            return list;
        } finally {
            session.close();
        }
    }

    public void saveMessage( Message message ) {
        Session session = sessionFactory.openSession();
        try {
            Transaction tx = session.beginTransaction();
            session.save( "message", message );
            tx.commit();
        } finally {
            session.close();
        }
    }
}
