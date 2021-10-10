package nl.hasan.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import nl.hasan.movieapp.R;
import nl.hasan.movieapp.activities.DetailActivity;
import nl.hasan.movieapp.models.Video;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private Context ctx;
    private List<Video> videoList;

    public VideoAdapter(Context ctx, List<Video> videoList) {
        this.ctx = ctx;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.cardview, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        Video video = videoList.get(position);
        holder.rating.setText(video.getRating().toString());
        holder.title.setText(video.getTitle());
        holder.overview.setText(video.getOverview());
        Glide.with(ctx).load(video.getPoster()).into(holder.movieImg);

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, DetailActivity.class);

            Bundle bundle = new Bundle();
            bundle.putInt("id", video.getID());
            bundle.putString("title", video.getTitle());
            bundle.putDouble("rating", video.getRating());
            bundle.putString("overview", video.getOverview());
            bundle.putString("poster", video.getPoster());

            intent.putExtras(bundle);

            ctx.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {

        ImageView movieImg;
        TextView title, overview, rating;
        CardView cardView;

        public VideoHolder(@NonNull View itemView) {
            super(itemView);

            movieImg = itemView.findViewById(R.id.movieImgId);
            title = itemView.findViewById(R.id.titleId);
            overview = itemView.findViewById(R.id.overviewId);
            rating = itemView.findViewById(R.id.ratingId);
            cardView = itemView.findViewById(R.id.cardId);
        }
    }
}
