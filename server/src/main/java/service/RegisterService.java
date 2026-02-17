package service;

import dataaccess.AuthDAO;
import exceptions.BadRequestException;
import dataaccess.UserDAO;
import models.*;

public class RegisterService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public SessionStartResult register(UserData data) throws Exception {
        if (data.username() == null ||
            data.password() == null ||
            data.email() == null)
        { throw new BadRequestException("bad request"); }

        userDAO.createUser(data);
        String authToken = authDAO.createAuth(data.username());

        return new SessionStartResult(data.username(), authToken);
    }
}
