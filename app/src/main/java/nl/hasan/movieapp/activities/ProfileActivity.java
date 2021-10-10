package nl.hasan.movieapp.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.hasan.movieapp.R;

public class ProfileActivity extends AppCompatActivity {
    public CircleImageView imgView;
    public Uri imageUri;
    public FirebaseFirestore db;
    public FirebaseStorage storage;
    public SharedPreferences preferences;
    public TextView name, email, phone;
    public DocumentReference docRef;
    public static final String PREFS_NAME = "saveLogin";
    public String userID;
    public FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgView = findViewById(R.id.imgView);
        name = findViewById(R.id.nameEdit);
        email = findViewById(R.id.emailEdit);
        phone = findViewById(R.id.phone);

        // database
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userID = fAuth.getCurrentUser().getUid();

        showUserData();
    }

    // start an activity for result
    ActivityResultLauncher<String> myGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            result -> {
                if (result != null) {
                    imgView.setImageURI(result);
                    imageUri = result;
                }
            });


    public void updateProfile(View v) {
        if (imageUri != null) {
            uploadToFirebase(imageUri);
        } else {
            Toast.makeText(this, "please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    // will run on img click
    public void selectImg(View v) {

        // select img from gallery
        myGetContent.launch("image/*");

    }

    public void back_to_home(View view) {
        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
    }

    public void uploadToFirebase(Uri uri) {

        StorageReference storageRef = storage.getReference().child(userID + "." + getFileExtension(uri));

        if (uri != null) {
            docRef = db.collection("users").document(userID);
            // store images in firebase
            storageRef.putFile(uri).addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl()
                    .addOnSuccessListener(uri1 -> {
                        String imageUrl = uri1.toString();

                        HashMap<String, Object> user = new HashMap<>();
                        user.put("img", imageUrl);

                        docRef.update(user)
                                .addOnSuccessListener(unused -> Log.d("success", "updated image"))
                                .addOnFailureListener(e -> Log.d("error", e.getMessage()));

                        Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Log.d("error", "error with uploading image"))
            );
        }
    }

    public String getFileExtension(Uri myUri) {
        ContentResolver contentRes = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentRes.getType(myUri));
    }

    public void showUserData() {
        docRef = db.collection("users").document(userID);

        String fname = preferences.getString("fname", "");
        String emailText = preferences.getString("email", "");
        String phoneText = preferences.getString("phone", "");

        docRef.addSnapshotListener((value, error) -> {
            if (value != null && value.exists()) {
                String img = value.getString("img");
                if (img == null) {
                    imgView.setImageResource(R.mipmap.profile);
                } else {
                    Glide.with(ProfileActivity.this)
                            .load(img)
                            .into(imgView);
                }

            }
        });
        name.setText(fname);
        email.setText(emailText);
        phone.setText(phoneText);

    }
}