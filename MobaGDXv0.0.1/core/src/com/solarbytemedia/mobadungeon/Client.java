package com.solarbytemedia.mobadungeon;

import org.java_websocket.client.DefaultWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;


public class Client {

    private WebSocketClient mWebSocketClient;

    public void OpenConnection(){
    	if(mWebSocketClient != null){
    		if(mWebSocketClient.getConnection().isClosed()){
    			connectWebSocket();
    		}
    	}
    }
    
    public void CloseConnection(){
    	if(mWebSocketClient != null){
    		if(!mWebSocketClient.getConnection().isClosed()){
    			mWebSocketClient.close();
    		}
    	}
    }
    
    public void SendMessage(String messageText) {
    	System.out.println("Sending message :: " + "Start: " + messageText);

        if(mWebSocketClient.getConnection().isOpen()){
            mWebSocketClient.send(messageText);
            System.out.println("Sending message :: " + "Sending: " + messageText);
        }
        System.out.println("Sending message :: " + "Finish");
    }
    
    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://58b9fd17.ngrok.io/HelloWebSocket/websocket");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        // Map<String, String> headers = new HashMap<>();
        // mWebSocketClient = new WebSocketClient(uri, new Draft_17(), headers, 0) {

        mWebSocketClient = new WebSocketClient(uri, new Draft_17()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                System.out.println("Websocket Open");
                mWebSocketClient.send("Hello from "+ "Laynes Game");
            }

            @Override
            public void onMessage(String s) {
                System.out.println(s);
                // Parse string and do thing
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                System.out.println("Websocket Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                System.out.println("Websocket Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }
	
}
