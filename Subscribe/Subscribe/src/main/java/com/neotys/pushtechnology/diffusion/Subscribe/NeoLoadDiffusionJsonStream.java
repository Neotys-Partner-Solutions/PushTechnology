package com.neotys.pushtechnology.diffusion.Subscribe;

import com.neotys.pushtechnology.diffusion.Utils.DiffusionStat;
import com.pushtechnology.diffusion.client.topics.details.TopicSpecification;
import com.pushtechnology.diffusion.datatype.json.JSON;

import java.util.concurrent.LinkedBlockingQueue;
import com.neotys.pushtechnology.diffusion.Utils.DiffusionMessage;
/**
 * Created by hrexed on 22/02/18.
 */
public class NeoLoadDiffusionJsonStream extends NeoLoadDiffusionStream<JSON> {
    public NeoLoadDiffusionJsonStream(LinkedBlockingQueue<DiffusionMessage<JSON>> queue,boolean ismonitor) {
        super(queue,ismonitor);
    }

    @Override
    public void onValue(String topicPath, TopicSpecification specification, JSON oldValue, JSON newValue) {
        addDataToQueue(new DiffusionMessage<JSON>(topicPath,newValue));
        if(this.EnableMonitoring)
            stat.AddJsonStat(newValue.toJsonString().getBytes().length);
    }
}
