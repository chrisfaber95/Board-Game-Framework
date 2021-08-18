package NetworkCore;

import java.util.EventObject;

public class NetEvent extends EventObject {
    private NetMessage msg;

    public NetEvent( Object source, NetMessage _msg) {
        super( source );
        this.msg = _msg;
    }

    public NetMessage netMessage() {
        return this.msg;
    }
}
