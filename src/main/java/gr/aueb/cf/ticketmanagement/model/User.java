package gr.aueb.cf.ticketmanagement.model;

import gr.aueb.cf.ticketmanagement.core.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User extends AbstractEntity implements IdentifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String vat;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.CLIENT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Getter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "issuedFromUser", fetch = FetchType.LAZY)
    private Set<Ticket> userTickets = new HashSet<>();

    @Getter(AccessLevel.PROTECTED)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_tickets")
    private Set<Ticket> assignedTickets = new HashSet<>();

    public Set<Ticket> getUserIssuedTickets() {
        return Collections.unmodifiableSet(userTickets);
    }

    public Set<Ticket> getUserAssignedTickets() {
        return Collections.unmodifiableSet(assignedTickets);
    }

    public void addCompany(Company company) {
        if (company == null) return;
        this.setCompany(company);
        company.addUser(this);
    }

    public void removeCompany(Company company) {
        if (company == null) return;
        this.setCompany(null);
        company.getUsers().remove(this);
    }

    public void addUserCreatedTicket(Ticket ticket) {
        if (ticket == null) return;
        userTickets.add(ticket);
        ticket.setIssuedFromUser(this);
    }

    public void removeUserCreatedTicket(Ticket ticket) {
        if (ticket == null) return;
        userTickets.remove(ticket);
        ticket.setIssuedFromUser(null);
    }

    public void addAssignedTicket(Ticket ticket) {
        if (ticket == null) return;
        assignedTickets.add(ticket);
        ticket.getAssignedToUsers().add(this);
    }

    public void removeAssignedTicket(Ticket ticket) {
        if (ticket == null) return;
        assignedTickets.remove(ticket);
        ticket.getAssignedToUsers().remove(this);
    }
}
