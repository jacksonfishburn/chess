package service;

import dataaccess.AuthDAO;
import exceptions.UnauthorizedException;
import models.AuthData;

public class AuthService {
    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public String authorize(AuthData authData) throws Exception {
        if (authData == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        return authData.username();
    }

    public void logout(AuthData authData) throws Exception {
        authorize(authData);

        authDAO.deleteAuth(authData.authToken());
    }
}
