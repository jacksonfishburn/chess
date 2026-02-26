package dataaccess;

import models.AuthData;

import java.util.*;

public class MemoryAuthDAO implements AuthDAO {
    Map<String, AuthData> data = new HashMap<>();

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String createAuth(String username) {
        String token = generateToken();
        AuthData authData = new AuthData(username, token);

        data.put(authData.authToken(), authData);
        return token;
    }

    @Override
    public void deleteAuth(String auth) {
        data.remove(auth);
    }

    @Override
    public AuthData getAuth(String auth) {
        return data.get(auth);
    }

    @Override
    public void clear() {
        data = new HashMap<>();
    }
}
