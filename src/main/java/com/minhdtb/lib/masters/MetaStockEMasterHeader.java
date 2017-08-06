package com.minhdtb.lib.masters;

import com.minhdtb.lib.annotations.DataField;
import com.minhdtb.lib.base.MetaStockElement;
import com.minhdtb.lib.base.MetaStockHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.io.InputStream;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockEMasterHeader extends MetaStockElement implements MetaStockHeader {

    @DataField(length = 2)
    private int totalFiles;

    @DataField(length = 2)
    private int lastFileNumber;

    @DataField(length = 188)
    private byte[] spare;

    MetaStockEMasterHeader(int totalFiles, int lastFileNumber) {
        this.totalFiles = totalFiles;
        this.lastFileNumber = lastFileNumber;
    }

    MetaStockEMasterHeader(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    @Override
    public int count() {
        return getTotalFiles();
    }
}
