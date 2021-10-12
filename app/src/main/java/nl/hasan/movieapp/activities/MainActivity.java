package nl.hasan.movieapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import nl.hasan.movieapp.R;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "saveLogin";
    public FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean login = preferences.getBoolean("login", false);
        fAuth = FirebaseAuth.getInstance();

        if (login && fAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    public void signUp(View v) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void logIn(View v) {
        startActivity(new Intent(this, LoginActivity.class));
    }

}