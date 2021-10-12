package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import beans.Company;

import database.ConnectionPool;
import database.Tables;

public class CompaniesDBDAO implements CompaniesDAO {
    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    /**
     * @param company
     */
    @Override
    public void addCompany(Company company) {
        String email = company.getEmail();
        int id = company.getId();
        String name = company.getName();
        String password = company.getPassword();
        // get connection from ConnectionPool:
        Connection connection = connectionPool.getConnection();
        // building sql query
        String sql = "insert into `coupon_project`.`" + Tables.COMPANIES.getName()
                + "` (`name`, `email`, `password`, `id`) VALUES ('" + name + "', '" + email + "','" + password + "','"
                + id + "')";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }

    }

    /**
     * @param companyID
     */
    @Override
    public void deleteCompany(int companyID) {
        Connection connection = connectionPool.getConnection();
        String sql = "DELETE FROM `coupon_project`.`" + Tables.COMPANIES.getName() + "`WHERE (`id` = '" + companyID
                + "');";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }

    }

    /**
     * @return ArrayList<Company>
     */
    @Override
    public ArrayList<Company> getAllCompanies() {
        ArrayList<Company> companies = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "select id, name, email, password FROM companies";
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            while (results.next()) // Iteration over each row / record
            {
                int id = results.getInt("id");
                String name = results.getString("name");
                String email = results.getString("email");
                String password = results.getString("password");
                companies.add(new Company(id, name, email, password,
                        connectionPool.getCouponsDAO().getAllCompanyCoupons(id)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }

        return companies;
    }

    /**
     * @param companyID
     * @return Company
     */
    @Override
    public Company getCompany(int companyID) {
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "select * FROM companies WHERE id=" + companyID;
        Company company = null;
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            results.next();
            String name = results.getString("name");
            String email = results.getString("email");
            String password = results.getString("password");
            company = new Company(companyID, name, email, password,
                    connectionPool.getCouponsDAO().getAllCompanyCoupons(companyID));
        } catch (SQLException e) {
            // System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return company;
    }

    /**
     * @param email
     * @return Company
     */
    @Override
    public Company getCompanyByMail(String email) {
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "select * FROM companies WHERE email='" + email + "'";
        Company company = null;
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            results.next();
            int id = results.getInt("id");
            String name = results.getString("name");
            String password = results.getString("password");
            company = new Company(id, name, email, password, connectionPool.getCouponsDAO().getAllCompanyCoupons(id));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("companies dao bug");
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return company;
    }

    /**
     * @param email
     * @param password
     * @return boolean
     */
    @Override
    public boolean isCompanyExist(String email, String password) {
        boolean isExist = false;
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "SELECT id, name, email, password FROM companies WHERE (email=\"" + email + "\" AND password = \""
                + password + "\");";
        ResultSet results;
        try {
            statement = connection.createStatement();
            results = statement.executeQuery(sql);
            if (results.next()) {
                isExist = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return isExist;
    }

    /**
     * @param company
     */
    @Override
    public void updateCompany(Company company) {
        String email = company.getEmail();
        int id = company.getId();
        String name = company.getName();
        String password = company.getPassword();
        // get connection from ConnectionPool:
        Connection connection = connectionPool.getConnection();
        String sql = "UPDATE companies SET name = '" + name + "', email='" + email + "', password='" + password
                + "' WHERE id=" + id;
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }
    }

    /**
     * @param companyID
     * @return boolean
     */
    @Override
    public boolean isCompanyExist(int companyID) {
        return genericIsCompanyExist("id", companyID);
    }

    /**
     * @param name
     * @return boolean
     */
    @Override
    public boolean isCompanyNameExist(String name) {
        return genericIsCompanyExist("name", "'" + name + "'");
    }

    /**
     * @param email
     * @return boolean
     */
    @Override
    public boolean isCompanyEmailExist(String email) {
        return genericIsCompanyExist("email", "'" + email + "'");
    }

    /**
     * @param column
     * @param object
     * @return boolean
     */
    private <T> boolean genericIsCompanyExist(String column, T object) {
        Connection connection = connectionPool.getConnection();
        String sql = "select id, name, email, password FROM companies WHERE " + column + "=" + object;
        Statement statement;
        ResultSet results = null;
        boolean isExist = false;
        try {
            statement = connection.createStatement();
            results = statement.executeQuery(sql);
            isExist = results.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return isExist;

    }
}
