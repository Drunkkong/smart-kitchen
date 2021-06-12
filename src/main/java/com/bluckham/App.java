package com.bluckham;

import com.bluckham.dao.KitchenDAO;
import com.bluckham.dao.WebScraper;

import java.util.logging.Logger;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        var dao = new KitchenDAO();
        var scraper = new WebScraper();

        var blog = dao.getRandomRecipe();
        logger.info("Blog: " + blog.getName() + " Recipe: " + scraper.getRandomRecipe(blog));
    }
}
