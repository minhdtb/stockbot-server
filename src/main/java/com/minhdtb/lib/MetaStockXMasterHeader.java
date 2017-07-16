package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockXMasterHeader extends MetaStockElement {

    private int totalFiles;
    private int totalFilesEx;
    private int lastFileNumber;

    public MetaStockXMasterHeader(int totalFiles, int totalFilesEx, int lastFileNumber) {
        this.totalFiles = totalFiles;
        this.totalFilesEx = totalFilesEx;
        this.lastFileNumber = lastFileNumber;
    }

    MetaStockXMasterHeader(LittleEndianDataInputStream is) throws IOException {
        this.is = is;
        this.parse();
    }

    @Override
    int encode(byte[] buffer, int i) {
        return 0;
    }

    @Override
    void parse() throws IOException {
        Skip(10);
        totalFiles = readUnsignedShort();
        Skip(2);
        totalFilesEx = readUnsignedShort();
        Skip(2);
        lastFileNumber = readUnsignedShort();
        Skip(130);
    }
}
