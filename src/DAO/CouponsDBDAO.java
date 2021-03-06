package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import Exceptions.NoSuchCategoryIdException;
import beans.Category;
import beans.Coupon;
import database.ConnectionPool;

public class CouponsDBDAO implements CouponsDAO {

    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    /**
     * @param coupon
     */
    @Override
    public void addCoupon(Coupon coupon) {
        // get connection from ConnectionPool:
        Connection connection = connectionPool.getConnection();
        // building sql query
        String sql = "insert into `coupon_project`.`coupons` (`id`, `company_id`, `category_id`, `title`,`description`,"
                + "`start_date`,`end_date`,`amount`,`price`,`image`) VALUES ('" + coupon.getId() + "', '"
                + coupon.getCompanyID() + "','" + coupon.getCategory().getId() + "','" + coupon.getTitle() + "','"
                + coupon.getDescription() + "','" + coupon.getStartDate() + "','" + coupon.getEndDate() + "','"
                + coupon.getAmount() + "','" + coupon.getPrice() + "','" + coupon.getImage() + "')";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);

    }

    /**
     * descriptions
     * @param CustomerID
     * @param CouponID
     */
    @Override
    public void addCouponPurchase(int CustomerID, int CouponID) {
        Connection connection = connectionPool.getConnection();
        String sql = "INSERT INTO customers_vs_coupons (`customer_id`, `coupon_id`) VALUES ('" + CustomerID + "','"
                + CouponID + "');";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);
    }

    /**
     * @return ArrayList<Coupon>
     */
    @Override
    public ArrayList<Coupon> getAllCoupons() {
        ArrayList<Coupon> coupons = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "select * FROM coupons order by end_date ASC";
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            while (results.next()) // Iteration over each row / record
            {
                try {
                    int id = results.getInt("id");
                    int companyID = results.getInt("company_id");
                    Category category;
                    category = Category.getCategoryByID(results.getInt("category_id"));
                    String title = results.getString("title");
                    String description = results.getString("description");
                    Timestamp startDate = results.getObject("start_date", Timestamp.class);
                    Timestamp endDate = results.getObject("end_date", Timestamp.class);
                    int amount = results.getInt("amount");
                    double price = results.getDouble("price");
                    String image = results.getString("image");

                    coupons.add(new Coupon(id, companyID, category, title, description, startDate, endDate, amount,
                            price, image));
                } catch (NoSuchCategoryIdException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);
        return coupons;
    }

    /**
     * @param companyID
     * @return ArrayList<Coupon>
     */
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

    /**
     * @param customerID
     * @return ArrayList<Coupon>
     */
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

    /**
     * @param couponID
     * @return Coupon
     */
    @Override
    public Coupon getOneCoupon(int couponID) {
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

    /**
     * @param couponID
     * @return boolean
     */
    @Override
    public boolean isCouponExist(int couponID) {
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "SELECT * FROM coupons WHERE id=" + couponID + ";";
        ResultSet results;
        boolean isExist = false;
        try {
            statement = connection.createStatement();
            results = statement.executeQuery(sql);
            isExist = results.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);
        return isExist;
    }

    /**
     * @param couponID
     * @param companyID
     * @return boolean
     */
    @Override
    public boolean isCouponExist(int couponID, int companyID) {
        Connection connection = connectionPool.getConnection();
        Statement statement;
        String sql = "SELECT * FROM coupons WHERE id=" + couponID + " And company_id=" + companyID + ";";
        ResultSet results;
        try {
            statement = connection.createStatement();
            results = statement.executeQuery(sql);
            if (results != null) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionPool.restoreConnection(connection);
        }
        return false;
    }

    /**
     * @param companyID
     * @param title
     * @return boolean
     */
    @Override
    public boolean isTitleExist(int companyID, String title) {
        for (Coupon coupon : getAllCompanyCoupons(companyID)) {
            if (coupon.getTitle().equals(title))
                return true;
        }
        return false;
    }

    /**
     * @param coupon
     */
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

    /**
     * @param couponID
     */
    @Override
    public void deleteCoupon(int couponID) {
        Connection connection = connectionPool.getConnection();
        String sql = "DELETE FROM coupons WHERE id=" + couponID + ";";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);

    }

    /**
     * @param CustomerID
     * @param CouponID
     */
    @Override
    public void deleteCouponPurchase(int CustomerID, int CouponID) {
        Connection connection = connectionPool.getConnection();
        String sql = "DELETE FROM customers_vs_coupons WHERE (customer_id=\"" + CustomerID + "\" AND coupon_id = \""
                + CouponID + "\");";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionPool.restoreConnection(connection);

    }

    /**
     * @param customerID
     */
    @Override
    public void deleteAllCustomerCouponsPurchases(int customerID) {
        Connection connection = connectionPool.getConnection();
        String sql = "DELETE FROM customers_vs_coupons WHERE (customer_id = " + customerID + ");";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        connectionPool.restoreConnection(connection);

    }

    /**
     * @param companyID
     */
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

    /**
     * @param couponID
     */
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
