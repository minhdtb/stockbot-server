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
        super(is);
    }

    @Override
    int encode(byte[] buffer) {
        int len = 2;

        byte[] tmpBuffer = getByteArray((byte) fileNumber);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += 3;

        tmpBuffer = getByteArray((byte) totalFields);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += 4;

        tmpBuffer = getStringArray(symbol);
        len += copyBuffer(tmpBuffer, buffer, len);

        return len;
    }

    @Override
    void parse() throws IOException {
        Skip(2);
        fileNumber = readUnsignedByte();
        Skip(3);
        totalFields = readUnsignedByte();
        Skip(4);
        symbol = readString(14);
        Skip(7);
        description = readString(16);
        Skip(12);
        period = readString(1);
        Skip(3);
        startDate = readFloatDate();
        Skip(4);
        endDate = readFloatDate();
        Skip(116);
    }
}
