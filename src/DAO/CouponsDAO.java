package DAO;

import java.util.ArrayList;
import beans.Coupon;

public interface CouponsDAO {
    public void addCoupon(Coupon coupon);

    public void addCouponPurchase(int customerID, int couponID);

    public void updateCoupon(Coupon coupon);

    public ArrayList<Coupon> getAllCoupons();

    public ArrayList<Coupon> getAllCompanyCoupons(int companyID);

    public ArrayList<Coupon> getAllCustomerCoupons(int customerID);

    public boolean isCouponExist(int couponID);

    public Coupon getOneCoupon(int CouponID);

    public void deleteCoupon(int couponID);

    public void deleteCompanyCoupons(int companyID);

    public void deleteAllCustomerCouponsPurchases(int customerID);

    public void deleteCouponPurchase(int customerID, int couponID);

    public void deleteCouponPurchases(int couponID);

    public boolean isTitleExist(int companyID, String title);

    public boolean isCouponExist(int id, int companyID);

}
