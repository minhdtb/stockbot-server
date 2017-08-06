import com.minhdtb.lib.data.MetaStockData;
import com.minhdtb.lib.data.MetaStockDataRecord;
import com.minhdtb.lib.masters.MetaStockEMaster;
import com.minhdtb.lib.masters.MetaStockEMasterRecord;
import com.minhdtb.lib.masters.MetaStockMaster;
import com.minhdtb.lib.masters.MetaStockMasterRecord;
import com.minhdtb.lib.masters.MetaStockXMaster;
import com.minhdtb.lib.masters.MetaStockXMasterRecord;

import java.io.File;
import java.util.Date;

public class TestApp {

    public static void main(String[] args) {
        try {
            // read data file
            MetaStockData data = new MetaStockData(new File("E:\\test\\F1037.MWD"));
            System.out.println(data.getHeader());
            MetaStockDataRecord record = data.getRecords().get(0);
            System.out.println(record.getDate());
            System.out.println(record.getOpen());

            // read emaster file
            MetaStockEMaster eMaster = new MetaStockEMaster(new File("E:\\test\\EMASTER"));
            System.out.println(eMaster.getHeader());
            MetaStockEMasterRecord eMasterRecord = eMaster.getRecords().get(0);
            System.out.println(eMasterRecord);

            // read xmaster file
            MetaStockXMaster xMaster = new MetaStockXMaster(new File("E:\\test\\XMASTER"));
            System.out.println(xMaster.getHeader());
            MetaStockXMasterRecord xMasterRecord = xMaster.getRecords().get(xMaster.getRecords().size() - 1);
            System.out.println(xMasterRecord);

            // read masters file
            MetaStockMaster master = new MetaStockMaster(new File("E:\\test\\MASTER"));
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
            eMaster1.save(new File("E:\\test2\\EMASTER"));


            MetaStockMaster master1 = new MetaStockMaster();
            MetaStockMasterRecord record2 = new MetaStockMasterRecord("AAA", "AAA", "D", 1,
                    "e", 7, null, new Date(), new Date());

            master1.getRecords().add(record2);
            MetaStockMasterRecord record3 = new MetaStockMasterRecord("XXXYYY", "XXXYYY", "D", 2,
                    "e", 7, null, new Date(), new Date());

            master1.getRecords().add(record3);
            master1.save(new File("E:\\test2\\MASTER"));

            MetaStockXMaster xMaster1 = new MetaStockXMaster();
            MetaStockXMasterRecord xMasterRecord1 = new MetaStockXMasterRecord("XYZT", "XYZT", "D", 1000, new Date(),
                    new Date(), new Date(), new Date());

            xMaster1.getRecords().add(xMasterRecord1);
            xMaster1.save(new File("E:\\test2\\XMASTER"));

            MetaStockData data1 = new MetaStockData(new File("E:\\test2\\F1.DAT"));
            MetaStockDataRecord dataRecord = new MetaStockDataRecord(new Date(), 10, 10, 10, 10, 10, 10);
            data1.getRecords().add(dataRecord);
            data1.save(new File("E:\\test2\\F1.DAT"));

            //============================================================


            MetaStockMaster master2 = new MetaStockMaster(new File("E:\\test2\\MASTER"));
            System.out.println(master2.getHeader());
            System.out.println(master2.getRecords().get(0));

            MetaStockXMaster xMaster2 = new MetaStockXMaster(new File("E:\\test2\\XMASTER"));
            System.out.println(xMaster2.getHeader());
            System.out.println(xMaster2.getRecords().get(0));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
