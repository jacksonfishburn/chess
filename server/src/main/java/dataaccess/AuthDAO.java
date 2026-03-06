package dataaccess;

import models.AuthData;

public interface AuthDAO {
    String createAuth(String username) throws Exception;
    void deleteAuth(String auth) throws Exception;
    AuthData getAuth(String auth) throws Exception;
    void clear() throws Exception;
}
