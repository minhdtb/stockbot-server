import com.google.common.io.LittleEndianDataInputStream;
import com.minhdtb.lib.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
