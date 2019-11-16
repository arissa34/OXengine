package rma.ox.data.network.socket;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.data.WebSocketCloseCode;
import com.github.czyzby.websocket.data.WebSocketException;
import rma.ox.engine.utils.Logx;

public abstract class AbsClientSocket implements WebSocketListener {

    private static final String TAG = AbsClientSocket.class.getSimpleName();

    private WebSocket socket;

    public AbsClientSocket(){
        //connect("wss://echo.websocket.org:443");
    }

    public void connect(String url){
        socket = WebSockets.newSocket(url);
        socket.addListener(this);
        try {
            socket.connect();
        }catch ( WebSocketException e){
            onError(socket, e);
        }
    }

    public abstract void dispatchMessage(String msg);

    public void send(String msg) {
        if(socket == null || !socket.isOpen()) return;
        socket.send(msg);
    }

    public void send(byte[] msg) {
        if(socket == null || !socket.isOpen()) return;
        socket.send(msg);
    }

    public void closeSocket(){
        WebSockets.closeGracefully(socket);
    }

    @Override
    public boolean onOpen(WebSocket webSocket) {
        Logx.d(this.getClass(), "onOpen");
        return false;
    }

    @Override
    public boolean onClose(WebSocket webSocket, WebSocketCloseCode code, String reason) {
        Logx.d(this.getClass(),"onClose");
        return false;
    }

    @Override
    public boolean onMessage(WebSocket webSocket, String packet) {
        Logx.d(this.getClass(), "onMessage receive : "+packet);
        dispatchMessage(packet);
        return false;
    }

    @Override
    public boolean onMessage(WebSocket webSocket, byte[] packet) {
        Logx.d(this.getClass(),"onMessage receive : "+packet.toString());
        dispatchMessage(packet.toString());
        return false;
    }

    @Override
    public boolean onError(WebSocket webSocket, Throwable error) {
        Logx.d(this.getClass(),"onError "+error.getMessage());
        return false;
    }


}
