package com.kalina.kochapp;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kalina on 20.11.2016.
 */

public class User {

    public FirebaseUser dbUserReference;

    public String name;
    public List<String> recipeIds;
    @Exclude
    public List<Recipe> recipes;


    public User(){
        name = "";
        recipeIds = new ArrayList<String>();
        recipes = new ArrayList<Recipe>();
    }

    public User(String name){
        this.name = name;
        recipeIds = new ArrayList<String>();
        recipes = new ArrayList<Recipe>();
    }

    public void loadRecipes(){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("recipes");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.e("TAG:" ," "+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    for(String recipeID : recipeIds){
                        if(postSnapshot.getKey().equals(recipeID)){
                            Recipe post = postSnapshot.getValue(Recipe.class);
                            recipes.add(post);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError de) {
                //Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });

    }

    public static void updateProfile(String name){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User profile updated.");
                        }
                    }
                });

    }

}
