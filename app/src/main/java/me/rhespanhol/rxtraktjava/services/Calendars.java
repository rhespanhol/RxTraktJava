package me.rhespanhol.rxtraktjava.services;

import java.util.List;

import me.rhespanhol.rxtraktjava.entities.CalendarMovieEntry;
import me.rhespanhol.rxtraktjava.entities.CalendarShowEntry;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Calendars {

    /**
     * <b>OAuth Required</b>
     *
     * @see #shows(String, int)
     */
    @GET("calendars/my/shows/{startdate}/{days}")
    Call<List<CalendarShowEntry>> myShows(
            @Path("startdate") String startDate,
            @Path("days") int days
    );

    /**
     * <b>OAuth Required</b>
     *
     * @see #newShows(String, int)
     */
    @GET("calendars/my/shows/new/{startdate}/{days}")
    Call<List<CalendarShowEntry>> myNewShows(
            @Path("startdate") String startDate,
            @Path("days") int days
    );

    /**
     * <b>OAuth Required</b>
     *
     * @see #seasonPremieres(String, int)
     */
    @GET("calendars/my/shows/premieres/{startdate}/{days}")
    Call<List<CalendarShowEntry>> mySeasonPremieres(
            @Path("startdate") String startDate,
            @Path("days") int days
    );

    /**
     * <b>OAuth Required</b>
     *
     * @see #movies(String, int)
     */
    @GET("calendars/my/movies/{startdate}/{days}")
    Call<List<CalendarMovieEntry>> myMovies(
            @Path("startdate") String startDate,
            @Path("days") int days
    );

    /**
     * Returns all shows airing during the time period specified.
     *
     * @param startDate Start the calendar on this date. Example: 2014-09-01.
     * @param days Number of days to display. Example: 7.
     */
    @GET("calendars/all/shows/{startdate}/{days}")
    Call<List<CalendarShowEntry>> shows(
            @Path("startdate") String startDate,
            @Path("days") int days
    );

    /**
     * Returns all new show premieres (season 1, episode 1) airing during the time period specified.
     *
     * @param startDate Start the calendar on this date. Example: 2014-09-01.
     * @param days Number of days to display. Example: 7.
     */
    @GET("calendars/all/shows/new/{startdate}/{days}")
    Call<List<CalendarShowEntry>> newShows(
            @Path("startdate") String startDate,
            @Path("days") int days
    );

    /**
     * Returns all show premieres (any season, episode 1) airing during the time period specified.
     *
     * @param startDate Start the calendar on this date. Example: 2014-09-01.
     * @param days Number of days to display. Example: 7.
     */
    @GET("calendars/all/shows/premieres/{startdate}/{days}")
    Call<List<CalendarShowEntry>> seasonPremieres(
            @Path("startdate") String startDate,
            @Path("days") int days
    );

    /**
     * Returns all movies with a release date during the time period specified.
     *
     * @param startDate Start the calendar on this date. Example: 2014-09-01.
     * @param days Number of days to display. Example: 7.
     */
    @GET("calendars/all/movies/{startdate}/{days}")
    Call<List<CalendarMovieEntry>> movies(
            @Path("startdate") String startDate,
            @Path("days") int days
    );

}
