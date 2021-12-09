package com.mechanitis.stockclient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WebClientStockClientIntegrationTest {
    @Test
    void ShouldReceiveStockPricesFromTheService() {
        WebClient webClient = WebClient.builder().build();
        WebClientStockClient webClientStockClient = new WebClientStockClient(webClient);
        var prices = webClientStockClient.pricesFor("SYMBOL");

        Assertions.assertNotNull(prices);
        Flux<StockPrice> fivePrices =  prices.take(5);
        assertEquals(5,fivePrices.count().block() );
        assertEquals("SYMBOL",fivePrices.blockFirst().getSymbol());
    }
}