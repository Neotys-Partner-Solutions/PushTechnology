package com.neotys.pushtechnology.diffusion.Monitoring;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.base.Strings;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;
import com.neotys.pushtechnology.diffusion.Utils.DiffusionAggregator;
import com.neotys.pushtechnology.diffusion.Utils.DiffusionStat;
import com.neotys.rest.dataexchange.client.DataExchangeAPIClient;
import com.neotys.rest.dataexchange.client.DataExchangeAPIClientFactory;
import com.neotys.rest.dataexchange.model.ContextBuilder;


public final class MonitoringActionEngine implements ActionEngine {
	static DiffusionAggregator aggregator;
	private DataExchangeAPIClient client;
	private String NeoloadHost;
	private String NeoLoadPort;
	private String NeoLoadAPI;
	private ContextBuilder Context;
	private String Location;
	private Timer time;
	static final int TIMERFREQUENCY=5000;
	static final int TIMERDELAY=0;
	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();


		for(ActionParameter parameter:parameters) {
			switch(parameter.getName())
			{
				case  MonitoringAction.NeoLoadHost:
					NeoloadHost = parameter.getValue();
					break;
				case  MonitoringAction.NeoLoadPort:
					NeoLoadPort = parameter.getValue();
					break;
				case  MonitoringAction.NeoLoadAPI:
					NeoLoadAPI = parameter.getValue();
					break;

			}
		}

		try
		{

			Context = new ContextBuilder();
			Context.hardware("PushTechnology").location("Neoload").software("Diffusion")

					.script("DiffusionMonitoring" + System.currentTimeMillis());

			client = DataExchangeAPIClientFactory.newClient("http://"+NeoloadHost+":"+NeoLoadPort+"/DataExchange/v1/Service.svc/", Context.build(), NeoLoadAPI);
			aggregator=new DiffusionAggregator(client);

			StartTimer();
			Thread.sleep(10000);
			StopTimer();
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

	public void StartTimer()
	{
		time = new Timer();
		time.scheduleAtFixedRate(aggregator,TIMERDELAY,TIMERFREQUENCY);
	}

	public void StopTimer()
	{
		aggregator.cancel();
	}
	/**
	 * This method allows to easily create an error result and log exception.
	 */
	private static SampleResult getErrorResult(final Context context, final SampleResult result, final String errorMessage, final Exception exception) {
		result.setError(true);
		result.setStatusCode("NL-Monitoring_ERROR");
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
		StopTimer();
	}


}
