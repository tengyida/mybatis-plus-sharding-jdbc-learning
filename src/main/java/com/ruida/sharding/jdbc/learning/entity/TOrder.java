package com.ruida.sharding.jdbc.learning.entity;

import lombok.Data;

@Data
public class TOrder {
    private Long id;
    private Long userId;
    private String descr;
}
