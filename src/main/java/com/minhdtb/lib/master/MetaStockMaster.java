package com.minhdtb.lib.master;

import com.google.common.io.LittleEndianDataOutputStream;
import com.minhdtb.lib.base.MetaStock;

import java.io.IOException;

public class MetaStockMaster extends MetaStock<MetaStockMasterRecord> {
    
    @Override
    protected void save(LittleEndianDataOutputStream os) throws IOException {

    }
}
