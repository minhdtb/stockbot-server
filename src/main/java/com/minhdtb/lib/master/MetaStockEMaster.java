package com.minhdtb.lib.master;

import com.minhdtb.lib.base.MetaStock;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockEMaster extends MetaStock<MetaStockEMasterRecord> {

    private MetaStockEMasterHeader header;

    public MetaStockEMaster(File file) {
        super(file);
    }

    @Override
    protected void load() throws IOException {
        getRecords().clear();

        header = new MetaStockEMasterHeader(is);
        for (int i = 0; i < header.count(); i++) {
            MetaStockEMasterRecord data = new MetaStockEMasterRecord(is);
            getRecords().add(data);
        }
    }

    @Override
    public void save() throws IOException {
        header = new MetaStockEMasterHeader((short) getRecords().size(), (short) getRecords().size());
        write(header, os);
    }
}
