package com.minhdtb.lib.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@ToString
@AllArgsConstructor
@Data
public class MetaStockHistoryData {
    private Date date;
    private float open;
    private float high;
    private float low;
    private float close;
    private float volume;
}
