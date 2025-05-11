package gr.aueb.cf.ticketmanagement.model;

import gr.aueb.cf.ticketmanagement.core.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket extends AbstractEntity implements IdentifiableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_from_user_id")
    private User issuedFromUser;

    @Getter(AccessLevel.PROTECTED)
    @ManyToMany(mappedBy = "assignedTickets", fetch = FetchType.LAZY)
    private Set<User> assignedToUsers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Getter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY)
    private Set<FileAttachment> attachments = new HashSet<>();


    public Set<User> getAssignedUsers() {
        return Collections.unmodifiableSet(assignedToUsers);
    }

    public Set<FileAttachment> getTicketAttachments() {
        return Collections.unmodifiableSet(attachments);
    }

    public void addTicketCreator(User user) {
        if (user == null) return;
        this.setIssuedFromUser(user);
        user.getUserTickets().add(this);
    }

    public void removeTicketCreator(User user) {
        if (user == null) return;
        this.setIssuedFromUser(null);
        user.getUserTickets().remove(this);
    }

    public void addCompany(Company company) {
        if (company == null) return;
        this.setCompany(company);
        company.getTickets().add(this);
    }


    public void removeCompany(Company company) {
        if (company == null) return;
        this.setCompany(null);
        company.getTickets().remove(this);
    }

    public void addAssignedUser(User user) {
        if (user == null) return;
        assignedToUsers.add(user);
        user.getAssignedTickets().add(this);
    }

    public void removeAssignedUser(User user) {
        if (user == null) return;
        assignedToUsers.remove(user);
        user.getAssignedTickets().remove(this);
    }

    public void addAttachment(FileAttachment attachment) {
        if (attachment == null) return;
        attachments.add(attachment);
        attachment.setTicket(this);
    }

    public void removeAttachment(FileAttachment attachment) {
        if (attachment == null) return;
        attachments.remove(attachment);
        attachment.setTicket(null);
    }
}
