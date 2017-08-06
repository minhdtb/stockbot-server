package com.minhdtb.lib.data;

import com.google.common.io.LittleEndianDataInputStream;
import com.minhdtb.lib.annotations.DataField;
import com.minhdtb.lib.base.MetaStockElement;
import com.minhdtb.lib.base.MetaStockHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@EqualsAndHashCode(callSuper = true)
@Data
final class MetaStockDataHeader extends MetaStockElement implements MetaStockHeader {

    @DataField(length = 2)
    private int totalRecords;

    @DataField(length = 2)
    private int lastRecord;

    @DataField(length = 24)
    private byte[] spare;

    MetaStockDataHeader(short totalRecords, short lastRecord) {
        this.totalRecords = totalRecords;
        this.lastRecord = lastRecord;
    }

    MetaStockDataHeader(InputStream inputStream, OutputStream outputStream) throws IOException {
        super(inputStream, outputStream);
    }

    @Override
    public int count() {
        return getLastRecord() - 1;
    }
}
