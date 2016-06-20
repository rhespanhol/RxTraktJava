package me.rhespanhol.rxtraktjava.services;

import me.rhespanhol.rxtraktjava.Trakt;
import me.rhespanhol.rxtraktjava.entities.AccessToken;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Single;

public interface Authentication {

    @FormUrlEncoded
    @POST(Trakt.OAUTH2_GET_TOKEN_URL)
    Single<AccessToken> exchangeCodeForAccessToken(
            @Field("grant_type") String grantType,
            @Field("code") String code,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("redirect_uri") String redirectUri
    );

    @FormUrlEncoded
    @POST(Trakt.OAUTH2_GET_TOKEN_URL)
    Single<AccessToken> refreshAccessToken(
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("redirect_uri") String redirectUri
    );

    @FormUrlEncoded
    @POST(Trakt.OAUTH2_REVOKE_TOKEN_URL)
    Single<Void> revokeAccessToken(
            @Field("access_token") String accessToken
    );

}
