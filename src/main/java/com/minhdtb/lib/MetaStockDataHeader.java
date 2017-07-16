package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;

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
        this.is = is;
        this.parse();
    }

    @Override
    int encode(byte[] buffer, int i) {
        return 0;
    }

    @Override
    void parse() throws IOException {
        totalRecords = readUnsignedShort();
        lastRecord = readUnsignedShort();
        Skip(24);
    }
}
