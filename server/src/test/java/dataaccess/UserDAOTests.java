package dataaccess;

import exceptions.DataAccessException;
import models.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;


public class UserDAOTests {
    private DatabaseUserDAO db;

    String username = "TestUser";
    String password = "secretPassword";
    String email = "EmailTest@email";

    @BeforeEach
    public void setup() throws Exception {
        db = new DatabaseUserDAO();
        db.clear();

        UserData userData = new UserData(username, password, email);
        db.createUser(userData);
    }

    @Test
    public void createValidUserTest() throws Exception {
        UserData result = db.getUser(username);
        Assertions.assertNotNull(result);
    }

    @Test
    public void createInvalidNullUserTest() throws Exception {
        UserData userData = new UserData(null, password, email);
        Assertions.assertThrows(DataAccessException.class, () ->
                db.createUser(userData)
        );
    }

    @Test
    public void getExistingUserTest() throws Exception {
        UserData result = db.getUser(username);
        Assertions.assertEquals(username, result.username());
        Assertions.assertTrue(BCrypt.checkpw(password, result.password()));
        Assertions.assertEquals(email, result.email());
    }

    @Test
    public void getNonexistentUserTest() throws Exception {
        UserData result = db.getUser("username");
        Assertions.assertNull(result);
    }

    @Test
    public void verifyCorrectPasswordTest() throws Exception {
        Assertions.assertTrue(db.verifyPassword(username, password));
    }

    @Test
    public void verifyIncorrectPasswordTest() throws Exception {
        Assertions.assertFalse(db.verifyPassword(username, "wrongPassword"));
    }

    @Test
    public void clearTest() throws Exception {
        UserData userData = new UserData("username", "password", "email");
        db.createUser(userData);
        UserData userData1 = new UserData("username1", "password1", "email1");
        db.createUser(userData1);
        UserData userData2 = new UserData("username2", "password2", "email2");
        db.createUser(userData2);

        db.clear();

        Assertions.assertNull(db.getUser("username"));
        Assertions.assertNull(db.getUser("username1"));
        Assertions.assertNull(db.getUser("username2"));
    }
}
