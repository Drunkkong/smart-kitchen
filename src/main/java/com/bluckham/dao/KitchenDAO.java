package com.bluckham.dao;

import com.bluckham.model.Blog;
import com.bluckham.model.SavedRecipe;

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
            var url = "jdbc:postgresql://localhost:2342/inventory";
            var user = "postgres";
            var password = "Drunkkong1";
            conn = DriverManager.getConnection(url, user, password);
            logger.info("Connection setup");
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            System.exit(500);
        }
        return conn;
    }

    public Blog getRandomRecipe() {
        var blogCount = 0;
        List<Blog> blogList = new ArrayList<>();
        Random rand = null;
        try {
            rand = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            logger.severe(e.getMessage());
            System.exit(1);
        }
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM blogs")) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                var blog = new Blog();
                blog.setName(rs.getString("name"));
                blog.setUrl(rs.getString("url"));
                blogList.add(blog);
                blogCount++;
            }

        } catch (SQLException sqlException) {
            logger.severe(sqlException.getMessage());
            System.exit(500);
        }
        return blogList.get(rand.nextInt(blogCount));
    }

    // TODO
    public String getSpecificBlogRecipe() {
        return null;
    }

    public List<SavedRecipe> retrieveFavoriteRecipes() {
        List<SavedRecipe> savedRecipeList = new ArrayList<>();
        try (PreparedStatement ps =
                     connection.prepareStatement("SELECT * FROM saved_recipes WHERE favorite = TRUE AND dislike = " +
                             "FALSE")) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                var savedRecipe = new SavedRecipe();
                savedRecipe.setRecipeName(rs.getString("recipe_name"));
                savedRecipe.setUrl(rs.getString("url"));
                savedRecipe.setBlog(rs.getString("blog"));
                savedRecipe.setFavorite(rs.getBoolean("favorite"));
                savedRecipe.setDislike(rs.getBoolean("dislike"));
                savedRecipe.setCategory(rs.getString("category"));
                savedRecipe.setCookTime(rs.getString("cook_time"));
                savedRecipeList.add(savedRecipe);
            }
        } catch (SQLException ex) {
            logger.severe(ex.getMessage());
            System.exit(500);
        }
        return savedRecipeList;
    }
}
