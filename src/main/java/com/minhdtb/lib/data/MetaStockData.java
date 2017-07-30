package com.minhdtb.lib.data;


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
public final class MetaStockData extends MetaStock<MetaStockDataRecord> {

    private MetaStockDataHeader header;

    public MetaStockData() {

    }

    public MetaStockData(File file) {
        try {
            LittleEndianDataInputStream is = new LittleEndianDataInputStream(new FileInputStream(file));
            header = new MetaStockDataHeader(is);
            for (int i = 0; i < header.count() - 1; i++) {
                MetaStockDataRecord data = new MetaStockDataRecord(is);
                getRecords().add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(File file) throws IOException {
        header = new MetaStockDataHeader((short) getRecords().size(), (short) getRecords().size());
        write(header, new LittleEndianDataOutputStream(new FileOutputStream(file)));
    }
}
