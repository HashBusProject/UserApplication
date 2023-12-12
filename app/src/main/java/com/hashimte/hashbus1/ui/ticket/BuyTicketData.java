package com.hashimte.hashbus1.ui.ticket;

public class BuyTicketData {
    private String buyYourneyName;
    private Double buyTicketPrice;

    public BuyTicketData(String buyYourneyName,Double buyTicketPrice) {
        this.buyYourneyName = buyYourneyName;
        this.buyTicketPrice = buyTicketPrice;
    }

    public String getBuyYourneyName() {
        return buyYourneyName;
    }

    public void setBuyYourneyName(String buyYourneyName) {
        this.buyYourneyName = buyYourneyName;
    }

    public Double getBuyTicketPrice() {
        return buyTicketPrice;
    }

    public void setBuyTicketPrice(Double buyTicketPrice) {
        this.buyTicketPrice = buyTicketPrice;
    }
}
