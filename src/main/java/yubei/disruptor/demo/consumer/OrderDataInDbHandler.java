package yubei.disruptor.demo.consumer;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import yubei.disruptor.demo.event.InOrderDataEvent;

/**
 * Created by 600194 on 2018/9/17.
 */
public class OrderDataInDbHandler implements EventHandler<InOrderDataEvent>,WorkHandler<InOrderDataEvent> {
    @Override
    public void onEvent(InOrderDataEvent event) throws Exception {
        long threadId = Thread.currentThread().getId();
        String order = event.getOrderCode();
        System.out.println(String.format("Thread Id %s save %s into database ....",threadId,order));
        System.out.println(String.format("Thread Id %s send %s in plaza messsage to kafka...",threadId,order));
    }

    @Override
    public void onEvent(InOrderDataEvent event, long sequence, boolean endOfBatch) throws Exception {
        // TODO Auto-generated method stub
        this.onEvent(event);
    }
}
