package com.hashimte.hashbus1.ui.ticket;

public class YourTikectData {
    private String journeyName;
    private Integer ticketId;

    private Double price;

    public YourTikectData(String journeyName, Integer ticketId, double price) {
        this.journeyName = journeyName;
        this.ticketId = ticketId;
        this.price = price;
    }

    public String getJourneyName() {
        return journeyName;
    }

    public void setJourneyName(String journeyName) {
        this.journeyName = journeyName;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
