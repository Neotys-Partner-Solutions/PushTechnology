package com.neotys.pushtechnology.diffusion.CreateSession;

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

public final class CreateSessionAction implements Action{
	private static final String BUNDLE_NAME = "com.neotys.pushtechnology.diffusion.CreateSession.bundle";
	private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayName");
	private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");
	private static final ImageIcon LOGO_ICON;
	static final String Diffusion_URL="Diffusion_URL";
	static final String Diffusion_Username="Diffusion_Username";
	static final String Diffusion_Password="Diffusion_Password";
	static {
		final URL iconURL = CreateSessionAction.class.getResource("pushtechnology.png");
		if (iconURL != null) {
			LOGO_ICON = new ImageIcon(iconURL);
		}
		else {
			LOGO_ICON = null;
		}
	}
	@Override
	public String getType() {
		return "CreateSession";
	}

	@Override
	public List<ActionParameter> getDefaultActionParameters() {
		final List<ActionParameter> parameters = new ArrayList<ActionParameter>();
		// TODO Add default parameters.
		parameters.add(new ActionParameter(Diffusion_URL,"ws://host:8080"));
		parameters.add(new ActionParameter(Diffusion_Username,"UserName"));
		parameters.add(new ActionParameter(Diffusion_Password,"password"));
		return parameters;
	}

	@Override
	public Class<? extends ActionEngine> getEngineClass() {
		return CreateSessionActionEngine.class;
	}

	@Override
	public Icon getIcon() {

		return LOGO_ICON;
	}

	@Override
	public boolean getDefaultIsHit(){
		return true;
	}

	@Override
	public String getDescription() {
		final StringBuilder description = new StringBuilder();
		// TODO Add description
		description.append("CreateSession description.\n")
				.append("The parameters are : \n")
				.append("Diffusion_URL  : Url to connect to Diffusion\n")
				.append("UserName  : Username to authenticate to diffusion\n")
				.append("Password : Password\n");
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
