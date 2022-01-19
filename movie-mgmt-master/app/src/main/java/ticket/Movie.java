package ticket;

import java.util.ArrayList;

public class Movie {
    private Rating rating;
    private String name;
    private int duration;
    private int movieID;
    private String movieSynopsis;
    private String movieRelease;
    private String movieDirector;
    private String[] movieCast;

    public Movie(String ratingName, String name, int duration, int movieID, String movieSynopsis, String movieRelease, String movieDirector, String[] movieCast){
        this.rating = getRatingWithName(ratingName);
        this.name = name;
        this.duration = duration;
        this.movieID = movieID;
        this.movieSynopsis = movieSynopsis;
        this.movieRelease = movieRelease;
        this.movieDirector = movieDirector;
        this.movieCast = movieCast;
    }

    public static Movie getMovieWithID(ArrayList<Movie> movies, int id){
        for (Movie m : movies){
            if (m.getID() == id) { return m; }
        }
        return null;
    }

    public int getDuration(){
        return this.duration;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Rating getRating(){
        return this.rating;
    }

    public void setRating(String rating){
        this.rating  = getRatingWithName(rating);
    }

    public int getID() { return this.movieID; }

    public String getSynopsis() { return this.movieSynopsis; }

    public void setSynopsis(String synopsis){
        this.movieSynopsis = synopsis;
    }

    public String getRelease() { return this.movieRelease; }

    public void setRelease(String release){
        this.movieRelease = release;
    }

    public String getDirector() { return this.movieDirector; }

    public void setDirector(String director){
        this.movieDirector = director;
    }

    public String[] getCast() { return this.movieCast; }

    public void setCast(String[] cast){
        this.movieCast = cast;
    }

    /**
     * Get the rating tied to a name
     * @param s the rating name
     * @return the assosiated ratings
     */
    private Rating getRatingWithName(String s){
        //Null check
        if (s == null){
            return Rating.RESTRICTED;
        }

        if (s.equalsIgnoreCase("general")){
            return Rating.GENERAL;
        } else if (s.equalsIgnoreCase("PARENTAL_GUIDANCE")){
            return Rating.PARENTAL_GUIDANCE;
        } else if (s.equalsIgnoreCase("MATURE")){
            return Rating.MATURE;
        } else if (s.equalsIgnoreCase("MATURE_ACCOMPANIES")){
            return Rating.MATURE_ACCOMPANIES;
        } else{
            return Rating.RESTRICTED;
        }
    }

    enum Rating{
        GENERAL,
        PARENTAL_GUIDANCE,
        MATURE,
        MATURE_ACCOMPANIES,
        RESTRICTED
    }

}
