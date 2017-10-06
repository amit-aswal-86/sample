package sample.kafka;

import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ConsumerKafka {

	public static void main(String[] args) {
		new ConsumerKafka().run();
	}
	
	private final Properties props = new Properties();
	private final String BOOTSTRAP_SERVERS = "localhost:9092";
	private final String TOPIC = "sample";
	private final Consumer<Long, String> consumer;
	
	public ConsumerKafka() {
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaConsumerSample");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		
		consumer = new KafkaConsumer<Long, String>(props);
		consumer.subscribe(Collections.singletonList(TOPIC));
	}
	
	public void run() {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				System.out.println("Message Subscribing : " +  new Date());
				
				try {
			        final int giveUp = 100;   int noRecordsCount = 0;

			        while (true) {
			            final ConsumerRecords<Long, String> consumerRecords =
			                    consumer.poll(1000);

			            if (consumerRecords.count()==0) {
			                noRecordsCount++;
			                if (noRecordsCount > giveUp) break;
			                else continue;
			            }

			            consumerRecords.forEach(record -> {
			                System.out.printf("Consumer Record:(%d, %s, %d, %d)\n",
			                        record.key(), record.value(),
			                        record.partition(), record.offset());
			            });

			            consumer.commitAsync();
			        }
				} 
				catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}, 0, (20 * 1000));
	}
}
