package dataaccess;

import exceptions.AlreadyTakenException;
import models.UserData;

import java.util.*;

public class MemoryUserDAO implements UserDAO{
    Map<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData data) throws Exception {
        if (getUser(data.username()) != null) {
            throw new AlreadyTakenException("Username already taken");
        }
        users.put(data.username(), data);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }
}