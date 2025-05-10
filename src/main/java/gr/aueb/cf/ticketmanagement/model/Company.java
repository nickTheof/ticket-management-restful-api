package gr.aueb.cf.ticketmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "companies")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Company extends AbstractEntity implements IdentifiableEntity{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String vat;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Getter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    @Getter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<Ticket> tickets = new HashSet<>();


    public Set<User> getCompanyUsers() {
        return Collections.unmodifiableSet(users);
    }

    public Set<Ticket> getCompanyTickets() {
        return Collections.unmodifiableSet(tickets);
    }

    public void addUser(User user) {
        if (user == null) return;
        users.add(user);
        user.setCompany(this);
    }

    public void removeUser(User user) {
        if (user == null) return;
        users.remove(user);
        user.removeCompany(this);
    }

    public void addTicket(Ticket ticket) {
        if (ticket == null) return;
        tickets.add(ticket);
        ticket.setCompany(this);
    }

    public void removeTicket(Ticket ticket) {
        if (ticket == null) return;
        tickets.remove(ticket);
        ticket.removeCompany(this);
    }
}
