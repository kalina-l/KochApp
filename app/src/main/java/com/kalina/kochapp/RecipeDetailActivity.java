package com.kalina.kochapp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ImageView;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // FILL THE COLLAPSINGTOOLBARLAYOUT WITH A RECEIPT IMAGE
        Bundle b = getIntent().getExtras();
        String item_id = "";
        if(b != null)
            item_id = b.getString(RecipeDetailFragment.ARG_ITEM_ID);
        final Recipe mItem = RecipeList.ALL_RECIPES_MAP.get(item_id);
        Drawable image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(mItem.image, 0, mItem.image.length));
        ImageView ctl = (ImageView) findViewById(R.id.receipt_image);
        ctl.setImageDrawable(image);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        setFloatingButtonSprite(fab, mItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ApplicationManager.currentUser.recipeIds.contains(mItem.id)){
                    //delete recipe from cookbook
                    ApplicationManager.removeRecipe(mItem);
                    Snackbar.make(view, "Dieses Rezept wurde aus deinem Kochbuch entfernt.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fab.setImageResource(R.drawable.plus_icon);
                }
                else {
                    //add recipe to the cookbook
                    ApplicationManager.addRecipe(mItem);
                    Snackbar.make(view, "Dieses Rezept wurde in dein Kochbuch hinzugefügt.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fab.setImageResource(R.drawable.minus_icon);
                }
            }
        });

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(RecipeDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(RecipeDetailFragment.ARG_ITEM_ID));
            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_container, fragment)
                    .commit();
        }
    }

    private void setFloatingButtonSprite(FloatingActionButton fb, Recipe item){
        if(ApplicationManager.currentUser.recipeIds.contains(item.id)) {
            fb.setImageResource(R.drawable.minus_icon);
        }
        else {
            fb.setImageResource(R.drawable.plus_icon);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, RecipeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
