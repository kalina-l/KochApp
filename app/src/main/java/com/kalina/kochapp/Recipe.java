package com.kalina.kochapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

/**
 * Created by Kalina on 20.11.2016.
 */

@IgnoreExtraProperties
public class Recipe {

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public String id;
    public String content;
    public HashMap<String, Double> ingredients;
    public String instructions;

    public Recipe(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Recipe(String content, HashMap<String, Double> ingredients, String instructions) {
        this.id = id;
        this.content = content;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return content;
    }

    public static Recipe writeNewRecipe(String content, HashMap<String, Double> ingredients, String instructions) {
        Recipe recipe = new Recipe(content, ingredients, instructions);

        mDatabase.child("recipes").push().setValue(recipe);
        recipe.id = mDatabase.child("recipes").push().getKey();

        return recipe;
    }
}
