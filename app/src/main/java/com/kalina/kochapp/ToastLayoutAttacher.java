package com.kalina.kochapp;

import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Kalina on 16.11.2016.
 */

public class ToastLayoutAttacher {

    public void attachLayout(String msg, AppCompatActivity startingActivity){
        LayoutInflater inflater = startingActivity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) startingActivity.findViewById(R.id.toast_layout_root));

        //ImageView image = (ImageView) layout.findViewById(R.id.image);
        //image.setImageResource(R.drawable.android);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);

        Toast toast = new Toast(startingActivity.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}
