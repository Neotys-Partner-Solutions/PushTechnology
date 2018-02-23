package com.neotys.pushtechnology.diffusion.ReadMessage;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import com.neotys.pushtechnology.diffusion.Utils.DiffusionMessage;
import com.google.common.base.Strings;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;

import com.pushtechnology.diffusion.client.topics.details.TopicType;
import com.pushtechnology.diffusion.datatype.binary.Binary;
import com.pushtechnology.diffusion.datatype.json.JSON;

public final class ReadMessageActionEngine implements ActionEngine {

	private String TopicSelector;
	private String sttimeout;
	private String Format;
	int Timeout;

	private final static String STRING="STRING";
	private final static String INT64="INT64";
	private final static String DOUBLE="DOUBLE";
	private final static String BINARY="BINARY";
	private final static String RECORD_V2="RECORD_V2";
	private final static String JSON="JSON";
	private final static String STATELESS="STATELESS";
	private final static String TIME_SERIES="TIME_SERIES";
	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();
		String host;
		TopicType type;
		String content;
		for(ActionParameter parameter:parameters)
		{
			switch(parameter.getName())
			{

				case  ReadMessageAction.TopicSelector:
					TopicSelector = parameter.getValue();
					break;
				case  ReadMessageAction.TimeOut:
					sttimeout = parameter.getValue();
					break;
				case  ReadMessageAction.Format:
					Format = parameter.getValue();
					break;
			}
		}



		if (Strings.isNullOrEmpty(TopicSelector)) {
			return getErrorResult(context, sampleResult, "Invalid argument: TopicSelector cannot be null "
					+ ReadMessageAction.TopicSelector + ".", null);
		}
		if (Strings.isNullOrEmpty(sttimeout)) {
			return getErrorResult(context, sampleResult, "Invalid argument: TimeOut cannot be null "
					+ ReadMessageAction.TimeOut + ".", null);
		}
		if (Strings.isNullOrEmpty(Format)) {
			return getErrorResult(context, sampleResult, "Invalid argument: Format cannot be null "
					+ ReadMessageAction.Format + ".", null);
		}
		type=GetTopicType();
		if(type==null)
		{
			return getErrorResult(context, sampleResult, "Invalid argument: unhandled format "
					+ ReadMessageAction.Format + ".", null);
		}
		try
		{
			Timeout=Integer.parseInt(sttimeout);

		}
		catch(NumberFormatException e)
		{
			return getErrorResult(context, sampleResult, "Invalid argument: TimeOut needs to be a digit "
					+ ReadMessageAction.TimeOut + ".", null);
		}

		try
		{

			sampleResult.sampleStart();
			content=ReceiveMessage(type,context);
			if(content!=null)
			{
						appendLineToStringBuilder(responseBuilder,content.toString());
			}
			else
			{
				appendLineToStringBuilder(responseBuilder,"No Message from" + TopicSelector);
			}
					// TODO perform execution.

			sampleResult.sampleEnd();
			sampleResult.setRequestContent(requestBuilder.toString());
			sampleResult.setResponseContent(responseBuilder.toString());

		}
		catch (InterruptedException e1)
		{
			return getErrorResult(context, sampleResult, "Technical Error ",e1);
		} catch (NeoLoadDiffusionException e1)
		{
			return getErrorResult(context, sampleResult, "No Subscription done", e1);
		}


		return sampleResult;
	}

	private void appendLineToStringBuilder(final StringBuilder sb, final String line){
		sb.append(line).append("\n");
	}

	/**
	 * This method allows to easily create an error result and log exception.
	 */

	private static SampleResult getErrorResult(final Context context, final SampleResult result, final String errorMessage, final Exception exception) {
		result.setError(true);
		result.setStatusCode("NL-ReadMessage_ERROR");
		result.setResponseContent(errorMessage);
		if(exception != null){
			context.getLogger().error(errorMessage, exception);
		} else{
			context.getLogger().error(errorMessage);
		}
		return result;
	}
	private String ReceiveMessage(TopicType t,Context c) throws InterruptedException, NeoLoadDiffusionException {

		if (t == TopicType.STRING) {
			DiffusionMessage<String> Data;
			LinkedBlockingQueue<DiffusionMessage<String>> queuest=(LinkedBlockingQueue<DiffusionMessage<String>> )c.getCurrentVirtualUser().get("Queue_STRING"+ TopicSelector);
			if(queuest!=null)
			{
				if(queuest.size()>0) {
					Data = queuest.poll(Timeout, TimeUnit.SECONDS);
					return Data.getTopic() + " : " + Data.getData();
				}else
					return null;
			}
			else throw new NeoLoadDiffusionException("No subsription done on the topic :"+TopicSelector +" with the  format "+Format);
		}


		if (t == TopicType.INT64) {
			DiffusionMessage<Long> Data;
			LinkedBlockingQueue<DiffusionMessage<Long>> queueint=(LinkedBlockingQueue<DiffusionMessage<Long>> )c.getCurrentVirtualUser().get("Queue_INT64"+ TopicSelector);
			if(queueint!=null) {
				if(queueint.size()>0) {
					Data = queueint.poll(Timeout, TimeUnit.SECONDS);
					return Data.getTopic() +":" + String.valueOf(Data.getData());
				}else
					return null;
			}
			else
				throw new NeoLoadDiffusionException("No subsription done on the topic :"+TopicSelector +" with the  format "+Format);
		}
		if (t == TopicType.DOUBLE) {
			DiffusionMessage<Double> Data;
			LinkedBlockingQueue<DiffusionMessage<Double>> queuedb=(LinkedBlockingQueue<DiffusionMessage<Double>> )c.getCurrentVirtualUser().get("Queue_DOUBLE"+ TopicSelector);
			if(queuedb!=null) {
				if(queuedb.size()>0) {
					Data=queuedb.poll(Timeout, TimeUnit.SECONDS);
					return Data.getTopic() +":" + String.valueOf(Data.getData());

				}else
					return null;
			}
			else
				throw new NeoLoadDiffusionException("No subsription done on the topic :"+TopicSelector +" with the  format "+Format);
		}

		if (t == TopicType.BINARY) {
			DiffusionMessage<Binary> Data;
			LinkedBlockingQueue<DiffusionMessage<Binary>> queuebn=(LinkedBlockingQueue<DiffusionMessage<Binary>> )c.getCurrentVirtualUser().get("Queue_BINARY"+ TopicSelector);
			if(queuebn!=null) {
				if(queuebn.size()>0) {
					Data=queuebn.poll(Timeout, TimeUnit.SECONDS);
					return Data.getTopic() +":"+Data.getData().toString();
				}else
					return null;
			}
			else
				throw new NeoLoadDiffusionException("No subsription done on the topic :"+TopicSelector +" with the  format "+Format);
		}


		if (t == TopicType.JSON)
		{
			DiffusionMessage<JSON> Data;
			LinkedBlockingQueue<DiffusionMessage<JSON>> queuejs=(LinkedBlockingQueue<DiffusionMessage<JSON>> )c.getCurrentVirtualUser().get("Queue_JSON"+ TopicSelector);
			if(queuejs!=null)
			{
				if(queuejs.size()>0) {
					Data=queuejs.poll(Timeout, TimeUnit.SECONDS);
					return Data.getTopic()+":"+ Data.getData().toJsonString();
				}else
					return null;
			}
			else
				throw new NeoLoadDiffusionException("No subsription done on the topic :"+TopicSelector +" with the  format "+Format);
		}


		return null;

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
	@Override
	public void stopExecute() {
		// TODO add code executed when the test have to stop.
	}

}
class NeoLoadDiffusionException extends Exception
{
	//Parameterless Constructor
	public NeoLoadDiffusionException() {}

	//Constructor that accepts a message
	public NeoLoadDiffusionException(String message)
	{
		super(message);
	}
}