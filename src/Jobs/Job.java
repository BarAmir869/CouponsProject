package Jobs;

public class Job {
    Thread jobThread;
    CouponExpirationDailyJob jobRunnable;

    public Job(String name) {
        jobRunnable = new CouponExpirationDailyJob();
        jobThread = new Thread(jobRunnable, "job" + name);
        jobThread.setDaemon(true);
    }

    public void startJob() {
        if (!jobThread.isAlive())
            jobThread.start();
    }

    public void stopJob() {
        jobRunnable.stop();
        try {
            jobThread.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
