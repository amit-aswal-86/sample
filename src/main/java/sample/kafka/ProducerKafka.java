package sample.kafka;

import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerKafka {
	
	public static void main(String[] args) {
		new ProducerKafka().run();
	}
	
	private final Properties props = new Properties();
	private final String BOOTSTRAP_SERVERS = "localhost:9092";
	private final String TOPIC = "sample";
	private final KafkaProducer<Long, String> producer;
	private final AtomicLong counter = new AtomicLong(1);
	
	public ProducerKafka() {
		Logger.getLogger("org").setLevel(Level.WARNING);
		Logger.getLogger("akka").setLevel(Level.WARNING);
		Logger.getLogger("kafka").setLevel(Level.WARNING);
		
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducerSample");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,	StringSerializer.class.getName());
		
		producer = new KafkaProducer<Long, String>(props);
	}
	
	public void run() {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					Date date = new Date();
					long index = System.currentTimeMillis();
					String message = ("Hello Mum " + counter.getAndIncrement());
					
					ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC, index, message);
					RecordMetadata metadata = producer.send(record).get();
					
					long elapsedTime = System.currentTimeMillis() - index;
					System.out.printf("sent record(key=%s value=%s) " +"meta(partition=%d, offset=%d) time=%d\n", 
							record.key(), record.value(), metadata.partition(), metadata.offset(), elapsedTime);
					
					System.out.println("Message Published : " + date);
				} 
				catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		}, 0, (20 * 1000));
	}
}
