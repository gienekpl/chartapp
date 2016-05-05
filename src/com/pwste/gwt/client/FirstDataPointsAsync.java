package com.pwste.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FirstDataPointsAsync {
    void getDataPoints(AsyncCallback<Number[]> callback);
}
