import com.minhdtb.lib.MetaStockHistoryData;
import com.minhdtb.lib.MetaStockManager;

import java.io.IOException;
import java.util.Date;

public class MainApp {

    public static void main(String[] args) throws IOException {
        MetaStockManager manager = new MetaStockManager();
        manager.open("D:\\Projects\\stocktest3");
        System.out.println(manager.getHistory("ABC"));
        //manager.addHistory("ABD", "D",
        //        new MetaStockHistoryData(new Date(), 11, 11, 10, 10, 10));
    }
}
