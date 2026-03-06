package database;

import dataaccess.DatabaseUserDAO;
import exceptions.DataAccessException;
import models.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;


public class UserDAOTests {
    DatabaseUserDAO db = new DatabaseUserDAO();

    public UserDAOTests() throws Exception {
    }

    @BeforeEach
    public void setup() throws Exception {
        db.clear();
        db = new DatabaseUserDAO();
    }

    @Test
    public void createValidUserTest() throws Exception {
        String username = "TestUser";
        String password = "secretPassword";
        String email = "EmailTest@email";

        UserData userData = new UserData(username, password, email);
        db.createUser(userData);

        UserData result = db.getUser(username);
        Assertions.assertEquals(username, result.username());
        Assertions.assertTrue(BCrypt.checkpw(password, result.password()));
        Assertions.assertEquals(email, result.email());
    }

    @Test
    public void createInvalidNullUserTest() throws Exception {
        String password = "secretPassword";
        String email = "EmailTest@email";

        UserData userData = new UserData(null, password, email);
        Assertions.assertThrows(DataAccessException.class, () ->
                db.createUser(userData)
        );
    }

    @Test
    public void getExistingUserTest() throws Exception {
        String username = "TestUser";
        String password = "secretPassword";
        String email = "EmailTest@email";

        UserData userData = new UserData(username, password, email);
        db.createUser(userData);

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
        String username = "TestUser";
        String password = "secretPassword";
        String email = "EmailTest@email";

        UserData userData = new UserData(username, password, email);
        db.createUser(userData);
        Assertions.assertTrue(db.verifyPassword(username, password));
    }

    @Test
    public void verifyIncorrectPasswordTest() throws Exception {
        String username = "TestUser";
        String password = "secretPassword";
        String email = "EmailTest@email";

        UserData userData = new UserData(username, password, email);
        db.createUser(userData);
        Assertions.assertFalse(db.verifyPassword(username, "wrongPassword"));
    }


}
