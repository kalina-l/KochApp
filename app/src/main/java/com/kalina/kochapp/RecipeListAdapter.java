package com.kalina.kochapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kalina on 05.12.2016.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private final List<Recipe> mValues;
    private boolean mTwoPane;
    private Fragment fragment;
    private int containerID;
    private int listID;

    private FragmentManager fm;

    public RecipeListAdapter(List<Recipe> items, boolean mTwoPane, Fragment fragment, int containerID, int listID) {
        mValues = items;
        this.mTwoPane = mTwoPane;
        this.fragment = fragment;
        this.containerID = containerID;
        this.listID = listID;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(listID, parent, false);

        fm = ((FragmentActivity) parent.getContext()).getSupportFragmentManager();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        Bitmap bmp = BitmapFactory.decodeByteArray(mValues.get(position).image, 0, mValues.get(position).image.length);
        holder.mPreviewView.setImageBitmap(bmp);
        holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(RecipeDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                    fragment.setArguments(arguments);
                    fm.beginTransaction()
                            .replace(containerID, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mPreviewView;
        public final TextView mContentView;
        public Recipe mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPreviewView = (ImageView) view.findViewById(R.id.recipe_image);
            mContentView = (TextView) view.findViewById(R.id.recipe_title);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

