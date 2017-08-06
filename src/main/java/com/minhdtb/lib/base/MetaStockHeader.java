package com.minhdtb.lib.base;

import java.io.IOException;
import java.io.InputStream;

public abstract class MetaStockHeader extends MetaStockElement implements IMetaStockHeader {

    protected MetaStockHeader() {

    }

    public MetaStockHeader(InputStream inputStream) throws IOException {
        super(inputStream);
    }
}
