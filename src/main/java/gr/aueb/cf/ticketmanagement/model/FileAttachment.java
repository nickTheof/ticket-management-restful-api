package gr.aueb.cf.ticketmanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "file_attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileAttachment extends AbstractEntity implements IdentifiableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String filetype;

    @Column(nullable = false)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public void addComment(Comment comment) {
        if (comment == null) return;
        this.setComment(comment);
        comment.getAttachments().add(this);
    }

    public void removeComment(Comment comment) {
        if (comment == null) return;
        this.setComment(null);
        comment.getAttachments().remove(this);
    }

    public void addTicket(Ticket ticket) {
        if (ticket == null) return;
        this.setTicket(ticket);
        ticket.getAttachments().add(this);
    }

    public void removeTicket(Ticket ticket) {
        if (ticket == null) return;
        this.setTicket(null);
        ticket.getAttachments().remove(this);
    }
}
