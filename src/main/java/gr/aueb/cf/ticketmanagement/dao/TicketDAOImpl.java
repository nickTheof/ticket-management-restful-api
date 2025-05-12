package gr.aueb.cf.ticketmanagement.dao;

import gr.aueb.cf.ticketmanagement.model.Ticket;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TicketDAOImpl extends AbstractDAO<Ticket> implements ITicketDAO {
    public TicketDAOImpl() {
        this.setPersistentClass(Ticket.class);
    }
}
