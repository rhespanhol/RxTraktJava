/*
 * Copyright 2014 Uwe Trottmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package me.rhespanhol.rxtraktjava;

import me.rhespanhol.rxtraktjava.entities.AccessToken;
import me.rhespanhol.rxtraktjava.entities.CheckinError;
import me.rhespanhol.rxtraktjava.enums.GrantType;
import me.rhespanhol.rxtraktjava.enums.ResponseType;
import me.rhespanhol.rxtraktjava.services.Authentication;
import me.rhespanhol.rxtraktjava.services.Calendars;
import me.rhespanhol.rxtraktjava.services.Checkin;
import me.rhespanhol.rxtraktjava.services.Comments;
import me.rhespanhol.rxtraktjava.services.Episodes;
import me.rhespanhol.rxtraktjava.services.Genres;
import me.rhespanhol.rxtraktjava.services.Movies;
import me.rhespanhol.rxtraktjava.services.People;
import me.rhespanhol.rxtraktjava.services.Recommendations;
import me.rhespanhol.rxtraktjava.services.Search;
import me.rhespanhol.rxtraktjava.services.Seasons;
import me.rhespanhol.rxtraktjava.services.Shows;
import me.rhespanhol.rxtraktjava.services.Sync;
import me.rhespanhol.rxtraktjava.services.Users;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Single;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.UUID;

/**
 * Helper class for easy usage of the trakt v2 API using mRetrofit.
 */
public class Trakt {

    /**
     * trakt API v2 URL.
     */
    public static final String API_HOST = "api-v2launch.trakt.tv";
    public static final String API_URL = "https://" + API_HOST + "/";
    public static final String API_VERSION = "2";

    public static final String SITE_URL = "https://trakt.tv";

    public static final String OAUTH2_URL = SITE_URL + "/oauth";
    public static final String OAUTH2_AUTHORIZATION_URL = OAUTH2_URL + "/authorize";
    public static final String OAUTH2_GET_TOKEN_URL = OAUTH2_URL + "/token";
    public static final String OAUTH2_REVOKE_TOKEN_URL = OAUTH2_URL + "/revoke";

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String HEADER_TRAKT_API_VERSION = "trakt-api-version";
    public static final String HEADER_TRAKT_API_KEY = "trakt-api-key";

    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;

    private String mClientId;
    private String mClientSecret;
    private String mRedirectUri;
    private AccessToken mAccessToken;

    /**
     * Get a new API manager instance capable of calling OAuth2 protected endpoints.
     *
     * @param clientId    The API key obtained from trakt, currently equal to the OAuth client id.
     * @param clientSecret The client secret obtained from trakt.
     * @param redirectUri  The redirect URI to use for OAuth2 token requests.
     */
    public Trakt(String clientId, String clientSecret, String redirectUri) {
        this.mClientId = clientId;
        this.mClientSecret = clientSecret;
        this.mRedirectUri = redirectUri;
    }

    public String getClientId() {
        return mClientId;
    }

    public void setClientId(String clientId) {
        mClientId = clientId;
    }

    /**
     * Sets the OAuth 2.0 access token to be appended to requests.
     * <p>
     * <p> If set, some methods will return user-specific data.
     *
     * @param accessToken A valid access token, obtained via e.g. {@link #exchangeCodeForAccessToken(String)}.
     */
    public Trakt setAccessToken(AccessToken accessToken) {
        this.mAccessToken = accessToken;
        return this;
    }

    public AccessToken getAccessToken() {
        return mAccessToken;
    }

    public String getRefreshToken() {
        return mAccessToken.refresh_token;
    }

    /**
     * Sets the OAuth 2.0 refresh token to be used, in case the current access token has expired, to get a new access
     * token.
     */
    public Trakt setRefreshToken(AccessToken refreshToken) {
        this.mAccessToken = refreshToken;
        return this;
    }

    /**
     * Creates a {@link Retrofit.Builder} that sets the base URL, adds a Gson converter and sets {@link #okHttpClient()}
     * as its client.
     *
     * @see #okHttpClient()
     */
    protected Retrofit.Builder retrofitBuilder() {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(TraktHelper.getGsonBuilder().create()))
                .client(okHttpClient());
    }

    /**
     * Returns the default OkHttp client instance. It is strongly recommended to override this and use your app
     * instance.
     *
     * @see #setOkHttpClientDefaults(OkHttpClient.Builder)
     */
    protected synchronized OkHttpClient okHttpClient() {
        if (mOkHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            setOkHttpClientDefaults(builder);
            mOkHttpClient = builder.build();
        }
        return mOkHttpClient;
    }

    /**
     * Adds a network interceptor to add version and auth headers and a regular interceptor to log requests.
     */
    protected void setOkHttpClientDefaults(OkHttpClient.Builder builder) {
        builder.addNetworkInterceptor(new TraktInterceptor(this));
        builder.authenticator(new TraktAuthenticator(this));
    }

    /**
     * Return the {@link Retrofit} instance. If called for the first time builds the instance.
     */
    protected Retrofit retrofit() {
        if (mRetrofit == null) {
            mRetrofit = retrofitBuilder().build();
        }
        return mRetrofit;
    }

    /**
     * Build an OAuth 2.0 authorization request to obtain an authorization code.
     * <p>
     * <p>Send the user to the location URI of this request. Once the user authorized your app, the server will redirect
     * to {@code mRedirectUri} with the authorization code and the sent state in the query parameter {@code code}.
     * <p>
     * <p>Ensure the state matches, then supply the authorization code to {@link #exchangeCodeForAccessToken} to get an
     * access token.
     */

    public String buildAuthenticationUrl() {
        return OAUTH2_AUTHORIZATION_URL +
                "?client_id=" + mClientId +
                "&redirect_uri=" + mRedirectUri +
                "&response_type=" + ResponseType.CODE.toString() +
                "&state=" + UUID.randomUUID().toString();
    }

    /**
     * Request an access token from trakt.
     * <p>
     * <p>Supply the received access token to {@link #setAccessToken(AccessToken)} and store the refresh token to later refresh
     * the access token once it has expired.
     * <p>
     * <p>On failure re-authorization of your app is required (see {@link #buildAuthenticationUrl}).
     *
     * @param authCode A valid authorization code (see {@link #buildAuthenticationUrl()}.
     */
    public Single<AccessToken> exchangeCodeForAccessToken(String authCode) {
        return authentication().exchangeCodeForAccessToken(
                GrantType.AUTHORIZATION_CODE.toString(),
                authCode,
                getAccessToken().access_token,
                mClientSecret,
                mRedirectUri
        );
    }

    /**
     * Request to refresh an expired access token for trakt. If your app is still authorized, returns a response which
     * includes a new access token.
     * <p>
     * <p>Supply the received access token to {@link #setAccessToken(AccessToken)} and store the refresh token to later refresh
     * the access token once it has expired.
     * <p>
     * <p>On failure re-authorization of your app is required (see {@link #buildAuthenticationUrl}).
     */
    public Single<AccessToken> refreshAccessToken() {
        return authentication().refreshAccessToken(
                GrantType.REFRESH_TOKEN.toString(),
                getAccessToken().refresh_token,
                getAccessToken().access_token,
                mClientSecret,
                mRedirectUri
        );
    }

    /**
     * If the response code is 409 tries to convert the body into a {@link CheckinError}, otherwise returns {@code
     * null}.
     *
     * @throws IOException If converting the error to {@link CheckinError} failed.
     */
    public CheckinError checkForCheckinError(Response response) throws IOException {
        if (response.code() != 409) {
            return null; // only code 409 can be a check-in error
        }
        Converter<ResponseBody, CheckinError> errorConverter =
                mRetrofit.responseBodyConverter(CheckinError.class, new Annotation[0]);
        return errorConverter.convert(response.errorBody());
    }

    public Authentication authentication() {
        return retrofit().create(Authentication.class);
    }

    /**
     * By default, the calendar will return all shows or movies for the specified time period. If OAuth is sent, the
     * items returned will be limited to what the user has watched, collected, or added to their watchlist. You'll most
     * likely want to send OAuth to make the calendar more relevant to the user.
     */
    public Calendars calendars() {
        return retrofit().create(Calendars.class);
    }

    /**
     * Checking in is a manual process used by mobile apps. While not as effortless as scrobbling, checkins help fill in
     * the gaps. You might be watching live tv, at a friend's house, or watching a movie in theaters. You can simply
     * checkin from your phone or tablet in those situations.
     */
    public Checkin checkin() {
        return retrofit().create(Checkin.class);
    }

    /**
     * Comments are attached to any movie, show, season, episode, or list and can be shorter shouts or more in depth
     * reviews. Each comment can have replies and can be voted up or down. These votes are used to determine popular
     * comments.
     */
    public Comments comments() {
        return retrofit().create(Comments.class);
    }

    /**
     * One or more genres are attached to all movies and shows. Some API methods allow filtering by genre, so it's good
     * to cache this list in your app.
     */
    public Genres genres() {
        return retrofit().create(Genres.class);
    }

    public Movies movies() {
        return retrofit().create(Movies.class);
    }

    public People people() {
        return retrofit().create(People.class);
    }

    /**
     * Recommendations are based on the watched history for a user and their friends. There are other factors that go
     * into the algorithm as well to further personalize what gets recommended.
     */
    public Recommendations recommendations() {
        return retrofit().create(Recommendations.class);
    }

    /**
     * Searches can use queries or ID lookups. Queries will search fields like the title and description. ID lookups are
     * helpful if you have an external ID and want to get the trakt ID and info. This method will search for movies,
     * shows, episodes, people, users, and lists.
     */
    public Search search() {
        return retrofit().create(Search.class);
    }

    public Shows shows() {
        return retrofit().create(Shows.class);
    }

    public Seasons seasons() {
        return retrofit().create(Seasons.class);
    }

    public Episodes episodes() {
        return retrofit().create(Episodes.class);
    }

    public Sync sync() {
        return retrofit().create(Sync.class);
    }

    public Users users() {
        return retrofit().create(Users.class);
    }

}
