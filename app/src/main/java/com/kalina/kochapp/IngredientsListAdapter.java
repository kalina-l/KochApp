package com.kalina.kochapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kalina on 28.10.2016.
 */

class IngredientsListAdapter extends ArrayAdapter<HashMap<String, Double>>{

    private final ArrayList mData;

    public IngredientsListAdapter(Context context, Map<String, Double> map) {
        super(context, R.layout.recipe_ingredients_row);
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    //@Override
    public Map.Entry<String, Double> getIngredient(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingredients_row, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, Double> item = getIngredient(position);
        Log.d("ADAPTER TEST: ", item.getKey() + " and " + Double.toString(item.getValue()) + " got printed.");
        // TODO replace findViewById by ViewHolder
        ((TextView) result.findViewById(R.id.tv_ingredients_measures)).setText(Double.toString(item.getValue()));
        ((TextView) result.findViewById(R.id.tv_ingredients_name)).setText(item.getKey());

        return result;
    }

}
