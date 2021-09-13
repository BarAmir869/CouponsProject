package DAO;

import java.sql.SQLException;
import java.util.ArrayList;

import beans.Company;

public interface CompaniesDAO {
    public boolean isCompanyExist(String email, String password);

    public boolean isCompanyExist(int companyID) throws SQLException;

    public boolean isCompanyNameExist(String name) throws SQLException;

    public boolean isCompanyEmailExist(String email) throws SQLException;

    public void addCompany(Company company);

    public void updateCompany(Company company);

    public void deleteCompany(int companyID);

    public ArrayList<Company> getAllCompanies();

    public Company getCompany(int companyID);

    public Company getCompanyByMail(String string);

}
