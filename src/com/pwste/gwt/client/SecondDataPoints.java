package com.pwste.gwt.client;

import java.io.IOException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("secondDataPoints")
public interface SecondDataPoints extends RemoteService {
    Number[] getDataPoints() throws IOException;
}
