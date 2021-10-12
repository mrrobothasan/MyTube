package nl.hasan.movieapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import nl.hasan.movieapp.R;

public class LoginActivity extends AppCompatActivity {

    public TextInputLayout emailInput, passInput;
    public FirebaseAuth fAuth;
    public ProgressBar progressBar;
    public CheckBox saveLogin;
    public static final String PREFS_NAME = "saveLogin";
    public SharedPreferences preferences;
    public FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.login_email_container);
        passInput = findViewById(R.id.login_password_container);

        saveLogin = findViewById(R.id.saveLogin);

        // database
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

    }

    public void goToRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void login(View v) {
        String emailText = emailInput.getEditText().getText().toString().trim();
        String passText = passInput.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(emailText)) {
            emailInput.setError("This field is Required");
            return;
        } else {
            emailInput.setError("");
        }
        if (TextUtils.isEmpty(passText)) {
            passInput.setError("This field is Required");
            return;
        }

        // authenticate user
        fAuth.signInWithEmailAndPassword(emailText, passText).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Logged in successfully ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomeActivity.class));
            } else {
                Toast.makeText(this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void itemClicked(View v) {
        if (saveLogin.isChecked()) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("login", true);
            editor.apply();
        }
    }
}