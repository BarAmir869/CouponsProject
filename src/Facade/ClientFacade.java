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

    /**
     * @param email
     * @param getCompaniesDAO(
     * @return boolean
     */
    public abstract boolean login(String email, String password);

    /**
     * @return CompaniesDAO
     */
    public CompaniesDAO getCompaniesDAO() {
        return companiesDAO;
    }

    /**
     * @return CustomersDAO
     */
    public CustomersDAO getCustomersDAO() {
        return customersDAO;
    }

    /**
     * @return CouponsDAO
     */
    public CouponsDAO getCouponsDAO() {
        return couponsDAO;
    }

}
