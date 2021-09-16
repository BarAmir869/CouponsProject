package Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

import Exeptions.FacadeException;
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
import database.TablesConstructing;

public class Test {
    static LoginManager loginManager = LoginManager.getInstance();
    static AdminFacade adminFacade;
    ClientFacade clientFacade;
    CustomerFacade customerFacade;
    ConnectionPool connectionPool;
    static Job job = new Job("1");

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

    private static void TestAllMenu() {
        boolean exit = false;
        int option = 0;
        Scanner input = new Scanner(System.in);
        while (!exit) {

            System.out.println("***********************************************************************");
            System.out.println("***************         Coupon project Tester           ***************");
            System.out.println("***************         Choose option by order          ***************");
            if (option != 0)
                System.out.println(
                        "***************             last choice: " + option + "              ***************");
            else
                System.out.println("*****       You can restart and choose 8 to test all by once      *****");

            System.out.println("***********************************************************************");
            System.out.print(staticMenu);

            try {
                option = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\n ***    Enter a number    ***");
                option = 0;
                input.next();
            }

            if (option != 0) {
                switch (option) {
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
                        companyTest();
                        break;
                    case 4:
                        System.out.println("");
                        customerTest();
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
                        System.out.println("\nYou can restart and choose 8 to test all by once");
                        break;
                    case 7:
                        TablesConstructing.dropAllTables();
                        break;
                    case 8:
                        TablesConstructing.construct();
                        break;
                    case 9:
                        TestAllMenu();
                        exit = true;
                        break;
                    default:
                        System.out.println("Enter a number from the options above");
                        break;
                }
            }
            System.out.println("");
            // try {
            // joinAllThreads();
            // } catch (InterruptedException e1) {
            // e1.printStackTrace();
            // }
            System.out.println();
        }

    }

    private static void stopDailyJob() {
        System.out.println("***********************************************************************");
        System.out.println("***************\t\t stopDailyJob \t\t\t***************");
        System.out.println("***********************************************************************");
        job.stopJob();
    }

    private static void customerTest() {
    }

    private static void companyTest() {
    }

    private static void startDailyJob() {
        System.out.println("***********************************************************************");
        System.out.println("***************\t\t startDailyJob \t\t\t***************");
        System.out.println("***********************************************************************");
        Thread t = new Thread(() -> job.startJob(), "startDailyJob");
        t.start();
    }

    private static void joinAllThreads() throws InterruptedException {
        ArrayList<String> names = new ArrayList<>(
                Arrays.asList("startDailyJob", "adminTest", "companyTest", "customerTest", "stopDailyJob", "addTest"));
        Thread.getAllStackTraces().keySet().stream().filter(t -> names.contains(t.getName())).forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public static class AdminTest {
        private static AdminFacade adminFacade;
        private static ArrayList<Coupon> staticCoupons = getRandomCoupons();
        private static final int couponIdRange = 20000;
        private static final int numOfCouponsToGenerate = 100;
        private static final String[] companiesNames = { "Apple", "Facebook", "Google", "Amazon", "Paypal", "Samsung",
                "Oracle", "Matrix", "Intel", "Elbit" };
        private static final String[] customerNames = { "Bar", "Rami", "Noa", "Yiftach", "Shahaf", "Tomer", "Jonathan",
                "Danny", "Veronica", "Itay", "Evgenie", "Roey", "Omer", "Yonatan", "Maor", "Yizhak", "Dorel", "Aviad",
                "Yuval", "Danny" };

        private static void adminTest() {
            // TODO Thread t = new Thread(target, "adminTest")
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
                        System.out.println("***************\t\t Administrator logged-in \t***************");
                    } catch (Exception e) {// TODO catch specific exception
                    }
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
                if (option != 0) {
                    switch (option) {
                        case 1:
                            job.startProcess();
                            addTest();
                            job.endProcess();
                            break;
                        case 2:
                            System.out.println("");
                            getAndUpdateTest();
                            break;
                        case 3:
                            System.out.println("");
                            deleteTest();
                            break;
                        case 4:
                            addTest();
                            getAndUpdateTest();
                            deleteTest();
                            getTest();
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
                // try {
                // joinAllThreads();
                // } catch (InterruptedException e1) {
                // e1.printStackTrace();
                // }
                System.out.println();

            }
        }

        private static void addTest() {

            for (int i = 0; i < companiesNames.length; i++) {
                try {
                    adminFacade.addCompany(new Company(i + 1, companiesNames[i], companiesNames[i] + "@company.com",
                            "1234", getRandomListFrom(staticCoupons)));
                } catch (FacadeException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < customerNames.length; i++) {
                try {
                    adminFacade.addCustomer(new Customer(i + 1, customerNames[i], "LN",
                            customerNames[i] + "@customer.com", "4321", getRandomListFrom(staticCoupons)));
                } catch (FacadeException e) {
                    System.out.println(e.getMessage());
                }
            }

        }

        private static void getAndUpdateTest() {
            // Get all companies and update random one:
            ArrayList<Company> companies = adminFacade.getAllCompanies();
            Company company = companies.get((int) (Math.random() * companiesNames.length));
            try {
                company.setEmail("myNewEmail@company.co.il");
            } catch (Exception e) {
                System.out.println("don't forget to add some entities...");

            }
            try {
                adminFacade.updateCompany(company);
            } catch (FacadeException e1) {
                System.out.println(e1.getMessage());
            }
            System.out.println("company id = " + company.getId() + "email updated");

            // Get all customers and update random one:
            ArrayList<Customer> customers = adminFacade.getAllCustomers();
            Customer customer = customers.get((int) (Math.random() * customerNames.length));
            try {
                customer.setEmail("myNewEmail@customer.co.il");

            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("don't forget to add some entities...");
            }
            adminFacade.updateCustomer(customer);
            System.out.println("customer id = " + customer.getId() + "email updated");

            // get one and update
            Company company1 = adminFacade.getOneCompany((int) (Math.random() * companiesNames.length) + 1);
            try {
                company1.setPassword("12345678");
            } catch (Exception e) {
            }
            try {
                adminFacade.updateCompany(company1);
            } catch (FacadeException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("company id = " + company1.getId() + "password updated");
            Customer customer1 = adminFacade.getOneCustomer((int) (Math.random() * customerNames.length) + 1);
            customer1.setFirstName("first name changed");
            adminFacade.updateCustomer(customer1);
            System.out.println("customer id = " + customer1.getId() + "first name updated");

        }

        private static void deleteTest() {

        }

        private static void getTest() {

        }

        private static ArrayList<Coupon> getRandomCoupons() {
            ArrayList<Coupon> coupons = new ArrayList<>();

            for (int i = 1; i <= numOfCouponsToGenerate; i++) {
                int randomID = (int) (Math.random() * couponIdRange);
                Timestamp startDate = new Timestamp(System.currentTimeMillis());
                Timestamp endDate = new Timestamp(startDate.getTime() + (1000 * 60 * ((int) (Math.random() * 20))));
                coupons.add(new Coupon(randomID, 1, Category.getRandom(), "My Coupon" + i, "The test works fine!!",
                        startDate, endDate, (int) (Math.random() * numOfCouponsToGenerate),
                        ((int) (Math.random() * 500) / 10) * 10, "no image yet"));
            }
            return coupons;
        }

        private static ArrayList<Coupon> getRandomListFrom(ArrayList<Coupon> coupons) {
            ArrayList<Coupon> coupons1 = new ArrayList<>();
            for (Coupon coupon : coupons) {

                if ((int) (Math.random() * 2) == 0)
                    coupons1.add(coupon);

            }
            return coupons1;
        }

        private static ArrayList<Coupon> getNewListFrom(ArrayList<Coupon> coupons) {
            ArrayList<Coupon> coupons1 = new ArrayList<>();
            for (Coupon coupon : coupons) {
                coupon.setId((int) (Math.random() * couponIdRange));
            }
            return coupons1;

        }
    }

    public enum Menus {
        MAIN_MENU(
                "\n *  MAIN MENU \n 1. Start Daily-Job \n 2. Administrator Test  \n 3. Company Test \n 4. Customer Test "
                        + "\n 5. Stop Daily-Job \n 6. Close All Connections \n 7.DropAllTable \n Choose an option: "),
        ADMIN_FACADE_MENU("\n *  AdminFacade MENU \n 1. addTest \n 2. Get and Update Test  \n 3. deleteTest "
                + "\n 4. testAll together \n 5. EXIT -> Back to MENU  \n Choose an option: ");

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
