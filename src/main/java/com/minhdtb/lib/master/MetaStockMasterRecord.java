package com.minhdtb.lib.master;

import com.google.common.io.LittleEndianDataInputStream;
import com.minhdtb.lib.base.MetaStockElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class MetaStockMasterRecord extends MetaStockElement {

    private String symbol;
    private String description;
    private String period;
    private int fileNumber;
    private String fileType;
    private int recordLength;
    private int numberOfFields;
    private String version;
    private Date startDate;
    private Date endDate;

    public MetaStockMasterRecord(String symbol, String description, String period, int fileNumber, String fileType,
                                 int numberOfFields, String version, Date startDate, Date endDate) {
        this.symbol = symbol;
        this.description = description;
        this.period = period;
        this.fileNumber = fileNumber;
        this.fileType = fileType;
        this.numberOfFields = numberOfFields;
        this.recordLength = numberOfFields * 4;
        this.version = version;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    MetaStockMasterRecord(LittleEndianDataInputStream is) throws IOException {
        super(is);
    }

    @Override
    protected int encode(byte[] buffer) {
        int len = 0;

        byte[] tmpBuffer = getByteArray((byte) fileNumber);
        len += copyBuffer(tmpBuffer, buffer, len);

        fileType = fileType.length() > 2 ? fileType.substring(0, 2) : fileType;
        tmpBuffer = getStringArray(fileType);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += (2 - fileType.length());

        tmpBuffer = getByteArray((byte) recordLength);
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getByteArray((byte) numberOfFields);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += 2;

        description = description.length() > 16 ? description.substring(0, 16) : description;
        tmpBuffer = getStringArray(description);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += (16 - description.length()) + 1;

        tmpBuffer = new byte[]{0x00}; // Version = null
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getFloatArray(FloatToMBF(DateToInt(startDate, true)));
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getFloatArray(FloatToMBF(DateToInt(endDate, true)));
        len += copyBuffer(tmpBuffer, buffer, len);

        period = period.length() > 1 ? period.substring(0, 1) : period;
        tmpBuffer = getStringArray(period);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += (1 - period.length()) + 2;

        symbol = symbol.length() > 14 ? symbol.substring(0, 14) : symbol;
        tmpBuffer = getStringArray(symbol);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += (14 - symbol.length()) + 3;

        return len;
    }

    @Override
    protected void parse() throws IOException {
        fileNumber = readUnsignedByte();
        fileType = readString(2);
        Skip(1);
        numberOfFields = readUnsignedByte();
        recordLength = numberOfFields * 4;
        Skip(2);
        description = readString(16);
        Skip(1);
        version = readString(1);
        startDate = readMBFDate();
        endDate = readMBFDate();
        period = readString(1);
        Skip(2);
        symbol = readString(14);
        Skip(3);
    }
}
