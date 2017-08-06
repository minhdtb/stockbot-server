package com.minhdtb.lib.masters;

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

    private LittleEndianDataInputStream inputStream;

    public MetaStockMaster() {

    }

    public MetaStockMaster(File file) {
        try {
            this.inputStream = new LittleEndianDataInputStream(new FileInputStream(file));
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void load() throws IOException {
        getRecords().clear();

        header = new MetaStockMasterHeader(inputStream);
        for (int i = 0; i < header.count(); i++) {
            MetaStockMasterRecord data = new MetaStockMasterRecord(inputStream);
            getRecords().add(data);
        }
    }

    @Override
    protected void addRecord(MetaStockMasterRecord record) {

    }

    @Override
    public void save(File file) throws IOException {
        LittleEndianDataOutputStream outputStream = new LittleEndianDataOutputStream(new FileOutputStream(file));
        header = new MetaStockMasterHeader((short) getRecords().size(), (short) getRecords().size());
        header.write(outputStream);
        for (MetaStockMasterRecord record : getRecords()) {
            record.write(outputStream);
        }
    }
}
