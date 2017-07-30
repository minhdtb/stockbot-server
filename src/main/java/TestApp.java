import com.minhdtb.lib.data.MetaStockData;
import com.minhdtb.lib.data.MetaStockDataRecord;
import com.minhdtb.lib.master.*;

import java.util.Date;

public class TestApp {

    public static void main(String[] args) {
        try {
            // read data file
            MetaStockData data = new MetaStockData("E:\\test\\F1037.MWD");
            System.out.println(data.getHeader());
            MetaStockDataRecord record = data.getRecords().get(0);
            System.out.println(record.getDate());
            System.out.println(record.getOpen());

            // read emaster file
            MetaStockEMaster eMaster = new MetaStockEMaster("E:\\test\\EMASTER");
            System.out.println(eMaster.getHeader());
            MetaStockEMasterRecord eMasterRecord = eMaster.getRecords().get(0);
            System.out.println(eMasterRecord);

            // read xmaster file
            MetaStockXMaster xMaster = new MetaStockXMaster("E:\\test\\XMASTER");
            System.out.println(xMaster.getHeader());
            MetaStockXMasterRecord xMasterRecord = xMaster.getRecords().get(xMaster.getRecords().size() - 1);
            System.out.println(xMasterRecord);

            // read master file
            MetaStockMaster master = new MetaStockMaster("E:\\test\\MASTER");
            System.out.println(master.getHeader());
            MetaStockMasterRecord record1 = master.getRecords().get(0);
            System.out.println(record1);

            //===============================================
            MetaStockEMaster eMaster1 = new MetaStockEMaster();
            MetaStockEMasterRecord eMasterRecord1 = new MetaStockEMasterRecord(
                    "AAA",
                    "AAA",
                    "D",
                    1,
                    7, new Date(), new Date());

            eMaster1.getRecords().add(eMasterRecord1);

            eMaster1.save("E:\\test2\\EMASTER");


            MetaStockMaster master1 = new MetaStockMaster();
            MetaStockMasterRecord record2 = new MetaStockMasterRecord("AAA", "AAA", "D", 1,
                    "e", 7, null, new Date(), new Date());

            master1.getRecords().add(record2);
            MetaStockMasterRecord record3 = new MetaStockMasterRecord("XXXYYY", "XXXYYY", "D", 1,
                    "e", 7, null, new Date(), new Date());

            master1.getRecords().add(record3);
            master1.save("E:\\test2\\MASTER");

            MetaStockXMaster xMaster1 = new MetaStockXMaster();
            MetaStockXMasterRecord xMasterRecord1 = new MetaStockXMasterRecord("XYZT", "XYZT", "D", 1000, new Date(),
                    new Date(), new Date(), new Date());

            xMaster1.getRecords().add(xMasterRecord1);
            xMaster1.save("E:\\test2\\XMASTER");

            MetaStockData data1 = new MetaStockData();
            MetaStockDataRecord dataRecord = new MetaStockDataRecord(new Date(), 10, 10, 10, 10, 10, 10);
            data1.getRecords().add(dataRecord);
            data1.save("E:\\test2\\F1.DAT");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
