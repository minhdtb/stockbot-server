package com.minhdtb.lib.data;

import com.google.common.io.LittleEndianDataInputStream;
import com.minhdtb.lib.base.MetaStockElement;
import com.minhdtb.lib.base.MetaStockHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
final class MetaStockDataHeader extends MetaStockElement implements MetaStockHeader {

    private int totalRecords;
    private int lastRecord;

    MetaStockDataHeader(short totalRecords, short lastRecord) {
        this.totalRecords = totalRecords;
        this.lastRecord = lastRecord;
    }

    MetaStockDataHeader(LittleEndianDataInputStream is) throws IOException {
        super(is);
    }

    @Override
    protected int encode(byte[] buffer) {
        int len = 0;

        byte[] tmpBuffer = getShortArray((short) totalRecords);
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getShortArray((short) lastRecord);
        len += copyBuffer(tmpBuffer, buffer, len);

        len += 24;

        return len;
    }

    @Override
    protected void parse() throws IOException {
        totalRecords = readUnsignedShort();
        lastRecord = readUnsignedShort();
        Skip(24);
    }


    @Override
    public int count() {
        return getLastRecord() - 1;
    }
}
