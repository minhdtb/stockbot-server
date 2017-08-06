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
public final class MetaStockXMaster extends MetaStock<MetaStockXMasterRecord> {

    private MetaStockXMasterHeader header;

    private LittleEndianDataInputStream inputStream;

    public MetaStockXMaster() {

    }

    public MetaStockXMaster(File file) {
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

        header = new MetaStockXMasterHeader(inputStream);
        for (int i = 0; i < header.count(); i++) {
            MetaStockXMasterRecord data = new MetaStockXMasterRecord(inputStream);
            getRecords().add(data);
        }
    }

    @Override
    protected void addRecord(MetaStockXMasterRecord record) {

    }

    @Override
    public void save(File file) throws IOException {
        LittleEndianDataOutputStream outputStream = new LittleEndianDataOutputStream(new FileOutputStream(file));
        header = new MetaStockXMasterHeader((short) getRecords().size(), (short) getRecords().size(), (short) getRecords().size());
        header.write(outputStream);
        for (MetaStockXMasterRecord record : getRecords()) {
            record.write(outputStream);
        }
    }
}
