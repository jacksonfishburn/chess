package dataaccess;

import models.models.UserData;

public interface UserDAO {
    void createUser(UserData data) throws Exception;
    UserData getUser(String username) throws Exception;
    boolean verifyPassword(String username, String password) throws Exception;
    void clear() throws Exception;
}
