package dataaccess;

import models.UserData;

public interface UserDAO {
    void createUser(UserData data) throws Exception;
    UserData getUser(String username);
}
