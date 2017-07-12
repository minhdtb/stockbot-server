import com.google.common.io.LittleEndianDataInputStream;
import com.minhdtb.lib.MetaStockXMaster;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainApp {

    public static void main(String[] args) {

        try {
            MetaStockXMaster master = new MetaStockXMaster(new LittleEndianDataInputStream(new FileInputStream("E:\\test\\XMASTER")));
            System.out.println(master.getHeader().getTotalFiles());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
