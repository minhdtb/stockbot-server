package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public class MetaStockDataHeader extends MetaStockElement {

    private int totalRecords;
    private int lastRecord;

    public MetaStockDataHeader(short totalRecords, short lastRecord) {
        this.totalRecords = totalRecords;
        this.lastRecord = lastRecord;
    }

    MetaStockDataHeader(LittleEndianDataInputStream is) throws IOException {
        totalRecords = is.readUnsignedShort();
        lastRecord = is.readUnsignedShort();
        is.read(new byte[24]);
    }

    @Override
    int encode(byte[] buffer, int i) {
        return 0;
    }
}