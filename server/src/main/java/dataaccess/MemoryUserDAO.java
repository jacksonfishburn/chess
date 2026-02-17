package dataaccess;

import models.UserData;

import java.util.*;

public class MemoryUserDAO implements UserDAO{
    Map<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData data) {
        users.put(data.username(), data);
    }
}