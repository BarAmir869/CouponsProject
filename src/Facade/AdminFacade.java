package Facade;

import java.util.ArrayList;

import Exceptions.FacadeException;
import Exceptions.LoginManagerException;
import Facade.LoginManager.ClientType;
import beans.Company;
import beans.Coupon;
import beans.Customer;

public class AdminFacade extends ClientFacade {
    // private static CompanyFacade companyFacade;
    // private static CustomerFacade customerFacade;
    private static LoginManager loginManager = LoginManager.getInstance();

    public AdminFacade() {
        super();
        // companyFacade = new CompanyFacade();
        // customerFacade = new CustomerFacade();
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
        CompanyFacade companyFacade;
        try {
            companyFacade = (CompanyFacade) loginManager.login(company.getEmail(), company.getPassword(),
                    ClientType.COMPANY);
            for (Coupon coupon : company.getCoupons()) {
                companyFacade.addCoupon(coupon);
            }
        } catch (LoginManagerException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateCompany(Company company) throws FacadeException {
        if (!companiesDAO.isCompanyExist(company.getId())) {
            throw new FacadeException("Company id = " + company.getId() + " doesn't exist");
        }
        Company oldCompany = companiesDAO.getCompany(company.getId());
        if (!oldCompany.getName().equals(company.getName())) {
            throw new FacadeException("Company id = " + company.getId() + " can't change name");
        }
        ArrayList<Coupon> oldCoupons = couponsDAO.getAllCompanyCoupons(company.getId());
        ArrayList<Coupon> newCoupons = company.getCoupons();
        companiesDAO.updateCompany(company);
        // Delete all old coupons and add all new coupons
        for (Coupon coupon : newCoupons) {
            if (!oldCoupons.contains(coupon)) {
                try {
                    CompanyFacade companyFacade;
                    companyFacade = (CompanyFacade) loginManager.login(company.getEmail(), company.getPassword(),
                            ClientType.COMPANY);
                    companyFacade.addCoupon(coupon);
                } catch (LoginManagerException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        for (Coupon coupon : oldCoupons) {
            if (!newCoupons.contains(coupon)) {
                CompanyFacade companyFacade;
                try {
                    companyFacade = (CompanyFacade) loginManager.login(company.getEmail(), company.getPassword(),
                            ClientType.COMPANY);
                    companyFacade.deleteCoupon(coupon.getId());
                } catch (LoginManagerException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

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

    public Company getOneCompany(int companyID) throws FacadeException {
        if (!companiesDAO.isCompanyExist(companyID)) {
            throw new FacadeException("Company id = " + companyID + " doesn't exist");
        }
        Company company = companiesDAO.getCompany(companyID);
        company.setCoupons(couponsDAO.getAllCompanyCoupons(companyID));
        return company;
    }

    public void addCustomer(Customer customer) throws FacadeException {
        if (customersDAO.isCustomerExist(customer.getId()))
            throw new FacadeException("Customer id = " + customer.getId() + " already exist");
        if (customersDAO.isCustomerEmailExist(customer.getEmail())) {
            throw new FacadeException("Customer Email = " + customer.getEmail() + " already exist");
        }
        customersDAO.addCustomer(customer);
        CustomerFacade customerFacade;
        try {
            customerFacade = (CustomerFacade) loginManager.login(customer.getEmail(), customer.getPassword(),
                    ClientType.CUSTOMER);
            for (Coupon coupon : customer.getCoupons())
                customerFacade.purchaseCoupon(coupon);
        } catch (LoginManagerException | FacadeException e) {
            System.out.println(e.getMessage());
        }

    }

    public void updateCustomer(Customer customer) throws FacadeException {
        if (customer == null) {
            System.out.println("argument was null->bug");
        }
        if (!customersDAO.isCustomerExist(customer.getId())) {
            throw new FacadeException("Customer id = " + customer.getId() + " doesn't exist");
        }
        ArrayList<Coupon> oldCoupons = couponsDAO.getAllCustomerCoupons(customer.getId());
        ArrayList<Coupon> newCoupons = customer.getCoupons();
        customersDAO.updateCustomer(customer);
        CustomerFacade customerFacade;
        try {
            customerFacade = (CustomerFacade) loginManager.login(customer.getEmail(), customer.getPassword(),
                    ClientType.CUSTOMER);
            // Delete all old purchased and add all new purchases
            for (Coupon coupon : newCoupons) {
                if (!oldCoupons.contains(coupon)) {
                    customerFacade.purchaseCoupon(coupon);
                }
            }
            for (Coupon coupon : oldCoupons) {
                if (!newCoupons.contains(coupon)) {
                    couponsDAO.deleteCouponPurchase(customer.getId(), coupon.getId());
                    coupon.setAmount(coupon.getAmount() + 1);
                    couponsDAO.updateCoupon(coupon);
                }

            }
        } catch (LoginManagerException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteCustomer(int customerID) throws FacadeException {
        if (!customersDAO.isCustomerExist(customerID)) {
            throw new FacadeException("Customer id = " + customerID + " doesn't exist");
        }
        customersDAO.deleteCustomer(customerID);
        if (couponsDAO.getAllCustomerCoupons(customerID).size() == 0)
            ;
        else
            couponsDAO.deleteAllCustomerCouponsPurchases(customerID);
    }

    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = customersDAO.getAllCustomers();
        for (Customer customer : customers) {
            customer.setCoupons(couponsDAO.getAllCustomerCoupons(customer.getId()));
        }
        return customers;
    }

    public Customer getOneCustomer(int customerID) throws FacadeException {
        if (!customersDAO.isCustomerExist(customerID)) {
            throw new FacadeException("Customer id = " + customerID + " doesn't exist");
        }
        Customer customer = customersDAO.getOneCustomer(customerID);
        customer.setCoupons(couponsDAO.getAllCustomerCoupons(customerID));
        return customer;
    }
}
