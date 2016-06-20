package me.rhespanhol.rxtraktjava.entities;

import java.util.List;

public class BaseSeason {

    public Integer number;
    public List<BaseEpisode> episodes;

    /** progress */
    public Integer aired;
    /** progress */
    public Integer completed;

}
