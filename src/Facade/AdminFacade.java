package Facade;

import java.util.ArrayList;

import Exeptions.FacadeException;
import beans.Company;
import beans.Coupon;
import beans.Customer;

public class AdminFacade extends ClientFacade {
    private static CompanyFacade companyFacade;
    private static CustomerFacade customerFacade;

    public AdminFacade() {
        super();
        companyFacade = new CompanyFacade();
        customerFacade = new CustomerFacade();
    }

    @Override
    public boolean login(String email, String password) {
        if (email.equals("admin@admin.com") && password.equals("admin")) {
            return true;
        }
        return false;
    }

    public void addCompany(Company company) throws FacadeException {
        if (companiesDAO.isCompanyExist(company.getId()))
            throw new FacadeException("Company id = " + company.getId() + " already exist");
        if (companiesDAO.isCompanyEmailExist(company.getEmail()))
            throw new FacadeException("Email = " + company.getEmail() + " is in use with another user, try another");
        if (companiesDAO.isCompanyNameExist(company.getName())) {
            throw new FacadeException("Name = " + company.getName() + " is in use with another user, try another");

        }
        companiesDAO.addCompany(company);
        for (Coupon coupon : company.getCoupons())
            companyFacade.addCoupon(coupon);
    }

    public void updateCompany(Company company) throws FacadeException {
        if (!companiesDAO.isCompanyExist(company.getId())) {
            throw new FacadeException("Company id = " + company.getId() + " doesn't exist");
        }
        Company oldCompany = companiesDAO.getCompany(company.getId());
        if (!oldCompany.getName().equals(company.getName())) {
            throw new FacadeException("Company id = " + company.getId() + " can't change name");

        }
        companiesDAO.updateCompany(company);
    }

    public void deleteCompany(int companyID) throws FacadeException {
        if (!companiesDAO.isCompanyExist(companyID)) {
            throw new FacadeException("Company id = " + companyID + " doesn't exist");

        }
        companiesDAO.deleteCompany(companyID);
        ArrayList<Coupon> coupons = couponsDAO.getAllCompanyCoupons(companyID);
        couponsDAO.deleteCompanyCoupons(companyID);// delete all company coupons
        for (Coupon coupon : coupons)
            couponsDAO.deleteCouponPurchases(coupon.getId());
    }

    public ArrayList<Company> getAllCompanies() {
        ArrayList<Company> companies = companiesDAO.getAllCompanies();
        for (Company company : companies) {
            company.setCoupons(couponsDAO.getAllCompanyCoupons(company.getId()));
        }
        return companies;
    }

    public Company getOneCompany(int companyID) {
        if (!companiesDAO.isCompanyExist(companyID)) {
            // TODO exception
            return null;
        }
        Company company = companiesDAO.getCompany(companyID);
        company.setCoupons(couponsDAO.getAllCompanyCoupons(companyID));
        return company;
    }

    public void addCustomer(Customer customer) throws FacadeException {
        if (customersDAO.isCustomerExist(customer.getId()))
            throw new FacadeException("Customer already exist(by ID)");
        if (customersDAO.isCustomerEmailExist(customer.getEmail())) {
            // TODO throw new Exception here
            System.out.println("Customer id = " + customer.getId() + " already exist");
            return;
        }
        customersDAO.addCustomer(customer);
        ArrayList<Coupon> coupons = couponsDAO.getAllCustomerCoupons(customer.getId());
        for (Coupon coupon : customer.getCoupons()) {
            if (!coupons.contains(coupon))// this is better than couponFacade.purchaseCoupon -> don't waste connections
                couponsDAO.addCouponPurchase(customer.getId(), coupon.getId());
        }
        System.out.println("Customer id = " + customer.getId() + " inserted ok.");
    }

    public void updateCustomer(Customer customer) {
        if (customer == null) {
            System.out.println("argument was null->bug");
        }
        if (!customersDAO.isCustomerExist(customer.getId())) {
            // TODO throw new exception here
            System.out.println("Customer id = " + customer.getId() + " doesn't exist");
            return;
        }
        ArrayList<Coupon> oldCoupons = couponsDAO.getAllCustomerCoupons(customer.getId());
        ArrayList<Coupon> newCoupons = customer.getCoupons();
        customersDAO.updateCustomer(customer);
        // Delete all old purchased and add all new purchases
        for (Coupon coupon : newCoupons) {
            if (!oldCoupons.contains(coupon))
                couponsDAO.addCouponPurchase(customer.getId(), coupon.getId());
        }
        for (Coupon coupon : oldCoupons) {
            if (!newCoupons.contains(coupon))
                couponsDAO.deleteCouponPurchase(customer.getId(), coupon.getId());

        }
    }

    public void deleteCustomer(int customerID) {
        customersDAO.deleteCustomer(customerID);
        couponsDAO.deleteAllCustomerCouponsPurchases(customerID);
    }

    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = customersDAO.getAllCustomers();
        for (Customer customer : customers) {
            customer.setCoupons(couponsDAO.getAllCustomerCoupons(customer.getId()));
        }
        return customers;
    }

    public Customer getOneCustomer(int customerID) {
        if (!customersDAO.isCustomerExist(customerID)) {
            // TODO exception
            return null;
        }
        Customer customer = customersDAO.getOneCustomer(customerID);
        customer.setCoupons(couponsDAO.getAllCustomerCoupons(customerID));
        return customer;
    }
}
