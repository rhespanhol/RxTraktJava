package me.rhespanhol.rxtraktjava.services;

import java.util.List;

import me.rhespanhol.rxtraktjava.entities.Comment;
import me.rhespanhol.rxtraktjava.entities.Episode;
import me.rhespanhol.rxtraktjava.entities.Ratings;
import me.rhespanhol.rxtraktjava.entities.Season;
import me.rhespanhol.rxtraktjava.enums.Extended;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Seasons {

    /**
     * Returns all seasons for a show including the number of episodes in each season.
     *
     * @param showId trakt ID, trakt slug, or IMDB ID. Example: "game-of-thrones".
     */
    @GET("shows/{id}/seasons")
    Call<List<Season>> summary(
            @Path("id") String showId,
            @Query(value = "extended", encoded = true) Extended extended
    );

    /**
     * Returns all episodes for a specific season of a show.
     *
     * @param showId trakt ID, trakt slug, or IMDB ID. Example: "game-of-thrones".
     * @param season Season number.
     */
    @GET("shows/{id}/seasons/{season}")
    Call<List<Episode>> season(
            @Path("id") String showId,
            @Path("season") int season,
            @Query(value = "extended", encoded = true) Extended extended
    );

    /**
     * Returns all top level comments for a season. Most recent comments returned first.
     *
     * @param showId trakt ID, trakt slug, or IMDB ID. Example: "game-of-thrones".
     * @param season Season number.
     */
    @GET("shows/{id}/seasons/{season}/comments")
    Call<List<Comment>> comments(
            @Path("id") String showId,
            @Path("season") int season
    );

    /**
     * Returns rating (between 0 and 10) and distribution for a season.
     *
     * @param showId trakt ID, trakt slug, or IMDB ID. Example: "game-of-thrones".
     * @param season Season number.
     */
    @GET("shows/{id}/seasons/{season}/ratings")
    Call<Ratings> ratings(
            @Path("id") String showId,
            @Path("season") int season
    );

}
