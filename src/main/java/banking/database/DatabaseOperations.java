package banking.database;

import banking.exceptions.UserNotFoundException;
import banking.exceptions.WrongPasswordException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class DatabaseOperations {
    private EntityManagerFactory factory;
    private PasswordEncoder passwordEncoder;

    public DatabaseOperations() {
        this.factory = JPAUtil.getEntityManagerFactory();
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void addUser(User user) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
    }

    public boolean isUsernameExist(String username) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Query q = em.createQuery("select m from User m");
        if (q.getResultList().size() == 0) {
            return false;
        }
        Query query = em.createQuery("select m from User m where m.userName=:user_name");
        query.setParameter("user_name", username);
        try {
            User user = (User) query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
//        if(user == null) {
//            return false;
//        } else {
//            return true;
//        }
    }

    public boolean isEmailExist(String email) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("select m from User m where m.email=:email");
        query.setParameter("email", email);
        try {
            User user = (User) query.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }

        return true;
    }

    public User findUser(String username, String password) throws UserNotFoundException, WrongPasswordException {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("select m from User m where m.userName=:username");
        query.setParameter("username", username);

        try {
            User user = (User) query.getSingleResult();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            } else {
                throw new WrongPasswordException("Wrong password");
            }
        } catch (NoResultException e) {
            throw new UserNotFoundException("No such user");
        }

//        if(user == null) {
//            throw new UserNotFoundException("No such user");
//        } else {
//            return user;
//        }
    }

}
