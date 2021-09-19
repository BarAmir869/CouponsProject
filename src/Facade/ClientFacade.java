package Facade;

import DAO.CompaniesDAO;
import DAO.CouponsDAO;
import DAO.CustomersDAO;
import database.ConnectionPool;

public abstract class ClientFacade {
    private ConnectionPool connectionPool = ConnectionPool.getInstance();
    protected CompaniesDAO companiesDAO = connectionPool.getCompaniesDAO();
    protected CustomersDAO customersDAO = connectionPool.getCustomersDAO();
    protected CouponsDAO couponsDAO = connectionPool.getCouponsDAO();

    public abstract boolean login(String email, String password);

    public CompaniesDAO getCompaniesDAO() {
        return companiesDAO;
    }

    public CustomersDAO getCustomersDAO() {
        return customersDAO;
    }

    public CouponsDAO getCouponsDAO() {
        return couponsDAO;
    }

}
