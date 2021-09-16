package DAO;

import java.util.ArrayList;
import beans.Customer;

public interface CustomersDAO {

    public void addCustomer(Customer customer);

    public void updateCustomer(Customer customer);

    public ArrayList<Customer> getAllCustomers();

    public Customer getOneCustomer(int customerID);

    public void deleteCustomer(int customerID);

    public boolean isCustomerExist(String email, String password);

    public boolean isCustomerExist(int customerID);

    public boolean isCustomerEmailExist(String email);

    public Customer getCustomerByMail(String email);
}
