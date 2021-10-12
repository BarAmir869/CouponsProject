package Facade;

import java.util.ArrayList;

import Exceptions.FacadeException;
import beans.Category;
import beans.Company;
import beans.Coupon;

public class CompanyFacade extends ClientFacade {

    private int companyID;

    public CompanyFacade() {
        super();
    }

    /**
     * @return int
     */
    public int getCompanyID() {
        return companyID;
    }

    /**
     * @param email
     * @param password
     * @return boolean
     */
    @Override
    public boolean login(String email, String password) {
        if (companiesDAO.isCompanyExist(email, password)) {
            companyID = companiesDAO.getCompanyByMail(email).getId();
            return true;
        }
        return false;
    }

    /**
     * @param coupon
     * @throws FacadeException
     */
    public void addCoupon(Coupon coupon) throws FacadeException {
        if (couponsDAO.isTitleExist(companyID, coupon.getTitle())) {
            throw new FacadeException(
                    "Title \"" + coupon.getTitle() + "\" already exist for Company id = " + companyID);
        }
        if (couponsDAO.isCouponExist(coupon.getId())) {
            throw new FacadeException("Coupon id=" + coupon.getId() + " already exist");
        }
        couponsDAO.addCoupon(coupon);
    }

    /**
     * @param coupon
     * @throws FacadeException
     */
    public void updateCoupon(Coupon coupon) throws FacadeException {// TODO Coupon coupon or int id??
        // for (Coupon coupon2 : couponsDAO.getAllCompanyCoupons(companyID)) {
        // if (!couponsDAO.isCouponExist(coupon2.getId())) {
        // throw new FacadeException("Can't update Company/ Coupon id = " +
        // coupon.getId());

        // }
        // }
        if (!couponsDAO.isCouponExist(coupon.getId(), companyID)) {
            throw new FacadeException("Coupon id = " + coupon.getId() + " doesn't exit for company id = " + companyID);
        } // ****test ****/
        couponsDAO.updateCoupon(coupon);
    }

    /**
     * @param couponID
     * @throws FacadeException
     */
    public void deleteCoupon(int couponID) throws FacadeException {
        if (!couponsDAO.isCouponExist(couponID, companyID)) {
            throw new FacadeException("Coupon id = " + couponID + " doesn't exist");
        }
        couponsDAO.deleteCoupon(couponID);
        couponsDAO.deleteCouponPurchases(couponID);
    }

    /**
     * @param couponID
     * @return Coupon
     * @throws FacadeException
     */
    public Coupon getCoupon(int couponID) throws FacadeException {

        if (!couponsDAO.isCouponExist(couponID, companyID)) {
            throw new FacadeException("Coupon id = " + couponID + " doesn't exit for company id = " + companyID);
        }
        return couponsDAO.getOneCoupon(couponID);
    }

    /**
     * @return ArrayList<Coupon>
     */
    public ArrayList<Coupon> getCompanyCoupons() {
        return couponsDAO.getAllCompanyCoupons(companyID);
    }

    /**
     * @param category
     * @return ArrayList<Coupon>
     */
    public ArrayList<Coupon> getCompanyCoupons(Category category) {
        ArrayList<Coupon> coupons = couponsDAO.getAllCompanyCoupons(companyID);
        ArrayList<Coupon> filteredCoupons = new ArrayList<>();
        for (Coupon coupon : coupons) {
            if (coupon.getCategory().equals(category))
                filteredCoupons.add(coupon);
        }
        return filteredCoupons;
    }

    /**
     * @param maxPrice
     * @return ArrayList<Coupon>
     */
    public ArrayList<Coupon> getCompanyCoupons(double maxPrice) {
        ArrayList<Coupon> coupons = couponsDAO.getAllCompanyCoupons(companyID);
        ArrayList<Coupon> filteredCoupons = new ArrayList<>();
        for (Coupon coupon : coupons) {
            if (coupon.getPrice() <= maxPrice)
                filteredCoupons.add(coupon);
        }
        return filteredCoupons;
    }

    /**
     * @return Company
     * @throws FacadeException
     */
    public Company getCompanyDetails() throws FacadeException {
        if (!companiesDAO.isCompanyExist(companyID))
            throw new FacadeException("Company id = " + companyID + " doesn't exist");
        return companiesDAO.getCompany(companyID);
    }
}
