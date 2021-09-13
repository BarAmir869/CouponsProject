package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import beans.Customer;
import database.ConnectionPool;

public class CustomerDBDAO implements CustomersDAO {
    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public void addCustomer(Customer customer) {
        // get connection from ConnectionPool:
        Connection connection = connectionPool.getConnection();
        // building sql query
        String sql = "insert into `coupon_project`.`customers` (`id`, `first_name`, `last_name`, `email`,`password`) VALUES ("
                + customer.getId() + ", '" + customer.getFirstName() + "','" + customer.getLastName() + "','"
                + customer.getEmail() + "','" + customer.getPassword() + "')";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }

    @Override
    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "select * FROM customers";
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            while (results.next()) // Iteration over each row / record
            {
                int id = results.getInt("id");
                String firstName = results.getString("first_name");
                String lastName = results.getString("last_name");
                String email = results.getString("email");
                String password = results.getString("password");
                customers.add(new Customer(id, firstName, lastName, email, password,
                        connectionPool.getCouponsDAO().getAllCustomerCoupons(id)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }

        return customers;
    }

    @Override
    public Customer getOneCustomer(int customerID) {
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "select * FROM customers WHERE id=" + customerID;
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            while (results.next()) // Iteration over each row / record
            {
                String firstName = results.getString("first_name");
                String lastName = results.getString("last_name");
                String email = results.getString("email");
                String password = results.getString("password");
                return new Customer(customerID, firstName, lastName, email, password,
                        connectionPool.getCouponsDAO().getAllCustomerCoupons(customerID));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return null;
    }

    @Override
    public Customer getCustomerByMail(String email) {
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "select * FROM customers WHERE email='" + email + "'";
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            while (results.next()) // Iteration over each row / record
            {
                int id = results.getInt("id");
                String firstName = results.getString("first_name");
                String lastName = results.getString("last_name");
                String password = results.getString("password");
                return new Customer(id, firstName, lastName, email, password,
                        connectionPool.getCouponsDAO().getAllCustomerCoupons(id));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return null;
    }

    @Override
    public void deleteCustomer(int customerID) {
        Connection connection = connectionPool.getConnection();
        String sql = "DELETE FROM `coupon_project`.`customers`WHERE (`id` = '" + customerID + "');";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }

    }

    @Override
    public boolean isCustomerExist(String email, String password) throws SQLException {
        Connection connection = connectionPool.getConnection();
        String sql = "SELECT * FROM customers WHERE (email='" + email + "' AND password = '" + password + "');";
        Statement statement;
        ResultSet results = null;
        statement = connection.createStatement();
        results = statement.executeQuery(sql);
        connectionPool.restoreConnection(connection);
        return results.next();
    }

    @Override
    public boolean isCustomerEmailExist(String email) throws SQLException {
        return genericIsCustomerExist("email", "'" + email + "'");
    }

    @Override
    public boolean isCustomerExist(int customerID) throws SQLException {
        return genericIsCustomerExist("id", customerID);
    }

    @Override
    public void updateCustomer(Customer customer) {
        int id = customer.getId();
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();
        String email = customer.getEmail();
        String password = customer.getPassword();
        Connection connection = connectionPool.getConnection();
        String sql = "UPDATE customers SET first_name = '" + firstName + "', last_name = '" + lastName + "', email='"
                + email + "', password='" + password + "' WHERE id=" + id;
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }

    private <T> boolean genericIsCustomerExist(String column, T object) throws SQLException {
        Connection connection = connectionPool.getConnection();
        String sql = "select * FROM customers WHERE " + column + "=" + object;
        Statement statement;
        ResultSet results = null;
        statement = connection.createStatement();
        results = statement.executeQuery(sql);
        connectionPool.restoreConnection(connection);
        return results.next();
    }

}
