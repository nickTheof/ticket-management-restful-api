package gr.aueb.cf.ticketmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentFile extends AbstractEntity implements IdentifiableEntity {
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
}
