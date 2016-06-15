package com.kbdunn.vaadin.addons;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.kbdunn.vaadin.addons.mediaelement.MediaElementPlayer;
import com.kbdunn.vaadin.addons.mediaelement.interfaces.CanPlayListener;
import com.kbdunn.vaadin.addons.mediaelement.interfaces.LoadedDataListener;
import com.kbdunn.vaadin.addons.mediaelement.interfaces.LoadedMetadataListener;
import com.kbdunn.vaadin.addons.mediaelement.interfaces.PausedListener;
import com.kbdunn.vaadin.addons.mediaelement.interfaces.PlaybackEndedListener;
import com.kbdunn.vaadin.addons.mediaelement.interfaces.PlayedListener;
import com.kbdunn.vaadin.addons.mediaelement.interfaces.PlayingListener;
import com.kbdunn.vaadin.addons.mediaelement.interfaces.SeekedListener;
import com.kbdunn.vaadin.addons.mediaelement.interfaces.StateUpdatedListener;
import com.kbdunn.vaadin.addons.mediaelement.interfaces.VolumeChangedListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.util.FileTypeResolver;

@SuppressWarnings("unused")
public class MejsAddonLayout extends VerticalLayout implements 
		CanPlayListener, LoadedDataListener, LoadedMetadataListener, PausedListener, 
		PlayedListener, SeekedListener, VolumeChangedListener, PlaybackEndedListener, PlayingListener, StateUpdatedListener {

	private static final long serialVersionUID = 1L;
	private static Map<String, Resource> MEDIA_FILES;
	
	static {
		FileTypeResolver.addExtension("ogg", "audio/ogg");
 		FileTypeResolver.addExtension("ogv", "video/ogg");
 		FileTypeResolver.addExtension("mp4", "video/mp4");
 		FileTypeResolver.addExtension("webm", "video/webm");
 		FileTypeResolver.addExtension("wmv", "video/x-ms-wmv");
 		FileTypeResolver.addExtension("wma", "audio/x-ms-wma");
 		FileTypeResolver.addExtension("flv", "video/x-flv");
 		FileTypeResolver.addExtension("avi", "video/x-msvideo");
		
		MEDIA_FILES = new LinkedHashMap<String, Resource>();
		if (false) { // set to true to test all resource types
			final File mp3File = new File("../standalone/deployments/VaadinAddonDemo.war/VAADIN/themes/addondemo/songs/radiohead.ogg"); // Wildfly. YMMV.
			MEDIA_FILES.put("FileResource", new FileResource(mp3File));
			MEDIA_FILES.put("ClassResource", new ClassResource("VAADIN/themes/addondemo/songs/radiohead.ogg"));
			MEDIA_FILES.put("StreamResource", new StreamResource(() -> { 
				try {
					return new FileInputStream(mp3File);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}, "radiohead.ogg"));
		}
		MEDIA_FILES.put("Bonobo - Noctuary (.mp3)", new ThemeResource("songs/01_Noctuary.mp3"));
		MEDIA_FILES.put("Radiohead - Pyramid Song (.ogg)", new ThemeResource("songs/radiohead.ogg"));
		MEDIA_FILES.put("Tame Impala - The Less I Know The Better (.wma)", new ThemeResource("songs/tame_impala.wma"));
		MEDIA_FILES.put("The Art of Flight - Trailer (YouTube)", new ExternalResource("https://www.youtube.com/watch?v=kh29_SERH0Y"));
		MEDIA_FILES.put("Danny MacAskill - Way Back Home (Vimeo)", new ExternalResource("https://vimeo.com/17892962"));
		MEDIA_FILES.put("José González - Down the Line (.flv)", new ThemeResource("videos/Down_the_Line.flv"));
		MEDIA_FILES.put("alt-J - Left Hand Free (.mp4)", new ThemeResource("videos/alt-J-Left_Hand_Free.mp4"));
		MEDIA_FILES.put("Gramatik - So Much For Love (.webm)", new ThemeResource("videos/gramatik.webm"));
	}
	
	private MediaElementPlayer player;
	private Label nowPlaying;
	private ComboBox resources;
	private Button play, pause, setVolume, setCurrentTime, mute, unmute;
	private TextField currentTimeInput, volumeInput, currentTimeDisplay, durationDisplay, volumeDisplay;
	private VerticalLayout playerLayout;
	
	public MejsAddonLayout() { 
		setMargin(true);
		setSpacing(true);
		buildLayout();
	}
	
	private void buildLayout() {
		setMargin(true);
		addComponent(new ControlPanel());
		
		Panel playerPanel = new Panel();
		playerPanel.setWidth("80%");
		playerPanel.addStyleName(ValoTheme.PANEL_WELL);
		playerLayout = new VerticalLayout();
		playerLayout.setMargin(true);
		playerLayout.setSpacing(true);
		playerLayout.setSizeFull();
		playerPanel.setContent(playerLayout);
		addComponent(playerPanel);
		setComponentAlignment(playerPanel, Alignment.MIDDLE_CENTER);
		
		Label h1 = new Label("MediaElement.js Player");
		h1.addStyleName(ValoTheme.LABEL_H1);
		h1.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		h1.setSizeUndefined();
		playerLayout.addComponent(h1);
		playerLayout.setComponentAlignment(h1, Alignment.MIDDLE_CENTER);
		player = new MediaElementPlayer();
		player.addCanPlayListener(this);
		player.addLoadedDataListener(this);
		player.addPauseListener(this);
		player.addPlaybackEndedListener(this);
		player.addPlayingListener(this);
		player.addPlayListener(this);
		player.addSeekedListener(this);
		player.addVolumeChangeListener(this);
		player.addStateUpdatedListener(this);
		nowPlaying = new Label();
		nowPlaying.setSizeUndefined();
		playerLayout.addComponent(nowPlaying);
		playerLayout.setComponentAlignment(nowPlaying, Alignment.MIDDLE_CENTER);
		playerLayout.addComponent(player);
		playerLayout.setComponentAlignment(player, Alignment.MIDDLE_CENTER);
		
		Label link = new Label("<a href='https://vaadin.com/directory#!addon/mediaelementjs-player' "
				+ "target='_blank'>https://vaadin.com/directory#!addon/mediaelementjs-player</a>", ContentMode.HTML);
		link.setSizeUndefined();
		link.addStyleName(ValoTheme.LABEL_SMALL);
		addComponent(link);
		setComponentAlignment(link, Alignment.BOTTOM_RIGHT);
	}

	@Override
	public void volumeChanged(MediaElementPlayer player) {
		Notification.show("Volume Changed!", Notification.Type.TRAY_NOTIFICATION);
	}

	@Override
	public void seeked(MediaElementPlayer player) {
		Notification.show("Seeked!", Notification.Type.TRAY_NOTIFICATION);
	}
	
	@Override
	public void played(MediaElementPlayer player) {
		Notification.show("Played!", Notification.Type.TRAY_NOTIFICATION);
	}

	@Override
	public void paused(MediaElementPlayer player) {
		Notification.show("Paused!", Notification.Type.TRAY_NOTIFICATION);
	}

	@Override
	public void metadataLoaded(MediaElementPlayer player) {
		Notification.show("Metadata Loaded!", Notification.Type.TRAY_NOTIFICATION);
	}

	@Override
	public void dataLoaded(MediaElementPlayer player) {
		Notification.show("Data Loaded!", Notification.Type.TRAY_NOTIFICATION);
	}

	@Override
	public void canPlay(MediaElementPlayer player) {
		Notification.show("Can Play!", Notification.Type.TRAY_NOTIFICATION);
	}

	@Override
	public void playing(MediaElementPlayer player) {
		Notification.show("Playing!", Notification.Type.TRAY_NOTIFICATION);
	}

	@Override
	public void playbackEnded(MediaElementPlayer player) {
		Notification.show("Playback Ended!", Notification.Type.TRAY_NOTIFICATION);
	}

	@Override
	public void stateUpdated(MediaElementPlayer player) {
		currentTimeDisplay.setValue(String.valueOf(player.getCurrentTime()));
		durationDisplay.setValue(String.valueOf(player.getDuration()));
		volumeDisplay.setValue(String.valueOf(player.getVolume()));
	}
	
	class ControlPanel extends Panel {
		
		private static final long serialVersionUID = 1L;
		private VerticalLayout content;
		
		public ControlPanel() {
			content = new VerticalLayout();
			content.setMargin(true);
			content.setSpacing(true);
			setContent(content);
			
			initMediaSelectors();
			initControlButtons();
			initInfoLabels();
			initControlInputs();
			
			content.addComponent(resources);
			
			HorizontalLayout inputs = new HorizontalLayout();
			inputs.setSpacing(true);
			inputs.addComponent(currentTimeDisplay);
			inputs.addComponent(durationDisplay);
			inputs.addComponent(volumeDisplay);
			inputs.addComponent(currentTimeInput);
			inputs.addComponent(volumeInput);
			content.addComponent(inputs);
			
			HorizontalLayout controlButtons = new HorizontalLayout();
			controlButtons.setSpacing(true);
			controlButtons.addComponent(play);
			controlButtons.addComponent(pause);
			controlButtons.addComponent(setVolume);
			controlButtons.addComponent(setCurrentTime);
			controlButtons.addComponent(mute);
			controlButtons.addComponent(unmute);
			content.addComponent(controlButtons);
		}
		
		private void initMediaSelectors() {
			resources = new ComboBox("Load File:");
			resources.setWidth("250px");
			resources.setNewItemsAllowed(false);
			resources.setTextInputAllowed(false);
			resources.setNullSelectionAllowed(false);
			resources.setInputPrompt("Select a File");
			for (String key : MEDIA_FILES.keySet()) {
				resources.addItem(key);
			}
			resources.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String key = (String) resources.getValue();
					player.setSource(MEDIA_FILES.get(key));
					nowPlaying.setValue("Now Playing: " + key);
				}
			});
		}
		
		private void initControlButtons() {
			play = new Button("Play", new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					player.play();
				}
			});
			pause = new Button("Pause", new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					player.pause();
				}
			});
			setVolume = new Button("Set Volume", new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					Integer v = null;
					try {
						v = Integer.valueOf(volumeInput.getValue());
						if (v < 0 || v > 10) {
							throw new IllegalArgumentException();
						}
						player.setVolume(v);
						volumeInput.setComponentError(null);
					} catch (Exception e) {
						volumeInput.setComponentError(new UserError("Enter a number 1-10"));
					}
				}
			});
			setCurrentTime = new Button("Set Current Time", new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					Integer t = null;
					try {
						t = Integer.valueOf(currentTimeInput.getValue());
						player.setCurrentTime(t);
						currentTimeInput.setComponentError(null);
					} catch (Exception e) {
						currentTimeInput.setComponentError(new UserError("Enter a number 0-" + player.getDuration()));
					}
				}
			});
			mute = new Button("Mute", new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					player.mute();
				}
			});
			unmute = new Button("Unmute", new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					player.unmute();
				}
			});
		}
		
		private void initInfoLabels() {
			currentTimeDisplay = new TextField("Current Time (seconds)");
			currentTimeDisplay.setEnabled(false);
			durationDisplay = new TextField("Duration (seconds)");
			durationDisplay.setEnabled(false);
			volumeDisplay = new TextField("Current Volume (1-10)");
			volumeDisplay.setEnabled(false);
		}
		
		private void initControlInputs() {
			currentTimeInput = new TextField("Set Current Time (seconds):");
			currentTimeInput.setValue("0");
			currentTimeInput.setRequired(true);
			volumeInput = new TextField("Set Volume (1-10):");
			volumeInput.setValue("10");
			volumeInput.setRequired(true);
		}
	}
}
