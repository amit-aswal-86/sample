package sample.kafka;

import java.util.Properties;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.utils.ZKStringSerializer;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;

public class CreateTopic {

	public static void main(String[] args) {
		String topicName = "sample";
		String zookeeperHosts = "localhost:2181";
		
		ZkClient zkClient = new ZkClient("localhost:2181", 10000, 10000, ZKStringSerializer$.MODULE$);
		zkClient.setZkSerializer(new ZkSerializer() {
            @Override
            public byte[] serialize(Object o) throws ZkMarshallingError {
                return ZKStringSerializer.serialize(o);
            }

            @Override
            public Object deserialize(byte[] bytes) throws ZkMarshallingError {
                return ZKStringSerializer.deserialize(bytes);
            }
        });
		
		System.out.println("Creating Topic [" + topicName + "]");

		ZkUtils zkUtils = new ZkUtils(zkClient, new ZkConnection(zookeeperHosts), false);
		AdminUtils.createTopic(zkUtils, topicName, 1, 1, new Properties(), RackAwareMode.Enforced$.MODULE$);
		
		System.out.println("Topic [" + topicName + "] Created..");
		zkClient.close();
	}
}
