package com.minhdtb.lib.master;

import com.google.common.io.LittleEndianDataInputStream;
import com.minhdtb.lib.base.MetaStockElement;
import com.minhdtb.lib.base.MetaStockHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public class MetaStockMasterHeader extends MetaStockElement implements MetaStockHeader {

    private int totalFiles;
    private int lastFileNumber;

    MetaStockMasterHeader(int totalFiles, int lastFileNumber) {
        this.totalFiles = totalFiles;
        this.lastFileNumber = lastFileNumber;
    }

    MetaStockMasterHeader(LittleEndianDataInputStream is) throws IOException {
        super(is);
    }

    @Override
    protected int encode(byte[] buffer) {
        int len = 0;

        byte[] tmpBuffer = getShortArray((short) totalFiles);
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getShortArray((short) lastFileNumber);
        len += copyBuffer(tmpBuffer, buffer, len);

        len += 49;

        return len;
    }

    @Override
    protected void parse() throws IOException {
        totalFiles = readUnsignedShort();
        lastFileNumber = readUnsignedShort();
        Skip(49);
    }

    @Override
    public int count() {
        return getTotalFiles();
    }
}
