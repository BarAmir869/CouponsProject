import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

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

public class Test {
    static LoginManager loginManager = LoginManager.getInstance();
    static AdminFacade adminFacade;
    ClientFacade clientFacade;
    CustomerFacade customerFacade;
    ConnectionPool connectionPool;
    static Job job = new Job("1");

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
                System.out.println("*****       You can restart and choose 7 to test all by once      *****");

            System.out.println("***********************************************************************");
            System.out.print("\n 1. Start Daily-Job \n 2. Administrator Test  \n 3. Company Test \n 4. Customer Test "
                    + "\n 5. Stop Daily-Job \n 6. Close All Connections \n Choose an option: ");

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
                        adminTest();
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
                        System.out.println("\nYou can restart and choose 7 to test all by once");
                        break;
                    default:
                        System.out.println("Enter a number from the options above");
                        break;
                }
            }
            System.out.println("");
            try {
                joinAllThreads();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.out.println();
        }

    }

    private static void stopDailyJob() {
        job.stopJob();
    }

    private static void customerTest() {
    }

    private static void companyTest() {
    }

    private static void adminTest() {
        // TODO Thread t = new Thread(target, "adminTest")
        System.out.println("***********************************************************************");
        System.out.println("***************\t\t\t AdminFacade \t\t***************");
        System.out.println("***********************************************************************");
        try {
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
        } catch (Exception e) {
        }
    }

    private static void startDailyJob() {
        System.out.println("***********************************************************************");
        System.out.println("***************\t\t\t startDailyJob \t\t***************");
        System.out.println("***********************************************************************");
        Thread t = new Thread(() -> job.startJob(), "startDailyJob");
        t.start();
    }

    private static void joinAllThreads() throws InterruptedException {
        ArrayList<String> names = new ArrayList<>(
                Arrays.asList("startDailyJob", "adminTest", "companyTest", "customerTest", "stopDailyJob"));
        Thread.getAllStackTraces().keySet().stream().filter(t -> names.contains(t.getName())).forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    private static ArrayList<Coupon> getRandomListFrom(ArrayList<Coupon> coupons) {
        Random random = new Random();
        ArrayList<Coupon> coupons1 = new ArrayList<>();
        for (Coupon coupon : coupons) {
            if (random.nextInt(2) == 0)
                coupons1.add(coupon);
        }
        return coupons1;
    }
}
