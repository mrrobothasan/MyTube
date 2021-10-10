package nl.hasan.movieapp.Api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nl.hasan.movieapp.models.Video;

public class ApiHelper {

    Context ctx;

    public ApiHelper(Context ctx) {
        this.ctx = ctx;
    }

    public interface VolleyResponseListener {
        void OnResponse(List<Video> videoList);

        void OnError(String msg);
    }

    public void getVideos(String moviesUrl, String titleVid, VolleyResponseListener volleyResponseListener) {
        List<Video> videoList = new ArrayList<>();

        // get the Array object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, moviesUrl, null, response -> {
            try {
                JSONArray moviesApi = response.getJSONArray("results");
                for (int i = 0; i < moviesApi.length(); i++) {


                    JSONObject firstMovie = (JSONObject) moviesApi.get(i);
                    int ID = firstMovie.getInt("id");
                    String title = firstMovie.getString(titleVid);
                    String overview = firstMovie.getString("overview");
                    String poster = firstMovie.getString("poster_path");
                    double rating = firstMovie.getDouble("vote_average");

                    Video video = new Video(ID, title, "https://image.tmdb.org/t/p/w500" + poster, overview, rating);
                    videoList.add(video);

                }
                volleyResponseListener.OnResponse(videoList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            volleyResponseListener.OnError(error.getMessage());
        });

        MySingleton.getInstance(ctx).addToRequestQueue(request);
    }
}
