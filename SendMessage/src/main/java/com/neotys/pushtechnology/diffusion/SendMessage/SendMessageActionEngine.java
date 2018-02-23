package com.neotys.pushtechnology.diffusion.SendMessage;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.common.base.Strings;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;
import com.pushtechnology.diffusion.client.Diffusion;
import com.pushtechnology.diffusion.client.features.control.topics.TopicControl;
import com.pushtechnology.diffusion.client.features.control.topics.TopicUpdateControl;
import com.pushtechnology.diffusion.client.session.Session;
import com.pushtechnology.diffusion.client.topics.details.TopicType;
import com.pushtechnology.diffusion.datatype.binary.Binary;
import com.pushtechnology.diffusion.datatype.json.JSON;
import com.pushtechnology.diffusion.client.features.control.topics.TopicUpdateControl.Updater.UpdateCallback;
import com.pushtechnology.diffusion.management.Notification;
import com.pushtechnology.diffusion.client.features.control.topics.TopicNotifications;


public final class SendMessageActionEngine implements ActionEngine {
	private String Format;
	private String Diffusion_URL;
	private String TopicSelector;
	private String Message;
	private final static String STRING="STRING";
	private final static String INT64="INT64";
	private final static String DOUBLE="DOUBLE";
	private final static String BINARY="BINARY";
	private final static String RECORD_V2="RECORD_V2";
	private final static String JSON="JSON";
	private final static String STATELESS="STATELESS";
	private final static String TIME_SERIES="TIME_SERIES";
	private Session sess;

	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();
		TopicType type;

		String host;
		//Messaging messaging;
		TopicControl topicControl;
		TopicUpdateControl updateControl;
		CompletableFuture<TopicControl.AddTopicResult> future;

		for(ActionParameter parameter:parameters)
		{
			switch(parameter.getName())
			{
				case  SendMessageAction.Diffusion_URL:
					Diffusion_URL = parameter.getValue();
					break;
				case  SendMessageAction.TopicSelector:
					TopicSelector = parameter.getValue();
					break;
				case  SendMessageAction.Format:
					Format = parameter.getValue();
					break;
				case  SendMessageAction.Message:
					Message = parameter.getValue();
					break;

			}
		}


		if (Strings.isNullOrEmpty(Diffusion_URL)) {
			return getErrorResult(context, sampleResult, "Invalid argument: Diffusion_URL cannot be null "
					+ SendMessageAction.Diffusion_URL + ".", null);
		}
		if (Strings.isNullOrEmpty(TopicSelector)) {
			return getErrorResult(context, sampleResult, "Invalid argument: TopicSelector cannot be null "
					+ SendMessageAction.TopicSelector + ".", null);
		}
		if (Strings.isNullOrEmpty(Message)) {
			return getErrorResult(context, sampleResult, "Invalid argument: Message cannot be null "
					+ SendMessageAction.Message + ".", null);
		}
		if (Strings.isNullOrEmpty(Format)) {
			return getErrorResult(context, sampleResult, "Invalid argument: Format cannot be null "
					+ SendMessageAction.Format + ".", null);
		}
		type=GetTopicType();
		if(type==null)
		{
			return getErrorResult(context, sampleResult, "Invalid argument: unhandled format "
					+ SendMessageAction.Format + ".", null);
		}
		try {
			host=GetHostNamefromUrl();
			sess=(Session)context.getCurrentVirtualUser().get(host+"Session");
			if(sess!=null)
			{
				if(sess.getState().isConnected())
				{
					sampleResult.sampleStart();
					topicControl = sess.feature(TopicControl.class);

					updateControl =sess.feature(TopicUpdateControl.class);

					//addTopic(TopicSelector,type,topicControl);
					SendMessage(type,sess,updateControl);
					//messaging = sess.feature(Messaging.class);

					//messaging.send(TopicSelector, Message).get(5, TimeUnit.SECONDS);


					appendLineToStringBuilder(requestBuilder, "SendMessage request." + Message+ "to topic "+ TopicSelector);
					// TODO perform execution.

					sampleResult.sampleEnd();

					sampleResult.setRequestContent(requestBuilder.toString());
					sampleResult.setResponseContent(responseBuilder.toString());
				}
				else
				{
					return getErrorResult(context, sampleResult, "Session created but not connected . You need to define first a session to diffusion",null);

				}
			}
			else
			{
				return getErrorResult(context, sampleResult, "No session created . You need to define first a session to diffusion",null);


			}
		}
		catch(Exception e)
		{
			return getErrorResult(context, sampleResult, "Technical Error ",e);

		}
		return sampleResult;
	}
	private void SendMessage(TopicType t,Session s,TopicUpdateControl updateControl) {

		UpdateCallback updateCallback = new UpdateCallback.Default();


		if (t == TopicType.STRING) {
			updateControl.updater().valueUpdater(String.class).update(
					TopicSelector,
					Message,
					updateCallback);
		}


		if (t == TopicType.INT64) {
			 long inte=Diffusion.dataTypes().int64().readValue(Message.getBytes());
			updateControl.updater().valueUpdater(long.class).update(TopicSelector,inte,updateCallback);
		}
		if (t == TopicType.DOUBLE) {
			updateControl.updater().valueUpdater(Double.class).update(
					TopicSelector,
					Double.parseDouble(Message),
					updateCallback);
		}

		if (t == TopicType.BINARY)
		{
			Binary b=Diffusion.dataTypes().binary().readValue(Message.getBytes());
			updateControl.updater().valueUpdater(Binary.class).update(TopicSelector,
					b,
					updateCallback);
		}
		if (t == TopicType.JSON)
		{
			JSON jon=Diffusion.dataTypes().json().fromJsonString(Message);
			updateControl.updater().valueUpdater(JSON.class).update(TopicSelector,
					jon,
					updateCallback);
		}

		/*if (t == TopicType.RECORD_V2) {
		{

			JSON value =  Diffusion.dataTypes().json().fromJsonString(Message);
			updateControl.updater().valueUpdater(JSON.class).update(
				TopicSelector,
					value,
					updateCallback);

	}*/


	}

	/*private boolean IsTopicExists()
	{
		TopicNotifications not=sess.feature(TopicNotifications.class);


	}*/
	private void addTopic(String TopicName, TopicType t, TopicControl control) throws InterruptedException, ExecutionException, TimeoutException {
		CompletableFuture<TopicControl.AddTopicResult> future = control.addTopic(
				TopicName,
				t);

		// Wait for the CompletableFuture to complete
		future.get(10, TimeUnit.SECONDS);
	}
	private void appendLineToStringBuilder(final StringBuilder sb, final String line){
		sb.append(line).append("\n");
	}

	/**
	 * This method allows to easily create an error result and log exception.
	 */
	private static SampleResult getErrorResult(final Context context, final SampleResult result, final String errorMessage, final Exception exception) {
		result.setError(true);
		result.setStatusCode("NL-SendMessage_ERROR");
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
