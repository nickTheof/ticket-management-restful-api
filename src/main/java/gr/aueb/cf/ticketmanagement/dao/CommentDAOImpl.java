package gr.aueb.cf.ticketmanagement.dao;

import gr.aueb.cf.ticketmanagement.model.Comment;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommentDAOImpl extends AbstractDAO<Comment> implements ICommentDAO {
    public CommentDAOImpl() {
        this.setPersistentClass(Comment.class);
    }
}
