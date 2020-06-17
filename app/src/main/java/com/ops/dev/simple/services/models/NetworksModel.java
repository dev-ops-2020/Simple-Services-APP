package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class NetworksModel implements Serializable {

    private String net;
    private String url;

    public NetworksModel() {

    }

    public NetworksModel(String net, String url) {
        this.net = net;
        this.url = url;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}