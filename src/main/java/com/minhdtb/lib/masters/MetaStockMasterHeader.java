package com.minhdtb.lib.masters;

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
public class MetaStockMasterHeader extends MetaStockElement implements MetaStockHeader {

    @DataField(length = 2)
    private int totalFiles;

    @DataField(length = 2)
    private int lastFileNumber;

    @DataField(length = 49)
    private byte[] spare;

    MetaStockMasterHeader(int totalFiles, int lastFileNumber) {
        this.totalFiles = totalFiles;
        this.lastFileNumber = lastFileNumber;
    }

    MetaStockMasterHeader(InputStream inputStream, OutputStream outputStream) throws IOException {
        super(inputStream, outputStream);
    }

    @Override
    public int count() {
        return getTotalFiles();
    }
}
