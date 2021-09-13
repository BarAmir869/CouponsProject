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

    @Override
    public Company getCompany(int companyID) {
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "select * FROM companies WHERE id=" + companyID;
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            while (results.next()) // Iteration over each row / record
            {
                String name = results.getString("name");
                String email = results.getString("email");
                String password = results.getString("password");
                return new Company(companyID, name, email, password,
                        connectionPool.getCouponsDAO().getAllCompanyCoupons(companyID));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return null;
    }

    @Override
    public Company getCompanyByMail(String email) {
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "select * FROM companies WHERE email='" + email + "'";
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            while (results.next()) // Iteration over each row / record
            {
                int id = results.getInt("id");
                String name = results.getString("name");
                String password = results.getString("password");
                return new Company(id, name, email, password, connectionPool.getCouponsDAO().getAllCompanyCoupons(id));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return null;
    }

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
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return isExist;
    }

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
        // TODO what to do with company.coupons??
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

    @Override
    public boolean isCompanyExist(int companyID) throws SQLException {
        return genericIsCompanyExist("id", companyID);
    }

    @Override
    public boolean isCompanyNameExist(String name) throws SQLException {
        return genericIsCompanyExist("name", "'" + name + "'");
    }

    @Override
    public boolean isCompanyEmailExist(String email) throws SQLException {
        return genericIsCompanyExist("email", "'" + email + "'");
    }

    private <T> boolean genericIsCompanyExist(String column, T object) throws SQLException {
        Connection connection = connectionPool.getConnection();
        String sql = "select id, name, email, password FROM companies WHERE " + column + "=" + object;
        Statement statement;
        ResultSet results = null;
        statement = connection.createStatement();
        results = statement.executeQuery(sql);
        connectionPool.restoreConnection(connection);
        return results.next();

    }
}
