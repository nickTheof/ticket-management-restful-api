package gr.aueb.cf.ticketmanagement.dao;

import gr.aueb.cf.ticketmanagement.model.FileAttachment;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileAttachmentDAOImpl extends AbstractDAO<FileAttachment> implements IFileAttachmentDAO {
    public FileAttachmentDAOImpl() {
        this.setPersistentClass(FileAttachment.class);
    }
}
