package Jobs;

import java.sql.Timestamp;
import java.util.ArrayList;

import DAO.CouponsDAO;
import DAO.CouponsDBDAO;
import Test.Test;
import beans.Coupon;

public class CouponExpirationDailyJob implements Runnable {
    private static Object lock = new Object();
    private boolean quit;
    private static boolean testProcess = false;
    private CouponsDAO couponsDAO;
    private static final int timeToSleep = 1000 * 3; // millis*seconds*minutes*hours*days

    public CouponExpirationDailyJob() {
        couponsDAO = new CouponsDBDAO();
    }

    /**
     * @return boolean
     */
    public boolean isTestProcess() {
        return testProcess;
    }

    /**
     * @param testProcess
     */
    public void setTestProcess(boolean testProcess) {
        CouponExpirationDailyJob.testProcess = testProcess;
    }

    /**
     * @return Object
     */
    public static Object getLock() {
        return lock;
    }

    @Override
    public void run() {
        quit = false;
        while (!quit) {
            synchronized (lock) {
                if (testProcess) {
                    try {
                        lock.wait();
                        System.out.println("waking up...");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            ArrayList<Coupon> coupons = couponsDAO.getAllCoupons();
            if (coupons.size() > 0) {
                boolean expired = false;
                Timestamp now = new Timestamp(System.currentTimeMillis());
                for (Coupon coupon : coupons) {
                    if (coupon.getEndDate().before(now)) {
                        couponsDAO.deleteCoupon(coupon.getId());
                        couponsDAO.deleteCouponPurchases(coupon.getId());
                        if (!expired) {
                            System.out.println();
                            System.out
                                    .println("***********************************************************************");
                            System.out
                                    .println("*****             <<<<<Time to clean some trash>>>>>>             *****");
                            System.out
                                    .println("***********************************************************************");

                        }
                        System.out.println("*****\t\tCoupon " + coupon.getId() + " expired! Deleted by Daily-"
                                + Thread.currentThread().getName() + "\t  *****");
                        expired = true;
                    }
                }
                if (expired) {
                    System.out.println("***********************************************************************");
                    System.out.println(Test.getStaticMenu());

                }
            }
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Job Stopped!");

    }

    public void stop() {
        quit = true;
    }

}
