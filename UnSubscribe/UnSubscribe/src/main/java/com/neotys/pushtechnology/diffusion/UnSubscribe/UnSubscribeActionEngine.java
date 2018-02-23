package com.neotys.pushtechnology.diffusion.UnSubscribe;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.base.Strings;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;
import com.pushtechnology.diffusion.client.content.Content;
import com.pushtechnology.diffusion.client.features.Topics;
import com.pushtechnology.diffusion.client.session.Session;
import com.pushtechnology.diffusion.client.topics.details.TopicType;


public final class UnSubscribeActionEngine implements ActionEngine {
	private String TopicSelector;


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
				case  UnSubscribeAction.TopicSelector:
					TopicSelector = parameter.getValue();
					break;

			}
		}


			if (Strings.isNullOrEmpty(TopicSelector)) {
			return getErrorResult(context, sampleResult, "Invalid argument: TopicSelector cannot be null "
					+ UnSubscribeAction.TopicSelector + ".", null);
		}

		try
		{
			topics=(Topics)context.getCurrentVirtualUser().get("Topics_"+TopicSelector);
			if(topics!=null)
			{

					sampleResult.sampleStart();

					topics.unsubscribe(TopicSelector);
										// TODO perform execution.

					sampleResult.sampleEnd();
					sampleResult.setRequestContent(requestBuilder.toString());
					sampleResult.setResponseContent(responseBuilder.toString());

			}
			else
				{
					return getErrorResult(context, sampleResult, "No subsciption done on this topic", null);
				}
		}
		catch(Exception e)
		{
			return getErrorResult(context, sampleResult, "Technical Error ",e);
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
		result.setStatusCode("NL-UnSubscribe_ERROR");
		result.setResponseContent(errorMessage);
		if(exception != null){
			context.getLogger().error(errorMessage, exception);
		} else{
			context.getLogger().error(errorMessage);
		}
		return result;
	}

	@Override
	public void stopExecute() {
		// TODO add code executed when the test have to stop.
	}


}
