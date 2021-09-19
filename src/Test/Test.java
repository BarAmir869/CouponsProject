package Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import Exeptions.FacadeException;
import Exeptions.LoginManagerException;
import Exeptions.NoSuchCategoryIdException;
import Facade.AdminFacade;
import Facade.ClientFacade;
import Facade.CompanyFacade;
import Facade.CustomerFacade;
import Facade.LoginManager;
import Facade.LoginManager.ClientType;
import Jobs.Job;
import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;
import database.ConnectionPool;
import database.TablesConstructing;

public class Test {
    static LoginManager loginManager = LoginManager.getInstance();
    static AdminFacade adminFacade;
    ClientFacade clientFacade;
    CustomerFacade customerFacade;
    ConnectionPool connectionPool;
    static Job job = new Job("1");
    private static final int couponIdRange = 20000;

    public static Menus getStaticMenu() {
        return staticMenu;
    }

    public static void setStaticMenu(Menus staticMenu) {
        Test.staticMenu = staticMenu;
    }

    static Menus staticMenu = Menus.MAIN_MENU;

    public Test() {
        connectionPool = ConnectionPool.getInstance();
    }

    public void TestAll() {
        TestAllMenu();
    }
    // *****************************************************************************
    // ****************************** TestAll **************************************
    // *****************************************************************************

    private static void TestAllMenu() {
        boolean exit = false;
        int option = -1;
        Scanner input = new Scanner(System.in);
        while (!exit) {

            System.out.println("***********************************************************************");
            System.out.println("***************         Coupon project Tester           ***************");
            System.out.println("***************         Choose option by order          ***************");
            if (option != -1)
                System.out.println(
                        "***************             last choice: " + option + "              ***************");
            System.out.println("***********************************************************************");
            System.out.print(staticMenu);

            try {
                option = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\n ***    Enter a number    ***");
                option = 0;
                input.nextInt();
            }

            switch (option) {
                case -1:
                    option = 0;
                case 1:
                    startDailyJob();
                    break;
                case 2:
                    System.out.println("");
                    setStaticMenu(Menus.ADMIN_FACADE_MENU);
                    AdminTest.adminTest();
                    break;
                case 3:
                    System.out.println("");
                    setStaticMenu(Menus.COMPANY_FACADE_MENU);
                    CompanyTest.companyTest();
                    break;
                case 4:
                    System.out.println("");
                    setStaticMenu(Menus.CUSTOMER_FACADE_MENU);
                    CustomerTest.customerTest();
                    break;
                case 5:
                    System.out.println("");
                    stopDailyJob();
                    break;
                case 6:
                    System.out.println("");
                    ConnectionPool.getInstance().closeAllConnectionsAvailable();
                    exit = true;
                    System.out.println("Grade 100 well deserved ;)");
                    break;
                case 7:
                    TablesConstructing.dropAllTables();
                    break;
                case 8:
                    try {
                        TablesConstructing.construct();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 9:
                    TestAllMenu();
                    exit = true;
                    break;
                default:
                    System.out.println("Enter a number from the options above");
                    break;
            }
            System.out.println("");
            try {
                joinStartDailyJob();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.out.println();
        }
        // input.close();
    }

    private static void stopDailyJob() {
        System.out.println("***********************************************************************");
        System.out.println("***************\t\t stopDailyJob \t\t\t***************");
        System.out.println("***********************************************************************");
        job.stopJob();
    }

    private static void startDailyJob() {
        System.out.println("***********************************************************************");
        System.out.println("***************\t\t startDailyJob \t\t\t***************");
        System.out.println("***********************************************************************");
        Thread t = new Thread(() -> job.startJob(), "startDailyJob");
        t.start();
    }

    private static void joinStartDailyJob() throws InterruptedException {
        ArrayList<String> names = new ArrayList<>(Arrays.asList("startDailyJob"));
        Thread.getAllStackTraces().keySet().stream().filter(t -> names.contains(t.getName())).forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                // TODO ask Rami what to do with interruptedException
                e.printStackTrace();
            }
        });
    }

    // *****************************************************************************
    // ****************************** AdminFacade Test *****************************
    // *****************************************************************************
    public static class AdminTest {
        private static AdminFacade adminFacade;
        private static ArrayList<Coupon> staticCoupons = new ArrayList<>();
        private static final int numOfCouponsToGenerate = 30;
        private static final String[] companiesNames = { "Apple", "Facebook", "Google", "Amazon", "Paypal", "Samsung",
                "Oracle", "Matrix", "Intel", "Elbit" };
        private static final String[] customerNames = { "Bar", "Rami", "Noa", "Yiftach", "Shahaf", "Tomer", "Jonathan",
                "Danny", "Veronica", "Itay", "Evgenie", "Roey", "Omer", "Yonatan", "Maor", "Yizhak", "Dorel", "Aviad",
                "Yuval", "Bar" };

        private static void adminTest() {
            boolean adminExit = false;
            int option = -1;
            Scanner input = new Scanner(System.in);
            while (!adminExit) {
                System.out.println("***********************************************************************");
                System.out.println("***************\t\t\t AdminFacade \t\t***************");
                if (option == -1) {
                    try {
                        adminFacade = (AdminFacade) loginManager.login("admin@admin.com", "admin",
                                ClientType.ADMINISTRATOR);
                    } catch (LoginManagerException e) {
                        System.out.println(e.getMessage());
                    }
                    System.out.println("***************\t\t Administrator logged-in \t***************");
                    // try {
                    // } catch (Exception e) {// TODO catch specific exception
                    // }
                }
                System.out.println("***********************************************************************");
                System.out.print(staticMenu);// AdminFacade Menu
                try {
                    option = input.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("\n ***    Enter a number    ***");
                    option = 0;
                    input.next();
                }
                switch (option) {
                    case -1:
                        option = 0;
                    case 1:
                        job.startProcess();
                        addTest();
                        job.endProcess();
                        break;
                    case 2:
                        job.startProcess();
                        getTest();
                        job.endProcess();
                        break;
                    case 3:
                        job.startProcess();
                        updateTest();
                        job.endProcess();
                        break;
                    case 4:
                        job.startProcess();
                        deleteTest();
                        job.endProcess();
                        break;
                    case 5:
                        System.out.println("");
                        adminExit = true;
                        Test.setStaticMenu(Menus.MAIN_MENU);
                        break;
                    default:
                        System.out.println("Enter a number from the options above");
                        break;
                }
                System.out.println("");
                try {
                    joinStartDailyJob();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                System.out.println();

            }
            // input.close();
        }

        private static void addTest() {
            for (int i = 0; i < companiesNames.length; i++) {
                try {
                    ArrayList<Coupon> coupons = getRandomCoupons(i + 1);
                    staticCoupons.addAll(coupons);
                    adminFacade.addCompany(
                            new Company(i + 1, companiesNames[i], companiesNames[i] + "@company.com", "1234", coupons));
                    System.out.println("Company id = " + (i + 1) + " inserted ok.");
                } catch (FacadeException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < customerNames.length; i++) {
                try {
                    ArrayList<Coupon> coupons1 = getRandomListFrom(staticCoupons);
                    System.out.println("\nTrying to add customer id = " + (i + 1) + "...");
                    adminFacade.addCustomer(new Customer(i + 1, customerNames[i], "LN",
                            customerNames[i] + "@customer.com", "4321", coupons1));
                    System.out.println("Customer id = " + (i + 1) + " inserted ok.");
                } catch (FacadeException e) {
                    System.out.println(e.getMessage());
                }
            }

        }

        private static void getTest() {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter Company Id");
            int companyID = input.nextInt();
            try {
                System.out.println(adminFacade.getOneCompany(companyID));
            } catch (FacadeException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Enter Customer Id");
            int customerID = input.nextInt();
            try {
                System.out.println(adminFacade.getOneCustomer(customerID));
            } catch (FacadeException e) {
                System.out.println(e.getMessage());
            }
            // input.close();
        }

        // update Random Company and Customer
        private static void updateTest() {
            // Get all companies and update random one:
            ArrayList<Company> companies = adminFacade.getAllCompanies();
            Company company;
            try {
                company = companies.get((int) (Math.random() * companies.size()));
                company.setEmail("myNewEmail@company.co.il");
                adminFacade.updateCompany(company);
                System.out.println("company id = " + company.getId() + " email updated to: " + company.getEmail());
            } catch (FacadeException e) {
                System.out.println(e.getMessage());
            } catch (Exception e1) {
                System.out.println("don't forget to add some entities...");
            }

            // Get all customers and update random one:
            ArrayList<Customer> customers = adminFacade.getAllCustomers();
            Customer customer = customers.get((int) (Math.random() * customers.size()));
            try {
                customer.setEmail("myNewEmail@customer.co.il");
                adminFacade.updateCustomer(customer);
            } catch (FacadeException e1) {
                System.out.println(e1.getMessage());
            } catch (Exception e) {// in case customer==null->customer.setEmail==exception
                System.out.println("don't forget to add some entities...");
            }

            System.out.println("customer id = " + customer.getId() + " email updated to: " + customer.getEmail());

            // get one and update
            Company company1;
            try {
                company1 = adminFacade.getOneCompany((int) (Math.random() * companies.size()));
                company1.setPassword("12345678");
                adminFacade.updateCompany(company1);
                System.out.println(
                        "company id = " + company1.getId() + " password updated to: " + company1.getPassword());
            } catch (FacadeException e1) {
                e1.getMessage();
            }
            try {
                Customer customer1 = adminFacade.getOneCustomer((int) (Math.random() * customers.size()));
                customer1.setFirstName("Dan");
                adminFacade.updateCustomer(customer1);
                System.out.println(
                        "customer id = " + customer1.getId() + " first name updated to: " + customer1.getFirstName());
            } catch (FacadeException e1) {
                e1.getMessage();
            }
        }

        // delete random Company and Customer
        private static void deleteTest() {
            {
                // Get all companies and delete random one:
                ArrayList<Company> companies = adminFacade.getAllCompanies();
                if (companies.size() != 0) {
                    Company company = companies.get((int) (Math.random() * companies.size()));
                    try {
                        companies.remove(company);
                        adminFacade.deleteCompany(company.getId());
                    } catch (FacadeException e2) {
                        System.out.println(e2.getMessage());
                    }
                    String couponsIdStrings = "";
                    if (company.getCoupons().size() == 0)
                        couponsIdStrings = "\"Empty coupons\"";
                    else {
                        couponsIdStrings = company.getCoupons().get(0).getId() + "";
                        for (Coupon coupon : company.getCoupons()) {
                            if (!coupon.equals(company.getCoupons().get(0))) {
                                couponsIdStrings += "," + coupon.getId();
                            }
                        }
                    }
                    System.out.println("company id = " + company.getId() + " deleted with its coupons: id = ("
                            + couponsIdStrings + ")");
                }
                // Get all customers and delete random one:
                ArrayList<Customer> customers = adminFacade.getAllCustomers();
                if (customers.size() != 0) {
                    Customer customer = customers.get((int) (Math.random() * customers.size()));
                    try {
                        adminFacade.deleteCustomer(customer.getId());
                    } catch (FacadeException e1) {
                        System.out.println(e1.getMessage());
                    }
                    System.out.println("customer id = " + customer.getId() + " deleted and all purchases history");
                }
            }
        }

        private static ArrayList<Coupon> getRandomCoupons(int companyID) {
            ArrayList<Coupon> coupons = new ArrayList<>();
            for (int i = 1; i <= numOfCouponsToGenerate; i++) {
                int randomID = (int) (Math.random() * couponIdRange);
                Timestamp startDate = new Timestamp(System.currentTimeMillis());
                Timestamp endDate = new Timestamp(startDate.getTime() + (1000 * 60 * ((int) (Math.random() * 20))));
                coupons.add(new Coupon(randomID, companyID, Category.getRandom(), "My Coupon" + i,
                        "The test works fine!!", startDate, endDate, (int) (Math.random() * numOfCouponsToGenerate),
                        ((int) (Math.random() * 500) / 10) * 10, "no image yet"));
            }
            return coupons;
        }

        private static ArrayList<Coupon> getRandomListFrom(ArrayList<Coupon> coupons) {
            int numOfCouponsToPick = 10;
            ArrayList<Coupon> coupons1 = new ArrayList<>();
            for (int i = 0; i < numOfCouponsToPick; i++) {
                coupons1.add(coupons.get((int) (Math.random() * coupons.size())));
            }
            return coupons1;
        }

    }

    // *****************************************************************************
    // ****************************** CompanyFacade Test ***************************
    // *****************************************************************************
    public static class CompanyTest {
        private static CompanyFacade companyFacade;

        public static void companyTest() {
            boolean adminExit = false;
            int option = -1;
            Scanner input = new Scanner(System.in);
            while (!adminExit) {
                job.startProcess();
                System.out.println("***********************************************************************");
                System.out.println("***************\t\t\t CompanyFacade\t\t***************");
                System.out.println("***********************************************************************");
                if (option == -1) {
                    System.out.println("Enter Email: ");
                    String email = input.next();
                    System.out.println("Enter Password: ");
                    String password = input.next();
                    try {
                        companyFacade = (CompanyFacade) loginManager.login(email, password, ClientType.COMPANY);
                    } catch (LoginManagerException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Redirected back to Main Menu...");
                        Test.setStaticMenu(Test.Menus.MAIN_MENU);
                        break;
                    }
                    try {
                        System.out.println("***********************************************************************");
                        System.out.println("***************\t\t Company " + companyFacade.getCompanyDetails().getName()
                                + " logged-in \t***************");
                        System.out.println("***********************************************************************");

                    } catch (FacadeException e) {
                        System.out.println(e.getMessage());
                    }
                    job.endProcess();
                }
                System.out.println("***********************************************************************");
                System.out.print(staticMenu);// Company Facade Menu
                try {
                    option = input.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("\n *** Enter a number ***");
                    option = 0;
                    input.next();
                }
                if (option != 0) {
                    switch (option) {
                        case 1:
                            job.startProcess();
                            addCouponTest();
                            job.endProcess();
                            break;
                        case 2:
                            job.startProcess();
                            getCoupon();
                            job.endProcess();
                        case 3:
                            job.startProcess();
                            System.out.println("");
                            updateCouponTest();
                            job.endProcess();
                            break;
                        case 4:
                            job.startProcess();
                            System.out.println("");
                            deleteCouponTest();
                            job.endProcess();
                            break;
                        case 5:
                            System.out.println("");
                            adminExit = true;
                            Test.setStaticMenu(Menus.MAIN_MENU);
                            break;
                        default:
                            System.out.println("Enter a number from the options above");
                            break;
                    }
                }
                System.out.println("");
                try {
                    joinStartDailyJob();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                System.out.println();

            }
            // input.close();
        }

        private static void deleteCouponTest() {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter coupon id you would like to delete: ");
            try {
                int id = input.nextInt();
                companyFacade.deleteCoupon(id);
                System.out.println("Coupon id = " + id + " deleted");
            } catch (FacadeException e) {
                System.out.println(e.getMessage());
            }
            // input.close();
        }

        private static void updateCouponTest() {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter coupon id you would like to update its price: ");
            int inputId = input.nextInt();
            ArrayList<Coupon> coupons = companyFacade.getCompanyCoupons();
            coupons.stream().filter(t -> t.getId() == inputId).forEach(t -> {
                try {
                    System.out.println("Enter new price: ");
                    double newPrice = input.nextDouble();
                    t.setPrice(newPrice);
                    companyFacade.updateCoupon(t);
                    System.out.println("Coupon id = " + inputId + " updated to " + newPrice);
                    return;
                } catch (FacadeException e) {
                    System.out.println(e.getMessage());
                }
            });
            // For test
            try {
                companyFacade.getCoupon(inputId);
            } catch (FacadeException e) {
                e.getMessage();
            }
            // input.close();
        }

        private static void getCoupon() {
            Scanner input = new Scanner(System.in);
            System.out.println("Get all coupons for Category:");
            try {
                for (Coupon coupon : companyFacade.getCompanyCoupons(Category.getCategoryByID(input.nextInt()))) {
                    System.out.println(coupon);
                }
            } catch (NoSuchCategoryIdException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Get all coupons up to price: ");
            for (Coupon coupon : companyFacade.getCompanyCoupons(input.nextInt())) {
                System.out.println(coupon);
            }
            try {
                System.out.println("Company Details: \n" + companyFacade.getCompanyDetails());
            } catch (FacadeException e) {
                System.out.println(e.getMessage());
            }
            // input.close();
        }

        private static void addCouponTest() {
            Scanner input = new Scanner(System.in);
            Scanner input1 = new Scanner(System.in);
            System.out.println("Enter Coupon Category id: ");
            int categoryId = input.nextInt();
            Category category;
            try {
                category = Category.getCategoryByID(categoryId);
            } catch (NoSuchCategoryIdException e) {
                System.out.println(e.getMessage() + ", try again");
                // input.close();
                return;
            }
            System.out.println("Enter Coupon title: ");
            String title = input1.nextLine();
            System.out.println("Enter Coupon description: ");
            String des = input1.nextLine();
            System.out.println("Enter Coupon time to be on air(int minutes): ");
            Timestamp startDate = new Timestamp(System.currentTimeMillis());
            long time = input.nextLong() * 1000 * 60;
            Timestamp endDate = new Timestamp(startDate.getTime() + time);
            System.out.println("Enter Coupon amount: ");
            int amount = input.nextInt();
            System.out.println("Enter Coupon price: ");
            double price = input.nextDouble();
            int id = (int) (Math.random() * couponIdRange);
            while (companyFacade.getCouponsDAO().isCouponExist(id)) {
                id = (int) (Math.random() * couponIdRange);
            }
            Coupon coupon = new Coupon(id, companyFacade.getCompanyID(), category, title, des, startDate, endDate,
                    amount, price, "no image yet");
            try {
                companyFacade.addCoupon(coupon);
            } catch (FacadeException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Coupon id = " + id + " inserted ok");
            // input.close();
        }
    }

    // *****************************************************************************
    // ****************************** CustomerFacade Test **************************
    // *****************************************************************************
    public static class CustomerTest {
        private static CustomerFacade customerFacade;

        public static void customerTest() {
            boolean adminExit = false;
            int option = -1;
            Scanner input = new Scanner(System.in);
            while (!adminExit) {
                System.out.println("***********************************************************************");
                System.out.println("***************\t\t\t CustomerFacade \t***************");
                System.out.println("***********************************************************************");
                if (option == -1) {
                    job.startProcess();
                    System.out.println("Enter Email: ");
                    String email = input.next();
                    System.out.println("Enter Password: ");
                    String password = input.next();
                    try {
                        customerFacade = (CustomerFacade) loginManager.login(email, password, ClientType.CUSTOMER);
                    } catch (LoginManagerException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Redirected back to Main Menu...");
                        Test.setStaticMenu(Test.Menus.MAIN_MENU);
                        job.endProcess();
                        break;
                    }
                    try {
                        System.out.println("***********************************************************************");
                        System.out.println("***************\t\t Customer "
                                + customerFacade.getCustomerDetails().getFirstName() + " logged-in \t***************");
                        System.out.println("***********************************************************************");

                    } catch (FacadeException e) {
                        System.out.println(e.getMessage());
                    }
                    job.endProcess();
                }
                System.out.println("***********************************************************************");
                System.out.print(staticMenu);// Customer Facade Menu
                try {
                    option = input.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("\n *** Enter a number ***");
                    option = 0;
                    input.next();
                }
                if (option != 0) {
                    switch (option) {
                        case 1:
                            job.startProcess();
                            Purchase();
                            job.endProcess();
                            break;
                        case 2:
                            job.startProcess();
                            getByCategory();
                            job.endProcess();
                        case 3:
                            job.startProcess();
                            System.out.println("");
                            getByPrice();
                            job.endProcess();
                            break;
                        case 4:
                            job.startProcess();
                            System.out.println("");
                            getCustomerDetails();
                            job.endProcess();
                            break;
                        case 5:
                            System.out.println("");
                            adminExit = true;
                            Test.setStaticMenu(Menus.MAIN_MENU);
                            break;
                        default:
                            System.out.println("Enter a number from the options above");
                            break;
                    }
                }
                System.out.println("");
                try {
                    joinStartDailyJob();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                System.out.println();

            }
            // input.close();
        }

        public static void Purchase() {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter Coupon id you want to purchase: ");
            Coupon coupon = customerFacade.getCouponsDAO().getOneCoupon(input.nextInt());
            try {
                customerFacade.purchaseCoupon(coupon);
                System.out.println("Coupon id = " + coupon.getId() + " was purchased by Customer = "
                        + customerFacade.getCustomerDetails().getFirstName());
            } catch (FacadeException e) {
                System.out.println(e.getMessage());
            }
            // input.close();
        }

        public static void getByCategory() {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter Category id: ");
            try {
                Category category = Category.getCategoryByID(input.nextInt());
                System.out.println(customerFacade.getCustomerCoupons(category));
            } catch (NoSuchCategoryIdException e) {
                System.out.println(e.getMessage());
            }
            // input.close();
        }

        public static void getByPrice() {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter max price: ");
            double maxPrice = input.nextDouble();
            System.out.println(customerFacade.getCustomerCoupons(maxPrice));
            // input.close();
        }

        public static void getCustomerDetails() {
            try {
                System.out.println(customerFacade.getCustomerDetails());
            } catch (FacadeException e) {
                e.getMessage();
            }
        }
    }

    public enum Menus {
        MAIN_MENU(
                "\n *  MAIN MENU \n 1. Start Daily-Job \n 2. Administrator Test  \n 3. Company Test \n 4. Customer Test "
                        + "\n 5. Stop Daily-Job \n 6. Close All Connections \n 7. *DropAllTables \n 8. *ConstructAllTables \n Choose an option: "),
        ADMIN_FACADE_MENU("\n *  AdminFacade MENU \n 1. addTest \n 2. Get Test \n 3. Update Test  \n 4. deleteTest "
                + "\n 5. EXIT -> Back to MENU  \n Choose an option: "),
        COMPANY_FACADE_MENU(
                "\n *  CompanyFacade MENU \n 1. Add coupon \n 2. Get coupon \n 3. Update Coupon  \n 4. Delete Coupon "
                        + "\n 5. EXIT -> Back to MENU  \n Choose an option: "),
        CUSTOMER_FACADE_MENU(
                "\n *  CustomerFacade MENU \n 1. Purchase coupon \n 2. Get coupons by Category \n 3. Get coupons up to maxPrice  \n 4. Get customer Details "
                        + "\n 5. EXIT -> Back to MENU  \n Choose an option: ");

        private String stringMenu;

        Menus(String stringMenu) {
            this.stringMenu = stringMenu;
        }

        public String getStringMenu() {
            return stringMenu;
        }

        @Override
        public String toString() {

            return this.stringMenu;
        }

    }
}
