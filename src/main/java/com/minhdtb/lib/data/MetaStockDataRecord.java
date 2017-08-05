package com.minhdtb.lib.data;

import com.google.common.io.LittleEndianDataInputStream;
import com.minhdtb.lib.annotations.DataField;
import com.minhdtb.lib.annotations.DataType;
import com.minhdtb.lib.base.MetaStockElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockDataRecord extends MetaStockElement {

    @DataField(type = DataType.MBF)
    private Date date;

    @DataField(length = 4)
    private float open;

    @DataField(length = 4)
    private float high;

    @DataField(length = 4)
    private float low;

    @DataField(length = 4)
    private float close;

    @DataField(length = 4)
    private float volume;

    @DataField(length = 4)
    private float openInterest;

    public MetaStockDataRecord(Date date, float open, float high, float low, float close, float volume, float openInterest) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.openInterest = openInterest;
    }

    MetaStockDataRecord(LittleEndianDataInputStream is) throws IOException {
        super(is);
    }
}
