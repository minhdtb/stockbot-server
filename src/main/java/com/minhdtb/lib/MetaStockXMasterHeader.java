package com.minhdtb.lib;

import com.google.common.io.LittleEndianDataInputStream;
import lombok.Data;

import java.io.IOException;

@Data
public class MetaStockXMasterHeader extends MetaStockElement {

    private int totalFiles;
    private int totalFilesEx;
    private int lastFileNumber;

    public MetaStockXMasterHeader(int totalFiles, int totalFilesEx, int lastFileNumber) {
        this.totalFiles = totalFiles;
        this.totalFilesEx = totalFilesEx;
        this.lastFileNumber = lastFileNumber;
    }

    MetaStockXMasterHeader(LittleEndianDataInputStream is) throws IOException {
        is.read(new byte[10]);
        totalFiles = is.readUnsignedShort();
        is.read(new byte[2]);
        totalFilesEx = is.readUnsignedShort();
        is.read(new byte[2]);
        lastFileNumber = is.readUnsignedShort();
        is.read(new byte[130]);
    }

    @Override
    int encode(byte[] buffer, int i) {
        return 0;
    }
}
