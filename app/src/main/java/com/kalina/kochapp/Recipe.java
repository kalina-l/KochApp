package com.kalina.kochapp;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kalina on 20.11.2016.
 */

@IgnoreExtraProperties
public class Recipe {

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public String id;
    public String content;
    public List<Ingredient> ingredients;
    public String instructions;
    public String image_path;
    @Exclude
    public byte[] image = new byte[0];
    public boolean isPrivite;

    public Recipe() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Recipe(String content, List<Ingredient> ingredients, String instructions, boolean isPrivite) {
        this.id = id;
        this.content = content;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.image_path = "";
        this.isPrivite = isPrivite;
        this.image = new byte[0];
    }

    @Override
    public String toString() {
        return content;
    }

    public void loadImage(final ProgressBar pb, final RecyclerView.Adapter adapter) {
        if (!image_path.isEmpty()) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(image_path);

            final long ONE_MEGABYTE = 2048 * 2048;
            //final long ONE_MEGABYTE = 1024 * 1024;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    image = bytes;
                    adapter.notifyDataSetChanged();
                    pb.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }

    public static Recipe writeNewRecipe(Context context, String content, List<Ingredient> ingredients, String instructions, Uri image_uri, boolean isPrivite) {
        Recipe recipe = new Recipe(content, ingredients, instructions, isPrivite);

        updateValues(recipe);

        if (image_uri != null) {
            try {
                //recipe.image = fullyReadFileToBytes(new File(image_uri.getPath()));
                InputStream iStream = context.getContentResolver().openInputStream(image_uri);
                recipe.image = getBytes(iStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadImageToStorage(context, image_uri, recipe);
        } else {
            CreateRecipe.unblockActivity();
        }

        return recipe;
    }

    public static void uploadImageToStorage(final Context context, Uri file, final Recipe recipe) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imagesRef = storage.getReferenceFromUrl("gs://kochapp-a8b3f.appspot.com/receipt_images/" + file.getPath().substring(file.getPath().lastIndexOf("/")+1));

        imagesRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        recipe.image_path = taskSnapshot.getStorage().toString();
                        updateValues(recipe);
                        CreateRecipe.unblockActivity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        CreateRecipe.unblockActivity();
                        Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //calculating progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        //displaying percentage in progress dialog
                        CreateRecipe.pg.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });
    }

    private static void updateValues(Recipe recipe){
        mDatabase.child("recipes").push().setValue(recipe);
        // TODO: THIS METHOD CREATES A NEW ENTRY INSTEAD OF UDATING THE OLD ONE. CHANGE!!!
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    private static byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis = new FileInputStream(f);
        ;
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        } catch (IOException e) {
            throw e;
        } finally {
            fis.close();
        }
        return bytes;
    }
}
