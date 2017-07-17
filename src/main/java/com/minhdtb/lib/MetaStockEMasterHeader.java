package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockEMasterHeader extends MetaStockElement {

    private int totalFiles;
    private int lastFileNumber;

    public MetaStockEMasterHeader(int totalFiles, int lastFileNumber) {
        this.totalFiles = totalFiles;
        this.lastFileNumber = lastFileNumber;
    }

    MetaStockEMasterHeader(LittleEndianDataInputStream is) throws IOException {
        super(is);
    }

    @Override
    int encode(byte[] buffer, int i) {
        return 0;
    }

    @Override
    void parse() throws IOException {
        totalFiles = readUnsignedShort();
        lastFileNumber = readUnsignedShort();
        Skip(188);
    }
}
