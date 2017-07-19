package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockDataHeader extends MetaStockElement {

    private int totalRecords;
    private int lastRecord;

    public MetaStockDataHeader(short totalRecords, short lastRecord) {
        this.totalRecords = totalRecords;
        this.lastRecord = lastRecord;
    }

    MetaStockDataHeader(LittleEndianDataInputStream is) throws IOException {
        super(is);
    }

    @Override
    int encode(byte[] buffer) {
        int len = 0;

        byte[] tmpBuffer = getShortArray((short) totalRecords);
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getShortArray((short) lastRecord);
        len += copyBuffer(tmpBuffer, buffer, len);

        len += 24;

        return len;
    }

    @Override
    void parse() throws IOException {
        totalRecords = readUnsignedShort();
        lastRecord = readUnsignedShort();
        Skip(24);
    }
}
