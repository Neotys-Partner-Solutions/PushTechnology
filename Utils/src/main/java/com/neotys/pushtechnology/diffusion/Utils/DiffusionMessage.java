package com.neotys.pushtechnology.diffusion.Utils;

/**
 * Created by hrexed on 23/02/18.
 */
public class DiffusionMessage<v>{
    private String Topic;
    private v data;


    public DiffusionMessage(String topic, v data) {
        Topic = topic;
        this.data = data;
    }

    public String getTopic() {
        return Topic;
    }

    public v getData() {
        return data;
    }
}
