package by.htp.library.service;

import by.htp.library.dao.Factory;
import by.htp.library.dao.HibernateUtil;
import by.htp.library.dao.UserOperationDAO;
import by.htp.library.entity.User;
import by.htp.library.service.exception.ServiceException;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;

public final class LoginService {
    private static Logger log = Logger.getLogger(LoginService.class.getName());

    public final static User checkLogin(String login, String password) throws Exception {
        if (!Validator.loginValidator(login, password)) {
            return null;
        } else {
            User result = null;
            Factory factory = Factory.getInstance();
            UserOperationDAO userOperationDAO = factory.getUserOperationDAO();
            Session session = HibernateUtil.getSession();
            log.info("session_chekLogin_service = " + session.hashCode());
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                result = userOperationDAO.checkLogin(login, password);
                log.info("session_chekLogin_service_return = " + session.hashCode());
                transaction.commit();
            } catch (HibernateException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new TransactionException("");
            }
            if (result.getBlacklist().equals("unblock")) {
                return result;
            }else{
                throw new Exception();
            }
        }
    }
    static class Validator {
        public static boolean loginValidator(String login, String password) throws ServiceException {
            if (login.isEmpty() | password.isEmpty()) {
                throw new ServiceException();
            }
            return true;
        }
    }
}