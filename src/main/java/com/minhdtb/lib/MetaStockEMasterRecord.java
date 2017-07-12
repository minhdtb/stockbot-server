package com.minhdtb.lib;


import com.google.common.io.LittleEndianDataInputStream;

import java.io.IOException;
import java.util.Date;

public class MetaStockEMasterRecord extends MetaStockElement {

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
        is.read(new byte[2]);
        fileNumber = is.readUnsignedByte();
        is.read(new byte[3]);
        totalFields = is.readUnsignedByte();
        is.read(new byte[4]);
        byte[] symbolBuffer = new byte[14];
        is.read(symbolBuffer);
        symbol = new String(symbolBuffer).split("\0")[0];
        is.read(new byte[7]);
        byte[] descriptionBuffer = new byte[16];
        is.read(descriptionBuffer);
        description = new String(descriptionBuffer).split("\0")[0];
        is.read(new byte[12]);
        byte[] periodBuffer = new byte[1];
        is.read(periodBuffer);
        period = new String(periodBuffer);
        is.read(new byte[3]);
        startDate = DateFromSingle(is.readFloat());
        is.read(new byte[4]);
        endDate = DateFromSingle(is.readFloat());
        is.read(new byte[116]);
    }

    @Override
    int encode(byte[] buffer, int i) {
        return 0;
    }
}
