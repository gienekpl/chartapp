package com.pwste.gwt.client;

import java.io.IOException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("firstDataPoints")
public interface FirstDataPoints extends RemoteService {
    Number[] getDataPoints() throws IOException;
}
