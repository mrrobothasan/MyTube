package nl.hasan.movieapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;
import nl.hasan.movieapp.R;
import nl.hasan.movieapp.adapter.FavoriteAdapter;
import nl.hasan.movieapp.db.MyDBHelper;

public class FavoriteActivity extends AppCompatActivity {

    RecyclerView favRecView;
    MyDBHelper dbHelper;
    List<String> id, title, overview, rating, poster;
    String userID;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        favRecView = findViewById(R.id.reView);

        dbHelper = new MyDBHelper(FavoriteActivity.this);
        id = new ArrayList<>();
        title = new ArrayList<>();
        overview = new ArrayList<>();
        poster = new ArrayList<>();
        rating = new ArrayList<>();

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();


        storeDataInArray();
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(FavoriteActivity.this, id, poster, userID);
        favRecView.setAdapter(favoriteAdapter);
        favRecView.setHasFixedSize(true);
        favRecView.setLayoutManager(new GridLayoutManager(FavoriteActivity.this, 2));

    }

    public void back_to_home(View view) {
        startActivity(new Intent(FavoriteActivity.this, HomeActivity.class));
    }

    public void storeDataInArray() {
        Cursor cursor = dbHelper.readData(userID);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Favorites", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                poster.add(cursor.getString(5));
            }
        }
    }
}