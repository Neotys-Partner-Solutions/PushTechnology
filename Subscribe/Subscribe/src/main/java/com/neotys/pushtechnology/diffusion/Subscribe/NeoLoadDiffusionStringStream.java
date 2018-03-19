package com.neotys.pushtechnology.diffusion.Subscribe;

import com.neotys.pushtechnology.diffusion.Utils.DiffusionStat;
import com.pushtechnology.diffusion.client.topics.details.TopicSpecification;

import java.util.concurrent.LinkedBlockingQueue;
import com.neotys.pushtechnology.diffusion.Utils.DiffusionMessage;
/**
 * Created by hrexed on 22/02/18.
 */
public class NeoLoadDiffusionStringStream extends NeoLoadDiffusionStream<String>{
    public NeoLoadDiffusionStringStream(LinkedBlockingQueue<DiffusionMessage<String>> queue, boolean ismonitor) {
        super(queue,ismonitor);
    }

    @Override
    public void onValue(String topicPath, TopicSpecification specification, String oldValue, String newValue) {
        addDataToQueue(new DiffusionMessage<String>(topicPath,newValue));
       if(this.EnableMonitoring)
         stat.AddStringStat(newValue.getBytes().length);
    }
}
