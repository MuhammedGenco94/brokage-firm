package com.ing.brokagefirm.model.dto;

import com.ing.brokagefirm.model.Side;
import com.ing.brokagefirm.model.Status;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDTO {

    private Long customerId;
    private String assetName;
    private Side side;
    private BigDecimal size;
    private BigDecimal price;
    private Status status;

}
