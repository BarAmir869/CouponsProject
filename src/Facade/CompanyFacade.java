package Facade;

import java.util.ArrayList;
import beans.Category;
import beans.Company;
import beans.Coupon;

public class CompanyFacade extends ClientFacade {

    private int companyID;

    public CompanyFacade() {
        super();
    }

    @Override
    public boolean login(String email, String password) {
        if (companiesDAO.isCompanyExist(email, password)) {
            companyID = companiesDAO.getCompanyByMail("email").getId();
            return true;
        }
        return false;
    }

    public void addCoupon(Coupon coupon) {
        if (couponsDAO.isTitleExist(companyID, coupon.getTitle())) {
            // TODO throw new exception here
            // System.out.println("Title \"" + coupon.getTitle() + "\" already exist for
            // Company id = " + companyID);
            return;
        }
        if (couponsDAO.isCouponExist(coupon.getId())) {
            // TODO throw
            // System.out.println("Coupon id=" + coupon.getId() + " already exist");
            return;
        }
        couponsDAO.addCoupon(coupon);
    }

    public void updateCoupon(Coupon coupon) {
        for (Coupon coupon2 : couponsDAO.getAllCompanyCoupons(companyID)) {
            if (!couponsDAO.isCouponExist(coupon2.getId())) {
                // TODO throw new exception here
                System.out.println("Can't update Company/ Coupon id = " + coupon.getId());
                return;
            }
        }
        // if (!couponsDAO.isCouponExist(coupon.getId(), companyID)) {
        // // TODO throw new exception here
        // } //****test ****/
        couponsDAO.updateCoupon(coupon);
    }

    public void deleteCoupon(int couponID) {
        if (!couponsDAO.isCouponExist(couponID, companyID)) {
            // TODO throw new exception here
            System.out.println("Coupon id = " + couponID + " doesn't exist");
            return;
        }
        couponsDAO.deleteCoupon(couponID);
        couponsDAO.deleteCouponPurchases(couponID);
    }

    public ArrayList<Coupon> getCompanyCoupons(Category category) {
        ArrayList<Coupon> coupons = couponsDAO.getAllCompanyCoupons(companyID);
        ArrayList<Coupon> filteredCoupons = new ArrayList<>();
        for (Coupon coupon : coupons) {
            if (coupon.getCategory().equals(category))
                filteredCoupons.add(coupon);
        }
        return filteredCoupons;
    }

    public ArrayList<Coupon> getCompanyCoupons(double maxPrice) {
        ArrayList<Coupon> coupons = couponsDAO.getAllCompanyCoupons(companyID);
        ArrayList<Coupon> filteredCoupons = new ArrayList<>();
        for (Coupon coupon : coupons) {
            if (coupon.getPrice() <= maxPrice)
                filteredCoupons.add(coupon);
        }
        return filteredCoupons;
    }

    public Company getCompanyDetails() {
        return companiesDAO.getCompany(companyID);
    }
}
