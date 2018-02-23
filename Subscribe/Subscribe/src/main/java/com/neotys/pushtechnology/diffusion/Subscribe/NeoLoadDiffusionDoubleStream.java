package com.neotys.pushtechnology.diffusion.Subscribe;

import com.neotys.pushtechnology.diffusion.Utils.DiffusionStat;
import com.pushtechnology.diffusion.client.topics.details.TopicSpecification;

import java.util.concurrent.LinkedBlockingQueue;
import com.neotys.pushtechnology.diffusion.Utils.DiffusionMessage;
/**
 * Created by hrexed on 22/02/18.
 */
public class NeoLoadDiffusionDoubleStream extends NeoLoadDiffusionStream<Double> {

    public NeoLoadDiffusionDoubleStream(LinkedBlockingQueue<DiffusionMessage<Double>> queue) {
        super(queue);
    }
    @Override
    public void onValue(String topicPath, TopicSpecification specification, Double oldValue, Double newValue) {
        addDataToQueue(new DiffusionMessage<Double>(topicPath,newValue));
        stat.AddDoubeStat(newValue.toString().getBytes().length);
    }
}
