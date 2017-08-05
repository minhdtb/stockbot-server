package com.minhdtb.lib.masters;

import com.minhdtb.lib.base.MetaStock;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockXMaster extends MetaStock<MetaStockXMasterRecord> {

    private MetaStockXMasterHeader header;


    public MetaStockXMaster(File file) {
        super(file);
    }

    @Override
    protected void load() throws IOException {
        getRecords().clear();

        header = new MetaStockXMasterHeader(is);
        for (int i = 0; i < header.count(); i++) {
            MetaStockXMasterRecord data = new MetaStockXMasterRecord(is);
            getRecords().add(data);
        }
    }

    @Override
    public void save() throws IOException {
        header = new MetaStockXMasterHeader((short) getRecords().size(), (short) getRecords().size(), (short) getRecords().size());
        write(header, os);
    }
}
