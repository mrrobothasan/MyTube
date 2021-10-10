package nl.hasan.movieapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nl.hasan.movieapp.Api.ApiHelper;
import nl.hasan.movieapp.R;
import nl.hasan.movieapp.adapter.VideoAdapter;
import nl.hasan.movieapp.models.Video;

public class MoviesFragment extends Fragment {
    public RecyclerView movieReView;
    public VideoAdapter adapter;
    public static final String moviesUrl = "https://api.themoviedb.org/3/movie/popular?api_key=3cbe39fc0fea22b0c7e3cb34bc8311a3";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        Context ctx = getActivity();

        movieReView = view.findViewById(R.id.reView);
        movieReView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(ctx);
        movieReView.setLayoutManager(llm);



        ApiHelper apiHelper = new ApiHelper(ctx);


        apiHelper.getVideos(moviesUrl, "title", new ApiHelper.VolleyResponseListener() {

            @Override
            public void OnResponse(List<Video> videoList) {
                adapter = new VideoAdapter(ctx, videoList);
                movieReView.setAdapter(adapter);
            }

            @Override
            public void OnError(String msg) {
            }
        });

        return view;
    }
}
