package nl.hasan.movieapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.hasan.movieapp.R;
import nl.hasan.movieapp.adapter.FragmentAdapter;

public class HomeActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private FirebaseFirestore db;
    private String userID, img;
    private FirebaseAuth fAuth;
    private TextView emailView, nameView;
    private LinearLayout bottomSheetLayout;
    private BottomSheetDialog bottomSheetDialog;
    private SharedPreferences preferences;
    private DocumentReference docRef;
    private View layout;
    private CircleImageView imgView, menuImg;
    private static final String PREFS_NAME = "saveLogin";
    private TabLayout tabs;
    private ViewPager2 viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("");

        toolbar = findViewById(R.id.toolbar);
        menuImg = findViewById(R.id.menuImg);
        setSupportActionBar(toolbar);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        bottomSheetLayout = findViewById(R.id.bottomSheetContainer);

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        // tabLayout
        tabs = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        setUpFragments();

        docRef = db.collection("users").document(userID);
        docRef.addSnapshotListener(this, (value, error) -> {
            if (error != null) {
                Log.d("error", error.toString());
            }
            if (value != null && value.exists()) {
                img = value.getString("img");
                if (img == null) {
                    menuImg.setImageResource(R.mipmap.profile);

                } else {
                    Glide.with(HomeActivity.this).load(img).into(menuImg);
                }
            }
        });
    }

    public void editProfile(View v) {
        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
    }

    public void showBottomSheetDialog(View v) {
        bottomSheetDialog = new BottomSheetDialog(HomeActivity.this, R.style.BottomSheetDialogTheme);

        layout = LayoutInflater.from(HomeActivity.this).
                inflate(R.layout.layout_bottom_sheet, bottomSheetLayout);

        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.show();
        showUsersData();
    }

    @SuppressLint("SetTextI18n")
    public void showUsersData() {
        // show users data
        nameView = layout.findViewById(R.id.name);
        emailView = layout.findViewById(R.id.email);
        imgView = layout.findViewById(R.id.profileImage);

        docRef.addSnapshotListener(this, (value, error) -> {
            if (error != null) {
                Log.d("error", error.toString());
            }
            if (value != null && value.exists()) {
                String fullName = value.getString("fName") + " " + value.getString("lName");
                String email = value.getString("email");
                String phone = value.getString("phone");

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("fname", fullName);
                editor.putString("email", email);
                editor.putString("phone", phone);

                editor.apply();

                nameView.setText(fullName);
                emailView.setText(email);

                if (img == null) {
                    imgView.setImageResource(R.mipmap.profile);
                } else {
                    Glide.with(HomeActivity.this).load(img).into(imgView);
                }
            }
        });
    }

    public void logout(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("login", false);
        editor.apply();

        bottomSheetDialog.dismiss();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void navigateToFavorite(View view) {
        startActivity(new Intent(this, FavoriteActivity.class));
    }

    public void setUpFragments() {
        FragmentAdapter adapter = new FragmentAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabs, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Movies");
            }
            if (position == 1) {
                tab.setText("TV Shows");
            }
        }).attach();
    }
}