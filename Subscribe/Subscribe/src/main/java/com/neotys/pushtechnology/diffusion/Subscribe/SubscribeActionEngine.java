package com.neotys.pushtechnology.diffusion.Subscribe;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.base.Strings;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;
import com.neotys.pushtechnology.diffusion.Utils.DiffusionMessage;
import com.neotys.pushtechnology.diffusion.Utils.DiffusionStat;
import com.pushtechnology.diffusion.client.Diffusion;
import com.pushtechnology.diffusion.client.content.Content;
import com.pushtechnology.diffusion.client.features.Topics;
import com.pushtechnology.diffusion.client.session.Session;
import com.pushtechnology.diffusion.client.topics.details.TopicType;
import com.pushtechnology.diffusion.datatype.binary.Binary;
import com.pushtechnology.diffusion.datatype.json.JSON;
import com.sun.org.apache.xpath.internal.operations.Bool;


public final class SubscribeActionEngine implements ActionEngine {
	private String Diffusion_URL;
	private String TopicSelector;
	private String Format;
	private String EnableMonitoring;
	private boolean bEnableMonitoring;
	private final static String STRING="STRING";
	private final static String INT64="INT64";
	private final static String DOUBLE="DOUBLE";
	private final static String BINARY="BINARY";
	private final static String RECORD_V2="RECORD_V2";
	private final static String JSON="JSON";
	private final static String STATELESS="STATELESS";
	private final static String TIME_SERIES="TIME_SERIES";
	DiffusionStat DiffusionStat;
	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();
		final Topics topics;

		LinkedBlockingQueue<Content> queue;
		String host;
		TopicType type;
		Session session;


		for(ActionParameter parameter:parameters)
		{
			switch(parameter.getName())
			{
				case  SubscribeAction.Diffusion_URL:
					Diffusion_URL = parameter.getValue();
					break;
				case  SubscribeAction.TopicSelector:
					TopicSelector = parameter.getValue();
					break;
				case  SubscribeAction.Format:
					Format = parameter.getValue();
					break;
				case SubscribeAction.EnableMonitoring:
					EnableMonitoring=parameter.getValue();
			}
		}

		if (Strings.isNullOrEmpty(EnableMonitoring)) {
			EnableMonitoring="False";
		}

		if (Strings.isNullOrEmpty(Diffusion_URL)) {
			return getErrorResult(context, sampleResult, "Invalid argument: Diffusion_URL cannot be null "
					+ SubscribeAction.Diffusion_URL + ".", null);
		}
		if (Strings.isNullOrEmpty(TopicSelector)) {
			return getErrorResult(context, sampleResult, "Invalid argument: TopicSelector cannot be null "
					+ SubscribeAction.TopicSelector + ".", null);
		}
		if (Strings.isNullOrEmpty(Format)) {
			return getErrorResult(context, sampleResult, "Invalid argument: Format cannot be null "
					+ SubscribeAction.Format + ".", null);
		}
		type=GetTopicType();
		if(type==null)
		{
			return getErrorResult(context, sampleResult, "Invalid argument: unhandled format "
					+ SubscribeAction.Format + ".", null);
		}
		if(EnableMonitoring.equalsIgnoreCase("true")||EnableMonitoring.equalsIgnoreCase("false"))
			bEnableMonitoring= Boolean.parseBoolean(EnableMonitoring);
		else
			return getErrorResult(context, sampleResult, "Invalid argument: unhandled format , EnableMonitoring needs to be a boolean"
					+ SubscribeAction.EnableMonitoring + ".", null);
		try
		{
			host=GetHostNamefromUrl();
			session=(Session)context.getCurrentVirtualUser().get(host+"Session");
			if(session!=null)
			{
				if(session.getState().isConnected())
				{

					sampleResult.sampleStart();

					topics = session.feature(Topics.class);
											// TODO perform execution.4
					Subscribe(topics,type,TopicSelector,context);
					sampleResult.sampleEnd();
					context.getCurrentVirtualUser().put("Topics_"+TopicSelector,topics);
					sampleResult.setRequestContent(requestBuilder.toString());
					sampleResult.setResponseContent(responseBuilder.toString());
				}
			}
			else
				{
					return getErrorResult(context, sampleResult, "No Diffusion session created on this url: you need to create a session before", null);
				}
		}
		catch(Exception e)
		{
			return getErrorResult(context, sampleResult, "Technical Error ",e);
		}
		return sampleResult;
	}

	private void Subscribe(Topics topic,TopicType t,String TopicSelector, Context c)
	{

		if (t == TopicType.INT64) {
			LinkedBlockingQueue<DiffusionMessage<Long>> queuelg=new LinkedBlockingQueue<DiffusionMessage<Long>>();
			topic.addStream(TopicSelector, Long.class, new NeoLoadDiffusionInt64Stream(queuelg,bEnableMonitoring));
			c.getCurrentVirtualUser().put("Queue_INT64"+TopicSelector,queuelg);
			// Add a new stream for 'foo/counter'
			topic.subscribe(TopicSelector);
		}
		if (t == TopicType.DOUBLE) {
			LinkedBlockingQueue<DiffusionMessage<Double>> queuedb=new LinkedBlockingQueue<DiffusionMessage<Double>>();
			topic.addStream(TopicSelector, Double.class, new NeoLoadDiffusionDoubleStream(queuedb,bEnableMonitoring));
			c.getCurrentVirtualUser().put("Queue_DOUBLE"+TopicSelector,queuedb);
			// Add a new stream for 'foo/counter'
			topic.subscribe(TopicSelector);

		}

		if (t == TopicType.BINARY) {
			LinkedBlockingQueue<DiffusionMessage<Binary>> queuebn=new LinkedBlockingQueue<DiffusionMessage<Binary>>();
			topic.addStream(TopicSelector, Binary.class, new NeoLoadDiffusionBinaryStream(queuebn,bEnableMonitoring));
			c.getCurrentVirtualUser().put("Queue_BINARY"+TopicSelector,queuebn);
			// Add a new stream for 'foo/counter'
			topic.subscribe(TopicSelector);
		}


		if (t == TopicType.STRING) {

			LinkedBlockingQueue<DiffusionMessage<String>> queuest=new LinkedBlockingQueue<DiffusionMessage<String>>();
			topic.addStream(TopicSelector, String.class, new NeoLoadDiffusionStringStream(queuest,bEnableMonitoring));
			c.getCurrentVirtualUser().put("Queue_STRING"+TopicSelector,queuest);
			// Add a new stream for 'foo/counter'
			topic.subscribe(TopicSelector);

		}
		if (t == TopicType.JSON)
		{

			LinkedBlockingQueue<DiffusionMessage<JSON>> queue=new LinkedBlockingQueue<DiffusionMessage<JSON>>();
			topic.addStream(TopicSelector, JSON.class, new NeoLoadDiffusionJsonStream(queue,bEnableMonitoring));
			c.getCurrentVirtualUser().put("Queue_JSON"+TopicSelector,queue);
			// Add a new stream for 'foo/counter'
			topic.subscribe(TopicSelector);



		}
	}

	private void appendLineToStringBuilder(final StringBuilder sb, final String line){
		sb.append(line).append("\n");
	}

	/**
	 * This method allows to easily create an error result and log exception.
	 */
	private static SampleResult getErrorResult(final Context context, final SampleResult result, final String errorMessage, final Exception exception) {
		result.setError(true);
		result.setStatusCode("NL-Subscribe_ERROR");
		result.setResponseContent(errorMessage);
		if(exception != null){
			context.getLogger().error(errorMessage, exception);
		} else{
			context.getLogger().error(errorMessage);
		}
		return result;
	}

	private TopicType GetTopicType()
	{
		switch(Format)
		{
			case STRING:
				return TopicType.STRING;

			case INT64:
				return TopicType.INT64;
			case DOUBLE:
				return TopicType.DOUBLE;

			case BINARY:
				return TopicType.BINARY;
			/*case RECORD_V2:
				return TopicType.RECORD_V2;*/
			case JSON:
				return TopicType.JSON;

			/*case TIME_SERIES:
				return TopicType.TIME_SERIES;*/
			default:
				return null;
		}
	}
	private String GetHostNamefromUrl() throws URISyntaxException
	{
		URI url=new URI(Diffusion_URL);
		return url.getHost();
	}
	@Override
	public void stopExecute() {
		// TODO add code executed when the test have to stop.
	}


}
