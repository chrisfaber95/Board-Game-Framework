package NetworkCore;

import java.util.HashMap;

public class NetMessage {
    private MsgType type;
    private HashMap<String, String> payload;

    public NetMessage(MsgType _type, HashMap<String,String> _payload) {
        this.type = _type;
        this.payload = _payload;
    }

    public MsgType getType() { return this.type; }

    public HashMap<String, String> getPayload() { return this.payload; }
}
