package com.kalina.kochapp.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class RecipeList {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Recipe> ITEMS = new ArrayList<Recipe>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Recipe> ITEM_MAP = new HashMap<String, Recipe>();

    private static final int COUNT = 25;

    public static void createRecipes(){
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createRecipeItem(i));
        }
    }

    private static void addItem(Recipe item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static Recipe createRecipeItem(int position) {
        return new Recipe(String.valueOf(position), "Item " + position, makeIngredients(position), makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    private static HashMap<String, Double> makeIngredients(int position) {
        HashMap<String, Double> newIngredients = new HashMap<>();
        newIngredients.put("First Ingredient", 1.5);
        newIngredients.put("Second Ingredient", 5.5);
        newIngredients.put("Third Ingredient", 3.0);
        return newIngredients;
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Recipe {
        public final String id;
        public final String content;
        public final HashMap<String, Double> ingredients;
        public final String instructions;

        public Recipe(String id, String content, HashMap<String, Double> ingredients, String instructions) {
            this.id = id;
            this.content = content;
            this.ingredients = ingredients;
            this.instructions = instructions;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
