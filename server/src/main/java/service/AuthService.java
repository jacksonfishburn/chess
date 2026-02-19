package service;

import dataaccess.AuthDAO;
import exceptions.UnauthorizedException;
import models.AuthData;

public class AuthService {
    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public String authenticate(AuthData authData) throws Exception {
        if (authData == null) {
            throw new UnauthorizedException("unauthorized");
        }
        return authData.username();
    }
}
