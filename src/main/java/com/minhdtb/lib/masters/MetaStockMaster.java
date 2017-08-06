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

    private LittleEndianDataOutputStream outputStream;
    private LittleEndianDataInputStream inputStream;

    public MetaStockMaster(File file) {
        try {
            if (file.exists()) {
                this.outputStream = new LittleEndianDataOutputStream(new FileOutputStream(file, true));
            } else {
                this.outputStream = new LittleEndianDataOutputStream(new FileOutputStream(file));
            }

            this.inputStream = new LittleEndianDataInputStream(new FileInputStream(file));

            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void load() throws IOException {
        getRecords().clear();

        header = new MetaStockMasterHeader(inputStream, outputStream);
        for (int i = 0; i < header.count(); i++) {
            MetaStockMasterRecord data = new MetaStockMasterRecord(inputStream, outputStream);
            getRecords().add(data);
        }
    }

    @Override
    protected void addRecord(MetaStockMasterRecord record) {

    }

    @Override
    public void save() throws IOException {
        header = new MetaStockMasterHeader((short) getRecords().size(), (short) getRecords().size());
        write(header);
    }

}
