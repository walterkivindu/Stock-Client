package com.mechanitis.stockclient;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;

import java.io.IOException;

@Log4j2
public class RSocketStockClient implements StockClient {

    private RSocketRequester rSocketRequester;

    public RSocketStockClient(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @Override
    public Flux<StockPrice> pricesFor(String symbol) {
        log.info("RSocket client");
        return rSocketRequester.route("stockPrice")
                .data(symbol)
                .retrieveFlux(StockPrice.class)
                .retry(5)
                .doOnError(IOException.class, e->log.error(e.getMessage()));
    }
}
