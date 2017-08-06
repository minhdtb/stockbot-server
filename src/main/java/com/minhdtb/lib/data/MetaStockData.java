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

    private LittleEndianDataInputStream inputStream;

    public MetaStockData() {

    }

    public MetaStockData(File file) {
        try {
            this.inputStream = new LittleEndianDataInputStream(new FileInputStream(file));
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() throws IOException {
        getRecords().clear();

        header = new MetaStockDataHeader(inputStream);
        for (int i = 0; i < header.count(); i++) {
            MetaStockDataRecord data = new MetaStockDataRecord(inputStream);
            getRecords().add(data);
        }
    }

    @Override
    public void addRecord(MetaStockDataRecord record) {

    }

    @Override
    public void save(File file) throws IOException {
        LittleEndianDataOutputStream outputStream = new LittleEndianDataOutputStream(new FileOutputStream(file));
        header = new MetaStockDataHeader((short) getRecords().size(), (short) (getRecords().size() + 1));
        header.write(outputStream);
        for (MetaStockDataRecord record : getRecords()) {
            record.write(outputStream);
        }
    }
}
