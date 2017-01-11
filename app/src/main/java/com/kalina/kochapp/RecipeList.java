package com.kalina.kochapp;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class RecipeList {

    public static List<Recipe> ALL_RECIPES = new ArrayList<Recipe>();
    public static final Map<String, Recipe> ALL_RECIPES_MAP = new HashMap<String, Recipe>();

    public static void fetchGlobalRecipes(final ProgressBar pg, final RecipeListAdapter la){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("recipes");
        pg.setVisibility(View.VISIBLE);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.e("TAG:" ," "+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Recipe post = postSnapshot.getValue(Recipe.class);
                    post.id = postSnapshot.getKey();
                    post.loadImage(pg, la);
                    Log.e("TAG:" ," "+post.content);
                    addGlobalRecipe(post);
                }
            }
            @Override
            public void onCancelled(DatabaseError de) {
                //Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }

    public static void getUserRecipes(final ProgressBar pg, final RecipeListAdapter la){

        pg.setVisibility(View.VISIBLE);

        do {
            for (Recipe recipe : ApplicationManager.currentUser.recipes) {
                recipe.loadImage(pg, la);
                ApplicationManager.CURRENT_USER_RECIPES.add(recipe);
                ApplicationManager.CURRENT_USER_RECIPE_MAP.put(recipe.id, recipe);
            }
        } while(!ApplicationManager.currentUserLoaded);

        pg.setVisibility(View.INVISIBLE);
    }

    public static void createRecipes(){
        List<Ingredient> newIngredients = new ArrayList<>();
        newIngredients.add(new Ingredient(3d, "", "Zuchinni"));
        newIngredients.add(new Ingredient(3d, "", "Knoblauchzehen"));
        newIngredients.add(new Ingredient(2d, "EL", "EL Creme Fraiche"));
        newIngredients.add(new Ingredient(2d, "EL", "Italienische Kräuter"));
        newIngredients.add(new Ingredient(2d, "EL", "Parmesan"));
        Recipe recipe = Recipe.writeNewRecipe("Zucchini-Nudeln", newIngredients,
                "Die Enden der gewaschenen Zucchini abschneiden. Die Zucchini dann längs mit einem Juliennehobel in dünne, lange Streifen schneiden und beiseite stellen.\n" +
                        "\n" +
                        "Das Olivenöl in einer beschichteten Pfanne erhitzen, zwischenzeitlich die Knoblauchzehen abziehen und fein würfeln. Die Knoblauchwürfel im Olivenöl anbraten, jedoch nicht braun werden lassen (der Knoblauch wird sonst bitter!). Anschließend die Hitze reduzieren und die Zucchinistreifen hinzugeben.\n" +
                        "\n" +
                        "Unter gelegentlichem Wenden die \"Zucchininudeln\" ca. 10 Minuten garen, sodass die Zucchinispaghetti noch bissfest sind. Mit Salz und Pfeffer würzen.\n" +
                        "\n" +
                        "2 EL Crème fraîche, 2 EL italienische Kräuter und 2 EL geriebenen Parmesan unterrühren, kurz miterhitzen und anschließend servieren.", "gs://kochapp-a8b3f.appspot.com/receipt_images/Rezept3.jpg", false);


        newIngredients = new ArrayList<>();
        newIngredients.add(new Ingredient(3d, "", "Äpfel"));
        newIngredients.add(new Ingredient(50d, "g", "Zucker"));
        newIngredients.add(new Ingredient(250d, "ml", "Bier"));
        newIngredients.add(new Ingredient(2d, "", "Eier"));
        newIngredients.add(new Ingredient(200d, "g", "Mehl"));
        recipe = Recipe.writeNewRecipe("Apfelküchle",  newIngredients,
                "Eier, Zucker und Hefeweizen miteinander vermischen und soviel Mehl hinzugeben bis der Teig zähflüssig wird.\n" +
                        "3 Äpfel waschen, schälen und in Ringe schneiden. Apfelringe im Teig wenden und in einer Friteuse oder einer Pfanne mit reichlich Öl braten bis sie goldbraun sind. Abtropfen lassen und mit Zucker und Zimt bestreut servieren. Dazu passt Vanilleeis.\n" +
                        "Tipp: Die Apfelküchle lassen sich hervorragend vorbereiten und später in der Mikrowelle aufwärmen..", "gs://kochapp-a8b3f.appspot.com/receipt_images/Rezept1.png", false);


        newIngredients = new ArrayList<>();
        newIngredients.add(new Ingredient(5d, "", "Tomaten"));
        newIngredients.add(new Ingredient(2d, "", "Knoblauchzehen"));
        newIngredients.add(new Ingredient(5d, "EL", "Olivenöl"));
        newIngredients.add(new Ingredient(2d, "", "Ciabatta"));
        newIngredients.add(new Ingredient(1d, "TL", "Gewürzmischung"));
        recipe = Recipe.writeNewRecipe("Bruschetta",  newIngredients,
                "Zuerst die Tomaten waschen, vom Grün befreien, halbieren und dann in kleine Würfel schneiden. Dann den Knoblauch sehr klein schneiden, zu den Tomatenstücken geben und mit gut 3 EL Öl sowie 1 - 2 TL Tomatengewürzsalz mischen. Unbedingt mindestens 2 Stunden im Kühlschrank ziehen lassen.\n" +
                        "\n" +
                        "Den Backofen auf 180 - 200 °C (Umluft) vorheizen, die Tomatenstücke aus dem Kühlschrank nehmen. \n" +
                        "\n" +
                        "Dann das Ciabatta in ca. 1 cm dicke Scheiben schneiden und diese mit dem restlichen Öl beträufeln.\n" +
                        "\n" +
                        "Backpapier auf ein Backofengitter legen (wichtig) und die darauf ausgebreiteten Ciabattascheiben in der Mitte des Ofens goldfarben backen - nicht zu dunkel, sonst werden sie zu hart, das dauert 5 - 8 Minuten.\n" +
                        "\n" +
                        "Die Ciabattascheiben aus dem Ofen holen und mit den Tomaten-Knoblauch Gemisch belegen, 1/2 - 1 EL pro Scheibe.\n" +
                        "\n" +
                        "Gelingt immer, toll für die Grillsaison, lässt sich gut vorbereiten, wenn Gäste kommen, und ist rein vegetarisch.", "gs://kochapp-a8b3f.appspot.com/receipt_images/Rezept2.png", false);
    }

    private static void addGlobalRecipe(Recipe item) {
        ALL_RECIPES.add(item);
        ALL_RECIPES_MAP.put(item.id, item);
    }
}
