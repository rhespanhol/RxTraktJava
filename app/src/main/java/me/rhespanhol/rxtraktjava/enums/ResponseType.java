package me.rhespanhol.rxtraktjava.enums;

public enum ResponseType {

    CODE("code"),
    TOKEN("token");

    private String code;

    ResponseType(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}