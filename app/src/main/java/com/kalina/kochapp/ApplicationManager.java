package com.kalina.kochapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
 * Created by Kalina on 13.12.2016.
 */

public class ApplicationManager {

    public static User currentUser = new User();
    public static boolean currentUserLoaded;

    public static List<Recipe> CURRENT_USER_RECIPES = new ArrayList<Recipe>();
    public static final Map<String, Recipe> CURRENT_USER_RECIPE_MAP = new HashMap<String, Recipe>();

    public static void getCurrentUser() {

        currentUserLoaded = false;
        final FirebaseUser dbUserReference = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getKey().equals(dbUserReference.getUid())) {
                        currentUser = child.getValue(User.class);
                        currentUser.uid = child.getKey();
                        currentUser.loadRecipes();
                        currentUserLoaded = true;
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void addRecipe(Recipe recipe){
        currentUser.addRecipe(recipe);
        CURRENT_USER_RECIPES.add(recipe);
        CURRENT_USER_RECIPE_MAP.put(recipe.id, recipe);
    }

    public static void removeRecipe(Recipe recipe){
        currentUser.removeRecipe(recipe);
        CURRENT_USER_RECIPES.remove(recipe);
        CURRENT_USER_RECIPE_MAP.remove(recipe.id);
    }
}
