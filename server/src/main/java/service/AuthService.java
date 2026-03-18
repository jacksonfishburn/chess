package service;

import dataaccess.AuthDAO;
import exceptions.UnauthorizedException;
import models.models.AuthData;

public class AuthService {
    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public String authorize(String authToken) throws Exception {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        return authData.username();
    }

    public void logout(String authToken) throws Exception {
        authorize(authToken);

        authDAO.deleteAuth(authToken);
    }
}
