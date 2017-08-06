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
public final class MetaStockEMaster extends MetaStock<MetaStockEMasterRecord> {

    private MetaStockEMasterHeader header;

    private LittleEndianDataOutputStream outputStream;
    private LittleEndianDataInputStream inputStream;

    public MetaStockEMaster(File file) {
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

        header = new MetaStockEMasterHeader(inputStream, outputStream);
        for (int i = 0; i < header.count(); i++) {
            MetaStockEMasterRecord data = new MetaStockEMasterRecord(inputStream, outputStream);
            getRecords().add(data);
        }
    }

    @Override
    protected void addRecord(MetaStockEMasterRecord record) {

    }

    @Override
    public void save() throws IOException {
        header = new MetaStockEMasterHeader((short) getRecords().size(), (short) getRecords().size());
        write(header);
    }
}
