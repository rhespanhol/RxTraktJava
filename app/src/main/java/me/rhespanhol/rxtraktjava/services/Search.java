package me.rhespanhol.rxtraktjava.services;

import java.util.List;

import me.rhespanhol.rxtraktjava.entities.SearchResult;
import me.rhespanhol.rxtraktjava.enums.IdType;
import me.rhespanhol.rxtraktjava.enums.Type;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Search {

    /**
     * Queries will search fields like the title and description.
     *
     * @param query Searches titles and descriptions.
     * @param type (optional) Narrow down search by element type.
     */
    @GET("search")
    Call<List<SearchResult>> textQuery(
            @Query("query") String query,
            @Query("type") Type type,
            @Query("year") Integer year,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );

    /**
     * ID lookups are helpful if you have an external ID and want to get the trakt ID and info. This method will search
     * for movies, shows, episodes, people, users, and lists.
     *
     * @param idType Set to any of {@link IdType}.
     * @param id ID that matches with the type.
     */
    @GET("search")
    Call<List<SearchResult>> idLookup(
            @Query(value = "id_type", encoded = true) IdType idType,
            @Query(value = "id", encoded = true) String id,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );

}
