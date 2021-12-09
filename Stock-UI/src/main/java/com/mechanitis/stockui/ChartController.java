package com.mechanitis.stockui;

import com.mechanitis.stockclient.StockClient;
import com.mechanitis.stockclient.StockPrice;
import com.mechanitis.stockclient.WebClientStockClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ChartController {
    @FXML
    public LineChart<String, Double> chart;

    private final StockClient stockClient;

    public ChartController(StockClient StockClient) {
        this.stockClient = StockClient;
    }

    @FXML
    public void initialize() {
        ObservableList<Series<String, Double>> data = FXCollections.observableArrayList();

        String symbol1 = "SYMBOL2";
        final PriceSubscriber priceSubscriber1 = new PriceSubscriber(symbol1);
        stockClient.pricesFor(symbol1).subscribe(priceSubscriber1);
        data.add(priceSubscriber1.getSeries());

        String symbol2 = "SYMBOL2";
        final PriceSubscriber priceSubscriber2 = new PriceSubscriber(symbol2);
        stockClient.pricesFor(symbol1).subscribe(priceSubscriber2);
        data.add(priceSubscriber2.getSeries());

        chart.setData(data);
    }

    private static class PriceSubscriber implements Consumer<StockPrice> {

        private final ObservableList<XYChart.Data<String, Double>> seriesData = FXCollections.observableArrayList();
        private final Series<String, Double> series;

        public Series<String, Double> getSeries() {
            return series;
        }

        public PriceSubscriber(String symbol) {
            this.series = new Series<>(symbol, seriesData);
        }

        @Override
        public void accept(StockPrice stockPrice) {
            Platform.runLater(() ->
                    seriesData.add(new XYChart.Data<>(String.valueOf(stockPrice.getTime().getSecond()), stockPrice.getPrice()))
            );
        }
    }
}
