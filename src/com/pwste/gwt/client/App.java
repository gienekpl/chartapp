package com.pwste.gwt.client;

import java.io.IOException;
import java.util.Date;

import org.moxieapps.gwt.highcharts.client.Axis;
import org.moxieapps.gwt.highcharts.client.AxisTitle;
import org.moxieapps.gwt.highcharts.client.BaseChart;
import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.ChartSubtitle;
import org.moxieapps.gwt.highcharts.client.ChartTitle;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.PlotLine;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.Series.Type;
import org.moxieapps.gwt.highcharts.client.Style;
import org.moxieapps.gwt.highcharts.client.events.ChartClickEvent;
import org.moxieapps.gwt.highcharts.client.events.ChartClickEventHandler;
import org.moxieapps.gwt.highcharts.client.events.PointClickEvent;
import org.moxieapps.gwt.highcharts.client.events.PointClickEventHandler;
import org.moxieapps.gwt.highcharts.client.labels.YAxisLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.ColumnPlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.SeriesPlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.SplinePlotOptions;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class App implements EntryPoint {
    final public static int DOWN_LIMIT = 0;
    final public static int UPPER_LIMIT = 100;
    final public static int POINTS = 10000;

    public static String firstColour;
    public static String secondColour;
    public static String backgroundColour;
    final static String firstDefaultColour = "#7CB5EC";
    final static String secondDefaultColour = "#434348";
    final static String redColour = "#FF0000";
    final static String greenColour = "#00FF00";
    final static String blueColour = "#0000FF";
    final static String whiteColour = "#FFFFFF";
    final static String milkyColour = "#FFFFF0";
    final static String pearlyColour = "#FAFAE7";
    final static String blackColor = "#000000";

    final static Type linearChartType = Series.Type.SPLINE;
    final static Type columnChartType = Series.Type.COLUMN;

    final static String positiveIntRegexString = "^[0-9]([0-9]*)$";
    final static String allIntRegexString = "^-?[0-9]([0-9]*)$";
    // final static String allIntRegexString = "^-?[0-9]\\d*(\\.\\d+)?$";
    final static String emptyRegexString = "^\\s*$";

    int chartIterator = 0;
    final int maxChartQuantity = 10;

    private final AppConstants AppConstants = GWT.create(AppConstants.class);
    protected static FirstDataPointsAsync firstDataPointsService = GWT
	    .create(FirstDataPoints.class);
    protected static SecondDataPointsAsync secondDataPointsService = GWT
	    .create(SecondDataPoints.class);

    @Override
    public void onModuleLoad() {
	final TabPanel appTabPanel = new TabPanel();
	appTabPanel.setAnimationEnabled(true);
	RootPanel.get().add(appTabPanel);

	final Label chartTypeLabel = new Label(AppConstants.SelectChartType());
	chartTypeLabel.addStyleName("chooseChartTypeLabel");
	final RadioButton chartTypeRadioButton1 = new RadioButton(
		"chartTypeRadioButtonsGroup", AppConstants.Linear());
	final RadioButton chartTypeRadioButton2 = new RadioButton(
		"chartTypeRadioButtonsGroup", AppConstants.LinearWithData());
	final RadioButton chartTypeRadioButton3 = new RadioButton(
		"chartTypeRadioButtonsGroup", AppConstants.Column());
	final RadioButton chartTypeRadioButton4 = new RadioButton(
		"chartTypeRadioButtonsGroup", AppConstants.ColumnWithData());
	final RadioButton chartTypeRadioButton5 = new RadioButton(
		"chartTypeRadioButtonsGroup", AppConstants.LinearAndColumn());
	final RadioButton chartTypeRadioButton6 = new RadioButton(
		"chartTypeRadioButtonsGroup",
		AppConstants.LinearAndColumnWithData());
	final RadioButton chartTypeRadioButton7 = new RadioButton(
		"chartTypeRadioButtonsGroup",
		AppConstants.RandomDataInRealTime());
	chartTypeRadioButton1.setValue(true);
	final VerticalPanel chartTypeRadioButtonsVerticalPanel = new VerticalPanel();
	chartTypeRadioButtonsVerticalPanel.add(chartTypeRadioButton1);
	chartTypeRadioButtonsVerticalPanel.add(chartTypeRadioButton2);
	chartTypeRadioButtonsVerticalPanel.add(chartTypeRadioButton3);
	chartTypeRadioButtonsVerticalPanel.add(chartTypeRadioButton4);
	chartTypeRadioButtonsVerticalPanel.add(chartTypeRadioButton5);
	chartTypeRadioButtonsVerticalPanel.add(chartTypeRadioButton6);
	chartTypeRadioButtonsVerticalPanel.add(chartTypeRadioButton7);

	final DisclosurePanel chartPropertiesDisclosurePanel = new DisclosurePanel(
		AppConstants.ChartProperties());
	chartPropertiesDisclosurePanel.setAnimationEnabled(true);
	final VerticalPanel chartPropertiesVerticalPanel = new VerticalPanel();
	final Label chartTextTitleLabel = new Label(AppConstants.ChartTitle()
		+ ":");
	final TextBox chartTextTitleTextBox = new TextBox();
	chartTextTitleTextBox.setText(AppConstants.ChartTitle() + "...");
	chartTextTitleTextBox.setMaxLength(50);
	final Label chartTextSubtitleLabel = new Label(
		AppConstants.ChartSubtitle() + ":");
	final TextBox chartTextSubtitleTextBox = new TextBox();
	chartTextSubtitleTextBox.setText(AppConstants.ChartSubtitle() + "...");
	chartTextSubtitleTextBox.setMaxLength(50);
	final Label numberOfPointsLabel = new Label(
		AppConstants.NumberOfPoints());
	final TextBox numberOfPointsTextBox = new TextBox();
	numberOfPointsTextBox.setValue(Integer.toString(POINTS));
	numberOfPointsTextBox.setMaxLength(10);
	final Label firstNumbersDownLimitLabel = new Label(
		AppConstants.LowerLimitOfRangeOfPoints());
	final TextBox firstNumbersDownLimitTextBox = new TextBox();
	firstNumbersDownLimitTextBox.setText(Integer.toString(DOWN_LIMIT));
	firstNumbersDownLimitTextBox.setMaxLength(10);
	final Label firstNumbersUpperLimitLabel = new Label(
		AppConstants.UpperLimitOfRangeOfPoints());
	final TextBox firstNumbersUpperLimitTextBox = new TextBox();
	firstNumbersUpperLimitTextBox.setText(Integer.toString(UPPER_LIMIT));
	firstNumbersUpperLimitTextBox.setMaxLength(10);
	final Label secondNumbersDownLimitLabel = new Label(
		AppConstants.LowerLimitOfSecondRangeOfPoints());
	final TextBox secondNumbersDownLimitTextBox = new TextBox();
	secondNumbersDownLimitTextBox.setText(Integer.toString(DOWN_LIMIT));
	secondNumbersDownLimitTextBox.setMaxLength(10);
	secondNumbersDownLimitLabel.setVisible(false);
	secondNumbersDownLimitTextBox.setVisible(false);
	final Label secondNumbersUpperLimitLabel = new Label(
		AppConstants.UpperLimitOfSecondRangeOfPoints());
	final TextBox secondNumbersUpperLimitTextBox = new TextBox();
	secondNumbersUpperLimitTextBox.setText(Integer.toString(UPPER_LIMIT));
	secondNumbersUpperLimitTextBox.setMaxLength(10);
	secondNumbersUpperLimitLabel.setVisible(false);
	secondNumbersUpperLimitTextBox.setVisible(false);
	final Label xAxisTitleLabel = new Label(AppConstants.XAxisTitle() + ":");
	final TextBox xAxisTextBox = new TextBox();
	xAxisTextBox.setText(AppConstants.XAxisTitle() + "...");
	xAxisTextBox.setMaxLength(100);
	final Label firstChartColourLabel = new Label(
		AppConstants.DataSeriesColour());
	final RadioButton firstChartColourRadioButton1 = new RadioButton(
		"firstChartColourRadioButtonsGroup", AppConstants.Default());
	final RadioButton firstChartColourRadioButton2 = new RadioButton(
		"firstChartColourRadioButtonsGroup", AppConstants.Red());
	final RadioButton firstChartColourRadioButton3 = new RadioButton(
		"firstChartColourRadioButtonsGroup", AppConstants.Green());
	final RadioButton firstChartColourRadioButton4 = new RadioButton(
		"firstChartColourRadioButtonsGroup", AppConstants.Blue());
	firstChartColourRadioButton1.setValue(true);
	final VerticalPanel firstChartColourVerticalPanel = new VerticalPanel();
	firstChartColourVerticalPanel.add(firstChartColourRadioButton1);
	firstChartColourVerticalPanel.add(firstChartColourRadioButton2);
	firstChartColourVerticalPanel.add(firstChartColourRadioButton3);
	firstChartColourVerticalPanel.add(firstChartColourRadioButton4);
	final Label yAxisTitleLabel = new Label(AppConstants.YAxisTitle() + ":");
	final TextBox yAxisTextBox = new TextBox();
	yAxisTextBox.setMaxLength(100);
	yAxisTextBox.setText(AppConstants.YAxisTitle() + "...");
	final Label anotherYAxisTitleLabel = new Label(
		AppConstants.SecondYAxisTitle() + ":");
	final TextBox anotherYAxisTextBox = new TextBox();
	anotherYAxisTextBox.setText(AppConstants.YAxisTitle() + "(1)...");
	anotherYAxisTextBox.setMaxLength(100);
	anotherYAxisTitleLabel.setVisible(false);
	anotherYAxisTextBox.setVisible(false);
	final Label secondChartColourLabel = new Label(
		AppConstants.SecondDataSeriesColour());
	final RadioButton secondChartColourRadioButton1 = new RadioButton(
		"secondChartColourRadioButtonsGroup", AppConstants.Default());
	final RadioButton secondChartColourRadioButton2 = new RadioButton(
		"secondChartColourRadioButtonsGroup", AppConstants.Red());
	final RadioButton secondChartColourRadioButton3 = new RadioButton(
		"secondChartColourRadioButtonsGroup", AppConstants.Green());
	final RadioButton secondChartColourRadioButton4 = new RadioButton(
		"secondChartColourRadioButtonsGroup", AppConstants.Blue());
	secondChartColourRadioButton1.setValue(true);
	final VerticalPanel secondChartColourVerticalPanel = new VerticalPanel();
	secondChartColourVerticalPanel.add(secondChartColourRadioButton1);
	secondChartColourVerticalPanel.add(secondChartColourRadioButton2);
	secondChartColourVerticalPanel.add(secondChartColourRadioButton3);
	secondChartColourVerticalPanel.add(secondChartColourRadioButton4);
	secondChartColourLabel.setVisible(false);
	secondChartColourVerticalPanel.setVisible(false);
	final Label backgroundChartColourLabel = new Label(
		AppConstants.ChartBackgroundColour());
	final RadioButton backgroundChartColourRadioButton1 = new RadioButton(
		"backgroundChartColourRadioButtonsGroup",
		AppConstants.Default());
	final RadioButton backgroundChartColourRadioButton2 = new RadioButton(
		"backgroundChartColourRadioButtonsGroup", AppConstants.Milky());
	final RadioButton backgroundChartColourRadioButton3 = new RadioButton(
		"backgroundChartColourRadioButtonsGroup", AppConstants.Pearly());
	backgroundChartColourRadioButton1.setValue(true);
	final VerticalPanel backgroundChartColourVerticalPanel = new VerticalPanel();
	backgroundChartColourVerticalPanel
		.add(backgroundChartColourRadioButton1);
	backgroundChartColourVerticalPanel
		.add(backgroundChartColourRadioButton2);
	backgroundChartColourVerticalPanel
		.add(backgroundChartColourRadioButton3);

	chartPropertiesVerticalPanel.add(chartTextTitleLabel);
	chartPropertiesVerticalPanel.add(chartTextTitleTextBox);
	chartPropertiesVerticalPanel.add(chartTextSubtitleLabel);
	chartPropertiesVerticalPanel.add(chartTextSubtitleTextBox);
	chartPropertiesVerticalPanel.add(numberOfPointsLabel);
	chartPropertiesVerticalPanel.add(numberOfPointsTextBox);
	chartPropertiesVerticalPanel.add(firstNumbersDownLimitLabel);
	chartPropertiesVerticalPanel.add(firstNumbersDownLimitTextBox);
	chartPropertiesVerticalPanel.add(firstNumbersUpperLimitLabel);
	chartPropertiesVerticalPanel.add(firstNumbersUpperLimitTextBox);
	chartPropertiesVerticalPanel.add(secondNumbersDownLimitLabel);
	chartPropertiesVerticalPanel.add(secondNumbersDownLimitTextBox);
	chartPropertiesVerticalPanel.add(secondNumbersUpperLimitLabel);
	chartPropertiesVerticalPanel.add(secondNumbersUpperLimitTextBox);
	chartPropertiesVerticalPanel.add(xAxisTitleLabel);
	chartPropertiesVerticalPanel.add(xAxisTextBox);
	chartPropertiesVerticalPanel.add(yAxisTitleLabel);
	chartPropertiesVerticalPanel.add(yAxisTextBox);
	chartPropertiesVerticalPanel.add(anotherYAxisTitleLabel);
	chartPropertiesVerticalPanel.add(anotherYAxisTextBox);
	chartPropertiesVerticalPanel.add(firstChartColourLabel);
	chartPropertiesVerticalPanel.add(firstChartColourVerticalPanel);
	chartPropertiesVerticalPanel.add(secondChartColourLabel);
	chartPropertiesVerticalPanel.add(secondChartColourVerticalPanel);
	chartPropertiesVerticalPanel.add(backgroundChartColourLabel);
	chartPropertiesVerticalPanel.add(backgroundChartColourVerticalPanel);
	chartPropertiesDisclosurePanel.setContent(chartPropertiesVerticalPanel);

	final Button generateChartButton = new Button(
		AppConstants.GenerateChart());
	generateChartButton.addStyleName("generateChartButton");

	final VerticalPanel componentsVerticalPanel = new VerticalPanel();
	componentsVerticalPanel.addStyleName("componentsVerticalPanel");
	componentsVerticalPanel.add(chartTypeLabel);
	componentsVerticalPanel.add(chartTypeRadioButtonsVerticalPanel);
	componentsVerticalPanel.add(chartPropertiesDisclosurePanel);
	componentsVerticalPanel.add(generateChartButton);

	appTabPanel.add(componentsVerticalPanel, AppConstants.ChartsApp());
	final VerticalPanel informationVerticalPanel = new VerticalPanel();
	final HTML infoHTML = new HTML(
		"<h1>ChartsApp</h1>"
			+ "<h3>Kamil Burdzy</h3>"
			+ "<h4>2016</h4>"
			+ "<br/>"
			+ "<h4>W oparciu o</h4>"
			+ "<h3><a href=\"http://www.gwtproject.org/\" target=\"_blank\">Google Web Toolkit</a></h3>"
			+ "<h4>oraz</h4>"
			+ "<h3><a href=\"http://www.moxiegroup.com/moxieapps/gwt-highcharts/\" target=\"_blank\">GWT Highcharts</a>.</h3>");
	final Label selectLanguageLabel = new Label(
		AppConstants.SelectLanguage());
	final Anchor polishLanguageAnchor = new Anchor("Polski",
		GWT.getHostPageBaseURL() + "?locale=pl");
	final Anchor englishLanguageAnchor = new Anchor("English",
		GWT.getHostPageBaseURL() + "?locale=en");
	final Anchor germanLanguageAnchor = new Anchor("Deutsch",
		GWT.getHostPageBaseURL() + "?locale=de");
	final Anchor spanishLanguageAnchor = new Anchor("Español",
		GWT.getHostPageBaseURL() + "?locale=es");
	final Anchor russianLanguageAnchor = new Anchor("Русский",
		GWT.getHostPageBaseURL() + "?locale=ru");
	informationVerticalPanel.add(infoHTML);
	informationVerticalPanel.add(selectLanguageLabel);
	informationVerticalPanel.add(polishLanguageAnchor);
	informationVerticalPanel.add(englishLanguageAnchor);
	informationVerticalPanel.add(germanLanguageAnchor);
	informationVerticalPanel.add(spanishLanguageAnchor);
	informationVerticalPanel.add(russianLanguageAnchor);
	appTabPanel.add(informationVerticalPanel, AppConstants.Information());
	appTabPanel.selectTab(0);

	generateChartButton.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (chartIterator >= maxChartQuantity) {
		    Window.alert(AppConstants.OnlyTenCharts());
		} else {
		    isPositiveIntTextBox(numberOfPointsTextBox);
		    isZeroTextBox(numberOfPointsTextBox);
		    isIntTextBox(firstNumbersDownLimitTextBox);
		    isIntTextBox(firstNumbersUpperLimitTextBox);
		    compareTextBoxValues(firstNumbersDownLimitTextBox,
			    firstNumbersUpperLimitTextBox);

		    final VerticalPanel chartTabVerticalPanel = new VerticalPanel();
		    chartTabVerticalPanel.setStyleName("chartTabVerticalPanel");
		    appTabPanel.add(chartTabVerticalPanel, AppConstants.Chart()
			    + " " + (chartIterator + 1) + ": "
			    + chartTextTitleTextBox.getValue());
		    appTabPanel.selectTab(chartIterator + 2);
		    chartIterator++;

		    Button backToFirstTabButton = new Button("< "
			    + AppConstants.BackToFirstTab());
		    chartTabVerticalPanel.add(backToFirstTabButton);

		    backToFirstTabButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			    appTabPanel.selectTab(0);
			}
		    });

		    if (chartTypeRadioButton1.getValue()) {
			chartTabVerticalPanel.add(showSingleChart(
				chartTextTitleTextBox.getText(),
				chartTextSubtitleTextBox.getText(),
				numberOfPointsTextBox.getValue(),
				xAxisTextBox.getText(), yAxisTextBox.getText(),
				firstNumbersDownLimitTextBox.getValue(),
				firstNumbersUpperLimitTextBox.getValue(),
				linearChartType));
		    } else if (chartTypeRadioButton2.getValue()) {
			try {
			    chartTabVerticalPanel.add(showDataSingleChart(
				    chartTextTitleTextBox.getText(),
				    chartTextSubtitleTextBox.getText(),
				    xAxisTextBox.getText(),
				    yAxisTextBox.getText(), linearChartType));
			} catch (IOException e) {
			    e.printStackTrace();
			    Window.alert(e.toString().trim());
			}
		    } else if (chartTypeRadioButton3.getValue()) {
			chartTabVerticalPanel.add(showSingleChart(
				chartTextTitleTextBox.getText(),
				chartTextSubtitleTextBox.getText(),
				numberOfPointsTextBox.getValue(),
				xAxisTextBox.getText(), yAxisTextBox.getText(),
				firstNumbersDownLimitTextBox.getValue(),
				firstNumbersUpperLimitTextBox.getValue(),
				columnChartType));
		    } else if (chartTypeRadioButton4.getValue()) {
			try {
			    chartTabVerticalPanel.add(showDataSingleChart(
				    chartTextTitleTextBox.getText(),
				    chartTextSubtitleTextBox.getText(),
				    xAxisTextBox.getText(),
				    yAxisTextBox.getText(), columnChartType));
			} catch (IOException e) {
			    e.printStackTrace();
			    Window.alert(e.toString().trim());
			}
		    } else if (chartTypeRadioButton5.getValue()) {
			isIntTextBox(secondNumbersDownLimitTextBox);
			isIntTextBox(secondNumbersUpperLimitTextBox);
			compareTextBoxValues(secondNumbersDownLimitTextBox,
				secondNumbersUpperLimitTextBox);

			chartTabVerticalPanel.add(showDoubleCharts(
				chartTextTitleTextBox.getText(),
				chartTextSubtitleTextBox.getText(),
				numberOfPointsTextBox.getValue(),
				xAxisTextBox.getText(), yAxisTextBox.getText(),
				anotherYAxisTextBox.getText(),
				firstNumbersDownLimitTextBox.getValue(),
				firstNumbersUpperLimitTextBox.getValue(),
				secondNumbersDownLimitTextBox.getValue(),
				secondNumbersUpperLimitTextBox.getValue()));
		    } else if (chartTypeRadioButton6.getValue()) {
			try {
			    chartTabVerticalPanel.add(showDataDoubleCharts(
				    chartTextTitleTextBox.getText(),
				    chartTextSubtitleTextBox.getText(),
				    xAxisTextBox.getText(),
				    yAxisTextBox.getText(),
				    anotherYAxisTextBox.getText()));
			} catch (IOException e) {
			    e.printStackTrace();
			    Window.alert(e.toString().trim());
			}
		    } else if (chartTypeRadioButton7.getValue()) {
			chartTabVerticalPanel.add(showLiveRandomChart(
				chartTextTitleTextBox.getText(),
				chartTextSubtitleTextBox.getText(),
				xAxisTextBox.getText(), yAxisTextBox.getText()));
		    }
		}
	    }
	});

	chartTypeRadioButton1.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (chartTypeRadioButton1.isEnabled()) {
		    numberOfPointsLabel.setVisible(true);
		    numberOfPointsTextBox.setVisible(true);
		    firstNumbersDownLimitLabel.setText(AppConstants
			    .LowerLimitOfRangeOfPoints());
		    firstNumbersDownLimitLabel.setVisible(true);
		    firstNumbersDownLimitTextBox.setText(Integer
			    .toString(DOWN_LIMIT));
		    firstNumbersDownLimitTextBox.setVisible(true);
		    firstNumbersUpperLimitLabel.setText(AppConstants
			    .UpperLimitOfRangeOfPoints());
		    firstNumbersUpperLimitLabel.setVisible(true);
		    firstNumbersUpperLimitTextBox.setText(Integer
			    .toString(UPPER_LIMIT));
		    firstNumbersUpperLimitTextBox.setVisible(true);
		    secondNumbersDownLimitLabel.setVisible(false);
		    secondNumbersDownLimitTextBox.setVisible(false);
		    secondNumbersUpperLimitLabel.setVisible(false);
		    secondNumbersUpperLimitTextBox.setVisible(false);
		    firstChartColourLabel.setText(AppConstants
			    .DataSeriesColour());
		    yAxisTitleLabel.setText(AppConstants.YAxisTitle() + ":");
		    anotherYAxisTitleLabel.setVisible(false);
		    anotherYAxisTextBox.setVisible(false);
		    secondChartColourLabel.setVisible(false);
		    secondChartColourVerticalPanel.setVisible(false);
		}
	    }
	});

	chartTypeRadioButton2.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		numberOfPointsLabel.setVisible(false);
		numberOfPointsTextBox.setVisible(false);
		firstNumbersDownLimitLabel.setVisible(false);
		firstNumbersDownLimitTextBox.setVisible(false);
		firstNumbersUpperLimitLabel.setVisible(false);
		firstNumbersUpperLimitTextBox.setVisible(false);
		secondNumbersDownLimitLabel.setVisible(false);
		secondNumbersDownLimitTextBox.setVisible(false);
		secondNumbersUpperLimitLabel.setVisible(false);
		secondNumbersUpperLimitTextBox.setVisible(false);
		firstChartColourLabel.setText(AppConstants.DataSeriesColour());
		yAxisTitleLabel.setText(AppConstants.YAxisTitle() + ":");
		anotherYAxisTitleLabel.setVisible(false);
		anotherYAxisTextBox.setVisible(false);
		secondChartColourLabel.setVisible(false);
		secondChartColourVerticalPanel.setVisible(false);
	    }
	});

	chartTypeRadioButton3.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (chartTypeRadioButton3.isEnabled()) {
		    numberOfPointsLabel.setVisible(true);
		    numberOfPointsTextBox.setVisible(true);
		    firstNumbersDownLimitLabel.setText(AppConstants
			    .LowerLimitOfRangeOfPoints());
		    firstNumbersDownLimitLabel.setVisible(true);
		    firstNumbersDownLimitTextBox.setText(Integer
			    .toString(DOWN_LIMIT));
		    firstNumbersDownLimitTextBox.setVisible(true);
		    firstNumbersUpperLimitLabel.setText(AppConstants
			    .UpperLimitOfRangeOfPoints());
		    firstNumbersUpperLimitLabel.setVisible(true);
		    firstNumbersUpperLimitTextBox.setText(Integer
			    .toString(UPPER_LIMIT));
		    firstNumbersUpperLimitTextBox.setVisible(true);
		    secondNumbersDownLimitLabel.setVisible(false);
		    secondNumbersDownLimitTextBox.setVisible(false);
		    secondNumbersUpperLimitLabel.setVisible(false);
		    secondNumbersUpperLimitTextBox.setVisible(false);
		    firstChartColourLabel.setText(AppConstants
			    .DataSeriesColour());
		    yAxisTitleLabel.setText(AppConstants.YAxisTitle() + ":");
		    anotherYAxisTitleLabel.setVisible(false);
		    anotherYAxisTextBox.setVisible(false);
		    secondChartColourLabel.setVisible(false);
		    secondChartColourVerticalPanel.setVisible(false);
		}
	    }
	});

	chartTypeRadioButton4.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		numberOfPointsLabel.setVisible(false);
		numberOfPointsTextBox.setVisible(false);
		firstNumbersDownLimitLabel.setVisible(false);
		firstNumbersDownLimitTextBox.setVisible(false);
		firstNumbersUpperLimitLabel.setVisible(false);
		firstNumbersUpperLimitTextBox.setVisible(false);
		secondNumbersDownLimitLabel.setVisible(false);
		secondNumbersDownLimitTextBox.setVisible(false);
		secondNumbersUpperLimitLabel.setVisible(false);
		secondNumbersUpperLimitTextBox.setVisible(false);
		firstChartColourLabel.setText(AppConstants.DataSeriesColour());
		yAxisTitleLabel.setText(AppConstants.YAxisTitle() + ":");
		anotherYAxisTitleLabel.setVisible(false);
		anotherYAxisTextBox.setVisible(false);
		secondChartColourLabel.setVisible(false);
		secondChartColourVerticalPanel.setVisible(false);
	    }
	});

	chartTypeRadioButton5.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (chartTypeRadioButton5.isEnabled()) {
		    numberOfPointsLabel.setVisible(true);
		    numberOfPointsTextBox.setVisible(true);
		    firstNumbersDownLimitLabel.setText(AppConstants
			    .LowerLimitOfFirstRangeOfPoints());
		    firstNumbersDownLimitLabel.setVisible(true);
		    firstNumbersDownLimitTextBox.setText(Integer
			    .toString(DOWN_LIMIT));
		    firstNumbersDownLimitTextBox.setVisible(true);
		    firstNumbersUpperLimitLabel.setText(AppConstants
			    .UpperLimitOfFirstRangeOfPoints());
		    firstNumbersUpperLimitLabel.setVisible(true);
		    firstNumbersUpperLimitTextBox.setText(Integer
			    .toString(UPPER_LIMIT));
		    firstNumbersUpperLimitTextBox.setVisible(true);
		    secondNumbersDownLimitLabel.setText(AppConstants
			    .LowerLimitOfSecondRangeOfPoints());
		    secondNumbersDownLimitLabel.setVisible(true);
		    secondNumbersDownLimitTextBox.setText(Integer
			    .toString(DOWN_LIMIT));
		    secondNumbersDownLimitTextBox.setVisible(true);
		    secondNumbersUpperLimitLabel.setText(AppConstants
			    .UpperLimitOfSecondRangeOfPoints());
		    secondNumbersUpperLimitLabel.setVisible(true);
		    secondNumbersUpperLimitTextBox.setText(Integer
			    .toString(UPPER_LIMIT));
		    secondNumbersUpperLimitTextBox.setVisible(true);
		    firstChartColourLabel.setText(AppConstants
			    .FirstDataSeriesColour());
		    yAxisTitleLabel.setText(AppConstants.FirstYAxisTitle()
			    + ":");
		    anotherYAxisTitleLabel.setVisible(true);
		    anotherYAxisTextBox.setVisible(true);
		    secondChartColourLabel.setVisible(true);
		    secondChartColourVerticalPanel.setVisible(true);
		}
	    }
	});

	chartTypeRadioButton6.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		numberOfPointsLabel.setVisible(false);
		numberOfPointsTextBox.setVisible(false);
		firstNumbersDownLimitLabel.setVisible(false);
		firstNumbersDownLimitTextBox.setVisible(false);
		firstNumbersUpperLimitLabel.setVisible(false);
		firstNumbersUpperLimitTextBox.setVisible(false);
		secondNumbersDownLimitLabel.setVisible(false);
		secondNumbersDownLimitTextBox.setVisible(false);
		secondNumbersUpperLimitLabel.setVisible(false);
		secondNumbersUpperLimitTextBox.setVisible(false);
		firstChartColourLabel.setText(AppConstants
			.FirstDataSeriesColour());
		yAxisTitleLabel.setText(AppConstants.FirstYAxisTitle() + ":");
		anotherYAxisTitleLabel.setVisible(true);
		anotherYAxisTextBox.setVisible(true);
		secondChartColourLabel.setVisible(true);
		secondChartColourVerticalPanel.setVisible(true);
	    }
	});

	chartTypeRadioButton7.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (chartTypeRadioButton7.isEnabled()) {
		    numberOfPointsLabel.setVisible(false);
		    numberOfPointsTextBox.setVisible(false);
		    firstNumbersDownLimitLabel.setVisible(false);
		    firstNumbersDownLimitTextBox.setVisible(false);
		    firstNumbersUpperLimitLabel.setVisible(false);
		    firstNumbersUpperLimitTextBox.setVisible(false);
		    secondNumbersDownLimitLabel.setVisible(false);
		    secondNumbersDownLimitTextBox.setVisible(false);
		    secondNumbersUpperLimitLabel.setVisible(false);
		    secondNumbersUpperLimitTextBox.setVisible(false);
		    firstChartColourLabel.setText(AppConstants
			    .DataSeriesColour());
		    yAxisTitleLabel.setText(AppConstants.YAxisTitle() + ":");
		    anotherYAxisTitleLabel.setVisible(false);
		    anotherYAxisTextBox.setVisible(false);
		    secondChartColourLabel.setVisible(false);
		    secondChartColourVerticalPanel.setVisible(false);
		}
	    }
	});

	xAxisTextBox.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		xAxisTextBox.setText("");
	    }
	});

	yAxisTextBox.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		yAxisTextBox.setText("");
	    }
	});

	chartTextTitleTextBox.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		chartTextTitleTextBox.setText("");
	    }
	});

	chartTextSubtitleTextBox.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		chartTextSubtitleTextBox.setText("");
	    }
	});

	firstChartColourRadioButton1.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (firstChartColourRadioButton1.getValue()) {
		    secondChartColourRadioButton1.setVisible(true);
		    secondChartColourRadioButton1.setEnabled(true);
		    secondChartColourRadioButton1.setValue(true);
		    secondChartColourRadioButton2.setVisible(true);
		    secondChartColourRadioButton2.setEnabled(true);
		    secondChartColourRadioButton3.setVisible(true);
		    secondChartColourRadioButton3.setEnabled(true);
		    secondChartColourRadioButton4.setVisible(true);
		    secondChartColourRadioButton4.setEnabled(true);
		    firstColour = firstDefaultColour;
		    secondColour = secondDefaultColour;
		}
	    }
	});

	firstChartColourRadioButton2.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (firstChartColourRadioButton2.getValue()) {
		    secondChartColourRadioButton1.setVisible(true);
		    secondChartColourRadioButton1.setEnabled(true);
		    secondChartColourRadioButton2.setVisible(true);
		    secondChartColourRadioButton2.setEnabled(false);
		    secondChartColourRadioButton3.setVisible(true);
		    secondChartColourRadioButton3.setEnabled(true);
		    secondChartColourRadioButton3.setValue(true);
		    secondChartColourRadioButton4.setVisible(true);
		    secondChartColourRadioButton4.setEnabled(true);
		    firstColour = redColour;
		    secondColour = greenColour;
		}
	    }
	});

	firstChartColourRadioButton3.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (firstChartColourRadioButton3.getValue()) {
		    secondChartColourRadioButton1.setVisible(true);
		    secondChartColourRadioButton1.setEnabled(true);
		    secondChartColourRadioButton2.setVisible(true);
		    secondChartColourRadioButton2.setEnabled(true);
		    secondChartColourRadioButton3.setVisible(true);
		    secondChartColourRadioButton3.setEnabled(false);
		    secondChartColourRadioButton4.setVisible(true);
		    secondChartColourRadioButton4.setEnabled(true);
		    secondChartColourRadioButton4.setValue(true);
		    firstColour = greenColour;
		    secondColour = blueColour;
		}
	    }
	});

	firstChartColourRadioButton4.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (firstChartColourRadioButton4.getValue()) {
		    secondChartColourRadioButton1.setVisible(true);
		    secondChartColourRadioButton1.setEnabled(true);
		    secondChartColourRadioButton2.setVisible(true);
		    secondChartColourRadioButton2.setEnabled(true);
		    secondChartColourRadioButton2.setValue(true);
		    secondChartColourRadioButton3.setVisible(true);
		    secondChartColourRadioButton3.setEnabled(true);
		    secondChartColourRadioButton4.setVisible(true);
		    secondChartColourRadioButton4.setEnabled(false);
		    firstColour = blueColour;
		    secondColour = redColour;
		}
	    }
	});

	secondChartColourRadioButton1.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (secondChartColourRadioButton1.getValue()) {
		    firstChartColourRadioButton1.setVisible(true);
		    firstChartColourRadioButton1.setEnabled(true);
		    firstChartColourRadioButton1.setValue(true);
		    firstChartColourRadioButton2.setVisible(true);
		    firstChartColourRadioButton2.setEnabled(true);
		    firstChartColourRadioButton3.setVisible(true);
		    firstChartColourRadioButton3.setEnabled(true);
		    firstChartColourRadioButton4.setVisible(true);
		    firstChartColourRadioButton4.setEnabled(true);
		    secondColour = secondDefaultColour;
		    firstColour = firstDefaultColour;
		    secondChartColourRadioButton1.setEnabled(true);
		    secondChartColourRadioButton2.setEnabled(true);
		    secondChartColourRadioButton3.setEnabled(true);
		    secondChartColourRadioButton4.setEnabled(true);
		}
	    }
	});

	secondChartColourRadioButton2.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (secondChartColourRadioButton2.getValue()) {
		    firstChartColourRadioButton1.setVisible(true);
		    firstChartColourRadioButton1.setEnabled(true);
		    firstChartColourRadioButton2.setVisible(true);
		    firstChartColourRadioButton2.setEnabled(false);
		    firstChartColourRadioButton3.setVisible(true);
		    firstChartColourRadioButton3.setEnabled(true);
		    firstChartColourRadioButton3.setValue(true);
		    firstChartColourRadioButton4.setVisible(true);
		    firstChartColourRadioButton4.setEnabled(true);
		    secondColour = redColour;
		    firstColour = greenColour;
		    secondChartColourRadioButton1.setEnabled(true);
		    secondChartColourRadioButton2.setEnabled(true);
		    secondChartColourRadioButton3.setEnabled(true);
		    secondChartColourRadioButton4.setEnabled(true);
		}
	    }
	});

	secondChartColourRadioButton3.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (secondChartColourRadioButton3.getValue()) {
		    firstChartColourRadioButton1.setVisible(true);
		    firstChartColourRadioButton1.setEnabled(true);
		    firstChartColourRadioButton2.setVisible(true);
		    firstChartColourRadioButton2.setEnabled(true);
		    firstChartColourRadioButton3.setVisible(true);
		    firstChartColourRadioButton3.setEnabled(false);
		    firstChartColourRadioButton4.setVisible(true);
		    firstChartColourRadioButton4.setEnabled(true);
		    firstChartColourRadioButton4.setValue(true);
		    secondColour = greenColour;
		    firstColour = blueColour;
		    secondChartColourRadioButton1.setEnabled(true);
		    secondChartColourRadioButton2.setEnabled(true);
		    secondChartColourRadioButton3.setEnabled(true);
		    secondChartColourRadioButton4.setEnabled(true);
		}
	    }
	});

	secondChartColourRadioButton4.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (secondChartColourRadioButton4.getValue()) {
		    firstChartColourRadioButton1.setVisible(true);
		    firstChartColourRadioButton1.setEnabled(true);
		    firstChartColourRadioButton2.setVisible(true);
		    firstChartColourRadioButton2.setEnabled(true);
		    firstChartColourRadioButton2.setValue(true);
		    firstChartColourRadioButton3.setVisible(true);
		    firstChartColourRadioButton3.setEnabled(true);
		    firstChartColourRadioButton4.setVisible(true);
		    firstChartColourRadioButton4.setEnabled(false);
		    secondColour = blueColour;
		    firstColour = redColour;
		    secondChartColourRadioButton1.setEnabled(true);
		    secondChartColourRadioButton2.setEnabled(true);
		    secondChartColourRadioButton3.setEnabled(true);
		    secondChartColourRadioButton4.setEnabled(true);
		}
	    }
	});

	backgroundChartColourRadioButton1.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (backgroundChartColourRadioButton1.getValue()) {
		    backgroundChartColourRadioButton1.setEnabled(true);
		    backgroundColour = whiteColour;
		}
	    }
	});

	backgroundChartColourRadioButton2.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (backgroundChartColourRadioButton2.getValue()) {
		    backgroundChartColourRadioButton2.setEnabled(true);
		    backgroundColour = milkyColour;
		}
	    }
	});

	backgroundChartColourRadioButton3.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (backgroundChartColourRadioButton3.getValue()) {
		    backgroundChartColourRadioButton3.setEnabled(true);
		    backgroundColour = pearlyColour;
		}
	    }
	});
    }

    final public Chart showSingleChart(String chartTitle, String chartSubtitle,
	    String numberOfPoints, String xAxisText, String yAxisText,
	    String pointsDownLimit, String pointsUpperLimit, Type chartType) {
	final Chart singleChart = new Chart();
	singleChart.setType(chartType);
	final ChartTitle columnChartTitle = new ChartTitle();
	columnChartTitle.setText(chartTitle);
	singleChart.setChartTitle(columnChartTitle);
	final ChartSubtitle columnChartSubtitle = new ChartSubtitle();
	columnChartSubtitle.setText(chartSubtitle);
	singleChart.setChartSubtitle(columnChartSubtitle);
	singleChart.setZoomType(BaseChart.ZoomType.X_AND_Y);
	final AxisTitle xTextAxisTitle = new AxisTitle();
	xTextAxisTitle.setText(xAxisText);
	singleChart.getXAxis().setAxisTitle(xTextAxisTitle);
	final AxisTitle yTextAxisTitle = new AxisTitle();
	yTextAxisTitle.setText(yAxisText);
	singleChart.getYAxis().setAxisTitle(yTextAxisTitle);
	singleChart.setBackgroundColor(backgroundColour);
	singleChart.setAnimation(true);

	final Series singleSeries = singleChart.createSeries();
	singleSeries.setPoints(GeneratePoints.randomPointsGenerator(
		Integer.parseInt(numberOfPoints),
		Integer.parseInt(pointsDownLimit),
		Integer.parseInt(pointsUpperLimit)));
	final SeriesPlotOptions seriesSeriesPlotOptions = new SeriesPlotOptions();
	seriesSeriesPlotOptions.setColor(firstColour);
	singleSeries.setPlotOptions(seriesSeriesPlotOptions);
	singleChart.addSeries(singleSeries);

	final SeriesPlotOptions deleteSinglePointSeriesPlotOptions = new SeriesPlotOptions();
	deleteSinglePointSeriesPlotOptions.setLineWidth(1);
	final PointClickEventHandler deletePointClickEventHandler = new PointClickEventHandler() {
	    @Override
	    public boolean onClick(PointClickEvent pointClickEvent) {
		final Series currentSeries = singleChart
			.getSeries(pointClickEvent.getSeriesId());

		if (currentSeries.getPoints().length > 1) {
		    pointClickEvent.getPoint().remove();
		} else if (currentSeries.getPoints().length == 1) {
		    Window.alert(AppConstants.DeleteLastPointAlert());
		}

		return true;
	    }
	};
	deleteSinglePointSeriesPlotOptions
		.setPointClickEventHandler(deletePointClickEventHandler);
	singleChart.setSeriesPlotOptions(deleteSinglePointSeriesPlotOptions);

	final ChartClickEventHandler addSinglePointChartClickEventHandler = new ChartClickEventHandler() {
	    @Override
	    public boolean onClick(ChartClickEvent chartClickEvent) {
		singleSeries.addPoint(chartClickEvent.getXAxisValue(),
			chartClickEvent.getYAxisValue());

		return true;
	    }
	};
	singleChart.setClickEventHandler(addSinglePointChartClickEventHandler);

	return singleChart;
    }

    final public Chart showDataSingleChart(String chartTitle,
	    String chartSubtitle, String xAxisText, String yAxisText,
	    Type chartType) throws IOException {
	final Chart dataSingleChart = new Chart();
	dataSingleChart.setType(chartType);
	final ChartTitle columnChartTitle = new ChartTitle();
	columnChartTitle.setText(chartTitle);
	dataSingleChart.setChartTitle(columnChartTitle);
	final ChartSubtitle columnChartSubtitle = new ChartSubtitle();
	columnChartSubtitle.setText(chartSubtitle);
	dataSingleChart.setChartSubtitle(columnChartSubtitle);
	dataSingleChart.setZoomType(BaseChart.ZoomType.X_AND_Y);
	final AxisTitle xTextAxisTitle = new AxisTitle();
	xTextAxisTitle.setText(xAxisText);
	dataSingleChart.getXAxis().setAxisTitle(xTextAxisTitle);
	final AxisTitle yTextAxisTitle = new AxisTitle();
	yTextAxisTitle.setText(yAxisText);
	dataSingleChart.getYAxis().setAxisTitle(yTextAxisTitle);
	dataSingleChart.setBackgroundColor(backgroundColour);
	dataSingleChart.setAnimation(true);

	final Series singleSeries = dataSingleChart.createSeries();
	firstDataPointsService.getDataPoints(new AsyncCallback<Number[]>() {
	    @Override
	    public void onFailure(Throwable caught) {
		Window.alert(caught.toString().trim());
	    }

	    @Override
	    public void onSuccess(Number[] result) {
		singleSeries.setPoints(result);
	    }
	});
	final SeriesPlotOptions seriesSeriesPlotOptions = new SeriesPlotOptions();
	seriesSeriesPlotOptions.setColor(firstColour);
	singleSeries.setPlotOptions(seriesSeriesPlotOptions);
	dataSingleChart.addSeries(singleSeries);

	final SeriesPlotOptions deleteSinglePointSeriesPlotOptions = new SeriesPlotOptions();
	deleteSinglePointSeriesPlotOptions.setLineWidth(1);
	final PointClickEventHandler deletePointClickEventHandler = new PointClickEventHandler() {
	    @Override
	    public boolean onClick(PointClickEvent pointClickEvent) {
		final Series currentSeries = dataSingleChart
			.getSeries(pointClickEvent.getSeriesId());

		if (currentSeries.getPoints().length > 1) {
		    pointClickEvent.getPoint().remove();
		} else if (currentSeries.getPoints().length == 1) {
		    Window.alert(AppConstants.DeleteLastPointAlert());
		}

		return true;
	    }
	};
	deleteSinglePointSeriesPlotOptions
		.setPointClickEventHandler(deletePointClickEventHandler);
	dataSingleChart
		.setSeriesPlotOptions(deleteSinglePointSeriesPlotOptions);

	final ChartClickEventHandler addSinglePointChartClickEventHandler = new ChartClickEventHandler() {
	    @Override
	    public boolean onClick(ChartClickEvent chartClickEvent) {
		singleSeries.addPoint(chartClickEvent.getXAxisValue(),
			chartClickEvent.getYAxisValue());

		return true;
	    }
	};
	dataSingleChart
		.setClickEventHandler(addSinglePointChartClickEventHandler);

	return dataSingleChart;
    }

    public Chart showDoubleCharts(String chartTitle, String chartSubtitle,
	    String numberOfPoints, String xAxisText, String firstYAxisText,
	    String secondYAxisText, String pointsDownLimitFirst,
	    String pointsUpperLimitFirst, String pointsDownLimitSecond,
	    String pointsUpperLimitSecond) {
	final Chart doubleChart = new Chart();
	final ChartTitle doubleChartTitle = new ChartTitle();
	doubleChartTitle.setText(chartTitle);
	doubleChart.setChartTitle(doubleChartTitle);
	final ChartSubtitle doubleChartSubtitle = new ChartSubtitle();
	doubleChartSubtitle.setText(chartSubtitle);
	doubleChart.setChartSubtitle(doubleChartSubtitle);
	doubleChart.setZoomType(BaseChart.ZoomType.X_AND_Y);
	final Legend doubleLegend = new Legend();
	doubleLegend.setLayout(Legend.Layout.VERTICAL);
	doubleLegend.setAlign(Legend.Align.LEFT);
	doubleLegend.setVerticalAlign(Legend.VerticalAlign.TOP);
	doubleLegend.setX(70);
	doubleLegend.setY(45);
	doubleLegend.setFloating(true);
	doubleLegend.setBackgroundColor(whiteColour);
	doubleLegend.setBorderColor(firstDefaultColour);
	doubleLegend.setBorderWidth(1);
	doubleChart.setLegend(doubleLegend);
	final AxisTitle xTextAxisTitle = new AxisTitle();
	xTextAxisTitle.setText(xAxisText);
	doubleChart.getXAxis().setAxisTitle(xTextAxisTitle);
	final AxisTitle y0AxisTitle = new AxisTitle();
	final YAxisLabels y0AxisLabels = new YAxisLabels();
	final Style y0Style = new Style();
	y0Style.setColor(firstColour);
	y0AxisLabels.setStyle(y0Style);
	y0AxisTitle.setText(firstYAxisText);
	doubleChart.getYAxis(0).setLabels(y0AxisLabels);
	doubleChart.getYAxis(0).setAxisTitle(y0AxisTitle);
	final AxisTitle y1AxisTitle = new AxisTitle();
	final YAxisLabels y1AxisLabels = new YAxisLabels();
	final Style y1Style = new Style();
	y1Style.setColor(secondColour);
	y1AxisLabels.setStyle(y1Style);
	y1AxisTitle.setText(secondYAxisText);
	doubleChart.getYAxis(1).setLabels(y1AxisLabels);
	doubleChart.getYAxis(1).setAxisTitle(y1AxisTitle);
	doubleChart.getYAxis(1).setOpposite(true);
	doubleChart.setBackgroundColor(backgroundColour);

	final Series y0Series = doubleChart.createSeries();
	y0Series.setPoints(GeneratePoints.randomPointsGenerator(
		Integer.parseInt(numberOfPoints),
		Integer.parseInt(pointsDownLimitFirst),
		Integer.parseInt(pointsUpperLimitFirst)));
	final ColumnPlotOptions y0ColumnPlotOptions = new ColumnPlotOptions();
	y0ColumnPlotOptions.setColor(firstColour);
	y0Series.setPlotOptions(y0ColumnPlotOptions);
	y0Series.setType(columnChartType);
	y0Series.setYAxis(1);
	doubleChart.addSeries(y0Series);

	final Series y1Series = doubleChart.createSeries();
	y1Series.setPoints(GeneratePoints.randomPointsGenerator(
		Integer.parseInt(numberOfPoints),
		Integer.parseInt(pointsDownLimitSecond),
		Integer.parseInt(pointsUpperLimitSecond)));
	final SplinePlotOptions y1SplinePlotOptions = new SplinePlotOptions();
	y1SplinePlotOptions.setColor(secondColour);
	y1Series.setPlotOptions(y1SplinePlotOptions);
	y1Series.setType(linearChartType);
	doubleChart.addSeries(y1Series);

	return doubleChart;
    }

    public Chart showDataDoubleCharts(String chartTitle, String chartSubtitle,
	    String xAxisText, String firstYAxisText, String secondYAxisText)
	    throws IOException {
	final Chart dataDoubleChart = new Chart();
	final ChartTitle doubleChartTitle = new ChartTitle();
	doubleChartTitle.setText(chartTitle);
	dataDoubleChart.setChartTitle(doubleChartTitle);
	final ChartSubtitle doubleChartSubtitle = new ChartSubtitle();
	doubleChartSubtitle.setText(chartSubtitle);
	dataDoubleChart.setChartSubtitle(doubleChartSubtitle);
	dataDoubleChart.setZoomType(BaseChart.ZoomType.X_AND_Y);
	final Legend doubleLegend = new Legend();
	doubleLegend.setLayout(Legend.Layout.VERTICAL);
	doubleLegend.setAlign(Legend.Align.LEFT);
	doubleLegend.setVerticalAlign(Legend.VerticalAlign.TOP);
	doubleLegend.setX(70);
	doubleLegend.setY(45);
	doubleLegend.setFloating(true);
	doubleLegend.setBackgroundColor(whiteColour);
	doubleLegend.setBorderColor(firstDefaultColour);
	doubleLegend.setBorderWidth(1);
	dataDoubleChart.setLegend(doubleLegend);
	final AxisTitle xTextAxisTitle = new AxisTitle();
	xTextAxisTitle.setText(xAxisText);
	dataDoubleChart.getXAxis().setAxisTitle(xTextAxisTitle);
	final AxisTitle y0AxisTitle = new AxisTitle();
	final YAxisLabels y0AxisLabels = new YAxisLabels();
	final Style y0Style = new Style();
	y0Style.setColor(firstColour);
	y0AxisLabels.setStyle(y0Style);
	y0AxisTitle.setText(firstYAxisText);
	dataDoubleChart.getYAxis(0).setLabels(y0AxisLabels);
	dataDoubleChart.getYAxis(0).setAxisTitle(y0AxisTitle);
	final AxisTitle y1AxisTitle = new AxisTitle();
	final YAxisLabels y1AxisLabels = new YAxisLabels();
	final Style y1Style = new Style();
	y1Style.setColor(secondColour);
	y1AxisLabels.setStyle(y1Style);
	y1AxisTitle.setText(secondYAxisText);
	dataDoubleChart.getYAxis(1).setLabels(y1AxisLabels);
	dataDoubleChart.getYAxis(1).setAxisTitle(y1AxisTitle);
	dataDoubleChart.getYAxis(1).setOpposite(true);
	dataDoubleChart.setBackgroundColor(backgroundColour);

	final Series y0Series = dataDoubleChart.createSeries();
	firstDataPointsService.getDataPoints(new AsyncCallback<Number[]>() {
	    @Override
	    public void onFailure(Throwable caught) {
		Window.alert(caught.toString().trim());
	    }

	    @Override
	    public void onSuccess(Number[] result) {
		y0Series.setPoints(result);
	    }
	});
	final ColumnPlotOptions y0ColumnPlotOptions = new ColumnPlotOptions();
	y0ColumnPlotOptions.setColor(firstColour);
	y0Series.setPlotOptions(y0ColumnPlotOptions);
	y0Series.setType(columnChartType);
	y0Series.setYAxis(1);
	dataDoubleChart.addSeries(y0Series);

	final Series y1Series = dataDoubleChart.createSeries();
	secondDataPointsService.getDataPoints(new AsyncCallback<Number[]>() {
	    @Override
	    public void onFailure(Throwable caught) {
		Window.alert(caught.toString().trim());
	    }

	    @Override
	    public void onSuccess(Number[] result) {
		y1Series.setPoints(result);
	    }
	});
	final SplinePlotOptions y1SplinePlotOptions = new SplinePlotOptions();
	y1SplinePlotOptions.setColor(secondColour);
	y1Series.setPlotOptions(y1SplinePlotOptions);
	y1Series.setType(linearChartType);
	dataDoubleChart.addSeries(y1Series);

	return dataDoubleChart;
    }

    public Chart showLiveRandomChart(String chartTitle, String chartSubtitle,
	    String xAxisText, String yAxisText) {
	final Chart liveRandomChart = new Chart();
	final ChartTitle liveRandomChartTitle = new ChartTitle();
	liveRandomChartTitle.setText(chartTitle);
	liveRandomChart.setChartTitle(liveRandomChartTitle);
	final ChartSubtitle liveRandomChartSubitle = new ChartSubtitle();
	liveRandomChartSubitle.setText(chartSubtitle);
	liveRandomChart.setChartSubtitle(liveRandomChartSubitle);
	liveRandomChart.setType(linearChartType);

	final AxisTitle liveRandomXAxisTitle = new AxisTitle();
	liveRandomXAxisTitle.setText(xAxisText);
	liveRandomChart.getXAxis().setTickPixelInterval(75);
	liveRandomChart.getXAxis().setType(Axis.Type.DATE_TIME);
	liveRandomChart.getXAxis().setAxisTitle(liveRandomXAxisTitle);

	final PlotLine liveRandomPlotLine = liveRandomChart.getYAxis()
		.createPlotLine();
	liveRandomPlotLine.setValue(0);
	liveRandomPlotLine.setWidth(1);
	liveRandomPlotLine.setColor(firstColour);
	liveRandomChart.getYAxis().setPlotLines(liveRandomPlotLine);
	final AxisTitle liveRandomYAxisTitle = new AxisTitle();
	liveRandomYAxisTitle.setText(yAxisText);
	liveRandomChart.getYAxis().setAxisTitle(liveRandomYAxisTitle);
	liveRandomChart.setBackgroundColor(backgroundColour);

	final Series liveRandomSeries = liveRandomChart.createSeries();
	liveRandomChart.addSeries(liveRandomSeries);

	for (int i = -25; i < 0; i++) {
	    liveRandomSeries.addPoint(new Date().getTime() + i * 1000,
		    Random.nextInt());
	}

	final SplinePlotOptions liveRandomSplinePlotOptions = new SplinePlotOptions();
	liveRandomSplinePlotOptions.setColor(firstColour);
	liveRandomSeries.setPlotOptions(liveRandomSplinePlotOptions);

	final Timer liveRandomChartTimer = new Timer() {
	    @Override
	    public void run() {
		liveRandomSeries.addPoint(new Date().getTime(),
			Random.nextInt(), true, true, true);
	    }
	};
	liveRandomChartTimer.scheduleRepeating(1000);

	return liveRandomChart;
    }

    public void isPositiveIntTextBox(TextBox currentTextBox) {
	String inputString = currentTextBox.getText();

	if (!inputString.matches(positiveIntRegexString)) {
	    Window.alert("'" + inputString + "' "
		    + AppConstants.InvalidSymbol());
	    Window.Location.reload();
	}
    }

    public void isIntTextBox(TextBox currentTextBox) {
	String inputString = currentTextBox.getText();

	if (!inputString.matches(allIntRegexString)) {
	    Window.alert("'" + inputString + "' "
		    + AppConstants.InvalidSymbol());
	    Window.Location.reload();
	}
    }

    public void isZeroTextBox(TextBox currentTextBox) {
	if (Integer.parseInt(currentTextBox.getValue()) <= 0) {
	    Window.alert("'" + currentTextBox.getValue() + "' "
		    + AppConstants.InvalidSymbol());
	    Window.Location.reload();
	}
    }

    public void compareTextBoxValues(TextBox firstTextBox, TextBox secondTextBox) {
	if (firstTextBox.getValue().equals(secondTextBox.getValue())
		|| (Integer.parseInt(firstTextBox.getValue()) > Integer
			.parseInt(secondTextBox.getValue()))) {
	    Window.alert("'" + firstTextBox.getValue() + "' "
		    + AppConstants.InvalidSymbol());
	    Window.Location.reload();
	}
    }
}
