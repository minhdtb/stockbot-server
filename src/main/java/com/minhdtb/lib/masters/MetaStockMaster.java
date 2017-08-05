package com.minhdtb.lib.masters;

import com.minhdtb.lib.base.MetaStock;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public class MetaStockMaster extends MetaStock<MetaStockMasterRecord> {

    private MetaStockMasterHeader header;


    public MetaStockMaster(File file) {
        super(file);
    }

    @Override
    protected void load() throws IOException {
        getRecords().clear();

        header = new MetaStockMasterHeader(is);
        for (int i = 0; i < header.count(); i++) {
            MetaStockMasterRecord data = new MetaStockMasterRecord(is);
            getRecords().add(data);
        }
    }

    @Override
    public void save() throws IOException {
        header = new MetaStockMasterHeader((short) getRecords().size(), (short) getRecords().size());
        write(header, os);
    }

}
