package nl.hasan.movieapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {

    private static Context ctx;
    private static MyDBHelper mInstance;
    private static final String DATABASE_NAME = "videos.db";
    private static final int DATABASE_VERSION = 5;


    public MyDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized MyDBHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyDBHelper(context);
            ctx = context;
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + MyDBInfo.FavTable.TABLE_NAME +
                " (" + MyDBInfo.FavColumn.ID + " INTEGER PRIMARY KEY, " +
                MyDBInfo.FavColumn.USER_ID + " TEXT, " +
                MyDBInfo.FavColumn.TITLE + " TEXT, " +
                MyDBInfo.FavColumn.OVERVIEW + " TEXT, " +
                MyDBInfo.FavColumn.RATING + " FLOAT, " +
                MyDBInfo.FavColumn.POSTER + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MyDBInfo.FavTable.TABLE_NAME);
        onCreate(db);
    }

    public void addToFav(String ID, int videoID, String title, String overview, String poster, double rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MyDBInfo.FavColumn.USER_ID, ID);
        cv.put(MyDBInfo.FavColumn.ID, videoID);
        cv.put(MyDBInfo.FavColumn.TITLE, title);
        cv.put(MyDBInfo.FavColumn.OVERVIEW, overview);
        cv.put(MyDBInfo.FavColumn.RATING, rating);
        cv.put(MyDBInfo.FavColumn.POSTER, poster);

        long result = db.insert(MyDBInfo.FavTable.TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(ctx, "This Show or Movie is already added to Favorites", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ctx, "Added to Favorites", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readData(String userID) {
        String query = "SELECT * FROM " + MyDBInfo.FavTable.TABLE_NAME + " WHERE " + MyDBInfo.FavColumn.USER_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{userID});
        }
        return cursor;
    }

    public void delRow(String userID, String videoID) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(MyDBInfo.FavTable.TABLE_NAME,
                MyDBInfo.FavColumn.ID + "=" + videoID + " and " + MyDBInfo.FavColumn.USER_ID + "=" + "'" + userID + "'",
                null);

        if (result == -1) {
            Toast.makeText(ctx, "Something went wrong", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ctx, "Removed successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
