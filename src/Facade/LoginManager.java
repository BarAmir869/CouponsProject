package Facade;

import Exeptions.LoginManagerException;

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

    public ClientFacade login(String email, String password, ClientType clientType) throws LoginManagerException {
        ClientFacade clientFacade;
        switch (clientType) {
            case ADMINISTRATOR:
                clientFacade = new AdminFacade();
                break;
            case COMPANY:
                clientFacade = new CompanyFacade();
                break;
            case CUSTOMER:
                clientFacade = new CustomerFacade();
                break;
            default:
                System.out.println("default clientType: null");
                throw new LoginManagerException("Wrong Client Type");
        }
        if (clientFacade.login(email, password)) {
            return clientFacade;
        } else
            throw new LoginManagerException("Wrong Email/Password!");
    }
}
