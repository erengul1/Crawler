package DAO;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;


import java.net.InetAddress;
import java.net.UnknownHostException;

public class ElasticDB {
public void insertDataToElastic(String jsonStr) {
    TransportClient client;

    {
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));

            IndexResponse response = client.prepareIndex("test_index", "test_type")
                    .setSource(jsonStr, XContentType.JSON)
                    .get();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}




}
