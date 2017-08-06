import com.minhdtb.lib.data.MetaStockData;
import com.minhdtb.lib.data.MetaStockDataRecord;
import com.minhdtb.lib.manager.MetaStockHistory;
import com.minhdtb.lib.manager.MetaStockHistoryData;
import com.minhdtb.lib.manager.MetaStockManager;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MainApp {

    public static void main(String[] args) throws IOException {
        MetaStockManager manager = new MetaStockManager();
        manager.open("E:\\test");
        MetaStockManager manager1 = new MetaStockManager();
        manager1.open("E:\\testxxx");

        for (MetaStockHistory history : manager.getHistoryList()) {
            System.out.println(history);
            for (MetaStockHistoryData data : manager.getHistory(history.getSymbol())) {
                manager1.addHistory(history.getSymbol(), "D", data);
            }
        }


        //System.out.println(manager.getHistory("ABD"));
//        manager1.addHistory("ABD", "D",
//                new MetaStockHistoryData(new Date(), 12, 16, 9, 10, 15));

//        File file = new File("E:\\F1.DATA");
        // MetaStockData data = new MetaStockData(file);
        // data.addRecord(new MetaStockDataRecord(new Date(), 12, 16, 9, 10, 15, 10));

//        MetaStockData data1 = new MetaStockData(file);
//        System.out.println(data1.getHeader());
//        System.out.println(data1.getRecords());
    }
}
