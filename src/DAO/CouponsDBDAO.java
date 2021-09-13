package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import Exeptions.NoSuchCategoryIdException;
import beans.Category;
import beans.Coupon;
import database.ConnectionPool;

public class CouponsDBDAO implements CouponsDAO {
    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public void addCoupon(Coupon coupon) throws SQLException {
        // get connection from ConnectionPool:
        Connection connection = connectionPool.getConnection();
        // building sql query
        String sql = "insert into `coupon_project`.`coupons` (`id`, `company_id`, `category_id`, `title`,`description`,"
                + "`start_date`,`end_date`,`amount`,`price`,`image`) VALUES ('" + coupon.getId() + "', '"
                + coupon.getCompanyID() + "','" + coupon.getCategory().getId() + "','" + coupon.getTitle() + "','"
                + coupon.getDescription() + "','" + coupon.getStartDate() + "','" + coupon.getEndDate() + "','"
                + coupon.getAmount() + "','" + coupon.getPrice() + "','" + coupon.getImage() + "')";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        connectionPool.restoreConnection(connection);

    }

    @Override
    public void addCouponPurchase(int CustomerID, int CouponID) throws SQLException {
        Connection connection = connectionPool.getConnection();
        String sql = "INSERT INTO customers_vs_coupons (`customer_id`, `coupon_id`) VALUES ('" + CustomerID + "','"
                + CouponID + "');";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        connectionPool.restoreConnection(connection);
    }

    @Override
    public ArrayList<Coupon> getAllCoupons() throws SQLException, NoSuchCategoryIdException {
        ArrayList<Coupon> coupons = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "select * FROM coupons order by end_date ASC";
        statement = connection.createStatement();
        ResultSet results = statement.executeQuery(sql);
        while (results.next()) // Iteration over each row / record
        {
            int id = results.getInt("id");
            int companyID = results.getInt("company_id");
            Category category = Category.getCategoryByID(results.getInt("category_id"));
            String title = results.getString("title");
            String description = results.getString("description");
            Timestamp startDate = results.getObject("start_date", Timestamp.class);
            Timestamp endDate = results.getObject("end_date", Timestamp.class);
            int amount = results.getInt("amount");
            double price = results.getDouble("price");
            String image = results.getString("image");

            coupons.add(
                    new Coupon(id, companyID, category, title, description, startDate, endDate, amount, price, image));
        }
        connectionPool.restoreConnection(connection);
        return coupons;
    }

    @Override
    public ArrayList<Coupon> getAllCompanyCoupons(int companyID) {
        ArrayList<Coupon> coupons = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "select * FROM coupons Where company_id= " + companyID;
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            while (results.next()) // Iteration over each row / record
            {
                int id = results.getInt("id");
                companyID = results.getInt("company_id");
                Category category = Category.getCategoryByID(results.getInt("category_id"));
                String title = results.getString("title");
                String description = results.getString("description");
                Timestamp startDate = results.getObject("start_date", Timestamp.class);
                Timestamp endDate = results.getObject("end_date", Timestamp.class);
                int amount = results.getInt("amount");
                double price = results.getDouble("price");
                String image = results.getString("image");
                coupons.add(new Coupon(id, companyID, category, title, description, startDate, endDate, amount, price,
                        image));
            }
        } catch (SQLException | NoSuchCategoryIdException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("There is not any coupons yet");
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return coupons;
    }

    @Override
    public ArrayList<Coupon> getAllCustomerCoupons(int customerID) {
        ArrayList<Coupon> coupons = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "SELECT coupons.* FROM coupons INNER JOIN customers_vs_coupons"
                + " ON customers_vs_coupons.coupon_id=coupons.id and customer_id= " + customerID;
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            while (results.next()) // Iteration over each row / record
            {
                int id = results.getInt("id");
                int companyID = results.getInt("company_id");
                Category category = Category.getCategoryByID(results.getInt("category_id"));
                String title = results.getString("title");
                String description = results.getString("description");
                Timestamp startDate = results.getObject("start_date", Timestamp.class);
                Timestamp endDate = results.getObject("end_date", Timestamp.class);
                int amount = results.getInt("amount");
                double price = results.getDouble("price");
                String image = results.getString("image");
                coupons.add(new Coupon(id, companyID, category, title, description, startDate, endDate, amount, price,
                        image));
            }
        } catch (SQLException | NoSuchCategoryIdException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }

        return coupons;
    }

    @Override
    public Coupon getOneCoupon(int couponID) {
        try {// needed to be caught by the next try
            if (!isCouponExist(couponID)) {
                System.out.println("there is not a coupon with id= " + couponID);
                return null;
            }
        } catch (Exception e) { // SQL Exception
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "select * FROM coupons WHERE id=" + couponID;
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            while (results.next()) // Iteration over each row / record
            {
                int id = results.getInt("id");
                int companyID = results.getInt("company_id");
                Category category = Category.getCategoryByID(results.getInt("category_id"));
                String title = results.getString("title");
                String description = results.getString("description");
                Timestamp startDate = results.getObject("start_date", Timestamp.class);
                Timestamp endDate = results.getObject("end_date", Timestamp.class);
                int amount = results.getInt("amount");
                double price = results.getDouble("price");
                String image = results.getString("image");
                return new Coupon(id, companyID, category, title, description, startDate, endDate, amount, price,
                        image);
            }
        } catch (SQLException | NoSuchCategoryIdException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return null;
    }

    @Override
    public boolean isCouponExist(int couponID) throws SQLException {
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "SELECT * FROM companies WHERE id=" + couponID + ";";
        ResultSet results;
        statement = connection.createStatement();
        results = statement.executeQuery(sql);
        connectionPool.restoreConnection(connection);
        return results.next();
    }

    @Override
    public boolean isCouponExist(int couponID, int companyID) {
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "SELECT * FROM companies WHERE id=" + couponID + " And company_id=" + companyID + ";";
        ResultSet results;
        try {
            statement = connection.createStatement();
            results = statement.executeQuery(sql);
            if (results != null) {
                return true;
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return false;
    }

    @Override
    public boolean isTitleExist(int companyID, String title) {
        for (Coupon coupon : getAllCompanyCoupons(companyID)) {
            if (coupon.getTitle().equals(title))
                return true;
        }
        return false;
    }

    @Override
    public void updateCoupon(Coupon coupon) {
        Connection connection = connectionPool.getConnection();
        String sql = "UPDATE coupons SET company_id=" + coupon.getCompanyID() + ", category_id="
                + coupon.getCategory().getId() + ", title='" + coupon.getTitle() + "',description='"
                + coupon.getDescription() + "'," + "start_date='" + coupon.getStartDate() + "',end_date='"
                + coupon.getEndDate() + "',amount=" + coupon.getAmount() + ",price=" + coupon.getPrice() + ",image='"
                + coupon.getImage() + "' WHERE id=" + coupon.getId();
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

    @Override
    public void deleteCoupon(int couponID) throws SQLException {
        Connection connection = connectionPool.getConnection();
        String sql = "DELETE FROM coupons WHERE id=" + couponID + ";";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        deleteCouponPurchases(couponID);
        connectionPool.restoreConnection(connection);

    }

    @Override
    public void deleteCouponPurchase(int CustomerID, int CouponID) throws SQLException {
        Connection connection = connectionPool.getConnection();
        String sql = "DELETE FROM customers_vs_coupons WHERE (customer_id=\"" + CustomerID + "\" AND coupon_id = \""
                + CouponID + "\");";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        connectionPool.restoreConnection(connection);

    }

    @Override
    public void deleteAllCustomerCouponsPurchases(int customerID) throws SQLException {
        Connection connection = connectionPool.getConnection();
        String sql = "DELETE FROM customers_vs_coupons WHERE (customer_id = " + customerID + ");";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();

        connectionPool.restoreConnection(connection);

    }

    @Override
    public void deleteCompanyCoupons(int companyID) {
        Connection connection = connectionPool.getConnection();
        String sql = "DELETE FROM coupons WHERE company_id=" + companyID + ";";
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
    public void deleteCouponPurchases(int couponID) {
        Connection connection = connectionPool.getConnection();
        String sql = "DELETE FROM customers_vs_coupons WHERE (coupon_id = " + couponID + ");";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }

    }
}
