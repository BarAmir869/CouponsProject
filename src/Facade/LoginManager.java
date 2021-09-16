package Facade;

import java.sql.SQLException;

public class LoginManager {
    public enum ClientType {
        ADMINISTRATOR(), COMPANY(), CUSTOMER();

        private ClientType() {
        }

    }

    private static LoginManager instance;
    private static Object constructorLock = new Object();

    private LoginManager() {

    }

    public static LoginManager getInstance() {
        if (instance == null) {
            synchronized (constructorLock) {
                if (instance == null) {
                    instance = new LoginManager();
                }
            }
        }
        return instance;
    }

    public ClientFacade login(String email, String password, ClientType clientType) throws SQLException {
        ClientFacade clientFacade;
        switch (clientType) {
            case ADMINISTRATOR:

                clientFacade = new AdminFacade();
                break;
            case COMPANY:
                System.out.println("Company logged-in");
                clientFacade = new CompanyFacade();
                break;
            case CUSTOMER:
                System.out.println("Customer logged-in");
                clientFacade = new CustomerFacade();
                break;
            default:
                System.out.println("default clientType: null");
                clientFacade = null;
                // TODO throw new exception
                break;
        }
        if (clientFacade == null)
            return null;
        if (clientFacade.login(email, password)) {
            return clientFacade;
        }
        return null;
    }
}
