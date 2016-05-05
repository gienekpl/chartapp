package com.pwste.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SecondDataPointsAsync {
    void getDataPoints(AsyncCallback<Number[]> callback);
}
