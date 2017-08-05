package com.minhdtb.lib.master;

import com.google.common.io.LittleEndianDataInputStream;
import com.minhdtb.lib.annotations.DataField;
import com.minhdtb.lib.base.MetaStockElement;
import com.minhdtb.lib.base.MetaStockHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
final class MetaStockXMasterHeader extends MetaStockElement implements MetaStockHeader {

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

    MetaStockXMasterHeader(LittleEndianDataInputStream is) throws IOException {
        super(is);
    }

    @Override
    public int count() {
        return getTotalFiles();
    }
}
