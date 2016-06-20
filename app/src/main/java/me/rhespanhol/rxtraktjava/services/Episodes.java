package me.rhespanhol.rxtraktjava.services;

import java.util.List;

import me.rhespanhol.rxtraktjava.entities.Comment;
import me.rhespanhol.rxtraktjava.entities.Episode;
import me.rhespanhol.rxtraktjava.entities.Ratings;
import me.rhespanhol.rxtraktjava.enums.Extended;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Episodes {

    /**
     * Returns a single episode's details.
     *
     * @param showId trakt ID, trakt slug, or IMDB ID. Example: "game-of-thrones".
     * @param season Season number.
     * @param episode Episode number.
     */
    @GET("shows/{id}/seasons/{season}/episodes/{episode}")
    Call<Episode> summary(
            @Path("id") String showId,
            @Path("season") int season,
            @Path("episode") int episode,
            @Query(value = "extended", encoded = true) Extended extended
    );

    /**
     * Returns all top level comments for an episode. Most recent comments returned first.
     *
     * @param showId trakt ID, trakt slug, or IMDB ID. Example: "game-of-thrones".
     * @param season Season number.
     * @param episode Episode number.
     */
    @GET("shows/{id}/seasons/{season}/episodes/{episode}/comments")
    Call<List<Comment>> comments(
            @Path("id") String showId,
            @Path("season") int season,
            @Path("episode") int episode,
            @Query("page") Integer page,
            @Query("limit") Integer limit,
            @Query(value = "extended", encoded = true) Extended extended
    );

    /**
     * Returns rating (between 0 and 10) and distribution for an episode.
     *
     * @param showId trakt ID, trakt slug, or IMDB ID. Example: "game-of-thrones".
     * @param season Season number.
     * @param episode Episode number.
     */
    @GET("shows/{id}/seasons/{season}/episodes/{episode}/ratings")
    Call<Ratings> ratings(
            @Path("id") String showId,
            @Path("season") int season,
            @Path("episode") int episode
    );

}
