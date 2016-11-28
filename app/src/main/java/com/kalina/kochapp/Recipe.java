package com.kalina.kochapp;

import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    public String image_path;
    public byte[] image = new byte[0];

    public Recipe(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Recipe(String content, HashMap<String, Double> ingredients, String instructions, String image_path) {
        this.id = id;
        this.content = content;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.image_path = image_path;
    }

    @Override
    public String toString() {
        return content;
    }

    public static Recipe writeNewRecipe(String content, HashMap<String, Double> ingredients, String instructions, String image_path) {
        Recipe recipe = new Recipe(content, ingredients, instructions, image_path);

        mDatabase.child("recipes").push().setValue(recipe);
        recipe.id = mDatabase.child("recipes").push().getKey();

        return recipe;
    }

    public void loadImage(final RecipeListActivity startingActivity){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(image_path);

        final long ONE_MEGABYTE = 2048 * 2048;
        //final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                image = bytes;
                startingActivity.listAdapter.notifyDataSetChanged();
                startingActivity.progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
