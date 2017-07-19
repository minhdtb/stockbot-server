package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockXMasterHeader extends MetaStockElement {

    private int totalFiles;
    private int totalFilesExtend;
    private int lastFileNumber;

    public MetaStockXMasterHeader(int totalFiles, int totalFilesExtend, int lastFileNumber) {
        this.totalFiles = totalFiles;
        this.totalFilesExtend = totalFilesExtend;
        this.lastFileNumber = lastFileNumber;
    }

    MetaStockXMasterHeader(LittleEndianDataInputStream is) throws IOException {
        super(is);
    }

    @Override
    int encode(byte[] buffer) {
        return 0;
    }

    @Override
    void parse() throws IOException {
        Skip(10);
        totalFiles = readUnsignedShort();
        Skip(2);
        totalFilesExtend = readUnsignedShort();
        Skip(2);
        lastFileNumber = readUnsignedShort();
        Skip(130);
    }
}
