package nl.hasan.movieapp.Api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApiSingleton {
    private RequestQueue requestQueue;
    private static ApiSingleton myInstance;
    private static Context ctx;

    private ApiSingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized ApiSingleton getInstance(Context ctx) {
        if (myInstance == null) {
            myInstance = new ApiSingleton(ctx);
        }
        return myInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
