package com.hashimte.hashbus1.model;

import lombok.Data;

@Data
public class Ticket {
    private long id;
    private double price;
    private Journey journey;
}
