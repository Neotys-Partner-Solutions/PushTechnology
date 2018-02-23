package com.neotys.pushtechnology.diffusion.CreateSession;

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

public final class CreateSessionActionEngine implements ActionEngine {

	private String Diffusion_URL;
	private String Username;
	private String Password;
	private Session sess;


	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();

		String host = null;
		//sess=null;
		for(ActionParameter parameter:parameters) {
			switch(parameter.getName())
			{
				case  CreateSessionAction.Diffusion_URL:
					Diffusion_URL = parameter.getValue();
					break;
				case  CreateSessionAction.Diffusion_Username:
					Username = parameter.getValue();
					break;
				case  CreateSessionAction.Diffusion_Password:
					Password = parameter.getValue();
					break;


			}
		}

		if (Strings.isNullOrEmpty(Diffusion_URL)) {
			return getErrorResult(context, sampleResult, "Invalid argument: Diffusion_URL cannot be null "
					+ CreateSessionAction.Diffusion_URL + ".", null);
		}
		if (!Strings.isNullOrEmpty(Username)) {
			if (!Strings.isNullOrEmpty(Password)) {

				return getErrorResult(context, sampleResult, "Invalid argument: Diffusion_Password cannot be null if you define a Username"
						+ CreateSessionAction.Diffusion_Password + ".", null);
			}
		}
		if (!Strings.isNullOrEmpty(Password)) {
			if (!Strings.isNullOrEmpty(Username)) {

				return getErrorResult(context, sampleResult, "Invalid argument: Diffusion_UserName cannot be null if you define a password"
						+ CreateSessionAction.Diffusion_Username + ".", null);
			}
		}

		try {
			sampleResult.sampleStart();
			host=GetHostNamefromUrl();
			CreateDiffusionsession();
			sampleResult.sampleEnd();


			appendLineToStringBuilder(requestBuilder, "CreateSession request.");
			appendLineToStringBuilder(responseBuilder, "Session created on " + Diffusion_URL);
			// TODO perform execution.


			sampleResult.setRequestContent(requestBuilder.toString());
			sampleResult.setResponseContent(responseBuilder.toString());
			context.getCurrentVirtualUser().put( host+"Session",sess);
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
		result.setStatusCode("NL-CreateSession_ERROR");
		result.setResponseContent(errorMessage);
		if(exception != null){
			context.getLogger().error(errorMessage, exception);
		} else{
			context.getLogger().error(errorMessage);
		}
		return result;
	}

	private  void CreateDiffusionsession()
	{

		if(Username!= null && Password!=null)
		{
			sess= Diffusion.sessions().principal(Username)
				.password(Password).open(Diffusion_URL);
		}
		else
		{
			sess= Diffusion.sessions().open(Diffusion_URL);
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
