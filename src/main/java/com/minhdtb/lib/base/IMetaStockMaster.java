package com.minhdtb.lib.base;

public interface IMetaStockMaster<T extends MetaStockElement> {
    void updateRecord(T record);
}
