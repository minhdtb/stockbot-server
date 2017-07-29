package com.minhdtb.lib.master;

import com.google.common.io.LittleEndianDataInputStream;
import com.minhdtb.lib.base.MetaStockElement;
import com.minhdtb.lib.base.MetaStockHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
final class MetaStockEMasterHeader extends MetaStockElement implements MetaStockHeader {

    private int totalFiles;
    private int lastFileNumber;

    MetaStockEMasterHeader(int totalFiles, int lastFileNumber) {
        this.totalFiles = totalFiles;
        this.lastFileNumber = lastFileNumber;
    }

    MetaStockEMasterHeader(LittleEndianDataInputStream is) throws IOException {
        super(is);
    }

    @Override
    protected int encode(byte[] buffer) {
        int len = 0;

        byte[] tmpBuffer = getShortArray((short) totalFiles);
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getShortArray((short) lastFileNumber);
        len += copyBuffer(tmpBuffer, buffer, len);

        len += 188;

        return len;
    }

    @Override
    protected void parse() throws IOException {
        totalFiles = readUnsignedShort();
        lastFileNumber = readUnsignedShort();
        Skip(188);
    }

    @Override
    public int count() {
        return getTotalFiles() + 1;
    }
}
