package com.kbdunn.vaadin.addons;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OAuth2PopupLayout extends VerticalLayout {
	
	private static final long serialVersionUID = 3494007695954643890L;

	public OAuth2PopupLayout() {
		buildLayout();
	}
	
	private void buildLayout() {
		setMargin(true);
		setSpacing(true);
		
		Panel buttonPanel = new Panel();
		buttonPanel.setWidth("40%");
		buttonPanel.addStyleName(ValoTheme.PANEL_WELL);
		addComponent(buttonPanel);
		setComponentAlignment(buttonPanel, Alignment.MIDDLE_CENTER);
		HorizontalLayout panelLayout = new HorizontalLayout();
		panelLayout.setSpacing(true);
		buttonPanel.setContent(panelLayout);
		VerticalLayout demoLayout = new org.vaadin.addon.oauthpopup.demo.DemoLayout();
		panelLayout.addComponent(demoLayout);
		panelLayout.setComponentAlignment(demoLayout, Alignment.MIDDLE_CENTER);

		Label fyi = new Label("This demo DOES NOT store or otherwise use OAuth tokens for anything other than demonstration purposes. "
				+ "The source code for this demo is available on "
				+ "<a href='https://github.com/bdunn44/vaadin-oauthpopup/tree/master/oauthpopup-demo' target='_blank'>GitHub</a>"
				+ " if you're nervous.", ContentMode.HTML);
		fyi.addStyleName(ValoTheme.LABEL_SMALL);
		fyi.addStyleName(ValoTheme.LABEL_LIGHT);
		addComponent(fyi);
		
		Label link = new Label("<a href='https://vaadin.com/directory#!addon/oauth2-popup-add-on' "
				+ "target='_blank'>https://vaadin.com/directory#!addon/oauth2-popup-add-on</a>", ContentMode.HTML);
		link.setSizeUndefined();
		link.addStyleName(ValoTheme.LABEL_SMALL);
		addComponent(link);
		setComponentAlignment(link, Alignment.BOTTOM_RIGHT);
	}
}
