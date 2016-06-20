package me.rhespanhol.rxtraktjava.services;

import java.util.List;

import me.rhespanhol.rxtraktjava.entities.Movie;
import me.rhespanhol.rxtraktjava.entities.Show;
import me.rhespanhol.rxtraktjava.enums.Extended;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Recommendations {

    /**
     * <b>OAuth Required</b>
     *
     * <p> Personalized movie recommendations for a user. Results returned with the top recommendation first.
     */
    @GET("recommendations/movies")
    Call<List<Movie>> movies(
            @Query(value = "extended", encoded = true) Extended extended
    );

    /**
     * <b>OAuth Required</b>
     *
     * <p> Dismiss a movie from getting recommended anymore.
     *
     * @param movieId trakt ID, trakt slug, or IMDB ID. Example: "tron-legacy-2010".
     */
    @DELETE("recommendations/movies/{id}")
    Call<Void> dismissMovie(
            @Path("id") String movieId
    );

    /**
     * <b>OAuth Required</b>
     *
     * <p> Personalized show recommendations for a user. Results returned with the top recommendation first.
     */
    @GET("recommendations/shows")
    Call<List<Show>> shows(
            @Query(value = "extended", encoded = true) Extended extended
    );

    /**
     * <b>OAuth Required</b>
     *
     * <p> Dismiss a show from getting recommended anymore.
     *
     * @param showId trakt ID, trakt slug, or IMDB ID. Example: 922.
     */
    @DELETE("recommendations/shows/{id}")
    Call<Void> dismissShow(
            @Path("id") String showId
    );

}
