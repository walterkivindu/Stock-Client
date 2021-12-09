package com.mechanitis.stockclient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RSocketStockClientIntegrationTest {
    @Autowired
    private RSocketRequester.Builder builder;

    private RSocketRequester createRSocketRequester() {
        return builder.tcp("localhost",7000);
    }

    @Test
    void ShouldReceiveStockPricesFromTheService() {
        RSocketStockClient rSocketStockClient = new RSocketStockClient(createRSocketRequester());
        Flux<StockPrice> prices = rSocketStockClient.pricesFor("SYMBOL");

        Assertions.assertNotNull(prices);
        Flux<StockPrice> fivePrices =  prices.take(5);
        assertEquals(5,fivePrices.count().block() );
        assertEquals("SYMBOL",fivePrices.blockFirst().getSymbol());

        StepVerifier.create(prices.take(2))
                .expectNextMatches(st->st.getSymbol().equals("SYMBOL"))
                .expectNextMatches(st->st.getSymbol().equals("SYMBOL"))
                .expectComplete()
                .verify();
    }
}