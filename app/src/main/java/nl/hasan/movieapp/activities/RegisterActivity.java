package nl.hasan.movieapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import nl.hasan.movieapp.R;

public class RegisterActivity extends AppCompatActivity {
    public TextInputLayout fName, lName, email, pass, conPass, phone;
    public FirebaseAuth fAuth;
    public ProgressBar progressBar;
    public TextView loginBtn;
    public FirebaseFirestore db;
    public String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fName = findViewById(R.id.fname_container);
        lName = findViewById(R.id.lname_container);
        email = findViewById(R.id.email_container);
        pass = findViewById(R.id.password_container);
        conPass = findViewById(R.id.confirm_password_container);
        loginBtn = findViewById(R.id.login_text);
        progressBar = findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    public void registerUser(View v) {

        String fnameText = fName.getEditText().getText().toString();
        String lnameText = lName.getEditText().getText().toString();
        String emailText = email.getEditText().getText().toString().trim();
        String passText = pass.getEditText().getText().toString().trim();
        String conPassText = conPass.getEditText().getText().toString().trim();


        if (TextUtils.isEmpty(fnameText)) {
            fName.setError("This field is Required");
            return;
        }
        if (TextUtils.isEmpty(lnameText)) {
            lName.setError("This field is Required");
            return;
        }
        if (TextUtils.isEmpty(emailText)) {
            email.setError("This field is Required");
            return;
        }
        if (TextUtils.isEmpty(passText)) {
            pass.setError("This field is Required");
            return;
        }
        if (TextUtils.isEmpty(conPassText)) {
            conPass.setError("This field is Required");
            return;
        }
        if (!conPassText.equals(passText)) {
            conPass.setError("Passwords don't match");
            return;
        }

        // register user
        fAuth.createUserWithEmailAndPassword(emailText, passText).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();

                // data opslaan in firestore
                userID = fAuth.getCurrentUser().getUid();
                DocumentReference docRef = db.collection("users").document(userID);
                HashMap<String, Object> user = new HashMap<>();
                user.put("fName", fnameText);
                user.put("lName", lnameText);
                user.put("email", emailText);
                user.put("img", null);

                docRef.set(user).addOnSuccessListener(unused -> {
                    Log.d("succes", "Account is successfully created");
                });
                startActivity(new Intent(this, HomeActivity.class));
            } else {
                Toast.makeText(this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}