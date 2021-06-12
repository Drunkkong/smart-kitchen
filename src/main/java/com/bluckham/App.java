package com.bluckham;

import com.bluckham.dao.KitchenDAO;
import com.bluckham.dao.WebScraper;
import com.bluckham.model.Blog;

import java.util.logging.Logger;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        KitchenDAO dao = new KitchenDAO();
        WebScraper scraper = new WebScraper();

        Blog blog = dao.getRandomRecipe();
        logger.info("Blog: " + blog.getName() + " Recipe: " + scraper.getRandomRecipe(blog));
    }
}
