package com.aston.crud.dao;

import com.aston.crud.entities.User;
import com.aston.crud.util.DBConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDAOImplTest {
    private UserDAOImpl userDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        createTables();

        userDAO = new UserDAOImpl();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dropTables();
    }

    @Test
    public void testGetUserById() throws SQLException {
        User user1 = new User(1, "testuser1", "test1@example.com");
        User user2 = new User(2, "testuser2", "test2@example.com");
        userDAO.addUser(user1);
        userDAO.addUser(user2);

        User expectedUser = user1;
        User actualUser = userDAO.getUserById(1);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testGetAllUsers() throws SQLException {
        User user1 = new User(1, "testuser1", "test1@example.com");
        User user2 = new User(2, "testuser2", "test2@example.com");
        userDAO.addUser(user1);
        userDAO.addUser(user2);

        List<User> expectedUsers = Arrays.asList(user1, user2);
        List<User> actualUsers = userDAO.getAllUsers();

        assertEquals(expectedUsers, actualUsers);

    }

    @Test
    public void testUpdateUserById() throws SQLException {
        User user = new User(1, "testuser1", "test1@example.com");
        userDAO.addUser(user);

        user.setUsername("updatedTestUser");
        user.setEmail("updated@test.com");
        userDAO.updateUser(user);

        User updatedUser = userDAO.getUserById(1);

        assertEquals("updatedTestUser", updatedUser.getUsername());
        assertEquals("updated@test.com", updatedUser.getEmail());

    }

    @Test
    public void testDeleteUserById() throws SQLException {
        User user1 = new User(1, "testuser1", "test1@example.com");
        User user2 = new User(2, "testuser2", "test2@example.com");
        User user3 = new User(3, "testuser3", "test3@example.com");

        userDAO.addUser(user1);
        userDAO.addUser(user2);
        userDAO.addUser(user3);

        List<User> expectedUsers = Arrays.asList(user1, user2);
        userDAO.deleteUserById(3);
        List<User> actualUsers = userDAO.getAllUsers();

        assertEquals(expectedUsers, actualUsers);
    }

    private void createTables() throws SQLException {
        Connection connection = DBConnection.getConnection();
        try (Statement statement = connection.createStatement()) {
            // Create users table
            statement.executeUpdate("CREATE TABLE users (id SERIAL PRIMARY KEY, username VARCHAR(50) NOT NULL, email VARCHAR(100) NOT NULL)");

            // Create categories table
            statement.executeUpdate("CREATE TABLE categories (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL)");

            // Create products table with foreign key constraint
            statement.executeUpdate("CREATE TABLE products (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, " +
                    "price DECIMAL(10, 2) NOT NULL, category_id INT NOT NULL, " +
                    "FOREIGN KEY (category_id) REFERENCES categories(id))");

            // Create addresses table with foreign key constraint
            statement.executeUpdate("CREATE TABLE addresses (id SERIAL PRIMARY KEY, " +
                    "street VARCHAR(255) NOT NULL, city VARCHAR(100) NOT NULL, " +
                    "state VARCHAR(100), postal_code VARCHAR(20) NOT NULL, " +
                    "user_id INT NOT NULL, FOREIGN KEY (user_id) REFERENCES users(id))");

            System.out.println("Tables created successfully.");
        }
    }

    private void dropTables() throws SQLException {
        Connection connection = DBConnection.getConnection();
        try (Statement statement = connection.createStatement()) {
            // Drop tables if they exist
            statement.executeUpdate("DROP TABLE IF EXISTS addresses");
            statement.executeUpdate("DROP TABLE IF EXISTS products");
            statement.executeUpdate("DROP TABLE IF EXISTS categories");
            statement.executeUpdate("DROP TABLE IF EXISTS users");

            System.out.println("Tables dropped successfully.");
        }
    }
}
