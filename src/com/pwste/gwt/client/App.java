package com.pwste.gwt.client;

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
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class App implements EntryPoint {
    public static int DOWN_LIMIT = 0;
    public static int UPPER_LIMIT = 100;
    public static int POINTS = 10000;
    public String firstColour;
    public String secondColour;
    public String backgroundColour;
    final String firstDefaultColour = "#7CB5EC";
    final String secondDefaultColour = "#434348";
    final String redColour = "#FF0000";
    final String greenColour = "#00FF00";
    final String blueColour = "#0000FF";
    final String whiteColour = "#FFFFFF";
    final String milkyColour = "#FFFFF0";
    final String pearlyColour = "#FAFAE7";
    final String blackColor = "#000000";
    final Type linearChartType = Series.Type.SPLINE;
    final Type columnChartType = Series.Type.COLUMN;

    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    // private static final String SERVER_ERROR = "An error occurred while "
    // +
    // "attempting to contact the server. Please check your network connection and try again.";
    /**
     * Create a remote service proxy to talk to the server-side Greeting
     * service.
     */
    // private final GreetingServiceAsync greetingService = GWT
    // .create(GreetingService.class);

    private final AppConstants AppConstants = GWT.create(AppConstants.class);

    /**
     * This is the entry point method.
     */
    @SuppressWarnings("deprecation")
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
		"chartTypeRadioButtonsGroup", AppConstants.Column());
	final RadioButton chartTypeRadioButton3 = new RadioButton(
		"chartTypeRadioButtonsGroup", AppConstants.LinearAndColumn());
	final RadioButton chartTypeRadioButton4 = new RadioButton(
		"chartTypeRadioButtonsGroup",
		AppConstants.RandomDataInRealTime());
	chartTypeRadioButton1.setChecked(true);
	final VerticalPanel chartTypeRadioButtonsVerticalPanel = new VerticalPanel();
	chartTypeRadioButtonsVerticalPanel.add(chartTypeRadioButton1);
	chartTypeRadioButtonsVerticalPanel.add(chartTypeRadioButton2);
	chartTypeRadioButtonsVerticalPanel.add(chartTypeRadioButton3);
	chartTypeRadioButtonsVerticalPanel.add(chartTypeRadioButton4);

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
	final IntegerBox numberOfPointsIntegerBox = new IntegerBox();
	numberOfPointsIntegerBox.setValue(10000);
	numberOfPointsIntegerBox.setMaxLength(10);
	final Label firstNumbersDownLimitLabel = new Label(
		AppConstants.LowerLimitOfRangeOfPoints());
	final IntegerBox firstNumbersDownLimitIntegerBox = new IntegerBox();
	firstNumbersDownLimitIntegerBox.setValue(0);
	firstNumbersDownLimitIntegerBox.setMaxLength(10);
	final Label firstNumbersUpperLimitLabel = new Label(
		AppConstants.UpperLimitOfRangeOfPoints());
	final IntegerBox firstNumbersUpperLimitIntegerBox = new IntegerBox();
	firstNumbersUpperLimitIntegerBox.setValue(100);
	firstNumbersUpperLimitIntegerBox.setMaxLength(10);
	final Label secondNumbersDownLimitLabel = new Label(
		AppConstants.LowerLimitOfSecondRangeOfPoints());
	final IntegerBox secondNumbersDownLimitIntegerBox = new IntegerBox();
	secondNumbersDownLimitIntegerBox.setValue(0);
	secondNumbersDownLimitIntegerBox.setMaxLength(10);
	secondNumbersDownLimitLabel.setVisible(false);
	secondNumbersDownLimitIntegerBox.setVisible(false);
	final Label secondNumbersUpperLimitLabel = new Label(
		AppConstants.UpperLimitOfSecondRangeOfPoints());
	final IntegerBox secondNumbersUpperLimitIntegerBox = new IntegerBox();
	secondNumbersUpperLimitIntegerBox.setValue(100);
	secondNumbersUpperLimitIntegerBox.setMaxLength(10);
	secondNumbersUpperLimitLabel.setVisible(false);
	secondNumbersUpperLimitIntegerBox.setVisible(false);
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
	firstChartColourRadioButton1.setChecked(true);
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
		AppConstants.SecondYAxisTitle());
	final TextBox anotherYAxisTextBox = new TextBox();
	anotherYAxisTextBox.setText("Tytuł osi Y(1)...");
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
	secondChartColourRadioButton1.setChecked(true);
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
	backgroundChartColourRadioButton1.setChecked(true);
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
	chartPropertiesVerticalPanel.add(numberOfPointsIntegerBox);
	chartPropertiesVerticalPanel.add(firstNumbersDownLimitLabel);
	chartPropertiesVerticalPanel.add(firstNumbersDownLimitIntegerBox);
	chartPropertiesVerticalPanel.add(firstNumbersUpperLimitLabel);
	chartPropertiesVerticalPanel.add(firstNumbersUpperLimitIntegerBox);
	chartPropertiesVerticalPanel.add(secondNumbersDownLimitLabel);
	chartPropertiesVerticalPanel.add(secondNumbersDownLimitIntegerBox);
	chartPropertiesVerticalPanel.add(secondNumbersUpperLimitLabel);
	chartPropertiesVerticalPanel.add(secondNumbersUpperLimitIntegerBox);
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
	final VerticalPanel languagesHorizontalPanel = new VerticalPanel();
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
	languagesHorizontalPanel.add(selectLanguageLabel);
	languagesHorizontalPanel.add(polishLanguageAnchor);
	languagesHorizontalPanel.add(englishLanguageAnchor);
	languagesHorizontalPanel.add(germanLanguageAnchor);
	languagesHorizontalPanel.add(spanishLanguageAnchor);
	languagesHorizontalPanel.add(russianLanguageAnchor);
	informationVerticalPanel.add(infoHTML);
	informationVerticalPanel.add(languagesHorizontalPanel);
	appTabPanel.add(informationVerticalPanel, AppConstants.Information());
	appTabPanel.selectTab(0);

	generateChartButton.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		final VerticalPanel chartTabVerticalPanel = new VerticalPanel();
		chartTabVerticalPanel.setStyleName("chartTabVerticalPanel");

		appTabPanel.add(chartTabVerticalPanel, AppConstants.Chart());
		appTabPanel.selectTab(2);

		if (chartTypeRadioButton1.isChecked()) {
		    chartTabVerticalPanel.add(showSingleChart(
			    chartTextTitleTextBox.getText(),
			    chartTextSubtitleTextBox.getText(),
			    numberOfPointsIntegerBox.getValue(),
			    xAxisTextBox.getText(), yAxisTextBox.getText(),
			    firstNumbersDownLimitIntegerBox.getValue(),
			    firstNumbersUpperLimitIntegerBox.getValue(),
			    linearChartType));
		} else if (chartTypeRadioButton2.isChecked()) {
		    chartTabVerticalPanel.add(showSingleChart(
			    chartTextTitleTextBox.getText(),
			    chartTextSubtitleTextBox.getText(),
			    numberOfPointsIntegerBox.getValue(),
			    xAxisTextBox.getText(), yAxisTextBox.getText(),
			    firstNumbersDownLimitIntegerBox.getValue(),
			    firstNumbersUpperLimitIntegerBox.getValue(),
			    columnChartType));
		} else if (chartTypeRadioButton3.isChecked()) {
		    chartTabVerticalPanel.add(showDoubleCharts(
			    chartTextTitleTextBox.getText(),
			    chartTextSubtitleTextBox.getText(),
			    numberOfPointsIntegerBox.getValue(),
			    xAxisTextBox.getText(), yAxisTextBox.getText(),
			    anotherYAxisTextBox.getText(),
			    firstNumbersDownLimitIntegerBox.getValue(),
			    firstNumbersUpperLimitIntegerBox.getValue(),
			    secondNumbersDownLimitIntegerBox.getValue(),
			    secondNumbersUpperLimitIntegerBox.getValue()));
		} else if (chartTypeRadioButton4.isChecked()) {
		    chartTabVerticalPanel.add(showLiveRandomLinearChart(
			    chartTextTitleTextBox.getText(),
			    chartTextSubtitleTextBox.getText(),
			    xAxisTextBox.getText(), yAxisTextBox.getText(),
			    firstNumbersDownLimitIntegerBox.getValue(),
			    firstNumbersUpperLimitIntegerBox.getValue()));
		}

		final Button closeChartButton = new Button(AppConstants
			.CloseChart());
		closeChartButton.addStyleName("closeChart");
		chartTabVerticalPanel.add(closeChartButton);

		closeChartButton.addClickHandler(new ClickHandler() {
		    @Override
		    public void onClick(ClickEvent event) {
			appTabPanel.selectTab(0);
			appTabPanel.remove(chartTabVerticalPanel);
			appTabPanel.getTabBar().removeTab(2);
		    }
		});
	    }
	});

	chartTypeRadioButton1.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (chartTypeRadioButton1.isEnabled()) {
		    numberOfPointsLabel.setVisible(true);
		    numberOfPointsIntegerBox.setVisible(true);
		    firstNumbersDownLimitLabel.setText(AppConstants
			    .LowerLimitOfRangeOfPoints());
		    firstNumbersUpperLimitLabel.setText(AppConstants
			    .UpperLimitOfRangeOfPoints());
		    secondNumbersDownLimitLabel.setVisible(false);
		    secondNumbersDownLimitIntegerBox.setVisible(false);
		    secondNumbersUpperLimitLabel.setVisible(false);
		    secondNumbersUpperLimitIntegerBox.setVisible(false);
		    yAxisTitleLabel.setText(AppConstants.YAxisTitle());
		    anotherYAxisTitleLabel.setVisible(false);
		    anotherYAxisTextBox.setVisible(false);
		    firstChartColourLabel.setText(AppConstants
			    .DataSeriesColour());
		    secondChartColourLabel.setVisible(false);
		    secondChartColourVerticalPanel.setVisible(false);
		}
	    }
	});

	chartTypeRadioButton2.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (chartTypeRadioButton2.isEnabled()) {
		    numberOfPointsLabel.setVisible(true);
		    numberOfPointsIntegerBox.setVisible(true);
		    firstNumbersDownLimitLabel.setText(AppConstants
			    .LowerLimitOfRangeOfPoints());
		    firstNumbersUpperLimitLabel.setText(AppConstants
			    .UpperLimitOfRangeOfPoints());
		    secondNumbersDownLimitLabel.setVisible(false);
		    secondNumbersDownLimitIntegerBox.setVisible(false);
		    secondNumbersUpperLimitLabel.setVisible(false);
		    secondNumbersUpperLimitIntegerBox.setVisible(false);
		    yAxisTitleLabel.setText(AppConstants.YAxisTitle());
		    anotherYAxisTitleLabel.setVisible(false);
		    anotherYAxisTextBox.setVisible(false);
		    firstChartColourLabel.setText(AppConstants
			    .DataSeriesColour());
		    secondChartColourLabel.setVisible(false);
		    secondChartColourVerticalPanel.setVisible(false);
		}
	    }
	});

	chartTypeRadioButton3.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (chartTypeRadioButton3.isEnabled()) {
		    numberOfPointsLabel.setVisible(true);
		    numberOfPointsIntegerBox.setVisible(true);
		    firstNumbersDownLimitLabel.setText(AppConstants
			    .LowerLimitOfFirstRangeOfPoints());
		    firstNumbersUpperLimitLabel.setText(AppConstants
			    .UpperLimitOfFirstRangeOfPoints());
		    secondNumbersDownLimitLabel.setVisible(true);
		    secondNumbersDownLimitIntegerBox.setVisible(true);
		    secondNumbersUpperLimitLabel.setVisible(true);
		    secondNumbersUpperLimitIntegerBox.setVisible(true);
		    yAxisTitleLabel.setText(AppConstants.FirstYAxisTitle());
		    anotherYAxisTitleLabel.setVisible(true);
		    anotherYAxisTextBox.setVisible(true);
		    firstChartColourLabel.setText(AppConstants
			    .FirstDataSeriesColour());
		    secondChartColourLabel.setVisible(true);
		    secondChartColourVerticalPanel.setVisible(true);
		}
	    }
	});

	chartTypeRadioButton4.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (chartTypeRadioButton4.isEnabled()) {
		    numberOfPointsLabel.setVisible(false);
		    numberOfPointsIntegerBox.setVisible(false);
		    firstNumbersDownLimitLabel.setText(AppConstants
			    .LowerLimitOfRangeOfPoints());
		    firstNumbersUpperLimitLabel.setText(AppConstants
			    .UpperLimitOfRangeOfPoints());
		    secondNumbersDownLimitLabel.setVisible(false);
		    secondNumbersDownLimitIntegerBox.setVisible(false);
		    secondNumbersUpperLimitLabel.setVisible(false);
		    secondNumbersUpperLimitIntegerBox.setVisible(false);
		    yAxisTitleLabel.setText(AppConstants.YAxisTitle());
		    anotherYAxisTitleLabel.setVisible(false);
		    anotherYAxisTextBox.setVisible(false);
		    firstChartColourLabel.setText(AppConstants
			    .DataSeriesColour());
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
		if (firstChartColourRadioButton1.isEnabled()) {
		    secondChartColourRadioButton1.setVisible(true);
		    secondChartColourRadioButton1.setChecked(true);
		    secondChartColourRadioButton2.setVisible(true);
		    secondChartColourRadioButton3.setVisible(true);
		    secondChartColourRadioButton4.setVisible(true);
		    firstColour = firstDefaultColour;
		    secondColour = secondDefaultColour;
		}
	    }
	});

	firstChartColourRadioButton2.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (firstChartColourRadioButton2.isEnabled()) {
		    secondChartColourRadioButton1.setVisible(true);
		    secondChartColourRadioButton2.setVisible(false);
		    secondChartColourRadioButton3.setVisible(true);
		    secondChartColourRadioButton3.setChecked(true);
		    secondChartColourRadioButton4.setVisible(true);
		    firstColour = redColour;
		    secondColour = greenColour;
		}
	    }
	});

	firstChartColourRadioButton3.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (firstChartColourRadioButton3.isEnabled()) {
		    secondChartColourRadioButton1.setVisible(true);
		    secondChartColourRadioButton2.setVisible(true);
		    secondChartColourRadioButton3.setVisible(false);
		    secondChartColourRadioButton4.setVisible(true);
		    secondChartColourRadioButton4.setChecked(true);
		    firstColour = greenColour;
		    secondColour = blueColour;
		}
	    }
	});

	firstChartColourRadioButton4.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (firstChartColourRadioButton4.isEnabled()) {
		    secondChartColourRadioButton1.setVisible(true);
		    secondChartColourRadioButton1.setChecked(true);
		    secondChartColourRadioButton2.setVisible(true);
		    secondChartColourRadioButton3.setVisible(true);
		    secondChartColourRadioButton4.setVisible(false);
		    firstColour = blueColour;
		    secondColour = redColour;
		}
	    }
	});

	secondChartColourRadioButton1.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (secondChartColourRadioButton1.isChecked()) {
		    secondColour = secondDefaultColour;
		}
	    }
	});

	secondChartColourRadioButton2.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (secondChartColourRadioButton2.isChecked()) {
		    secondColour = redColour;
		}
	    }
	});

	secondChartColourRadioButton3.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (secondChartColourRadioButton3.isChecked()) {
		    secondColour = greenColour;
		}
	    }
	});

	secondChartColourRadioButton4.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (secondChartColourRadioButton4.isChecked()) {
		    secondColour = blueColour;
		}
	    }
	});

	backgroundChartColourRadioButton1.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (backgroundChartColourRadioButton1.isChecked()) {
		    backgroundChartColourRadioButton1.setEnabled(true);
		    backgroundColour = whiteColour;
		}
	    }
	});

	backgroundChartColourRadioButton2.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (backgroundChartColourRadioButton2.isChecked()) {
		    backgroundChartColourRadioButton2.setEnabled(true);
		    backgroundColour = milkyColour;
		}
	    }
	});

	backgroundChartColourRadioButton3.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		if (backgroundChartColourRadioButton3.isChecked()) {
		    backgroundChartColourRadioButton3.setEnabled(true);
		    backgroundColour = pearlyColour;
		}
	    }
	});
    }

    public Number[] randomPointsGenerator(int pointsQuantity, int downLimit,
	    int upperLimit) {
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

    public Chart showSingleChart(String chartTitle, String chartSubtitle,
	    int numberOfPoints, String xAxisText, String yAxisText,
	    int pointsDownLimit, int pointsUpperLimit, Type chartType) {
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
	singleSeries.setPoints(randomPointsGenerator(numberOfPoints,
		pointsDownLimit, pointsUpperLimit));
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

    public Chart showDoubleCharts(String chartTitle, String chartSubtitle,
	    int numberOfPoints, String xAxisText, String firstYAxisText,
	    String secondYAxisText, int pointsDownLimitFirst,
	    int pointsUpperLimitFirst, int pointsDownLimitSecond,
	    int pointsUpperLimitSecond) {
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
	y0Series.setPoints(randomPointsGenerator(numberOfPoints,
		pointsDownLimitFirst, pointsUpperLimitFirst));
	final ColumnPlotOptions y0ColumnPlotOptions = new ColumnPlotOptions();
	y0ColumnPlotOptions.setColor(firstColour);
	y0Series.setPlotOptions(y0ColumnPlotOptions);
	y0Series.setType(columnChartType);
	y0Series.setYAxis(1);
	doubleChart.addSeries(y0Series);

	final Series y1Series = doubleChart.createSeries();
	y1Series.setPoints(randomPointsGenerator(numberOfPoints,
		pointsDownLimitSecond, pointsUpperLimitSecond));
	final SplinePlotOptions y1SplinePlotOptions = new SplinePlotOptions();
	y1SplinePlotOptions.setColor(firstColour);
	y1Series.setPlotOptions(y1SplinePlotOptions);
	y1Series.setType(linearChartType);
	doubleChart.addSeries(y1Series);

	return doubleChart;
    }

    public Chart showLiveRandomLinearChart(String chartTitle,
	    String chartSubtitle, String xAxisText, String yAxisText,
	    int pointsDownLimit, int pointsUpperLimit) {
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
			Random.nextInt(), true, false, true);
	    }
	};
	liveRandomChartTimer.scheduleRepeating(1000);

	return liveRandomChart;
    }

    public Chart showLiveRandomColumnChart(String chartTitle,
	    String chartSubtitle, String xAxisText, String yAxisText,
	    int pointsDownLimit, int pointsUpperLimit) {
	final Chart liveRandomChart = new Chart();
	final ChartTitle liveRandomChartTitle = new ChartTitle();
	liveRandomChartTitle.setText(chartTitle);
	liveRandomChart.setChartTitle(liveRandomChartTitle);
	final ChartSubtitle liveRandomChartSubitle = new ChartSubtitle();
	liveRandomChartSubitle.setText(chartSubtitle);
	liveRandomChart.setChartSubtitle(liveRandomChartSubitle);
	liveRandomChart.setType(columnChartType);

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

	final ColumnPlotOptions liveRandomSplinePlotOptions = new ColumnPlotOptions();
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
}
