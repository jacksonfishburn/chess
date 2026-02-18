package service;

import dataaccess.AuthDAO;
import exceptions.UnauthorizedException;
import models.AuthRequest;

public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logout(AuthRequest auth) {
        if (authDAO.getAuth(auth.authToken()) == null) {
            throw new UnauthorizedException("unauthorized");
        }
        authDAO.deleteAuth(auth.authToken());
    }
}
