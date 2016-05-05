package com.pwste.gwt.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pwste.gwt.client.FirstDataPoints;

public class FirstDataPointsImpl extends RemoteServiceServlet implements
	FirstDataPoints {
    private static final long serialVersionUID = 1L;

    @Override
    public Number[] getDataPoints() throws IOException {
	File dataFile = new File("firstDataPoints.txt");
	FileReader dataFileReader = new FileReader(dataFile);
	BufferedReader dataBufferedReader = new BufferedReader(dataFileReader);
	Number[] arrayNumber = new Number[100];
	String dataString = dataBufferedReader.readLine();

	for (int i = 0; i < arrayNumber.length; i++) {
	    arrayNumber[i] = Integer.parseInt(dataString);
	    dataString = dataBufferedReader.readLine();
	}
	dataBufferedReader.close();

	return arrayNumber;
    }
}
