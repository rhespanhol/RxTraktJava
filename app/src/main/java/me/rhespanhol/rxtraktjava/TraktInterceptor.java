package me.rhespanhol.rxtraktjava;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class TraktInterceptor implements Interceptor {

    private Trakt mTrakt;

    public TraktInterceptor(Trakt mTrakt) {
        this.mTrakt = mTrakt;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return handleIntercept(chain, mTrakt.getClientId(), mTrakt.getAccessToken().access_token);
    }

    /**
     * If the host matches {@link Trakt#API_HOST} adds a header for the current {@link Trakt#API_VERSION}, {@link
     * Trakt#HEADER_TRAKT_API_KEY} with the given api key, {@link Trakt#HEADER_CONTENT_TYPE} and if not present an
     * Authorization header using the given access token.
     */
    public static Response handleIntercept(Chain chain, String clientId, String accessToken) throws IOException {
        Request request = chain.request();
        if (!Trakt.API_HOST.equals(request.url().host())) {
            // do not intercept requests for other hosts
            // this allows the interceptor to be used on a shared okhttp client
            return chain.proceed(request);
        }

        Request.Builder builder = request.newBuilder();

        // set required content type, api key and request specific API version
        builder.header(Trakt.HEADER_CONTENT_TYPE, Trakt.CONTENT_TYPE_JSON);
        builder.header(Trakt.HEADER_TRAKT_API_KEY, clientId);
        builder.header(Trakt.HEADER_TRAKT_API_VERSION, Trakt.API_VERSION);

        // add authorization header
        if (hasNoAuthorizationHeader(request) && accessTokenIsNotEmpty(accessToken)) {
            builder.header(Trakt.HEADER_AUTHORIZATION, "Bearer" + " " + accessToken);
        }
        return chain.proceed(builder.build());
    }

    private static boolean hasNoAuthorizationHeader(Request request) {
        return request.header(Trakt.HEADER_AUTHORIZATION) == null;
    }

    private static boolean accessTokenIsNotEmpty(String accessToken) {
        return accessToken != null && accessToken.length() != 0;
    }
}
