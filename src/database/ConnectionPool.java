package database;

/**
* This class represents a Database Manager. The Database Manager contain <code>Set<Connection></code> sized with a final variable of our choice.
* This is class is a singleton, and every time we use <code>getInstance()</code> the instance, we should use instance.ConnectDB().
* Also this class has variables of the DAO interface.
* *This class is synchronized-safe
*@version 05.09.2021
*@author Bar Amir
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import DAO.CompaniesDAO;
import DAO.CompaniesDBDAO;
import DAO.CouponsDAO;
import DAO.CouponsDBDAO;
import DAO.CustomerDBDAO;
import DAO.CustomersDAO;

public class ConnectionPool {
    // ****************************** Local Variables ******************************
    // singleton instance
    private static ConnectionPool instance;
    // number of the connections we permit to use
    private static final int numOfConnections = 10;
    // Set of Connections
    private Set<Connection> connectionsAvailable;
    // private int connectionsAvailable.size();
    // synchronized locks
    private static Object getConnectionLock = new Object();
    private static Object constructorLock = new Object();
    // Dao interfaces
    private CompaniesDAO companiesDAO;
    private CouponsDAO couponsDAO;
    private CustomersDAO customersDAO;
    private static boolean isConnected = false;

    // db variables
    private static boolean ConnectMessage = true;
    private static String serverName = "localhost";
    private static String dbName = "coupon_project";
    private static String userName = "root";
    private static String password = "1234";
    private static String connectionString = "jdbc:mysql://" + serverName + "/" + dbName + "?user=" + userName
            + "&password=" + password;

    // ****************************** Constructors ******************************
    /**
     * Constructs a new instance of the ConnectionPool, also initialize a new
     * HashSet<Connection> to handle all of our connections. The constructor uses
     * TableConstructing class to construct all tables we need on database.
     */
    private ConnectionPool() {
        connectionsAvailable = new HashSet<>();
    }

    // ****************************** Methods ******************************
    /**
     * initialize <code>numOfConnection</code> Connections and add it to
     * <code>connectionsAvailable</code>
     */
    public void connectDB() {
        if (!isConnected) {
            for (int i = 1; i <= numOfConnections; i++) {
                Connection connection;
                try {
                    connection = DriverManager.getConnection(connectionString);
                    connectionsAvailable.add(connection);
                } catch (SQLException e) {
                    System.out.println("********Connection BUG********");
                    System.out.println(e.getMessage());
                }
                if (i == numOfConnections && ConnectMessage) {
                    ConnectMessage = false;
                }
            }
            // connectionsAvailable.size() = numOfConnections;
            companiesDAO = new CompaniesDBDAO();
            couponsDAO = new CouponsDBDAO();
            customersDAO = new CustomerDBDAO();
            isConnected = true;
            try {
                TablesConstructing.construct();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * initialized the instance of ConnectionPool if it is null and returns it,
     * return the exist instance otherwise.
     * 
     * @return ConnectionPool singleton instance
     */
    public static ConnectionPool getInstance() {
        if (instance == null) {
            synchronized (constructorLock) {// or ConnectionPool.class
                if (instance == null) {
                    System.out.println("***********************************************************************");
                    System.out.println("***************\t\t Making Connections \t\t***************");
                    instance = new ConnectionPool();
                    System.out.println("***************\t\t Connections Making Done \t***************");
                    instance.connectDB();
                    System.out.println("***********************************************************************");
                }
            }
        }
        return instance;
    }

    /**
     * giving a Connection if there is one available, waiting for one to be
     * available otherwise.
     * 
     * @return an available <code>Connection</code> from
     *         <code>connectionsAvailable</code>
     * @throws InterruptedException
     */
    public Connection getConnection() {
        synchronized (getConnectionLock) {
            Connection connection;
            // if there is not an empty Connection, wait:
            if (connectionsAvailable.size() == 0) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e1) {
                        System.out.println(e1.getMessage());
                    }
                }
            }
            // after an empty Connection is reached:
            connection = connectionsAvailable.iterator().next();
            synchronized (connectionsAvailable) {
                connectionsAvailable.remove(connection);
                // connectionsAvailable.size()--;
            }
            return connection;

        }
    }

    /**
     * after every use in a connection, this method needs to be done to restore it
     * back to connectionsAvailable.
     * 
     * @param connection the Connection that is restored back to
     *                   connectionsAvailable.
     */
    public void restoreConnection(Connection connection) {
        synchronized (connectionsAvailable) {
            connectionsAvailable.add(connection);
            // connectionsAvailable.size()++;
            synchronized (this) {
                notify();
            }
        }
    }

    /**
     * close ConnectionPool when done using it.
     */
    public void closeAllConnectionsAvailable() {
        synchronized (getConnectionLock) {
            while (connectionsAvailable.size() != numOfConnections) {
                synchronized (this) {
                    try {
                        System.out.println("waiting for Threads to finish job");
                        String str = (numOfConnections - connectionsAvailable.size()) == 1 ? "is" : "are";
                        System.out.println(numOfConnections - connectionsAvailable.size() + " " + str + " missing");
                        wait();
                    } catch (InterruptedException | IllegalMonitorStateException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            for (Connection connection : connectionsAvailable) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            }
            System.out.println("all connections closed");
            isConnected = false;
            instance = null;
        }

    }

    // ****************************** Getters/Setters ******************************

    /**
     * @return int The number of connections we permit to use
     */
    public int getNumConnections() {
        return numOfConnections;
    }

    /**
     * @return int The number of available connections we have right now
     */
    public int getNumConnectionsAvailable() {
        synchronized (instance) {
            return connectionsAvailable.size();
        }
    }

    /**
     * return the interface that is needed to access the Companies Table.
     * 
     * @return CompaniesDAO
     */
    public CompaniesDAO getCompaniesDAO() {
        return companiesDAO;
    }

    /**
     * return the interface that is needed to access the Coupons Table.
     * 
     * @return CouponsDAO
     */
    public CouponsDAO getCouponsDAO() {
        return couponsDAO;
    }

    /**
     * return the interface that is needed to access the Customers Table.
     * 
     * @return CustomersDAO
     */
    public CustomersDAO getCustomersDAO() {
        return customersDAO;
    }

}
