package service;

import dataaccess.AuthDAO;
import models.AuthData;

public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logout(AuthData authData) throws Exception {
        AuthService authService = new AuthService(authDAO);
        authService.authenticate(authData);

        authDAO.deleteAuth(authData.authToken());
    }
}
