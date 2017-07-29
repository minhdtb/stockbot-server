package com.minhdtb.lib.master;

import com.google.common.io.LittleEndianDataInputStream;
import com.minhdtb.lib.base.MetaStockElement;
import com.minhdtb.lib.base.MetaStockHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
final class MetaStockXMasterHeader extends MetaStockElement implements MetaStockHeader {

    private int totalFiles;
    private int totalFilesExtend;
    private int lastFileNumber;

    MetaStockXMasterHeader(int totalFiles, int totalFilesExtend, int lastFileNumber) {
        this.totalFiles = totalFiles;
        this.totalFilesExtend = totalFilesExtend;
        this.lastFileNumber = lastFileNumber;
    }

    MetaStockXMasterHeader(LittleEndianDataInputStream is) throws IOException {
        super(is);
    }

    @Override
    protected int encode(byte[] buffer) {
        int len = 0;

        byte[] tmpBuffer = new byte[]{0x5D, (byte) 0xFE, 0x58, 0x4D, (byte) 0x8B, 0x02,
                0x59, (byte) 0xB2, (byte) 0xC1, (byte) 0x8F}; // magic header
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getShortArray((short) totalFiles);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += 2;

        tmpBuffer = getShortArray((short) totalFilesExtend);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += 2;

        tmpBuffer = getShortArray((short) lastFileNumber);
        len += copyBuffer(tmpBuffer, buffer, len);
        len += 130;

        return len;
    }

    @Override
    protected void parse() throws IOException {
        Skip(10);
        totalFiles = readUnsignedShort();
        Skip(2);
        totalFilesExtend = readUnsignedShort();
        Skip(2);
        lastFileNumber = readUnsignedShort();
        Skip(130);
    }

    @Override
    public int count() {
        return getTotalFiles() + 1;
    }
}
