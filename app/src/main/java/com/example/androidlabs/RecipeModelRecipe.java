package com.example.androidlabs;


public class RecipeModelRecipe {


    private final String msg;
    private final long id;//for database number

    public RecipeModelRecipe(String msg, long id) {
        this.msg = msg;
        this.id=id;
    }

    public String getMsg() {
        return msg;
    }

    public long getId() {
        return id;
    }

}
