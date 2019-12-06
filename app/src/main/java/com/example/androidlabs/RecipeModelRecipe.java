package com.example.androidlabs;

/**
 * The class which stores the record for the database
 */

public class RecipeModelRecipe {


    /**
     * Stores the title
     */
    private final String msg;
    /**
     * Stores the database id
     */
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
