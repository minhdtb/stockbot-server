package com.minhdtb.lib.master;

import com.google.common.io.LittleEndianDataInputStream;
import com.minhdtb.lib.base.MetaStockElement;
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

    public MetaStockXMasterRecord(String symbol, String description, String period, int fileNumber, Date startDate,
                                  Date endDate, Date firstDate, Date lastDate) {
        this.symbol = symbol;
        this.description = description;
        this.period = period;
        this.fileNumber = fileNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.firstDate = firstDate;
        this.lastDate = lastDate;
    }

    MetaStockXMasterRecord(LittleEndianDataInputStream is) throws IOException {
        super(is);
    }

    @Override
    protected int encode(byte[] buffer) {
        int len = 0;

        byte[] tmpBuffer = {0x01}; // Start byte
        len += copyBuffer(tmpBuffer, buffer, len);

        symbol = symbol.length() > 15 ? symbol.substring(0, 15) : symbol;
        tmpBuffer = getStringArray(symbol);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += (15 - symbol.length());

        description = description.length() > 46 ? description.substring(0, 46) : description;
        tmpBuffer = getStringArray(description);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += (46 - description.length());

        period = period.length() > 1 ? period.substring(0, 1) : period;
        tmpBuffer = getStringArray(period);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += (1 - period.length()) + 2;

        tmpBuffer = getShortArray((short) fileNumber);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += 3;

        tmpBuffer = new byte[]{0x7f}; // Delete symbol
        len += copyBuffer(tmpBuffer, buffer, len);
        len += 9;

        tmpBuffer = getIntArray(DateToInt(startDate, false));
        len += copyBuffer(tmpBuffer, buffer, len);
        len += 20;

        tmpBuffer = getIntArray(DateToInt(endDate, false));
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getIntArray(DateToInt(firstDate, false));
        len += copyBuffer(tmpBuffer, buffer, len);
        len += 4;

        tmpBuffer = getIntArray(DateToInt(lastDate, false));
        len += copyBuffer(tmpBuffer, buffer, len);
        len += 30;

        return len;
    }

    @Override
    protected void parse() throws IOException {
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
