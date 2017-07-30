import com.minhdtb.lib.MetaStockManager;

public class MainApp {

    public static void main(String[] args) {
        MetaStockManager manager = new MetaStockManager();
        manager.open("D:\\Projects\\stocktest");
        System.out.println(manager.getHistory("ABC"));
    }
}
