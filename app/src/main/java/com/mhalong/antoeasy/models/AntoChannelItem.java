package com.mhalong.antoeasy.models;

/**
 * Created by passa on 10/10/2559.
 */

public class AntoChannelItem {
    private String key;
    private String think;
    private String channel;
    private String name;
    private int value;

    public AntoChannelItem(String name, String think, String channel, int value, String key) {
        this.key = key;
        this.think = think;
        this.channel = channel;
        this.value = value;
        this.name = name;

    }

    public AntoChannelItem() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getThink() {
        return think;
    }

    public void setThink(String think) {
        this.think = think;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
