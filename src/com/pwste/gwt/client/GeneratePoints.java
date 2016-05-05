package com.pwste.gwt.client;

import com.google.gwt.user.client.Random;

public class GeneratePoints extends App {
    public static Number[] randomPointsGenerator(int pointsQuantity,
	    int downLimit, int upperLimit) {
	final int minimumValue;
	final int maximumValue;
	final int numberOfPoints;

	if (downLimit != DOWN_LIMIT) {
	    minimumValue = downLimit;
	} else {
	    minimumValue = DOWN_LIMIT;
	}

	if (upperLimit != UPPER_LIMIT) {
	    maximumValue = upperLimit;
	} else {
	    maximumValue = UPPER_LIMIT;
	}

	if (pointsQuantity != POINTS) {
	    numberOfPoints = pointsQuantity;
	} else {
	    numberOfPoints = POINTS;
	}

	final Number[] numbersData = new Number[numberOfPoints];

	for (int i = 0; i < numberOfPoints; i++) {
	    numbersData[i] = Random.nextInt(maximumValue - minimumValue)
		    + minimumValue;
	}

	return numbersData;
    }
}
