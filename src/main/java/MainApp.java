import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import com.minhdtb.lib.data.MetaStockData;
import com.minhdtb.lib.data.MetaStockDataRecord;
import com.minhdtb.lib.master.MetaStockEMaster;
import com.minhdtb.lib.master.MetaStockEMasterRecord;
import com.minhdtb.lib.master.MetaStockXMaster;
import com.minhdtb.lib.master.MetaStockXMasterRecord;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

public class MainApp {

    public static void main(String[] args) {

        try {
            MetaStockData data = new MetaStockData(new LittleEndianDataInputStream(new FileInputStream("E:\\test\\F1037.MWD")));
            System.out.println(data.getHeader());
            MetaStockDataRecord record = data.getRecords().get(0);
            System.out.println(record.getDate());
            System.out.println(record.getOpen());

            MetaStockEMaster eMaster = new MetaStockEMaster(new LittleEndianDataInputStream(new FileInputStream("E:\\test\\EMASTER")));
            System.out.println(eMaster.getHeader());
            MetaStockEMasterRecord eMasterRecord = eMaster.getRecords().get(0);
            System.out.println(eMasterRecord);

            MetaStockXMaster xMaster = new MetaStockXMaster(new LittleEndianDataInputStream(new FileInputStream("E:\\test\\XMASTER")));
            System.out.println(xMaster.getHeader());
            MetaStockXMasterRecord xMasterRecord = xMaster.getRecords().get(xMaster.getRecords().size() - 1);
            System.out.println(xMasterRecord);
            //===============================================
            MetaStockEMaster eMaster1 = new MetaStockEMaster();
            MetaStockEMasterRecord eMasterRecord1 = new MetaStockEMasterRecord(1, 7, "AAA",
                    "AAA", "D", new Date(), new Date());
            eMaster1.getRecords().add(eMasterRecord1);

            eMaster1.save(new LittleEndianDataOutputStream(new FileOutputStream("E:\\test2\\EMASTER")));

            MetaStockData data1 = new MetaStockData();
            MetaStockDataRecord dataRecord = new MetaStockDataRecord(new Date(), 10, 10, 10, 10, 10, 10);
            data1.getRecords().add(dataRecord);
            data1.save(new LittleEndianDataOutputStream(new FileOutputStream("E:\\test2\\F1.DAT")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
