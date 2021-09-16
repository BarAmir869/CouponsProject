package DAO;

import java.util.ArrayList;

import beans.Company;

public interface CompaniesDAO {
    public boolean isCompanyExist(String email, String password);

    public boolean isCompanyExist(int companyID);

    public boolean isCompanyNameExist(String name);

    public boolean isCompanyEmailExist(String email);

    public void addCompany(Company company);

    public void updateCompany(Company company);

    public void deleteCompany(int companyID);

    public ArrayList<Company> getAllCompanies();

    public Company getCompany(int companyID);

    public Company getCompanyByMail(String string);

}
