package Jobs;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import DAO.CouponsDAO;
import DAO.CouponsDBDAO;
import Exeptions.NoSuchCategoryIdException;
import beans.Coupon;
import database.ConnectionPool;

public class CouponExpirationDailyJob implements Runnable {
    private boolean quit = false;
    private ConnectionPool connectionPool;
    private CouponsDAO couponsDAO;
    private static final int timeToSleep = 1000 * 3; // millis*seconds*minutes*hours*days

    public CouponExpirationDailyJob() {
        connectionPool = ConnectionPool.getInstance();
        // connectionPool.connectDB();
        couponsDAO = new CouponsDBDAO();
    }

    @Override
    public void run() {
        System.out.println("Daily-" + Thread.currentThread().getName() + " Start his Work!");
        while (!quit) {
            ArrayList<Coupon> coupons = new ArrayList<>();
            try {
                coupons = couponsDAO.getAllCoupons();
            } catch (SQLException | NoSuchCategoryIdException e1) {
            }
            if (coupons != null) {
                Timestamp now = new Timestamp(System.currentTimeMillis());
                for (Coupon coupon : coupons) {
                    if (coupon.getEndDate().before(now)) {
                        try {
                            couponsDAO.deleteCoupon(coupon.getId());
                            couponsDAO.deleteCouponPurchases(coupon.getId());
                            System.out.println("\nCoupon " + coupon.getId() + " expired! Deleted by Daily-"
                                    + Thread.currentThread().getName());
                        } catch (SQLException e) {
                            // quit = true;
                        }
                    }
                }
            }
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("Job Stopped!");

    }

    public void stop() {
        quit = true;
    }

}
