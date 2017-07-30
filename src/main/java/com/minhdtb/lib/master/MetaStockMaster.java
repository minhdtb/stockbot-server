package com.minhdtb.lib.master;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import com.minhdtb.lib.base.MetaStock;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public class MetaStockMaster extends MetaStock<MetaStockMasterRecord> {

    private MetaStockMasterHeader header;

    public MetaStockMaster() {

    }

    public MetaStockMaster(File file) {
        try {
            LittleEndianDataInputStream is = new LittleEndianDataInputStream(new FileInputStream(file));
            header = new MetaStockMasterHeader(is);
            for (int i = 0; i < header.count(); i++) {
                MetaStockMasterRecord data = new MetaStockMasterRecord(is);
                getRecords().add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(File file) throws IOException {
        header = new MetaStockMasterHeader((short) getRecords().size(), (short) getRecords().size());
        write(header, new LittleEndianDataOutputStream(new FileOutputStream(file)));
    }
}
