package me.rhespanhol.rxtraktjava.enums;

/**
 * Created by rhespanhol on 17/06/16.
 */

public enum GrantType {

    AUTHORIZATION_CODE("authorization_code"),
    REFRESH_TOKEN("refresh_token");

    private String grantType;

    GrantType(String grantType) {
        this.grantType = grantType;
    }

    @Override
    public String toString() {
        return grantType;
    }
}
