import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import yubei.disruptor.demo.consumer.OrderDataInCardVerificationHander;
import yubei.disruptor.demo.consumer.OrderDataInDbHandler;
import yubei.disruptor.demo.consumer.OrderDataSmsHandler;
import yubei.disruptor.demo.consumer.OrderDataToKafkaHandler;
import yubei.disruptor.demo.event.InOrderDataEvent;
import yubei.disruptor.demo.publisher.OrderDataEventPublisher;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 600194 on 2018/9/17.
 */
public class testDemo {
    public static void main(String[] args) throws InterruptedException {
        long beginTime=System.currentTimeMillis();

        int bufferSize=1024;
        //Disruptor交给线程池来处理，四个线程
        ExecutorService executor= Executors.newFixedThreadPool(4);
        //构造缓冲区与事件生成
        Disruptor<InOrderDataEvent> disruptor=new Disruptor<InOrderDataEvent>(new EventFactory<InOrderDataEvent>() {
            @Override
            public InOrderDataEvent newInstance() {
                return new InOrderDataEvent();
            }
        }, bufferSize, executor, ProducerType.SINGLE, new YieldingWaitStrategy());

        OrderDataInCardVerificationHander orderDataInCardVerificationHander=new OrderDataInCardVerificationHander();

        //信用卡验证
        EventHandlerGroup<InOrderDataEvent> handlerGroup=disruptor
                .handleEventsWith(orderDataInCardVerificationHander);
        //kafka消息，数据库更新
        handlerGroup.then(new OrderDataInDbHandler());
        OrderDataSmsHandler orderDataSmsHandler=new OrderDataSmsHandler();
        //发送sms
        handlerGroup.then(orderDataSmsHandler);
        //启动
        disruptor.start();
        CountDownLatch latch=new CountDownLatch(1);
        //开始生产
        executor.submit(new OrderDataEventPublisher(latch, disruptor));
        //等待生产者结束
        latch.await();
        //Disruptor会阻塞至所有事件处理完毕
        disruptor.shutdown();
        executor.shutdown();

        System.out.println("消耗时间:"+(System.currentTimeMillis()-beginTime));
    }
}
