package com.example.androidlabs;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterRecipe extends BaseAdapter {

    private final ArrayList<RecipeModelRecipe> recipeModelRecipes;
    private final Context context;


    public CustomAdapterRecipe(Context context, ArrayList<RecipeModelRecipe> recipeList) {
        this.recipeModelRecipes = recipeList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return recipeModelRecipes.size();
    }

    @Override
    public Object getItem(int i) {
        return recipeModelRecipes.get(i);
    }


//    This function is used to return the database ID of the element at row position.
//    We will learn databases next week so for now, just return 0, or the int position converted to long:

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override

//    The getView(int position, View old, ViewGroup parent) – this specifies how each row looks.

    public View getView(int i, View view, ViewGroup viewGroup) {

//        You need a LayoutInflater object to load an XML layout file:
//        LayoutInflater inflater = getLayoutInflater();
//the context is ListViewActivity

        LayoutInflater inflater = LayoutInflater.from(context);
        View newView;


//            newView is now the root object from your XML file. It contains the widgets that are in your layout.
        //Here, we are setting the layout to Left

        newView = inflater.inflate(R.layout.blankrecipe,null);
        TextView msgView = newView.findViewById(R.id.blank);
        msgView.setText(recipeModelRecipes.get(i).getMsg());


        return newView;
    }

}


