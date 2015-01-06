package com.kbdunn.vaadin.addons;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("addondemo")
public class VdnUI extends UI  {
	
	private static final long serialVersionUID = 1L;
	
	private VerticalLayout layout;
	private TabSheet tabsheet;
	
	@Override
	protected void init(VaadinRequest request) {
		setSizeFull();
		Panel content = new Panel();
		content.setSizeFull();
		setContent(content);
		
		layout = new VerticalLayout();
		content.setContent(layout);
		layout.addStyleName(ValoTheme.LAYOUT_WELL);
		layout.setMargin(true);
		layout.setSpacing(true);
		
		String sbody = "<h2 style='margin:0 30px;'>Welcome to Bryson's Addon Demo App!</h2>";
		sbody += "<p style='font-size: 16px; margin: 10px 30px;'>Use the tabs below to browse addons. "
				+ "You can find the source code for this demo on GitHub <a href='https://github.com/bdunn44/Vaadin-Addon-Demo'>here</a>.</p>";
		Label body = new Label(sbody, ContentMode.HTML);
		layout.addComponent(body);
		
		tabsheet = new TabSheet();
		tabsheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
		tabsheet.setSizeFull();
		layout.addComponent(tabsheet);
		
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
		layout.addComponent(new Label(footer, ContentMode.HTML));
		
		if (request.getParameter("a") != null) {
			String addon = request.getParameter("a");
			if ("FontAwesomeLabel".equals(addon)) tabsheet.setSelectedTab(0);
			else if ("MejsPlayer".equals(addon)) tabsheet.setSelectedTab(1);
		}
	}
}