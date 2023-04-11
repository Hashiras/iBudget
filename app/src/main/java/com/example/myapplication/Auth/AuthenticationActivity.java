package com.example.myapplication.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.myapplication.Data.Helper.Database;
import com.example.myapplication.R;

public class AuthenticationActivity extends AppCompatActivity {

    public static SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_activity);
        new Database(this);
        preferences = getSharedPreferences("FinancePreferences", Context.MODE_PRIVATE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_container, new SplashFragment())
                .commit();
    }
}