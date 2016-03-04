package com.kbdunn.vaadin.addons;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = VdnUI.class)
public class VdnServlet extends VaadinServlet  { 
	private static final long serialVersionUID = 1L; 
}
