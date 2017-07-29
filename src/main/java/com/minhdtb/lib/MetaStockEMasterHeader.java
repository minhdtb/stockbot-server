package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@EqualsAndHashCode(callSuper = true)
@Data
final class MetaStockEMasterHeader extends MetaStockElement {

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
    int encode(byte[] buffer) {
        int len = 0;

        byte[] tmpBuffer = getShortArray((short) totalFiles);
        len += copyBuffer(tmpBuffer, buffer, len);

        tmpBuffer = getShortArray((short) lastFileNumber);
        len += copyBuffer(tmpBuffer, buffer, len);

        len += 188;

        return len;
    }

    @Override
    void parse() throws IOException {
        totalFiles = readUnsignedShort();
        lastFileNumber = readUnsignedShort();
        Skip(188);
    }
}
