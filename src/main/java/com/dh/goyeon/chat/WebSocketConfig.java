package com.dh.goyeon.chat;

import jakarta.websocket.server.ServerEndpointConfig;

public class WebSocketConfig extends ServerEndpointConfig.Configurator {
    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return super.getEndpointInstance(endpointClass);
    }
}
