package rma.ox.data.network.socket;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.websocket.WebSocket;
import rma.ox.engine.utils.Logx;

public class TestWebSocket extends AbsClientSocket {

    public TestWebSocket() {
        connect("wss://echo.websocket.org:443");
    }

    @Override
    public void dispatchMessage(String msg) {
        Logx.d(this.getClass(), "dispatchMessage msg : "+msg);
    }

    @Override
    public boolean onOpen(WebSocket webSocket) {
         super.onOpen(webSocket);
         send("KAKA");
        return true;
    }
}
