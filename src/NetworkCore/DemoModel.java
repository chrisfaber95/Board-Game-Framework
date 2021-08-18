package NetworkCore;

public class DemoModel {
    public static void main(String[] args) {
        NetworkController controller = new NetworkController();
        DemoListener listener = new DemoListener();

        controller.addNetEventListener(listener);
        controller.start();
    }
}
