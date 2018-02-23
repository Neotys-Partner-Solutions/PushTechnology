package com.neotys.pushtechnology.diffusion.Monitoring;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;

import com.google.common.base.Optional;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.pushtechnology.diffusion.Monitoring.MonitoringActionEngine;

public final class MonitoringAction implements Action{
	private static final String BUNDLE_NAME = "com.neotys.pushtechnology.diffusion.Monitoring.bundle";
	private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayName");
	private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");
	private static final ImageIcon LOGO_ICON;
	static final String NeoLoadHost="NeoLoadHost";
	static final String NeoLoadPort="NeoLoadPort";
	static final String NeoLoadAPI="NeoLoadAPI";
	static {
		final URL iconURL = MonitoringAction.class.getResource("pushtechnology.png");
		if (iconURL != null) {
			LOGO_ICON = new ImageIcon(iconURL);
		}
		else {
			LOGO_ICON = null;
		}
	}
	@Override
	public String getType() {
		return "Monitoring";
	}

	@Override
	public List<ActionParameter> getDefaultActionParameters() {
		final List<ActionParameter> parameters = new ArrayList<ActionParameter>();
		parameters.add(new ActionParameter(NeoLoadHost,"localhost"));
		parameters.add(new ActionParameter(NeoLoadPort,"7400"));

		// TODO Add default parameters.
		// TODO Add default parameters.



		return parameters;
	}

	@Override
	public Class<? extends ActionEngine> getEngineClass() {
		return MonitoringActionEngine.class;
	}

	@Override
	public Icon getIcon() {
		// TODO Add an icon
		return LOGO_ICON;
	}

	@Override
	public boolean getDefaultIsHit(){
		return false;
	}

	@Override
	public String getDescription() {
		final StringBuilder description = new StringBuilder();
		// TODO Add description
		description.append("Monitoring action Retrieve KPI to NL dataExchangeAPI")
				.append("The parameters are : \n")
				.append("NeoLoadHost  : Host or IP of the NeoLoad Controller\n")
				.append("NeoLoadPort  : port of the dataexchange APi\n")
				.append("NeoLoadAPI : Optionnal API key of the DataExchangeAPI");

		return description.toString();
	}

	@Override
	public String getDisplayName() {
		return DISPLAY_NAME;
	}

	@Override
	public String getDisplayPath() {
		return DISPLAY_PATH;
	}

	@Override
	public Optional<String> getMinimumNeoLoadVersion() {
		return Optional.of("6.1");
	}

	@Override
	public Optional<String> getMaximumNeoLoadVersion() {
		return Optional.absent();
	}
}
