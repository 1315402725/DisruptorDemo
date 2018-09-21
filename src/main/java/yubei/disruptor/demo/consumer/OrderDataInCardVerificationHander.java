package yubei.disruptor.demo.consumer;

import com.lmax.disruptor.EventHandler;
import yubei.disruptor.demo.event.InOrderDataEvent;

/**
 * Created by 600194 on 2018/9/20.
 */
public class OrderDataInCardVerificationHander implements EventHandler<InOrderDataEvent> {
    @Override
    public void onEvent(InOrderDataEvent event,long sequence, boolean endOfBatch) throws Exception {
        Thread.sleep(1000L);
        long threadId = Thread.currentThread().getId();
        String order = event.getOrderCode();
        System.out.println(String.format("Thread Id %s 信用卡验证成功 ....",threadId,order));
    }
}
