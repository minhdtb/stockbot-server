import com.minhdtb.lib.MetaStockHistory;
import com.minhdtb.lib.MetaStockHistoryData;
import com.minhdtb.lib.MetaStockManager;

import java.io.IOException;
import java.util.Date;

public class MainApp {

    public static void main(String[] args) throws IOException {
        MetaStockManager manager = new MetaStockManager();
        manager.open("E:\\test");
        MetaStockManager manager1 = new MetaStockManager();
        manager1.open("E:\\testxxx");

        for (MetaStockHistory history : manager.getHistoryList()) {
            System.out.println(history.getSymbol());
            for (MetaStockHistoryData data : manager.getHistory(history.getSymbol())) {
                System.out.println(data);
                manager1.addHistory(history.getSymbol(), "D", data);
            }
        }


        //System.out.println(manager.getHistory("ABD"));
        //manager.addHistory("ABD", "D",
        //        new MetaStockHistoryData(new Date(), 12, 16, 9, 10, 15));
    }
}
