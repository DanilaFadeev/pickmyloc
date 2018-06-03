package addressBook.helpers;

import addressBook.models.Contact;
import addressBook.models.Settings;
import addressBook.models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.logging.Level;

public class HibernateUtil {

    static private HibernateUtil instance;
    private HibernateUtil() {}

    private SessionFactory sessionFactory;
    private Session session;

    public static HibernateUtil getInstance() {
        if (instance != null) {
            return instance;
        }

        instance = new HibernateUtil();
        instance.sessionFactory = getSessionFactory();

        return instance;
    }

    public void closeConnection() {
        sessionFactory.close();
    }

    private static SessionFactory getSessionFactory() {
        System.setProperty("org.jboss.logging.provider", "jdk");
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    public User getUser(String emailOrUsername, String password) {
        session = sessionFactory.openSession();

        Criterion email = Restrictions.eq("username", emailOrUsername);
        Criterion username = Restrictions.eq("email", emailOrUsername);
        LogicalExpression loginCondition = Restrictions.or(email, username);

        Criteria criteria = session.createCriteria(User.class);
        criteria.add(loginCondition);
        criteria.add( Restrictions.eq("password", password) );

        List resultsList = criteria.list();
        if (resultsList.isEmpty()) {
            return null;
        }

        session.createCriteria(CriteriaBuilder.class);
        session.close();

        return (User) resultsList.get(0);
    }

    public List<Contact> getUserContacts(int userId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<Contact> contacts = session.createCriteria(Contact.class)
                .createCriteria("user")
                .add( Restrictions.idEq(userId) )
                .list();

        transaction.commit();
        session.close();

        return contacts;
    }

    public Object save(Object object) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.saveOrUpdate(object);

        transaction.commit();
        session.close();

        return object;
    }

    public void deleteContact(Contact contact) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.delete(contact);

        transaction.commit();
        session.close();
    }
}
