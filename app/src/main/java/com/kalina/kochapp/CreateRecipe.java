package com.kalina.kochapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.io.IOException;

public class CreateRecipe extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    Button uploadImageBtn;
    Button addIngredientsRowBtn;
    Button saveRecipeBtn;
    ImageView recipeIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_recipe_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        createRows(3);

        recipeIv = (ImageView) findViewById(R.id.recipe_image);

        uploadImageBtn = (Button) findViewById(R.id.recipe_upload_button);
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(getIntent, "Select Picture"), PICK_IMAGE_REQUEST_CODE);
            }
        });

        addIngredientsRowBtn = (Button) findViewById(R.id.add_ingredients_rows);
        addIngredientsRowBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createRows(1);
            }
        });

        saveRecipeBtn = (Button) findViewById(R.id.save_recipe_button);
        saveRecipeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               saveRecipe();
            }
        });
    }

    private void createRows(int number){
        LinearLayout llParent = (LinearLayout) findViewById(R.id.ingredients_row_ll);

        for (int i = 0; i <number; i++) {

            LinearLayout ll = new LinearLayout(this);

            EditText et1 = new EditText(this);
            et1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            et1.setInputType(InputType.TYPE_CLASS_NUMBER);
            et1.setMaxLines(1);
            ll.addView(et1);

            Spinner spinner = new Spinner(this);
            createSpinnerAdapter(spinner);
            ll.addView(spinner);

            EditText et2 = new EditText(this);
            et2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3));
            et2.setInputType(InputType.TYPE_CLASS_TEXT);
            et2.setMaxLines(1);
            ll.addView(et2);

            llParent.addView(ll);
        }
    }

    private void createSpinnerAdapter(Spinner spinner){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.metrics_array, R.layout.spinner_layout);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private void saveRecipe(){
        boolean cancel = false;
        View focusView = null;

        EditText recipeNameEt = (EditText) findViewById(R.id.recipe_name_editText);
        String recipeName = recipeNameEt.getText().toString();
        if(recipeName.isEmpty()){
            recipeNameEt.setError("Dieses Feld darf nicht leer sein.");
            focusView = recipeNameEt;
            cancel = true;
        }



        Recipe recipe = new Recipe();
    }

    /* This method is invoked when a user picks a photo of a recipe to upload it.*/
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                //Log.i(TAG, "Uri: " + uri.toString());
                //recipeIv.setImageURI(uri);
                try {
                    // The new size we want to scale to
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, o);

                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int maxWidth = size.x;
                    final int MAX_HEIGHT=800;

                    // Find the correct scale value. It should be the power of 2.
                    int scale = 1;
                    while(o.outWidth / scale / 2 >= maxWidth ||
                            o.outHeight / scale / 2 >= MAX_HEIGHT) {
                        scale *= 2;
                    }

                    // Decode with inSampleSize
                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize = scale;
                    Bitmap preview_bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, o2);

                    //BitmapFactory.Options options = new BitmapFactory.Options();
                    //options.inSampleSize = 8;
                    //Bitmap preview_bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);

                    //recipeIv.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
                    recipeIv.setImageBitmap(preview_bitmap);
                    recipeIv.requestLayout();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
