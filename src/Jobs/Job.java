package Jobs;

public class Job {
    Thread jobThread;
    private static CouponExpirationDailyJob jobRunnable = new CouponExpirationDailyJob();
    private static Object lock = CouponExpirationDailyJob.getLock();

    public Job(String name) {
        jobThread = new Thread(jobRunnable, "job" + name);
        jobThread.setDaemon(true);
    }

    public void startJob() {
        if (!jobThread.isAlive())
            jobThread.start();
    }

    public void startProcess() {
        synchronized (lock) {
            jobRunnable.setTestProcess(true);
        }
    }

    public void endProcess() {
        synchronized (lock) {
            lock.notifyAll();
            jobRunnable.setTestProcess(false);
        }
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
