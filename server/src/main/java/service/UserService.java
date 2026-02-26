package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import models.LoginRequest;
import models.SessionStartResult;
import models.UserData;

import java.util.Objects;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
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

    public SessionStartResult login(LoginRequest data) throws Exception {
        if (data.username() == null ||
                data.password() == null)
        { throw new BadRequestException(""); }

        UserData user = userDAO.getUser(data.username());

        if (user == null){
            throw new UnauthorizedException("");
        }
        if (!Objects.equals(user.password(), data.password())) {
            throw new UnauthorizedException("");
        }

        String authToken = authDAO.createAuth(data.username());
        return new SessionStartResult(data.username(), authToken);
    }


}
