package com.kbdunn.vaadin.addons;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("addondemo")
public class VdnUI extends UI  {
	
	private static final long serialVersionUID = 1L;
	
	private VerticalLayout content;
	private TabSheet tabsheet;
	
	@Override
	protected void init(VaadinRequest request) {
		content = new VerticalLayout();
		setContent(content);
		content.addStyleName(ValoTheme.LAYOUT_WELL);
		content.setMargin(true);
		content.setSpacing(true);
		
		String sbody = "";
		sbody += "<span style='font-size: 22px'>Welcome to Bryson's Addon Demo App. Use the tabs below to browse addons.</span>";
		Label body = new Label(sbody, ContentMode.HTML);
		content.addComponent(body);
		
		tabsheet = new TabSheet();
		tabsheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
		tabsheet.setSizeFull();
		content.addComponent(tabsheet);
		
		tabsheet.addTab(new FontAwesomeAddonLayout(), "Font Awesome Label");
		tabsheet.addTab(new MejsAddonLayout(), "MediaElement.js Player");
		
		String footer = "";
		footer += "<ul id='links'>";
			footer += "<li><a href='http://fortawesome.github.io/Font-Awesome/'>Font Awesome</a></li>";
			footer += "<li><a href='http://mediaelementjs.com/'>MediaElement.js</a></li>";
			footer += "<li><a href='https://github.com/bdunn44/'>Bryson on GitHub</a></li>";
			footer += "<li><a href='http://www.linkedin.com/in/kbrysondunn/'>Bryson on LinkedIn</a></li>";
			footer += "<li><a href='http://cloudnimbus.org'>Nimbus Personal Cloud</a></li>";
		footer += "</ul>";
		content.addComponent(new Label(footer, ContentMode.HTML));
		
		if (request.getParameter("addon") != null) {
			String addon = request.getParameter("addon");
			if ("FontAwesomeLabel".equals(addon)) tabsheet.setSelectedTab(0);
			else if ("MejsPlayer".equals(addon)) tabsheet.setSelectedTab(1);
		}
	}
}