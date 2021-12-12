package com.bluckham.dao;

import com.bluckham.model.Blog;
import com.bluckham.model.SavedRecipe;
import org.jetbrains.annotations.NotNull;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
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
            logger.log(Level.INFO, "Connection setup");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
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
            logger.log(Level.SEVERE, e.getMessage());
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
            logger.log(Level.SEVERE, sqlException.getMessage());
            System.exit(500);
        }
        return blogList.get(rand.nextInt(blogCount));
    }

    public SavedRecipe getSpecificBlogRecipe(String blogName, String recipeName) {
        var savedRecipe = new SavedRecipe();
        try (PreparedStatement ps = connection.prepareStatement("SELECT TOP 1 FROM saved_recipes WHERE blog = ? " +
                "AND recipe_name = ?")) {
            ps.setString(1, blogName);
            ps.setString(2, recipeName);
            ResultSet rs = ps.executeQuery();

            if (rs.first()) {
                savedRecipe.setRecipeName(rs.getString("recipe_name"));
                savedRecipe.setUrl(rs.getString("url"));
                savedRecipe.setBlog(rs.getString("blog"));
                savedRecipe.setFavorite(rs.getBoolean("favorite"));
                savedRecipe.setDislike(rs.getBoolean("dislike"));
                savedRecipe.setCategory(rs.getString("category"));
                savedRecipe.setCookTime(rs.getString("cook_time"));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            System.exit(500);
        }
        return savedRecipe;
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
            logger.log(Level.SEVERE, ex.getMessage());
            System.exit(500);
        }
        return savedRecipeList;
    }

    public void setFavoriteRecipe(@NotNull SavedRecipe savedRecipe) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT TOP 1 FROM saved_recipes WHERE url = ?")) {
            ps.setString(1, savedRecipe.getUrl());
            ResultSet rs = ps.executeQuery();
            if (rs.first())
                updateFavorite(savedRecipe);
            else
                insertNewFavorite(savedRecipe);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            System.exit(500);
        }
    }

    private void insertNewFavorite(@NotNull SavedRecipe savedRecipe) throws SQLException {
        try (PreparedStatement insert = connection.prepareStatement("INSERT INTO saved_recipes (recipe_name, " +
                "url, blog, favorite, dislike, category, cook_time) VALUES (?, ?, " +
                "?, ?, ?, ?, ?")) {
            insert.setString(1, savedRecipe.getRecipeName());
            insert.setString(2, savedRecipe.getUrl());
            insert.setString(3, savedRecipe.getBlog());
            insert.setBoolean(4, true);
            insert.setBoolean(5, false);
            insert.setString(6, savedRecipe.getCategory());
            insert.setString(7, savedRecipe.getCookTime());
            insert.executeQuery();
        } catch (SQLException ex) {
            throw ex;
        }
    }

    private void updateFavorite(@NotNull SavedRecipe savedRecipe) throws SQLException {
        try (PreparedStatement update =
                     connection.prepareStatement("UPDATE saved_recipes SET favorite = ? WHERE url = ?")) {
            update.setBoolean(1, true);
            update.setString(2, savedRecipe.getUrl());
            update.executeQuery();
        } catch (SQLException ex) {
            throw ex;
        }
    }
}
