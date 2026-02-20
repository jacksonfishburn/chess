package dataaccess;

import models.AuthData;

public interface AuthDAO {
    String createAuth(String username);
    void deleteAuth(String auth);
    AuthData getAuth(String auth);
    void clear();
}
