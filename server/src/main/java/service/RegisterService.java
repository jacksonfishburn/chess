package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import models.*;

public class RegisterService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(UserData data) throws Exception {
        userDAO.createUser(data);
        String authToken = authDAO.createAuth(data.username());

        return new RegisterResult(data.username(), authToken);
    }
}
