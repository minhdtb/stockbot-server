package com.minhdtb.lib.masters;

import com.minhdtb.lib.annotations.DataField;
import com.minhdtb.lib.base.MetaStockHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.io.InputStream;

@EqualsAndHashCode(callSuper = true)
@Data
final class MetaStockXMasterHeader extends MetaStockHeader {

    @DataField(length = 10)
    private byte[] magic;

    @DataField(length = 2)
    private int totalFiles;

    @DataField(length = 2)
    private byte[] spare1;

    @DataField(length = 2)
    private int totalFilesExtend;

    @DataField(length = 2)
    private byte[] spare2;

    @DataField(length = 2)
    private int lastFileNumber;

    @DataField(length = 130)
    private byte[] spare3;

    MetaStockXMasterHeader(int totalFiles, int totalFilesExtend, int lastFileNumber) {
        this.magic = new byte[]{0x5D, (byte) 0xFE, 0x58, 0x4D, (byte) 0x8B, 0x02,
                0x59, (byte) 0xB2, (byte) 0xC1, (byte) 0x8F};

        this.totalFiles = totalFiles;
        this.totalFilesExtend = totalFilesExtend;
        this.lastFileNumber = lastFileNumber;
    }

    MetaStockXMasterHeader(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    @Override
    public int count() {
        return getTotalFiles();
    }
}
