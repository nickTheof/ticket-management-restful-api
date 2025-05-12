package gr.aueb.cf.ticketmanagement.dao;

import gr.aueb.cf.ticketmanagement.model.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserDAOImpl extends AbstractDAO<User> implements IUserDAO {
    public UserDAOImpl() {
        this.setPersistentClass(User.class);
    }
}
