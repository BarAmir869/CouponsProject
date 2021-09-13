package DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import beans.Customer;

public interface CustomersDAO {

    public void addCustomer(Customer customer);

    public void updateCustomer(Customer customer);

    public ArrayList<Customer> getAllCustomers();

    public Customer getOneCustomer(int customerID);

    public void deleteCustomer(int customerID);

    public boolean isCustomerExist(String email, String password) throws SQLException;

    public boolean isCustomerExist(int customerID) throws SQLException;

    public boolean isCustomerEmailExist(String email) throws SQLException;

    public Customer getCustomerByMail(String email);
}
