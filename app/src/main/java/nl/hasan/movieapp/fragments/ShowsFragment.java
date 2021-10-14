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

public class ShowsFragment extends Fragment {
    public RecyclerView showReView;
    VideoAdapter adapter;
    public static final String tvUrl = "https://api.themoviedb.org/3/tv/popular?api_key=3cbe39fc0fea22b0c7e3cb34bc8311a3";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv, container, false);
        Context ctx = getActivity();

        showReView = view.findViewById(R.id.reView);
        showReView.setHasFixedSize(true);
        showReView.setLayoutManager(new LinearLayoutManager(ctx));

        adapter = new VideoAdapter();
        showReView.setAdapter(adapter);

        setAdapter(ctx);

        return view;
    }

    public void setAdapter(Context ctx) {
        ApiHelper apiHelper = new ApiHelper(ctx);

        apiHelper.getVideos(tvUrl, "name", new ApiHelper.VolleyResponseListener() {
            @Override
            public void OnResponse(List<Video> videoList) {
                adapter = new VideoAdapter(ctx, videoList);
                showReView.setAdapter(adapter);
            }

            @Override
            public void OnError(String msg) {
            }
        });
    }
}
