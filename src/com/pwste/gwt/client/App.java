package com.pwste.gwt.client;

import java.util.Date;

import org.moxieapps.gwt.highcharts.client.Axis;
import org.moxieapps.gwt.highcharts.client.AxisTitle;
import org.moxieapps.gwt.highcharts.client.BaseChart;
import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.ChartSubtitle;
import org.moxieapps.gwt.highcharts.client.ChartTitle;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.Series.Type;
import org.moxieapps.gwt.highcharts.client.Style;
import org.moxieapps.gwt.highcharts.client.events.ChartClickEvent;
import org.moxieapps.gwt.highcharts.client.events.ChartClickEventHandler;
import org.moxieapps.gwt.highcharts.client.events.PointClickEvent;
import org.moxieapps.gwt.highcharts.client.events.PointClickEventHandler;
import org.moxieapps.gwt.highcharts.client.labels.DataLabels;
import org.moxieapps.gwt.highcharts.client.labels.YAxisLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.BarPlotOptions;
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
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network connection and try again.";
	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onModuleLoad() {
		final TabPanel appTabPanel = new TabPanel();
		appTabPanel.setAnimationEnabled(true);
		RootPanel.get().add(appTabPanel);

		final Label chartTypeLabel = new Label("Wybierz typ wykresu:");
		chartTypeLabel.addStyleName("chooseChartTypeLabel");
		final RadioButton chartTypeRadioButton1 = new RadioButton("chartTypeRadioButtonsGroup", "Liniowy");
		final RadioButton chartTypeRadioButton2 = new RadioButton("chartTypeRadioButtonsGroup", "Słupkowy");
		final RadioButton chartTypeRadioButton3 = new RadioButton("chartTypeRadioButtonsGroup",
				"Liniowy i słupkowy (2 serie danych)");
		final RadioButton chartTypeRadioButton4 = new RadioButton("chartTypeRadioButtonsGroup",
				"Losowe dane w czasie rzeczywistym");
		chartTypeRadioButton1.setChecked(true);
		final VerticalPanel chartTypeRadioButtonsVerticalPanel = new VerticalPanel();
		chartTypeRadioButtonsVerticalPanel.add(chartTypeRadioButton1);
		chartTypeRadioButtonsVerticalPanel.add(chartTypeRadioButton2);
		chartTypeRadioButtonsVerticalPanel.add(chartTypeRadioButton3);
		chartTypeRadioButtonsVerticalPanel.add(chartTypeRadioButton4);

		final DisclosurePanel chartPropertiesDisclosurePanel = new DisclosurePanel("Właściwości wykresu (rozwiń)");
		chartPropertiesDisclosurePanel.setAnimationEnabled(true);
		final VerticalPanel chartPropertiesVerticalPanel = new VerticalPanel();
		final Label chartTextTitleLabel = new Label("Tytuł wykresu:");
		final TextBox chartTextTitleTextBox = new TextBox();
		chartTextTitleTextBox.setText("Tytuł wykresu...");
		chartTextTitleTextBox.setMaxLength(50);
		final Label chartTextSubtitleLabel = new Label("Podtytuł wykresu:");
		final TextBox chartTextSubtitleTextBox = new TextBox();
		chartTextSubtitleTextBox.setText("Podtytuł wykresu...");
		chartTextSubtitleTextBox.setMaxLength(50);
		final Label numberOfPointsLabel = new Label("Ilość punktów (domyślnie 10 000):");
		final IntegerBox numberOfPointsIntegerBox = new IntegerBox();
		numberOfPointsIntegerBox.setValue(10000);
		numberOfPointsIntegerBox.setMaxLength(10);
		final Label firstNumbersDownLimitLabel = new Label("Dolna granica zakresu punktów (domyślnie 0):");
		final IntegerBox firstNumbersDownLimitIntegerBox = new IntegerBox();
		firstNumbersDownLimitIntegerBox.setValue(0);
		firstNumbersDownLimitIntegerBox.setMaxLength(10);
		final Label firstNumbersUpperLimitLabel = new Label("Górna granica zakresu punktów (domyślnie 100):");
		final IntegerBox firstNumbersUpperLimitIntegerBox = new IntegerBox();
		firstNumbersUpperLimitIntegerBox.setValue(100);
		firstNumbersUpperLimitIntegerBox.setMaxLength(10);
		final Label secondNumbersDownLimitLabel = new Label("Dolna granica drugiego zakresu punktów (domyślnie 0):");
		final IntegerBox secondNumbersDownLimitIntegerBox = new IntegerBox();
		secondNumbersDownLimitIntegerBox.setValue(0);
		secondNumbersDownLimitIntegerBox.setMaxLength(10);
		secondNumbersDownLimitLabel.setVisible(false);
		secondNumbersDownLimitIntegerBox.setVisible(false);
		final Label secondNumbersUpperLimitLabel = new Label("Górna granica drugiego zakresu punktów (domyślnie 100):");
		final IntegerBox secondNumbersUpperLimitIntegerBox = new IntegerBox();
		secondNumbersUpperLimitIntegerBox.setValue(100);
		secondNumbersUpperLimitIntegerBox.setMaxLength(10);
		secondNumbersUpperLimitLabel.setVisible(false);
		secondNumbersUpperLimitIntegerBox.setVisible(false);
		final Label xAxisTitleLabel = new Label("Tytuł osi X:");
		final TextBox xAxisTextBox = new TextBox();
		xAxisTextBox.setText("Tytuł osi X...");
		xAxisTextBox.setMaxLength(100);
		final Label firstChartColourLabel = new Label("Kolor serii danych:");
		final RadioButton firstChartColourRadioButton1 = new RadioButton("firstChartColourRadioButtonsGroup",
				"Domyślny");
		final RadioButton firstChartColourRadioButton2 = new RadioButton("firstChartColourRadioButtonsGroup",
				"Czerwony");
		final RadioButton firstChartColourRadioButton3 = new RadioButton("firstChartColourRadioButtonsGroup", "Zielony");
		final RadioButton firstChartColourRadioButton4 = new RadioButton("firstChartColourRadioButtonsGroup",
				"Niebieski");
		firstChartColourRadioButton1.setChecked(true);
		final VerticalPanel firstChartColourVerticalPanel = new VerticalPanel();
		firstChartColourVerticalPanel.add(firstChartColourRadioButton1);
		firstChartColourVerticalPanel.add(firstChartColourRadioButton2);
		firstChartColourVerticalPanel.add(firstChartColourRadioButton3);
		firstChartColourVerticalPanel.add(firstChartColourRadioButton4);
		final Label yAxisTitleLabel = new Label("Tytuł osi Y:");
		final TextBox yAxisTextBox = new TextBox();
		yAxisTextBox.setMaxLength(100);
		yAxisTextBox.setText("Tytuł osi Y...");
		final Label anotherYAxisTitleLabel = new Label("Tytuł drugiej osi Y:");
		final TextBox anotherYAxisTextBox = new TextBox();
		anotherYAxisTextBox.setText("Tytuł osi Y(1)...");
		anotherYAxisTextBox.setMaxLength(100);
		anotherYAxisTitleLabel.setVisible(false);
		anotherYAxisTextBox.setVisible(false);
		final Label secondChartColourLabel = new Label("Kolor drugiej serii danych:");
		final RadioButton secondChartColourRadioButton1 = new RadioButton("secondChartColourRadioButtonsGroup",
				"Domyślny");
		final RadioButton secondChartColourRadioButton2 = new RadioButton("secondChartColourRadioButtonsGroup",
				"Czerwony");
		final RadioButton secondChartColourRadioButton3 = new RadioButton("secondChartColourRadioButtonsGroup",
				"Zielony");
		final RadioButton secondChartColourRadioButton4 = new RadioButton("secondChartColourRadioButtonsGroup",
				"Niebieski");
		secondChartColourRadioButton1.setChecked(true);
		final VerticalPanel secondChartColourVerticalPanel = new VerticalPanel();
		secondChartColourVerticalPanel.add(secondChartColourRadioButton1);
		secondChartColourVerticalPanel.add(secondChartColourRadioButton2);
		secondChartColourVerticalPanel.add(secondChartColourRadioButton3);
		secondChartColourVerticalPanel.add(secondChartColourRadioButton4);
		secondChartColourLabel.setVisible(false);
		secondChartColourVerticalPanel.setVisible(false);
		final Label backgroundChartColourLabel = new Label("Kolor tła wykresu:");
		final RadioButton backgroundChartColourRadioButton1 = new RadioButton("backgroundChartColourRadioButtonsGroup",
				"Domyślny");
		final RadioButton backgroundChartColourRadioButton2 = new RadioButton("backgroundChartColourRadioButtonsGroup",
				"Mleczny");
		final RadioButton backgroundChartColourRadioButton3 = new RadioButton("backgroundChartColourRadioButtonsGroup",
				"Perłowy");
		backgroundChartColourRadioButton1.setChecked(true);
		final VerticalPanel backgroundChartColourVerticalPanel = new VerticalPanel();
		backgroundChartColourVerticalPanel.add(backgroundChartColourRadioButton1);
		backgroundChartColourVerticalPanel.add(backgroundChartColourRadioButton2);
		backgroundChartColourVerticalPanel.add(backgroundChartColourRadioButton3);

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

		final Button generateChartButton = new Button("Generuj wykres");
		generateChartButton.addStyleName("generateChartButton");

		final VerticalPanel componentsVerticalPanel = new VerticalPanel();
		componentsVerticalPanel.addStyleName("componentsVerticalPanel");
		componentsVerticalPanel.add(chartTypeLabel);
		componentsVerticalPanel.add(chartTypeRadioButtonsVerticalPanel);
		componentsVerticalPanel.add(chartPropertiesDisclosurePanel);
		componentsVerticalPanel.add(generateChartButton);

		appTabPanel.add(componentsVerticalPanel, "ChartsApp");
		final HTML infoHTML = new HTML(
				"<h1>ChartsApp</h1>"
						+ "<h3>Kamil Burdzy</h3>"
						+ "<h4>2016</h4>"
						+ "<br/>"
						+ "<h4>W oparciu o</h4>"
						+ "<h3><a href=\"http://www.gwtproject.org/\" target=\"_blank\">Google Web Toolkit</a></h3>"
						+ "<h4>oraz</h4>"
						+ "<h3><a href=\"http://www.moxiegroup.com/moxieapps/gwt-highcharts/\" target=\"_blank\">GWT Highcharts</a>.</h3>");
		appTabPanel.add(infoHTML, "Informacja");
		appTabPanel.selectTab(0);

		generateChartButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final VerticalPanel chartTabVerticalPanel = new VerticalPanel();
				chartTabVerticalPanel.setStyleName("chartTabVerticalPanel");

				appTabPanel.add(chartTabVerticalPanel, "Wykres");
				appTabPanel.selectTab(2);

				if (chartTypeRadioButton1.isChecked()) {
					chartTabVerticalPanel.add(showSingleChart(chartTextTitleTextBox.getText(),
							chartTextSubtitleTextBox.getText(), numberOfPointsIntegerBox.getValue(),
							xAxisTextBox.getText(), yAxisTextBox.getText(), firstNumbersDownLimitIntegerBox.getValue(),
							firstNumbersUpperLimitIntegerBox.getValue(), linearChartType));
				} else if (chartTypeRadioButton2.isChecked()) {
					chartTabVerticalPanel.add(showSingleChart(chartTextTitleTextBox.getText(),
							chartTextSubtitleTextBox.getText(), numberOfPointsIntegerBox.getValue(),
							xAxisTextBox.getText(), yAxisTextBox.getText(), firstNumbersDownLimitIntegerBox.getValue(),
							firstNumbersUpperLimitIntegerBox.getValue(), columnChartType));
				} else if (chartTypeRadioButton3.isChecked()) {
					chartTabVerticalPanel.add(showDoubleCharts(chartTextTitleTextBox.getText(),
							chartTextSubtitleTextBox.getText(), numberOfPointsIntegerBox.getValue(),
							xAxisTextBox.getText(), yAxisTextBox.getText(), anotherYAxisTextBox.getText(),
							firstNumbersDownLimitIntegerBox.getValue(), firstNumbersUpperLimitIntegerBox.getValue(),
							secondNumbersDownLimitIntegerBox.getValue(), secondNumbersUpperLimitIntegerBox.getValue()));
				} else if (chartTypeRadioButton4.isChecked()) {
					chartTabVerticalPanel.add(showLiveRandomChart(chartTextTitleTextBox.getText(),
							chartTextSubtitleTextBox.getText(), xAxisTextBox.getText(), yAxisTextBox.getText(),
							firstNumbersDownLimitIntegerBox.getValue(), firstNumbersUpperLimitIntegerBox.getValue()));
				}

				final Button closeChartButton = new Button("Zamknij wykres");
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
					firstNumbersDownLimitLabel.setText("Dolna granica zakresu punktów (domyślnie 0):");
					firstNumbersUpperLimitLabel.setText("Górna granica zakresu punktów (domyślnie 100):");
					secondNumbersDownLimitLabel.setVisible(false);
					secondNumbersDownLimitIntegerBox.setVisible(false);
					secondNumbersUpperLimitLabel.setVisible(false);
					secondNumbersUpperLimitIntegerBox.setVisible(false);
					yAxisTitleLabel.setText("Tytuł osi Y:");
					anotherYAxisTitleLabel.setVisible(false);
					anotherYAxisTextBox.setVisible(false);
					firstChartColourLabel.setText("Kolor serii danych:");
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
					firstNumbersDownLimitLabel.setText("Dolna granica zakresu punktów (domyślnie 0):");
					firstNumbersUpperLimitLabel.setText("Górna granica zakresu punktów (domyślnie 100):");
					secondNumbersDownLimitLabel.setVisible(false);
					secondNumbersDownLimitIntegerBox.setVisible(false);
					secondNumbersUpperLimitLabel.setVisible(false);
					secondNumbersUpperLimitIntegerBox.setVisible(false);
					yAxisTitleLabel.setText("Tytuł osi Y:");
					anotherYAxisTitleLabel.setVisible(false);
					anotherYAxisTextBox.setVisible(false);
					firstChartColourLabel.setText("Kolor serii danych:");
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
					firstNumbersDownLimitLabel.setText("Dolna granica pierwszego zakresu punktów (domyślnie 0):");
					firstNumbersUpperLimitLabel.setText("Górna granica pierwszego zakresu punktów (domyślnie 100):");
					secondNumbersDownLimitLabel.setVisible(true);
					secondNumbersDownLimitIntegerBox.setVisible(true);
					secondNumbersUpperLimitLabel.setVisible(true);
					secondNumbersUpperLimitIntegerBox.setVisible(true);
					yAxisTitleLabel.setText("Tytuł pierwszej osi Y:");
					anotherYAxisTitleLabel.setVisible(true);
					anotherYAxisTextBox.setVisible(true);
					firstChartColourLabel.setText("Kolor pierwszej serii danych:");
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
					firstNumbersDownLimitLabel.setText("Dolna granica zakresu punktów (domyślnie 0):");
					firstNumbersUpperLimitLabel.setText("Górna granica zakresu punktów (domyślnie 100):");
					secondNumbersDownLimitLabel.setVisible(false);
					secondNumbersDownLimitIntegerBox.setVisible(false);
					secondNumbersUpperLimitLabel.setVisible(false);
					secondNumbersUpperLimitIntegerBox.setVisible(false);
					yAxisTitleLabel.setText("Tytuł osi Y:");
					anotherYAxisTitleLabel.setVisible(false);
					anotherYAxisTextBox.setVisible(false);
					firstChartColourLabel.setText("Kolor serii danych:");
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

	public Number[] randomPointsGenerator(int pointsQuantity, int downLimit, int upperLimit) {
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
			numbersData[i] = Random.nextInt(maximumValue - minimumValue) + minimumValue;
		}

		return numbersData;
	}

	public Chart showSingleChart(String chartTitle, String chartSubtitle, int numberOfPoints, String xAxisText,
			String yAxisText, int pointsDownLimit, int pointsUpperLimit, Type chartType) {
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
		singleSeries.setPoints(randomPointsGenerator(numberOfPoints, pointsDownLimit, pointsUpperLimit));
		final ColumnPlotOptions seriesColourColumnPlotOptions = new ColumnPlotOptions();
		seriesColourColumnPlotOptions.setColor(firstColour);
		singleSeries.setPlotOptions(seriesColourColumnPlotOptions);
		singleChart.addSeries(singleSeries);

		final SeriesPlotOptions deleteSinglePointSeriesPlotOptions = new SeriesPlotOptions();
		deleteSinglePointSeriesPlotOptions.setLineWidth(1);
		final PointClickEventHandler deletePointClickEventHandler = new PointClickEventHandler() {
			@Override
			public boolean onClick(PointClickEvent pointClickEvent) {
				final Series currentSeries = singleChart.getSeries(pointClickEvent.getSeriesId());

				if (currentSeries.getPoints().length > 1) {
					pointClickEvent.getPoint().remove();
				} else if (currentSeries.getPoints().length == 1) {
					Window.alert("Nie można usunąć ostatniego punktu!");
				}

				return true;
			}
		};
		deleteSinglePointSeriesPlotOptions.setPointClickEventHandler(deletePointClickEventHandler);
		singleChart.setSeriesPlotOptions(deleteSinglePointSeriesPlotOptions);

		final ChartClickEventHandler addSinglePointChartClickEventHandler = new ChartClickEventHandler() {
			@Override
			public boolean onClick(ChartClickEvent chartClickEvent) {
				singleSeries.addPoint(chartClickEvent.getXAxisValue(), chartClickEvent.getYAxisValue());

				return true;
			}
		};
		singleChart.setClickEventHandler(addSinglePointChartClickEventHandler);

		return singleChart;
	}

	public Chart showDoubleCharts(String chartTitle, String chartSubtitle, int numberOfPoints, String xAxisText,
			String firstYaxisText, String secondYaxisText, int pointsDownLimitFirst, int pointsUpperLimitFirst,
			int pointsDownLimitSecond, int pointsUpperLimitSecond) {
		final Chart doubleChart = new Chart();
		final ChartTitle doubleChartTitle = new ChartTitle();
		doubleChartTitle.setText(chartTitle);
		doubleChart.setChartTitle(doubleChartTitle);
		final ChartSubtitle doubleChartSubtitle = new ChartSubtitle();
		doubleChartSubtitle.setText(chartSubtitle);
		doubleChart.setChartSubtitle(doubleChartSubtitle);
		doubleChart.setZoomType(BaseChart.ZoomType.X_AND_Y);
		doubleChart.setLegend(new Legend().setLayout(Legend.Layout.VERTICAL).setAlign(Legend.Align.LEFT)
				.setVerticalAlign(Legend.VerticalAlign.TOP).setX(70).setY(45).setFloating(true)
				.setBackgroundColor(backgroundColour).setBorderColor(firstDefaultColour).setBorderWidth(1));
		doubleChart.getXAxis().setAxisTitle(new AxisTitle().setText(xAxisText));
		doubleChart.getYAxis(0).setAxisTitle(new AxisTitle().setText(firstYaxisText))
				.setLabels(new YAxisLabels().setStyle(new Style().setColor(firstColour)));
		doubleChart.getYAxis(1).setAxisTitle(new AxisTitle().setText(secondYaxisText)).setOpposite(true)
				.setLabels(new YAxisLabels().setStyle(new Style().setColor(secondColour)));
		doubleChart.setBackgroundColor(backgroundColour);

		doubleChart.addSeries(doubleChart.createSeries().setName(firstYaxisText).setType(Series.Type.COLUMN)
				.setPlotOptions(new ColumnPlotOptions().setColor(firstColour)).setYAxis(1)
				.setPoints(randomPointsGenerator(numberOfPoints, pointsDownLimitFirst, pointsUpperLimitFirst)));
		doubleChart.addSeries(doubleChart.createSeries().setName(secondYaxisText).setType(Series.Type.SPLINE)
				.setPlotOptions(new SplinePlotOptions().setColor(secondColour))
				.setPoints(randomPointsGenerator(numberOfPoints, pointsDownLimitSecond, pointsUpperLimitSecond)));

		return doubleChart;
	}

	public Chart showLiveRandomChart(String chartTitle, String chartSubtitle, String xAxisText, String yAxisText,
			int pointsDownLimit, int pointsUpperLimit) {
		final Chart liveRandomChart = new Chart();
		liveRandomChart.setType(Series.Type.SPLINE);
		liveRandomChart.setChartSubtitle(new ChartSubtitle().setText(chartSubtitle));
		final ChartTitle liveRandomChartTitle = new ChartTitle();
		liveRandomChartTitle.setText(chartTitle);
		liveRandomChart.setChartTitle(liveRandomChartTitle);
		final ChartSubtitle liveRandomChartSubtitle = new ChartSubtitle();
		liveRandomChartSubtitle.setText(chartSubtitle);
		liveRandomChart.setChartSubtitle(liveRandomChartSubtitle);
		liveRandomChart.setBarPlotOptions(new BarPlotOptions().setDataLabels(new DataLabels().setEnabled(true)));

		liveRandomChart.getXAxis().setAxisTitle(new AxisTitle().setText(xAxisText)).setType(Axis.Type.DATE_TIME)
				.setTickPixelInterval(50);

		liveRandomChart
				.getYAxis()
				.setAxisTitle(new AxisTitle().setText(yAxisText))
				.setPlotLines(liveRandomChart.getYAxis().createPlotLine().setValue(0).setWidth(1).setColor(firstColour));
		liveRandomChart.setBackgroundColor(backgroundColour);

		final Series liveSeries = liveRandomChart.createSeries();
		liveRandomChart.addSeries(liveSeries);

		// final long time = new Date().getTime();
		for (int i = -25; i < 0; i++) {
			liveSeries.addPoint(new Date().getTime() + i * 1000, Random.nextInt());
		}

		liveSeries.setPlotOptions(new ColumnPlotOptions().setColor(firstColour));

		final Timer temporaryTimer = new Timer() {
			@Override
			public void run() {
				liveSeries.addPoint(new Date().getTime(), Random.nextInt(), true, true, true);
			}
		};
		temporaryTimer.scheduleRepeating(1000);

		return liveRandomChart;
	}
}
