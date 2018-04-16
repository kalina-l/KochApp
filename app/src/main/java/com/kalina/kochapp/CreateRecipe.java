package com.kalina.kochapp;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateRecipe extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    Button uploadImageBtn;
    Button addIngredientsRowBtn;
    Button saveRecipeBtn;
    ImageView recipeIv;

    private ArrayList<EditText> quantityList;
    private ArrayList<Spinner> metricsList;
    private ArrayList<EditText> descriptionList;

    private Uri imagePath;

    public static boolean waitForServersAnswer;
    //public static FrameLayout progressBarHolder;
    //public static Window window;
    public static ProgressDialog pg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_recipe_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        //window = getWindow();
        pg = new ProgressDialog(this);

        quantityList = new ArrayList<>();
        metricsList = new ArrayList<>();
        descriptionList = new ArrayList<>();
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

    private void createRows(int number) {
        LinearLayout llParent = (LinearLayout) findViewById(R.id.ingredients_row_ll);

        for (int i = 0; i < number; i++) {

            LinearLayout ll = new LinearLayout(this);

            EditText et1 = new EditText(this);
            et1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            et1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            ;
            et1.setMaxLines(1);
            ll.addView(et1);
            quantityList.add(et1);

            Spinner spinner = new Spinner(this);
            createSpinnerAdapter(spinner);
            ll.addView(spinner);
            metricsList.add(spinner);

            EditText et2 = new EditText(this);
            et2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3));
            et2.setInputType(InputType.TYPE_CLASS_TEXT);
            et2.setMaxLines(1);
            ll.addView(et2);
            descriptionList.add(et2);

            llParent.addView(ll);
        }
    }

    private void createSpinnerAdapter(Spinner spinner) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.metrics_array, R.layout.spinner_layout);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private void saveRecipe() {
        boolean cancel = false;
        View focusView = null;

        // Recipes name
        EditText recipeNameEt = (EditText) findViewById(R.id.recipe_name_editText);
        String recipeName = recipeNameEt.getText().toString();
        if (recipeName.isEmpty()) {
            recipeNameEt.setError("Dieses Feld darf nicht leer sein.");
            focusView = recipeNameEt;
            cancel = true;
        }

        //Recipes description
        EditText recipeDescriptionEt = (EditText) findViewById(R.id.recipe_description_editText);
        String recipesDescr = recipeDescriptionEt.getText().toString();
        if (recipesDescr.isEmpty()) {
            recipeNameEt.setError("Dieses Feld darf nicht leer sein.");
            focusView = recipeNameEt;
            cancel = true;
        }

        //Recipes ingredients
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < descriptionList.size(); i++) {
            if (!descriptionList.get(i).getText().toString().isEmpty()) {
                String quantityString = quantityList.get(i).getText().toString();
                Double quantityDouble;
                if (!quantityString.isEmpty()) {
                    quantityDouble = Double.parseDouble(quantityString);
                } else quantityDouble = 0.0;
                Ingredient newIngredient = new Ingredient(quantityDouble, metricsList.get(i).getSelectedItem().toString(), descriptionList.get(i).getText().toString());
                ingredients.add(newIngredient);
            }
        }
        if (ingredients.isEmpty()) {
            descriptionList.get(0).setError("Ein Rezept braucht zumindest eine Zutat.");
            focusView = descriptionList.get(0);
            cancel = true;
        }

        //Privacy CheckBox
        CheckBox checkBoxm = (CheckBox) findViewById(R.id.privacy_checkBox);
        boolean recipesPrivacy = checkBoxm.isActivated();

        if (cancel) {
            focusView.requestFocus();
        } else {
            Recipe recipe = Recipe.writeNewRecipe(getApplicationContext(), recipeName, ingredients, recipesDescr, imagePath, recipesPrivacy);
            blockActivity();
            RecipeList.addGlobalRecipe(recipe);
            Intent recipeView = new Intent(CreateRecipe.this, RecipeDetailActivity.class);
            recipeView.putExtra(RecipeDetailFragment.ARG_ITEM_ID, recipe.id);
            CreateRecipe.this.startActivity(recipeView);
        }
    }

    public static void blockActivity() {
        waitForServersAnswer = true;
        //progressBarHolder.setVisibility(View.VISIBLE);
        //window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
               // WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pg.setTitle("Uploading...");
        pg.show();
    }

    public static void unblockActivity() {
        if (waitForServersAnswer) {
            //progressBarHolder.setVisibility(View.GONE);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            pg.dismiss();
        }
    }

    /* This method is invoked when a user picks a photo of a recipe to upload it.*/
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            uri = data.getData();
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
                final int MAX_HEIGHT = 800;

                // Find the correct scale value. It should be the power of 2.
                int scale = 1;
                while (o.outWidth / scale / 2 >= maxWidth ||
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

                imagePath = uri;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
