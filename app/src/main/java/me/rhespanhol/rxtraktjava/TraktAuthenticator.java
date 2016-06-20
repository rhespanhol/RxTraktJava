package me.rhespanhol.rxtraktjava;

import me.rhespanhol.rxtraktjava.entities.AccessToken;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import rx.functions.Action1;

import java.io.IOException;

public class TraktAuthenticator implements Authenticator {

    private final Trakt mTrakt;

    public TraktAuthenticator(Trakt trakt) {
        this.mTrakt = trakt;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        return handleAuthenticate(response, mTrakt);
    }

    /**
     * If not doing a trakt {@link Trakt#API_URL} request tries to refresh the access token with the refresh token.
     *
     * @param response The response passed to {@link #authenticate(Route, Response)}.
     * @param trakt The {@link Trakt} instance to get the API key from and to set the updated JSON web token on.
     * @return A request with updated authorization header or null if no auth is possible.
     */
    public static Request handleAuthenticate(Response response, final Trakt trakt) throws IOException {
        if (!Trakt.API_HOST.equals(response.request().url().host())) {
            return null; // not a trakt API endpoint (possibly trakt OAuth or other API), give up.
        }
        if (responseCount(response) >= 2) {
            return null; // failed 2 times, give up.
        }
        if (trakt.getRefreshToken() == null || trakt.getRefreshToken().length() == 0) {
            return null; // have no refresh token, give up.
        }

        trakt.refreshAccessToken().doOnSuccess(new Action1<AccessToken>() {
            @Override
            public void call(AccessToken accessToken) {
                trakt.setAccessToken(accessToken);

            }
        });

        // retry request
        return response.request().newBuilder()
                .header(Trakt.HEADER_AUTHORIZATION, "Bearer " + trakt.getAccessToken().access_token)
                .build();
    }

    private static int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }

}
