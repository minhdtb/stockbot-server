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
public class MetaStockMasterRecord extends MetaStockElement {

    @DataField(length = 1)
    private int fileNumber;

    @DataField(length = 2)
    private String fileType;

    @DataField(length = 1)
    private int recordLength;

    @DataField(length = 1)
    private int numberOfFields;

    @DataField(length = 2)
    private byte[] spare1;

    @DataField(length = 16)
    private String description;

    @DataField(length = 1)
    private byte[] spare2;

    @DataField(length = 1)
    private String version;

    @DataField(length = 4, type = DataType.MBF)
    private Date startDate;

    @DataField(length = 4, type = DataType.MBF)
    private Date endDate;

    @DataField(length = 1)
    private String period;

    @DataField(length = 2)
    private byte[] spare3;

    @DataField(length = 14)
    private String symbol;

    @DataField(length = 1)
    private byte[] spare4;

    @DataField(length = 1)
    private String flag;

    @DataField(length = 1)
    private byte[] spare5;

    public MetaStockMasterRecord(String symbol, String description, String period, int fileNumber, String fileType,
                                 int numberOfFields, String version, Date startDate, Date endDate) {
        this.symbol = symbol;
        this.description = description;
        this.period = period;
        this.fileNumber = fileNumber;
        this.fileType = fileType;
        this.numberOfFields = numberOfFields;
        this.recordLength = numberOfFields * 4;
        this.version = version;
        this.startDate = startDate;
        this.endDate = endDate;
        this.flag = " ";
    }

    MetaStockMasterRecord(InputStream inputStream) throws IOException {
        super(inputStream);
    }
}
