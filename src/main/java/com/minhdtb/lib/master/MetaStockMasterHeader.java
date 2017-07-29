package com.minhdtb.lib.master;

import com.minhdtb.lib.base.MetaStockElement;
import com.minhdtb.lib.base.MetaStockHeader;

import java.io.IOException;

public class MetaStockMasterHeader extends MetaStockElement implements MetaStockHeader {

    @Override
    protected int encode(byte[] buffer) {
        return 0;
    }

    @Override
    protected void parse() throws IOException {

    }

    @Override
    public int count() {
        return 0;
    }
}
