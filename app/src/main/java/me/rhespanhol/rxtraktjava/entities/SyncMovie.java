package me.rhespanhol.rxtraktjava.entities;

import org.joda.time.DateTime;

import me.rhespanhol.rxtraktjava.enums.Rating;

public class SyncMovie {

    public MovieIds ids;

    public DateTime collected_at;
    public DateTime watched_at;
    public DateTime rated_at;
    public Rating rating;

    public SyncMovie id(MovieIds id) {
        this.ids = id;
        return this;
    }

    public SyncMovie collectedAt(DateTime collectedAt) {
        this.collected_at = collectedAt;
        return this;
    }

    public SyncMovie watchedAt(DateTime watchedAt) {
        this.watched_at = watchedAt;
        return this;
    }

    public SyncMovie ratedAt(DateTime ratedAt) {
        this.rated_at = ratedAt;
        return this;
    }

    public SyncMovie rating(Rating rating) {
        this.rating = rating;
        return this;
    }

}
