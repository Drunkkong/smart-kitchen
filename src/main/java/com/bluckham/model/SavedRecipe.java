package com.bluckham.model;

import lombok.Data;

public @Data
class SavedRecipe {
    private String recipeName;
    private String url;
    private String blog;
    private Boolean favorite;
    private Boolean dislike;
    private String category;
    private String cookTime;
}
