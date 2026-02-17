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
        AuthData auth = new AuthData(username, token);
        data.put(auth.authToken(), auth);
        return token;
    }
}
