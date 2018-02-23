package com.neotys.pushtechnology.diffusion.Subscribe;

import com.neotys.pushtechnology.diffusion.Utils.DiffusionStat;
import com.pushtechnology.diffusion.client.features.Topics;
import com.pushtechnology.diffusion.client.topics.details.TopicSpecification;

import java.util.concurrent.LinkedBlockingQueue;
import com.neotys.pushtechnology.diffusion.Utils.DiffusionMessage;
/**
 * Created by hrexed on 19/02/18.
 */
public class NeoLoadDiffusionStream<v>  extends Topics.ValueStream.Default<v>
{
    private final LinkedBlockingQueue<DiffusionMessage<v>> queue;
    DiffusionStat stat;

    public NeoLoadDiffusionStream(LinkedBlockingQueue<DiffusionMessage<v>> queue)
    {
        this.queue=queue;
        stat=DiffusionStat.instance;
    }
    @Override
    public void onSubscription(String topicPath, TopicSpecification specification) {
        System.out.println("Subscribed to: " + topicPath);
    }

    public void addDataToQueue(DiffusionMessage<v> dat )
    {

        this.queue.add(dat);

    }
}
