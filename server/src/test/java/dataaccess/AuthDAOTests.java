package dataaccess;

import exceptions.DataAccessException;
import models.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthDAOTests {
    DatabaseAuthDAO db;

    @BeforeEach
    public void setup() throws Exception {
        db = new DatabaseAuthDAO();
        db.clear();
    }

    @Test
    public void successfulCreateAuthTest() throws Exception {
        String username = "TestName";
        String auth = db.createAuth(username);

        AuthData result = db.getAuth(auth);

        Assertions.assertEquals(username, result.username());
    }

    @Test
    public void createAuthNullUsernameTest() {
        Assertions.assertThrows(DataAccessException.class, () ->
                db.createAuth(null)
        );
    }

    @Test
    public void deleteValidAuthTest() throws Exception {
        String username = "TestName";
        String auth = db.createAuth(username);

        db.deleteAuth(auth);
        AuthData result = db.getAuth(auth);

        Assertions.assertNull(result);
    }

    @Test
    public void deleteInvalidAuthTest() {
        Assertions.assertDoesNotThrow(() ->
                db.deleteAuth("nonexistentAuth")
        );
    }

    @Test
    public void getValidAuthTest() throws Exception {
        String username = "TestName";
        String auth = db.createAuth(username);

        AuthData result = db.getAuth(auth);

        Assertions.assertEquals(username, result.username());
    }

    @Test
    public void getInvalidAuthTest() throws Exception {
        AuthData result = db.getAuth("auth");

        Assertions.assertNull(result);
    }

    @Test
    public void clearTest() throws Exception {
        db.createAuth("username");
        db.createAuth("username1");
        db.createAuth("username2");

        db.clear();

        Assertions.assertNull(db.getAuth("username"));
        Assertions.assertNull(db.getAuth("username1"));
        Assertions.assertNull(db.getAuth("username2"));
    }
}
