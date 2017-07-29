package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockEMasterRecord extends MetaStockElement {

    private String symbol;
    private String description;
    private String period;
    private int fileNumber;
    private int totalFields;
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
        int len = 0;

        byte[] tmpBuffer = {0x36, 0x36}; // Asc symbol
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getByteArray((byte) fileNumber);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += 3;

        tmpBuffer = getByteArray((byte) totalFields);
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = new byte[]{0x7f}; // Delete symbol
        len += copyBuffer(tmpBuffer, buffer, len);
        len += 1;

        tmpBuffer = new byte[]{0x20}; // Space symbol
        len += copyBuffer(tmpBuffer, buffer, len);
        len += 1;

        symbol = symbol.length() > 14 ? symbol.substring(0, 14) : symbol;
        tmpBuffer = getStringArray(symbol);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += (14 - symbol.length()) + 7;

        description = description.length() > 16 ? description.substring(0, 16) : description;
        tmpBuffer = getStringArray(description);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += (16 - description.length()) + 12;

        period = period.length() > 1 ? period.substring(0, 1) : period;
        tmpBuffer = getStringArray(period);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += (1 - period.length()) + 3;

        tmpBuffer = getFloatArray(DateToInt(startDate, true));
        len += copyBuffer(tmpBuffer, buffer, len);
        len += 4;

        tmpBuffer = getFloatArray(DateToInt(endDate, true));
        len += copyBuffer(tmpBuffer, buffer, len);
        len += 116;

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
