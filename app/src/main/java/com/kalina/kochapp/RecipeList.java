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
    public static List<Recipe> ITEMS = new ArrayList<Recipe>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Recipe> ITEM_MAP = new HashMap<String, Recipe>();

    public static void fetchRecipes(final ProgressBar pg, final RecipeListAdapter la){
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
                    addItem(post);
                }
                //tartingActivity.listAdapter.notifyDataSetChanged();
                //startingActivity.progressBar.setVisibility(View.GONE); // do it in the recipe class after loading images is over?
            }
            @Override
            public void onCancelled(DatabaseError de) {
                //Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }

    public static void createRecipes(){
        HashMap<String, Double> newIngredients = new HashMap<>();
        newIngredients.put("Zuchinni", 3d);
        newIngredients.put("Knoblauchzehen", 3d);
        newIngredients.put("EL Creme Fraiche", 2d);
        newIngredients.put("EL Italienische Kräuter", 2d);
        newIngredients.put("EL Parmesan", 2d);
        Recipe recipe = Recipe.writeNewRecipe("Zucchini-Nudeln", newIngredients,
                "Die Enden der gewaschenen Zucchini abschneiden. Die Zucchini dann längs mit einem Juliennehobel in dünne, lange Streifen schneiden und beiseite stellen.\n" +
                        "\n" +
                        "Das Olivenöl in einer beschichteten Pfanne erhitzen, zwischenzeitlich die Knoblauchzehen abziehen und fein würfeln. Die Knoblauchwürfel im Olivenöl anbraten, jedoch nicht braun werden lassen (der Knoblauch wird sonst bitter!). Anschließend die Hitze reduzieren und die Zucchinistreifen hinzugeben.\n" +
                        "\n" +
                        "Unter gelegentlichem Wenden die \"Zucchininudeln\" ca. 10 Minuten garen, sodass die Zucchinispaghetti noch bissfest sind. Mit Salz und Pfeffer würzen.\n" +
                        "\n" +
                        "2 EL Crème fraîche, 2 EL italienische Kräuter und 2 EL geriebenen Parmesan unterrühren, kurz miterhitzen und anschließend servieren.", "gs://kochapp-a8b3f.appspot.com/receipt_images/Rezept3.jpg", false);


        newIngredients = new HashMap<>();
        newIngredients.put("Äpfel", 3d);
        newIngredients.put("g Zucker", 50d);
        newIngredients.put("ml Bier", 250d);
        newIngredients.put("Eier", 2d);
        newIngredients.put("g Mehl", 200d);
        recipe = Recipe.writeNewRecipe("Apfelküchle", newIngredients,
                "Eier, Zucker und Hefeweizen miteinander vermischen und soviel Mehl hinzugeben bis der Teig zähflüssig wird.\n" +
                        "3 Äpfel waschen, schälen und in Ringe schneiden. Apfelringe im Teig wenden und in einer Friteuse oder einer Pfanne mit reichlich Öl braten bis sie goldbraun sind. Abtropfen lassen und mit Zucker und Zimt bestreut servieren. Dazu passt Vanilleeis.\n" +
                        "Tipp: Die Apfelküchle lassen sich hervorragend vorbereiten und später in der Mikrowelle aufwärmen..", "gs://kochapp-a8b3f.appspot.com/receipt_images/Rezept1.png", false);


        newIngredients = new HashMap<>();
        newIngredients.put("Tomaten", 5d);
        newIngredients.put("Knoblauchzehen", 2d);
        newIngredients.put("EL Olivenöl", 5d);
        newIngredients.put("Ciabatta", 2d);
        newIngredients.put("TL Gewürzmischung", 1d);
        recipe = Recipe.writeNewRecipe("Bruschetta", newIngredients,
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

    private static void addItem(Recipe item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
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
}
