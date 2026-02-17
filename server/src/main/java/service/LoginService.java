package service;

import dataaccess.AuthDAO;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import dataaccess.UserDAO;
import models.*;

import java.util.Objects;

public class LoginService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public SessionStartResult login(LoginRequest data) throws Exception {
        if (data.username() == null ||
                data.password() == null)
        { throw new BadRequestException("bad request"); }

        UserData user = userDAO.getUser(data.username());

        if (user == null){
            throw new BadRequestException("bad request");
        }
        if (!Objects.equals(user.password(), data.password())) {
            throw new UnauthorizedException("unauthorized");
        }

        String authToken = authDAO.createAuth(data.username());
        return new SessionStartResult(data.username(), authToken);
    }
}
