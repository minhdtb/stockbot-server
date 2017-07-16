package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockEMasterRecord extends MetaStockElement {

    private int fileNumber;
    private int totalFields;
    private String symbol;
    private String description;
    private String period;
    private Date startDate;
    private Date endDate;

    public MetaStockEMasterRecord(int fileNumber, int totalFields, String symbol, String description,
                                  String period, Date startDate, Date endDate) {
        this.fileNumber = fileNumber;
        this.totalFields = totalFields;
        this.symbol = symbol;
        this.description = description;
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    MetaStockEMasterRecord(LittleEndianDataInputStream is) throws IOException {
        this.is = is;
        this.parse();
    }

    @Override
    int encode(byte[] buffer, int i) {
        return 0;
    }

    @Override
    void parse() throws IOException {
        Skip(2);
        fileNumber = readUnsignedByte();
        Skip(3);
        totalFields = readUnsignedByte();
        Skip(4);
        symbol = readString(14);
        is.skip(7);
        description = readString(16);
        Skip(12);
        period = readString(1);
        Skip(3);
        startDate = readDate();
        Skip(4);
        endDate = readDate();
        Skip(116);
    }
}
