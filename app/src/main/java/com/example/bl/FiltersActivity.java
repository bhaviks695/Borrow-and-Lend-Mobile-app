package com.example.bl;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class FiltersActivity  extends AppCompatActivity {

    //widgets
    private Button mSave;
    private EditText mLocation;
    private ImageView mBackArrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        mSave = (Button) findViewById(R.id.btnSave);
        mLocation = (EditText) findViewById(R.id.input_location);
        mBackArrow = (ImageView) findViewById(R.id.backArrow);

        init();
    }
    private void init(){
        getFilterPreferences();

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(FiltersActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString(getString(R.string.preferences_item), mLocation.getText().toString());
                editor.commit();
            }
        });

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getFilterPreferences(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String item = preferences.getString(getString(R.string.preferences_item),"");

        mLocation.setText(item);
    }
}

