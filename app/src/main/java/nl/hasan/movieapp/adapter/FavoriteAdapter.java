package nl.hasan.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import nl.hasan.movieapp.R;
import nl.hasan.movieapp.activities.DetailActivity;
import nl.hasan.movieapp.activities.FavoriteActivity;
import nl.hasan.movieapp.db.MyDBHelper;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {
    private Context ctx;
    private List id, poster;
    private String userID;


    public FavoriteAdapter(Context ctx, List id, List poster, String userID ) {
        this.ctx = ctx;
        this.id = id;
        this.poster = poster;
        this.userID = userID;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.cardview_fav, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(ctx).load(poster.get(position)).into(holder.videoImg);
        holder.delFromFav.setOnClickListener(v -> {
            MyDBHelper myDBHelper = new MyDBHelper(ctx);
            myDBHelper.delRow(userID,  id.get(position).toString());

            Intent intent = new Intent(ctx, FavoriteActivity.class);
            ctx.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView videoImg;
        Button delFromFav;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            videoImg = itemView.findViewById(R.id.movieImgId);
            delFromFav = itemView.findViewById(R.id.delFromFav);
        }
    }
}
