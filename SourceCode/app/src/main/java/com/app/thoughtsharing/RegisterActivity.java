package com.app.thoughtsharing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dewneot-pc on 12/11/2015.
 */
public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, RegisterFragment.newInstance("hai","hello"), this.toString())
                    .commit();
        }
    }
}