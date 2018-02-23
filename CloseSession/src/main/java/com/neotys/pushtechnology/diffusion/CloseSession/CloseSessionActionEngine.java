package com.neotys.pushtechnology.diffusion.CloseSession;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import com.google.common.base.Strings;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;
import com.pushtechnology.diffusion.client.Diffusion;
import com.pushtechnology.diffusion.client.features.control.topics.TopicUpdateControl;
import com.pushtechnology.diffusion.client.session.Session;

public final class CloseSessionActionEngine implements ActionEngine {

	private String Diffusion_URL;

	private Session sess;
	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();
		String host = null;

		for(ActionParameter parameter:parameters) {
			switch(parameter.getName())
			{
				case  CloseSessionAction.Diffusion_URL:
					Diffusion_URL = parameter.getValue();
					break;



			}
		}

		if (Strings.isNullOrEmpty(Diffusion_URL)) {
			return getErrorResult(context, sampleResult, "Invalid argument: Diffusion_URL cannot be null "
					+ CloseSessionAction.Diffusion_URL + ".", null);
		}


		try {
			host=GetHostNamefromUrl();
			sess = (Session)context.getCurrentVirtualUser().get(host+"Session");

			if(sess!=null)
			{
				if(sess.getState().isConnected()) {
					sampleResult.sampleStart();

					sess.close();
					sess=null;
					sampleResult.sampleEnd();


						appendLineToStringBuilder(responseBuilder, "Session Closed on " + Diffusion_URL);
					// TODO perform execution.


					sampleResult.setRequestContent(requestBuilder.toString());
					sampleResult.setResponseContent(responseBuilder.toString());
				}
				else
				{
					return getErrorResult(context, sampleResult, "Session already closed on "
							+ CloseSessionAction.Diffusion_URL + ".", null);
				}
			}
			else
			{
				return getErrorResult(context, sampleResult, "There are no session defined on : "
						+ CloseSessionAction.Diffusion_URL + ".", null);
			}
		}
		catch (Exception e)
		{
			return getErrorResult(context, sampleResult, "Technical Error : "
					, e);
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
		result.setStatusCode("NL-CloseSession_ERROR");
		result.setResponseContent(errorMessage);
		if(exception != null){
			context.getLogger().error(errorMessage, exception);
		} else{
			context.getLogger().error(errorMessage);
		}
		return result;
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
