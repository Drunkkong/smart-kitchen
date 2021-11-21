package com.bluckham;

import com.bluckham.dao.KitchenDAO;
import com.bluckham.dao.WebScraper;

import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        var dao = new KitchenDAO();
        var scraper = new WebScraper();

        var blog = dao.getRandomRecipe();
        logger.log(Level.INFO, "Blog: {0}\nRecipe: {1}", new Object[]{blog.getName(), scraper.getRandomRecipe(blog)});
        var savedRecipeList = dao.retrieveFavoriteRecipes();
        logger.log(Level.INFO, "savedRecipeList {0}", savedRecipeList);
    }
}