package service;

import dataaccess.AuthDAO;

public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logout(String auth) {
        authDAO.deleteAuth(auth);
    }
}
