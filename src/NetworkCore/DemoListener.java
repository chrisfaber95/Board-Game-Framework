package NetworkCore;

public class DemoListener implements NetEventListener {
    @Override
    public void onDataReceived(NetEvent event) {
        NetMessage msg = event.netMessage();
        switch(msg.getType()) {
            default:
                break;
        }
    }
}
