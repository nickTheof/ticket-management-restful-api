package gr.aueb.cf.ticketmanagement.dao;

import gr.aueb.cf.ticketmanagement.model.Company;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CompanyDAOImpl extends AbstractDAO<Company> implements ICompanyDAO{
    public CompanyDAOImpl() {
        this.setPersistentClass(Company.class);
    }
}
