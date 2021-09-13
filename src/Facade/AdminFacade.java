package Facade;

import java.sql.SQLException;
import java.util.ArrayList;
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

    public void addCompany(Company company) throws SQLException {
        if (companiesDAO.isCompanyExist(company.getId()) || companiesDAO.isCompanyEmailExist(company.getEmail())
                || companiesDAO.isCompanyNameExist(company.getName())) {
            // TODO throw new Exception here
            System.out.println("company id = " + company.getId() + " already exist");
            return;
        }
        companiesDAO.addCompany(company);
        for (Coupon coupon : company.getCoupons())
            couponsDAO.addCoupon(coupon);
        System.out.println("company id = " + company.getId() + " inserted ok");
    }

    public void updateCompany(Company company) throws SQLException {
        if (!companiesDAO.isCompanyExist(company.getId())) {
            // TODO throw new exception here
            System.out.println("Company id = " + company.getId() + " doesn't exist");
            return;
        }
        Company oldCompany = companiesDAO.getCompany(company.getId());
        if (!oldCompany.getName().equals(company.getName())) {
            // TODO throw new exception here
            System.out.println("Company id = " + company.getId() + " can't change name");
            return;
        }
        companiesDAO.updateCompany(company);
        System.out.println("company id = " + company.getId() + " updated");
    }

    public void deleteCompany(int companyID) throws SQLException {
        if (!companiesDAO.isCompanyExist(companyID)) {
            // TODO throw new exception here
            System.out.println("Company id = " + companyID + " doesn't exist");
            return;
        }
        companiesDAO.deleteCompany(companyID);
        ArrayList<Coupon> coupons = couponsDAO.getAllCompanyCoupons(companyID);
        couponsDAO.deleteCompanyCoupons(companyID);// delete all company coupons
        for (Coupon coupon : coupons)
            couponsDAO.deleteCouponPurchases(coupon.getId());
        System.out.println("company id = " + companyID + " deleted");
    }

    public ArrayList<Company> getAllCompanies() {
        ArrayList<Company> companies = companiesDAO.getAllCompanies();
        for (Company company : companies) {
            company.setCoupons(couponsDAO.getAllCompanyCoupons(company.getId()));
        }
        return companies;
    }

    public Company getOneCompany(int companyID) {
        Company company = companiesDAO.getCompany(companyID);
        company.setCoupons(couponsDAO.getAllCompanyCoupons(companyID));
        return company;
    }

    public void addCustomer(Customer customer) throws SQLException {
        if (customersDAO.isCustomerExist(customer.getId()) || customersDAO.isCustomerEmailExist(customer.getEmail())) {
            // TODO throw new Exception here
            System.out.println("Customer id = " + customer.getId() + " 'already exist");
            return;
        }
        customersDAO.addCustomer(customer);
        ArrayList<Coupon> coupons = couponsDAO.getAllCustomerCoupons(customer.getId());
        for (Coupon coupon : customer.getCoupons()) {
            if (!coupons.contains(coupon))
                couponsDAO.addCouponPurchase(customer.getId(), coupon.getId());
        }
        System.out.println("Customer id = " + customer.getId() + " inserted ok.");
    }

    public void updateCustomer(Customer customer) throws SQLException {

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
                try {
                    couponsDAO.deleteCouponPurchase(customer.getId(), coupon.getId());
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        System.out.println("Customer id = " + customer.getId() + " updated.");
    }

    public void deleteCustomer(int customerID) {
        customersDAO.deleteCustomer(customerID);
        try {
            couponsDAO.deleteAllCustomerCouponsPurchases(customerID);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("customer id = " + customerID + " deleted");
    }

    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = customersDAO.getAllCustomers();
        for (Customer customer : customers) {
            customer.setCoupons(couponsDAO.getAllCustomerCoupons(customer.getId()));
        }
        return customers;
    }

    public Customer getOneCustomer(int customerID) {
        Customer customer = customersDAO.getOneCustomer(customerID);
        customer.setCoupons(couponsDAO.getAllCustomerCoupons(customerID));
        return customer;
    }
}
