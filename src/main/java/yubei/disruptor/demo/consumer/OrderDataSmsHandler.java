package yubei.disruptor.demo.consumer;

import com.lmax.disruptor.EventHandler;
import yubei.disruptor.demo.event.InOrderDataEvent;

/**
 * Created by 600194 on 2018/9/17.
 */
public class OrderDataSmsHandler implements EventHandler<InOrderDataEvent> {
    @Override
    public void onEvent(InOrderDataEvent event, long sequence, boolean endOfBatch) throws Exception {
        long threadId = Thread.currentThread().getId();
        String order = event.getOrderCode();
        System.out.println(String.format("Thread Id %s send %s in plaza sms to user",threadId,order));
    }

}
