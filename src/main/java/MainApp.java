import com.google.common.io.LittleEndianDataInputStream;
import com.minhdtb.lib.MetaStockEMaster;
import com.minhdtb.lib.MetaStockEMasterRecord;
import com.minhdtb.lib.MetaStockXMaster;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainApp {

    public static void main(String[] args) {

        try {
            MetaStockEMaster master = new MetaStockEMaster(new LittleEndianDataInputStream(new FileInputStream("D:\\Projects\\stocktest\\EMASTER")));
            MetaStockEMasterRecord record = master.getRecords().get(1);
            System.out.println(record.getSymbol());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
