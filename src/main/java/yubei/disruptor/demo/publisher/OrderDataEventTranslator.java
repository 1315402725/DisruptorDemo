package yubei.disruptor.demo.publisher;

import com.lmax.disruptor.EventTranslator;
import yubei.disruptor.demo.event.InOrderDataEvent;

/**
 * Created by 600194 on 2018/9/17.
 */
public class OrderDataEventTranslator implements EventTranslator<InOrderDataEvent> {
    //Disruptor对于已消费的事件是不删除的，有新事件时只是用新事件的属性去替换旧事件的属性。
    //将新事件的属性转移以覆盖旧事件的好处，这样做虽然占用了内存，但是降低了垃圾回收出现的频率
    @Override
    public void translateTo(InOrderDataEvent event, long sequence) {
        this.generateTradeTransaction(event);
    }
    private InOrderDataEvent generateTradeTransaction(InOrderDataEvent event){
        int num =  (int)(Math.random()*8000);
        num = num + 1000;
        event.setOrderCode("订单" + num);
        System.out.println("Thread Id " + Thread.currentThread().getId() + " 获取了一个订单");
        return event;
    }
}
