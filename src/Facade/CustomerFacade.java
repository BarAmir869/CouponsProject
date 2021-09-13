package Facade;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.Category;
import beans.Coupon;
import beans.Customer;

public class CustomerFacade extends ClientFacade {
    private int customerID;

    public CustomerFacade() {
        super();
    }

    @Override
    public boolean login(String email, String password) throws SQLException {
        if (customersDAO.isCustomerExist(email, password)) {
            customerID = customersDAO.getCustomerByMail("email").getId();
            return true;
        }
        return false;
    }

    public void purchaseCoupon(Coupon coupon) throws SQLException {

        if (isPurchaseExist(coupon.getId(), customerID)) {
            // TODO throw
            System.out.println("Coupon id = " + coupon.getId() + " has already bought by Customer id = " + customerID);
            return;
        }
        if (coupon.getAmount() == 0) {
            // TODO throw
            System.out.println("Coupon id = " + coupon.getId() + " out of stock");
            return;
        }
        if (coupon.getEndDate().before(new Date(System.currentTimeMillis()))) {
            // TODO throw expired
            System.out.println("Coupon id = " + coupon.getId() + " expired");
        }
        couponsDAO.addCouponPurchase(customerID, coupon.getId());
        coupon.setAmount(coupon.getAmount() - 1);
        couponsDAO.updateCoupon(coupon);
    }

    private boolean isPurchaseExist(int couponID, int customerID) {
        ArrayList<Coupon> purchases = couponsDAO.getAllCustomerCoupons(customerID);
        for (Coupon purchase : purchases) {
            if (purchase.getId() == couponID)
                return true;
        }
        return false;
    }

    public ArrayList<Coupon> getCustomerCoupons() {
        return couponsDAO.getAllCustomerCoupons(customerID);
    }

    public ArrayList<Coupon> getCustomerCoupons(Category category) {
        ArrayList<Coupon> coupons = couponsDAO.getAllCustomerCoupons(customerID);
        ArrayList<Coupon> filteredCoupons = new ArrayList<>();
        for (Coupon coupon : coupons) {
            if (coupon.getCategory().equals(category))
                filteredCoupons.add(coupon);
        }
        return filteredCoupons;
    }

    public ArrayList<Coupon> getCustomerCoupons(double maxPrice) {
        ArrayList<Coupon> coupons = couponsDAO.getAllCustomerCoupons(customerID);
        ArrayList<Coupon> filteredCoupons = new ArrayList<>();
        for (Coupon coupon : coupons) {
            if (coupon.getPrice() <= maxPrice)
                filteredCoupons.add(coupon);
        }
        return filteredCoupons;
    }

    public Customer getCustomerDetails() {
        return customersDAO.getOneCustomer(customerID);
    }
}
