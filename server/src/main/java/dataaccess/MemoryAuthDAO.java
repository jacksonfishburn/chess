package dataaccess;

import models.AuthData;

import java.util.*;

public class MemoryAuthDAO implements AuthDAO {
    Map<String, AuthData> data = new HashMap<>();

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void createAuth(String username) {
        AuthData auth = new AuthData(username, generateToken());
        data.put(auth.authToken(), auth);
    }
}
