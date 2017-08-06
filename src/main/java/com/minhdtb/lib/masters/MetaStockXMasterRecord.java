package com.minhdtb.lib.masters;

import com.minhdtb.lib.annotations.DataField;
import com.minhdtb.lib.annotations.DataType;
import com.minhdtb.lib.base.MetaStockElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public final class MetaStockXMasterRecord extends MetaStockElement {

    @DataField(length = 1)
    private byte[] magic;

    @DataField(length = 15)
    private String symbol;

    @DataField(length = 46)
    private String description;

    @DataField(length = 1)
    private String period;

    @DataField(length = 2)
    private byte[] spare1;

    @DataField(length = 2)
    private int fileNumber;

    @DataField(length = 13)
    private byte[] spare2;

    @DataField(length = 4, type = DataType.INTEGER)
    private Date startDate;

    @DataField(length = 20)
    private byte[] spare3;

    @DataField(length = 4, type = DataType.INTEGER)
    private Date endDate;

    @DataField(length = 4, type = DataType.INTEGER)
    private Date firstDate;

    @DataField(length = 4)
    private byte[] spare4;

    @DataField(length = 4, type = DataType.INTEGER)
    private Date lastDate;

    @DataField(length = 30)
    private byte[] spare5;

    public MetaStockXMasterRecord(String symbol, String description, String period, int fileNumber, Date startDate,
                                  Date endDate, Date firstDate, Date lastDate) {
        this.magic = new byte[]{0x01};
        this.symbol = symbol;
        this.description = description;
        this.period = period;
        this.fileNumber = fileNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.firstDate = firstDate;
        this.lastDate = lastDate;
    }

    MetaStockXMasterRecord(InputStream inputStream) throws IOException {
        super(inputStream);
    }
}
