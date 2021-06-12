package com.bluckham.dao;

import com.bluckham.model.Blog;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class KitchenDAO {
    private final Logger logger = Logger.getLogger(KitchenDAO.class.getName());
    private final Connection connection;

    public KitchenDAO() {
        connection = connect();
    }

    private Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:postgresql://localhost:2342/inventory";
            String user = "postgres";
            String password = "Drunkkong1";
            conn = DriverManager.getConnection(url, user, password);
            logger.info("Connection setup");
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            System.exit(500);
        }
        return conn;
    }

    public Blog getRandomRecipe() {
        int blogCount = 0;
        List<Blog> blogList = new ArrayList<>();
        Random rand = null;
        try {
            rand = SecureRandom.getInstance("NativePRNG");
        } catch (NoSuchAlgorithmException e) {
            logger.severe(e.getMessage());
            System.exit(1);
        }
        try(PreparedStatement ps = connection.prepareStatement("SELECT * FROM blogs")) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Blog blog = new Blog();
                blog.setName(rs.getString("name"));
                blog.setUrl(rs.getString("url"));
                blogList.add(blog);
                blogCount++;
            }

        } catch (SQLException throwables) {
            logger.severe(throwables.getMessage());
            System.exit(500);
        }
        return blogList.get(rand.nextInt(blogCount));
    }

    // TODO
    public String getSpecificBlogRecipe() {
        return null;
    }
}
