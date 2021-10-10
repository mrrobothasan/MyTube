package nl.hasan.movieapp.models;

public class Video {
    private int ID;
    private String title, poster, overview;
    private Double rating;

    public Video(int id, String title, String poster, String overview, Double rating) {
        this.ID = id;
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.rating = rating;
    }


    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getOverview() {
        return overview;
    }

    public Double getRating() {
        return rating;
    }

    public int getID() {
        return ID;
    }


}
