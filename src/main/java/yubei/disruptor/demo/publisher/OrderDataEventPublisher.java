package yubei.disruptor.demo.publisher;

import com.lmax.disruptor.dsl.Disruptor;
import yubei.disruptor.demo.event.InOrderDataEvent;

import java.util.concurrent.CountDownLatch;

/**
 * Created by 600194 on 2018/9/17.
 */
public class OrderDataEventPublisher implements Runnable  {
    Disruptor<InOrderDataEvent> disruptor;
    private CountDownLatch latch;
    private static int LOOP=10;//10个订单


    public OrderDataEventPublisher(CountDownLatch latch, Disruptor<InOrderDataEvent> disruptor) {
        this.disruptor=disruptor;
        this.latch=latch;
    }

    @Override
    public void run() {
        OrderDataEventTranslator tradeTransloator=new OrderDataEventTranslator();
        for(int i=0;i<LOOP;i++){
            try {
                Thread.currentThread().sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally{
                //publishEvent法必须包含在 finally 中以确保必须得到调用；如果某个请求的 sequence 未被提交，将会堵塞后续的发布操作或者其它的 producer。
                disruptor.publishEvent(tradeTransloator);
            }
        }
        latch.countDown();
        System.out.println("生产者写完" +LOOP + "个消息");
    }

}
