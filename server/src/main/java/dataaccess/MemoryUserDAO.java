package dataaccess;

import models.UserData;

import java.util.*;

public class MemoryUserDAO implements UserDAO{
    Map<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData data) {
        users.put(data.username(), data);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public boolean verifyPassword(String username, String password) {
        String correctPassword = users.get(username).password();
        return Objects.equals(correctPassword, password);
    }

    @Override
    public void clear() {
        users = new HashMap<>();
    }

}