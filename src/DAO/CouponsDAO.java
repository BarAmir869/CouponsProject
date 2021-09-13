package DAO;

import java.sql.SQLException;
import java.util.ArrayList;

import Exeptions.NoSuchCategoryIdException;
import beans.Coupon;

public interface CouponsDAO {
    public void addCoupon(Coupon coupon) throws SQLException;

    public void addCouponPurchase(int customerID, int couponID) throws SQLException;

    public void updateCoupon(Coupon coupon);

    public ArrayList<Coupon> getAllCoupons() throws SQLException, NoSuchCategoryIdException;

    public ArrayList<Coupon> getAllCompanyCoupons(int companyID);

    public ArrayList<Coupon> getAllCustomerCoupons(int customerID);

    public boolean isCouponExist(int couponID) throws SQLException;

    public Coupon getOneCoupon(int CouponID);

    public void deleteCoupon(int couponID) throws SQLException;

    public void deleteCompanyCoupons(int companyID);

    public void deleteAllCustomerCouponsPurchases(int customerID) throws SQLException;

    public void deleteCouponPurchase(int customerID, int couponID) throws SQLException;

    public void deleteCouponPurchases(int couponID);

    public boolean isTitleExist(int companyID, String title);

    public boolean isCouponExist(int id, int companyID);

}
