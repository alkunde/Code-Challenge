package com.arctouch.codechallenge.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.arctouch.codechallenge.R;

public class DetailActivity extends AppCompatActivity {

    private int idMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            idMovie = bundle.getInt("id");
    }
}
