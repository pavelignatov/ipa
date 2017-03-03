package com.ipa;

import com.hazelcast.config.Config;
import com.hazelcast.core.Client;
import com.hazelcast.core.ClientListener;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HazelcastServer {
    public static void main(String [] argc) {
        Config config = new Config();
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);

        hz.getClientService().addClientListener(new ClientListener() {
            @Override
            public void clientConnected(Client client) {
                log.info("{}, {} is connected", client.getClientType(), client.getUuid());
            }

            @Override
            public void clientDisconnected(Client client) {
                log.info("{}, {} is disconnected", client.getClientType(), client.getUuid());
            }
        });
    }
}
