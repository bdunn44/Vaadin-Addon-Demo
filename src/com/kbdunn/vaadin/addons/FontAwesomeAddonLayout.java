package com.kbdunn.vaadin.addons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.kbdunn.vaadin.addons.fontawesome.FontAwesome;
import com.kbdunn.vaadin.addons.fontawesome.FontAwesomeLabel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class FontAwesomeAddonLayout extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private IconConfigurator baseConfig, stackedConfig;
	private Panel iconDisplay;
	private HorizontalLayout iconLayout;

	public FontAwesomeAddonLayout() {
		setMargin(true);
		setSpacing(true);
		buildLayout();
	}
	
	private void buildLayout() {
		Panel controlPanel = new Panel();
		controlPanel.setWidth("100%");
		addComponent(controlPanel);
		VerticalLayout controlLayout = new VerticalLayout();
		controlLayout.setMargin(true);
		controlLayout.setSpacing(true);
		controlPanel.setContent(controlLayout);
		
		baseConfig = new IconConfigurator(this);
		baseConfig.setCaption("Base Icon");
		controlLayout.addComponent(baseConfig);
		stackedConfig = new IconConfigurator(this);
		stackedConfig.setCaption("Stacked Icon");
		stackedConfig.setVisible(false);
		controlLayout.addComponent(stackedConfig);
		controlLayout.addComponent(new Button("Stack", new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (stackedConfig.isVisible()) {
					stackedConfig.setVisible(false);
					event.getComponent().setCaption("Stack");
					refresh();
				} else {
					stackedConfig.setVisible(true);
					event.getComponent().setCaption("Remove Stack");
				}
			}
		}));
		
		iconDisplay = new Panel();
		iconDisplay.addStyleName(ValoTheme.PANEL_WELL);
		iconDisplay.setHeight("200px");
		iconDisplay.setWidth("400px");
		addComponent(iconDisplay);
		setComponentAlignment(iconDisplay, Alignment.MIDDLE_CENTER);
		
		iconLayout = new HorizontalLayout();
		iconLayout.setSizeFull();
		iconLayout.setMargin(true);
		iconDisplay.setContent(iconLayout);
		
		Label link = new Label("<a href='https://vaadin.com/addon/fontawesomelabel' target='_blank'>https://vaadin.com/addon/fontawesomelabel</a>", ContentMode.HTML);
		link.setSizeUndefined();
		link.addStyleName(ValoTheme.LABEL_SMALL);
		addComponent(link);
		setComponentAlignment(link, Alignment.BOTTOM_RIGHT);
	}
	
	private void refresh() {
		baseConfig.setIcon(this.baseConfig.getCurrentIcon());
		FontAwesomeLabel icon = this.baseConfig.getCurrentLabel();
		icon.addStyleName(ValoTheme.LABEL_COLORED);
		icon.addStyleName("icon-preview-label");
		if (this.stackedConfig.isVisible()) {
			stackedConfig.setIcon(this.stackedConfig.getCurrentIcon());
			FontAwesomeLabel stacked = this.stackedConfig.getCurrentLabel();
			icon.stack(stacked);
		}
		iconLayout.removeAllComponents();
		iconLayout.addComponent(icon);
		iconLayout.setComponentAlignment(icon, Alignment.MIDDLE_CENTER);
	}
	
	private class IconConfigurator extends HorizontalLayout implements ValueChangeListener {
		
		private static final long serialVersionUID = -2067432519886807260L;
		private static final String DEFAULT = "default";
		private static final String SIZE_LARGE = "Large";
		private static final String SIZE_2X = "2x";
		private static final String SIZE_3X = "3x";
		private static final String SIZE_4X = "4x";
		private static final String SIZE_5X = "5x";
		private static final String SIZE_6X = "6x";
		private static final String ROTATE_90 = "90 Degrees";
		private static final String ROTATE_180 = "180 Degrees";
		private static final String ROTATE_270 = "270 Degrees";
		private static final String FLIP_HORIZONTAL = "Horizontal";
		private static final String FLIP_VERTICAL = "Vertical";
		private static final String PULL_LEFT = "Left";
		private static final String PULL_RIGHT = "Right";
		private static final String BORDER = "Enable Border";
		private static final String SPIN = "Spin";
		private static final String INVERSE_COLOR = "Inverse Color";
		private static final String REVERSE_STACK_SIZE = "Reverse Stack Size";
		private final String[] SIZES = new String[]{ DEFAULT, SIZE_LARGE, SIZE_2X, SIZE_3X, SIZE_4X, SIZE_5X, SIZE_6X };
		private final String[] ROTATIONS = new String[]{ DEFAULT, ROTATE_90, ROTATE_180, ROTATE_270 };
		private final String[] FLIPS = new String[]{ DEFAULT, FLIP_HORIZONTAL, FLIP_VERTICAL };
		private final String[] PULLS = new String[]{ DEFAULT, PULL_LEFT, PULL_RIGHT };
		private final String[] OTHERS = new String[]{ BORDER, SPIN, INVERSE_COLOR, REVERSE_STACK_SIZE };
		
		private FontAwesomeAddonLayout parent;
		private ComboBox icon, size, rotation, flip, pull;
		private OptionGroup other;
		
		public IconConfigurator(FontAwesomeAddonLayout parent) {
			this.parent = parent;
			addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
			setSpacing(true);
			setWidth("100%");
			build();
		}
		
		private void build() {
			icon = new ComboBox("Font Awesome Icon");
			icon.setImmediate(true);
			icon.addStyleName(ValoTheme.COMBOBOX_TINY);
			icon.setNewItemsAllowed(false);
			icon.setNullSelectionAllowed(false);
			icon.setTextInputAllowed(true);
			icon.setInputPrompt("Select an Icon");
			addComponent(icon);
			setComponentAlignment(icon, Alignment.MIDDLE_LEFT);
			for (FontAwesome fa : FontAwesome.values()) {
				icon.addItem(fa.name());
			}
			
			size = getEmptyOptionGroup();
			size.setImmediate(true);
			size.setCaption("Size:");
			for (String s : SIZES) {
				size.addItem(s);
			}
			addComponent(size);
			setComponentAlignment(size, Alignment.MIDDLE_LEFT);
			
			rotation = getEmptyOptionGroup();
			rotation.setImmediate(true);
			rotation.setCaption("Rotation:");
			for (String s : ROTATIONS) {
				rotation.addItem(s);
			}
			addComponent(rotation);
			setComponentAlignment(rotation, Alignment.MIDDLE_LEFT);
			
			flip = getEmptyOptionGroup();
			flip.setImmediate(true);
			flip.setCaption("Flip:");
			for (String s : FLIPS) {
				flip.addItem(s);
			}
			addComponent(flip);
			setComponentAlignment(flip, Alignment.MIDDLE_LEFT);
			
			pull = getEmptyOptionGroup();
			pull.setImmediate(true);
			pull.setCaption("Pull:");
			for (String s : PULLS) {
				pull.addItem(s);
			}
			addComponent(pull);
			setComponentAlignment(pull, Alignment.MIDDLE_LEFT);
			
			other = new OptionGroup();
			other.addStyleName(ValoTheme.OPTIONGROUP_SMALL);
			other.setNewItemsAllowed(false);
			other.setImmediate(true);
			other.setCaption("Other Options:");
			other.setMultiSelect(true);
			for (String s : OTHERS) {
				other.addItem(s);
			}
			addComponent(other);
			setComponentAlignment(other, Alignment.MIDDLE_LEFT);
			
			icon.addValueChangeListener(this);
			size.addValueChangeListener(this);
			rotation.addValueChangeListener(this);
			flip.addValueChangeListener(this);
			pull.addValueChangeListener(this);
			other.addValueChangeListener(this);
		}
		
		private ComboBox getEmptyOptionGroup() {
			ComboBox og = new ComboBox();
			og.addStyleName(ValoTheme.COMBOBOX_TINY);
			og.setNewItemsAllowed(false);
			og.setNullSelectionAllowed(true);
			og.setNullSelectionItemId(DEFAULT);
			return og;
		}
		
		protected FontAwesomeLabel getCurrentLabel() {
			FontAwesomeLabel current = getCurrentIcon().getLabel();
			
			String size = (String) this.size.getValue();
			String rotation = (String) this.rotation.getValue();
			String flip = (String) this.flip.getValue();
			String pull = (String) this.pull.getValue();
			
			if (!DEFAULT.equals(size)) {
				if (SIZE_LARGE.equals(size))
					current.setSizeLg();
				else if (SIZE_2X.equals(size))
					current.setSize2x();
				else if (SIZE_3X.equals(size))
					current.setSize3x();
				else if (SIZE_4X.equals(size))
					current.setSize4x();
				else if (SIZE_5X.equals(size))
					current.setSize5x();
				else if (SIZE_6X.equals(size))
					current.setSize6x();
			}
			
			if (!DEFAULT.equals(rotation)) {
				if (ROTATE_90.equals(rotation))
					current.rotate90();
				else if (ROTATE_180.equals(rotation))
					current.rotate180();
				else if (ROTATE_270.equals(rotation))
					current.rotate270();
			}
			
			if (!DEFAULT.equals(flip)) {
				if (FLIP_HORIZONTAL.equals(flip))
					current.flipHorizontal();
				else if (FLIP_VERTICAL.equals(flip))
					current.flipVertical();
			}
			
			if (!DEFAULT.equals(pull)) {
				if (PULL_LEFT.equals(pull))
					current.pullLeft();
				else if (PULL_RIGHT.equals(pull))
					current.pullRight();
			}
			
			Object others = this.other.getValue();
			List<String> otherOptions =  new ArrayList<String>();
			if (others instanceof Collection) {
				for (Object o : (Collection<?>) others) {
					otherOptions.add((String) o);
				}
			} else if (this.other.getValue() instanceof String) {
				otherOptions.add((String) this.other.getValue());
			}
			for (String s : otherOptions) {
				if (BORDER.equals(s))
					current.enableBorder();
				else if (SPIN.equals(s))
					current.spin();
				else if (INVERSE_COLOR.equals(s))
					current.inverseColor();
				else if (REVERSE_STACK_SIZE.equals(s))
					current.reverseStackSize();
			}
			
			
			return current;
		}
		
		protected FontAwesome getCurrentIcon() {
			return FontAwesome.valueOf((String) icon.getValue());
		}

		@Override
		public void valueChange(ValueChangeEvent event) {
			parent.refresh();
		}
	}
}
