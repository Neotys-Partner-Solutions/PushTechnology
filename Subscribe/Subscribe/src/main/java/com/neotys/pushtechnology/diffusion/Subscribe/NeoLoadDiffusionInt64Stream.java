package com.neotys.pushtechnology.diffusion.Subscribe;

import com.neotys.pushtechnology.diffusion.Utils.DiffusionStat;
import com.pushtechnology.diffusion.client.topics.details.TopicSpecification;

import java.util.concurrent.LinkedBlockingQueue;
import com.neotys.pushtechnology.diffusion.Utils.DiffusionMessage;
/**
 * Created by hrexed on 22/02/18.
 */
public class NeoLoadDiffusionInt64Stream extends NeoLoadDiffusionStream<Long> {

    public NeoLoadDiffusionInt64Stream(LinkedBlockingQueue<DiffusionMessage<Long>> queue,boolean ismonitor) {
        super(queue,ismonitor);

    }

    @Override
    public void onValue(String topicPath, TopicSpecification specification, Long oldValue, Long newValue) {
        addDataToQueue(new DiffusionMessage<Long>(topicPath,newValue));
        if(this.EnableMonitoring)
            stat.AddInt64Stat(newValue.toString().getBytes().length);
    }
}
