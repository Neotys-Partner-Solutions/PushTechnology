package com.neotys.pushtechnology.diffusion.Subscribe;

import com.neotys.pushtechnology.diffusion.Utils.DiffusionMessage;
import com.neotys.pushtechnology.diffusion.Utils.DiffusionStat;
import com.pushtechnology.diffusion.client.topics.details.TopicSpecification;
import com.pushtechnology.diffusion.datatype.binary.Binary;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by hrexed on 22/02/18.
 */
public class NeoLoadDiffusionBinaryStream extends  NeoLoadDiffusionStream<Binary> {
    public NeoLoadDiffusionBinaryStream(LinkedBlockingQueue<DiffusionMessage<Binary>> queue,boolean ismonitor) {
        super(queue,ismonitor);

    }

    @Override
    public void onValue(String topicPath, TopicSpecification specification, Binary oldValue, Binary newValue) {
        addDataToQueue(new DiffusionMessage<Binary>(topicPath,newValue));
        if(this.EnableMonitoring)
            stat.AddBinaryStat(newValue.toString().getBytes().length);
    }
}
