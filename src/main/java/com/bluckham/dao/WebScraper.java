package com.bluckham.dao;

import com.bluckham.model.Blog;
import com.bluckham.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

public class WebScraper {
    private static final Logger logger = Logger.getLogger(WebScraper.class.getName());

    // TODO
    public String getRandomRecipe(@NotNull Blog blog) {
        Document document = null;
        Set<String> recipeSet = new HashSet<>();
        Random rand = new Random(System.currentTimeMillis());
        switch (blog.getName().toLowerCase(Locale.ROOT)) {
            case "hot for food":
                try {
                    document =
                            Jsoup.connect(blog.getUrl()).data("s", "seitan").userAgent(Constants.CHROME).timeout(Constants.DEFAULT_TIMEOUT).get();
                } catch (IOException e) {
                    logger.severe(e.getMessage());
                    System.exit(1);
                }
                for (Element element : document.select("a[href*=/recipes/]"))
                    recipeSet.add(element.attr(Constants.ABS_HREF));

                return (String) recipeSet.toArray()[rand.nextInt(recipeSet.size())];
            case "avant garde vegan":
                try {
                    document =
                            Jsoup.connect("https://www.avantgardevegan.com/recipes").data("_sf_s", "sandwich").userAgent(
                                    Constants.CHROME).timeout(Constants.DEFAULT_TIMEOUT).get();
                } catch (IOException e) {
                    logger.severe(e.getMessage());
                    System.exit(1);
                }

                for (Element element : document.select("a[href*=/recipes/]"))
                    recipeSet.add((element.attr(Constants.ABS_HREF)));

                return (String) recipeSet.toArray()[rand.nextInt(recipeSet.size())];
            case "nora cooks":
            case "minimalist baker":
                try {
                    document =
                            Jsoup.connect(blog.getUrl()).data("s", "seitan").userAgent(Constants.CHROME).timeout(Constants.DEFAULT_TIMEOUT).get();
                } catch (IOException e) {
                    logger.severe(e.getMessage());
                    System.exit(1);
                }
                for (Element element : document.select("a[href]"))
                    recipeSet.add(element.attr(Constants.ABS_HREF));

                return (String) recipeSet.toArray()[rand.nextInt(recipeSet.size())];
            default:
                return null;
        }
    }
}
