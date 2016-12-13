package com.kalina.kochapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Kalina on 13.12.2016.
 */

public class ApplicationManager {

    public static User currentUser = new User();
    public static boolean currentUserLoaded;

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
}
