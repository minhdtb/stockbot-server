package com.minhdtb.lib.masters;

import com.minhdtb.lib.annotations.DataField;
import com.minhdtb.lib.base.MetaStockHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.io.InputStream;

@EqualsAndHashCode(callSuper = true)
@Data
public class MetaStockMasterHeader extends MetaStockHeader {

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

    MetaStockMasterHeader(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    @Override
    public int count() {
        return getTotalFiles();
    }
}
