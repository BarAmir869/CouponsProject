package Facade;

import java.sql.SQLException;

import DAO.CompaniesDAO;
import DAO.CouponsDAO;
import DAO.CustomersDAO;
import database.ConnectionPool;

public abstract class ClientFacade {
    protected ConnectionPool connectionPool = ConnectionPool.getInstance();
    protected CompaniesDAO companiesDAO = connectionPool.getCompaniesDAO();
    protected CustomersDAO customersDAO = connectionPool.getCustomersDAO();
    protected CouponsDAO couponsDAO = connectionPool.getCouponsDAO();

    public abstract boolean login(String email, String password) throws SQLException;
}
