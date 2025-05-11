package gr.aueb.cf.ticketmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "comments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends AbstractEntity implements IdentifiableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Getter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private Set<FileAttachment> attachments = new HashSet<>();

    public Set<FileAttachment> getAttachFiles() {
        return Collections.unmodifiableSet(attachments);
    }


    public void addAttachment(FileAttachment file) {
        if (file == null) return;
        attachments.add(file);
        file.setComment(this);
    }

    public void removeAttachment(FileAttachment file) {
        if (file == null) return;
        attachments.remove(file);
        file.setComment(null);
    }
}
