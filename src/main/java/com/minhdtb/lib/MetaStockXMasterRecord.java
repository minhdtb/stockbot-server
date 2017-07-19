package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockXMasterRecord extends MetaStockElement {

    private String symbol;
    private String description;
    private String period;
    private int fileNumber;
    private Date startDate;
    private Date endDate;
    private Date firstDate;
    private Date lastDate;

    MetaStockXMasterRecord(LittleEndianDataInputStream is) throws IOException {
        super(is);
    }

    @Override
    int encode(byte[] buffer) {
        return 0;
    }

    @Override
    void parse() throws IOException {
        Skip(1);
        symbol = readString(15);
        description = readString(46);
        period = readString(1);
        Skip(2);
        fileNumber = readUnsignedShort();
        Skip(13);
        startDate = readIntDate();
        Skip(20);
        endDate = readIntDate();
        firstDate = readIntDate();
        Skip(4);
        lastDate = readIntDate();
        Skip(30);
    }
}
