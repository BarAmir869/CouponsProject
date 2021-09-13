
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import DAO.CompaniesDAO;
import DAO.CouponsDAO;
import DAO.CustomersDAO;
import Facade.AdminFacade;
import Facade.ClientFacade;
import Facade.CustomerFacade;
import Facade.LoginManager;
import Facade.LoginManager.ClientType;
import Jobs.Job;
import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;
import database.ConnectionPool;

public class TestDAO {
    public static ConnectionPool connectionPool = ConnectionPool.getInstance();
    public static CompaniesDAO companiesDAO;
    public static CouponsDAO couponsDAO;
    public static CustomersDAO customerDAO;

    public static void main(String[] args) throws SQLException, InterruptedException {
        System.out.println("Test Started");
        companiesDAO = connectionPool.getCompaniesDAO();
        couponsDAO = connectionPool.getCouponsDAO();
        customerDAO = connectionPool.getCustomersDAO();

        // Customer bar = customerDAO.getOneCustomer(2);
        // System.out.println(bar);
        // for (int i = 1; i < 100; i++) {
        // addCompanyThread(new Company(i, "company" + i, "email@gmail.com", "1234", new
        // ArrayList<Coupon>()));
        // }
        // // closeAllConnectionsAvailableThread();
        // Date startDate = new Date(System.currentTimeMillis());
        // Date endDate = new Date((long) (startDate.getTime() + (3.1556926 *
        // Math.pow(10, 10))));
        // for (int i = 1; i <= 20; i++) {

        // couponsDAO.addCoupon(new Coupon(i, i, Category.FOOD, "hamburger", "coupon for
        // 220g hamburger", startDate,
        // endDate, 5, 10.90, "myImage"));

        // }

        // companiesDAO.deleteCompany(5);
        // System.out.println(companiesDAO.getAllCompanies());
        // System.out.println("***********\n" + companiesDAO.getCompany(14));
        // System.out.println(companiesDAO.isCompanyExist("email@gmail.com", "1234"));
        // companiesDAO.updateCompany(new Company(22, "myNewCompany",
        // "newEmail@gmail.com", "234222", new ArrayList<>()));
        // System.out.println("***********\n" + companiesDAO.getCompany(22));

        // for (int i = 1; i <= 10; i++) {
        // couponsDAO.addCouponPurchase(i, i);
        // }
        // couponsDAO.deleteCouponPurchase(100, 100);
        // couponsDAO.addCouponPurchase(20, 20);
        // System.out.println(couponsDAO.getAllCoupons());
        // System.out.println("***********\n" + couponsDAO.getOneCoupon(14));
        // couponsDAO.updateCoupon(new Coupon(5, 12, Category.ELECTRICITY, "computer",
        // "apple MacBook",
        // new java.sql.Date(50000), new java.sql.Date(5000000), 1000, 500, "apple
        // image"));
        // System.out.println(couponsDAO.getAllCompanyCoupons(12));
        // System.out.println(couponsDAO.isCouponExist(12));
        // Customer customer = new Customer(2, "Bar", "Amir", "bar@gmail.com", "1234",
        // new ArrayList<Coupon>(Arrays.asList(new Coupon(10000, 10, Category.VACATION,
        // "fiji vacation", "weekend",
        // startDate, endDate, 1, 10, "Beach image"))));
        // customerDAO.addCustomer(customer);

        // joinAllThreads();System.out.println("Test Ended");
    }

    LoginManager loginManager = LoginManager.getInstance();
    AdminFacade adminFacade;
    ClientFacade clientFacade;
    CustomerFacade customerFacade;
    // ConnectionPool connectionPool;

    public void TestAll() {
        Job job = new Job("1");
        try {
            job.startJob();
            System.out.println("***************\t\t\t AdminFacade \t\t***************");
            adminFacade = (AdminFacade) loginManager.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);
            ArrayList<Coupon> coupons = new ArrayList<>();

            for (int i = 1; i <= 50; i++) {
                int randomID = (int) (Math.random() * 500);
                Timestamp startDate = new Timestamp(System.currentTimeMillis());
                Timestamp endDate = new Timestamp(startDate.getTime() + (1000 * 60 * ((int) (Math.random() * 20))));
                coupons.add(new Coupon(randomID, 1, Category.getRandom(), "My Coupon" + i, "The test works fine!!",
                        startDate, endDate, (int) (Math.random() * 50), ((int) (Math.random() * 500) / 10) * 10,
                        "no image yet"));
            }
            adminFacade.addCompany(new Company(1, "Bar Company", "bar@email.com", "1234", coupons));
            adminFacade.addCompany(
                    new Company(2, "Nir Company", "nir@email.com", "467925588", getRandomListFrom(coupons)));
            adminFacade.addCustomer(
                    new Customer(13, "Bar", "Amir", "bar@customer.com", "4321", getRandomListFrom(coupons)));
            adminFacade.addCustomer(
                    new Customer(20, "Noam", "Amir", "noam@customer.com", "9913487", getRandomListFrom(coupons)));
            adminFacade.updateCustomer(
                    new Customer(13, "Bar", "Amir", "bar@customer.com", "87654321", getRandomListFrom(coupons)));
            Thread.sleep(200000);
            job.stopJob();
            connectionPool.closeAllConnectionsAvailable();
            System.out.println("waiting for job 1 to stop...");
            joinThread("job 1");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private ArrayList<Coupon> getRandomListFrom(ArrayList<Coupon> coupons) {
        return null;
    }

    private void joinThread(String string) {
    }

    private static void joinAllThreads() throws InterruptedException {
        // for (Thread t : Thread.getAllStackTraces().keySet()) {
        // if (t.getName().contains("company")) {
        // t.join();
        // }
        // }
        Thread.getAllStackTraces().keySet().stream().filter(t -> t.getName().contains("company")).forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        Thread.getAllStackTraces().keySet().stream().filter(t -> t.getName().equals("closingThread")).forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    private static void closeAllConnectionsAvailableThread() {
        Thread thread = new Thread(() -> {
            connectionPool.closeAllConnectionsAvailable();
        }, "closingThread");
        thread.start();
    }

    private static void addCompanyThread(Company company) {
        Thread t1 = new Thread(() -> {

            companiesDAO.addCompany(company);

        }, company.getName());
        t1.start();
    }
}
