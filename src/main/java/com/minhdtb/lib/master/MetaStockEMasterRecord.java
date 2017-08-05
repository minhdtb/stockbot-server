package com.minhdtb.lib.master;

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
public final class MetaStockEMasterRecord extends MetaStockElement {

    @DataField(length = 2)
    private byte[] spare;

    @DataField(length = 1)
    private int fileNumber;

    @DataField(length = 3)
    private byte[] spare1;

    @DataField(length = 1)
    private int totalFields;

    @DataField(length = 4)
    private byte[] spare2;

    @DataField(length = 14)
    private String symbol;

    @DataField(length = 7)
    private byte[] spare3;

    @DataField(length = 16)
    private String description;

    @DataField(length = 12)
    private byte[] spare4;

    @DataField(length = 1)
    private String period;

    @DataField(length = 3)
    private byte[] spare5;

    @DataField(type = DataType.FLOAT)
    private Date startDate;

    @DataField(length = 4)
    private byte[] spare6;

    @DataField(type = DataType.FLOAT)
    private Date endDate;

    @DataField(length = 116)
    private byte[] spare7;

    public MetaStockEMasterRecord(String symbol, String description, String period, int fileNumber,
                                  int totalFields, Date startDate, Date endDate) {
        this.fileNumber = fileNumber;
        this.totalFields = totalFields;
        this.symbol = symbol;
        this.description = description;
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    MetaStockEMasterRecord(LittleEndianDataInputStream is) throws IOException {
        super(is);
    }
}
