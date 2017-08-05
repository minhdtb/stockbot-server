package com.minhdtb.lib.data;

import com.minhdtb.lib.base.MetaStock;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockData extends MetaStock<MetaStockDataRecord> {

    private MetaStockDataHeader header;

    public MetaStockData(File file) {
        super(file);
    }

    @Override
    protected void load() throws IOException {
        getRecords().clear();

        header = new MetaStockDataHeader(is);
        for (int i = 0; i < header.count(); i++) {
            MetaStockDataRecord data = new MetaStockDataRecord(is);
            getRecords().add(data);
        }
    }

    @Override
    public void save() throws IOException {
        header = new MetaStockDataHeader((short) getRecords().size(), (short) (getRecords().size() + 1));
        write(header, os);
    }
}
