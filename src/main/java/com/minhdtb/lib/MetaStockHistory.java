package com.minhdtb.lib;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.File;
import java.util.Date;

@ToString
@AllArgsConstructor
@Data
public class MetaStockHistory {
    private String symbol;
    private Date startDate;
    private Date endDate;
    private int fileNumber;
    private File file;
}
