package nl.hasan.movieapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import nl.hasan.movieapp.R;
import nl.hasan.movieapp.db.MyDBHelper;

public class DetailActivity extends AppCompatActivity {
    ImageView movieImg;
    TextView titleView, overviewView, ratingView;
    String title, poster, overview, userID;
    int ID;
    double rating;
    private FirebaseAuth fAuth;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle b = getIntent().getExtras();
        fAuth = FirebaseAuth.getInstance();

        ID = b.getInt("id");
        userID = fAuth.getCurrentUser().getUid();
        title = b.getString("title");
        poster = b.getString("poster");
        overview = b.getString("overview");
        rating = b.getDouble("rating");

        movieImg = findViewById(R.id.movieImg);
        titleView = findViewById(R.id.title);
        overviewView = findViewById(R.id.overview);
        ratingView = findViewById(R.id.rating);

        Glide.with(this).load(poster).into(movieImg);
        titleView.setText(title);
        overviewView.setText(overview);
        ratingView.setText(Double.toString(rating));


    }

    public void addToFav(View v) {
        MyDBHelper myDBHelper = new MyDBHelper(DetailActivity.this);
        myDBHelper.addToFav(userID, ID, title, overview, poster, rating);
    }

    public void backHome(View v) {
        startActivity(new Intent(DetailActivity.this, HomeActivity.class));
    }


}