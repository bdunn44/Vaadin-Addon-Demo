package com.kbdunn.vaadin.addons;

import java.util.LinkedHashMap;
import java.util.Map;

import com.kbdunn.vaadin.addons.mediaelement.CanPlayListener;
import com.kbdunn.vaadin.addons.mediaelement.LoadedDataListener;
import com.kbdunn.vaadin.addons.mediaelement.LoadedMetadataListener;
import com.kbdunn.vaadin.addons.mediaelement.MediaComponent;
import com.kbdunn.vaadin.addons.mediaelement.PausedListener;
import com.kbdunn.vaadin.addons.mediaelement.PlaybackEndedListener;
import com.kbdunn.vaadin.addons.mediaelement.PlayedListener;
import com.kbdunn.vaadin.addons.mediaelement.PlayingListener;
import com.kbdunn.vaadin.addons.mediaelement.SeekedListener;
import com.kbdunn.vaadin.addons.mediaelement.VolumeChangedListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
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

public class MejsAddonLayout extends VerticalLayout implements 
		CanPlayListener, LoadedDataListener, LoadedMetadataListener, PausedListener, 
		PlayedListener, SeekedListener, VolumeChangedListener, PlaybackEndedListener, PlayingListener {

	private static final long serialVersionUID = 1L;
	private static Map<String, Resource> MEDIA_FILES;
	private static final String SONG_ACDC = "AC/DC - Back in Black (.ogg)";
	private static final String SONG_BONOBO = "Bonobo - Noctuary (.mp3)";
	private static final String VIDEO_FAKER = "Chet Faker - Archangel (.mp4)";
	private static final String VIDEO_ALTJ = "alt-J - Left Hand Free (.mp4)";
	
	static {
		MEDIA_FILES = new LinkedHashMap<String, Resource>();
		MEDIA_FILES.put(SONG_BONOBO, new ThemeResource("songs/01_Noctuary.mp3"));
		MEDIA_FILES.put(SONG_ACDC, new ThemeResource("songs/ACDC_-_Back_In_Black-sample.ogg"));
		MEDIA_FILES.put(VIDEO_FAKER, new ThemeResource("videos/Chet_Faker-Archangel_Live_Sessions.mp4"));
		MEDIA_FILES.put(VIDEO_ALTJ, new ThemeResource("videos/alt-J-Left_Hand_Free.mp4"));
		MEDIA_FILES.put(VIDEO_ALTJ, new ThemeResource("videos/alt-J-Left_Hand_Free.mp4"));
	}
	
	private MediaComponent player;
	private Label nowPlaying;
	private ComboBox resources;
	private Button play, pause, setVolume, setCurrentTime, mute, unmute;
	private TextField currentTimeValue, volumeValue, currentTime, duration, volume;
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
		playerPanel.setSizeUndefined();
		playerPanel.addStyleName(ValoTheme.PANEL_WELL);
		playerLayout = new VerticalLayout();
		playerLayout.setMargin(true);
		playerLayout.setSpacing(true);
		playerPanel.setContent(playerLayout);
		addComponent(playerPanel);
		setComponentAlignment(playerPanel, Alignment.MIDDLE_CENTER);
		
		Label h1 = new Label("MediaElement.js Player");
		h1.addStyleName(ValoTheme.LABEL_H1);
		h1.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		playerLayout.addComponent(h1);
		playerLayout.setComponentAlignment(h1, Alignment.MIDDLE_CENTER);
		player = new MediaComponent(MediaComponent.Type.AUDIO);
		nowPlaying = new Label();
		playerLayout.addComponent(nowPlaying);
		playerLayout.setComponentAlignment(nowPlaying, Alignment.MIDDLE_CENTER);
		playerLayout.addComponent(player);
		playerLayout.setComponentAlignment(player, Alignment.MIDDLE_CENTER);
		
		Label link = new Label("<a href='https://vaadin.com/addon/mediaelementjs-player' target='_blank'>https://vaadin.com/addon/mediaelementjs-player</a>", ContentMode.HTML);
		link.setSizeUndefined();
		link.addStyleName(ValoTheme.LABEL_SMALL);
		addComponent(link);
		setComponentAlignment(link, Alignment.BOTTOM_RIGHT);
	}
	
	private void showAudio() {
		MediaComponent newplayer = new MediaComponent(MediaComponent.Type.AUDIO);
		playerLayout.replaceComponent(player, newplayer);
		player = newplayer;
		player.addVolumeChangeListener(this);
		player.addCanPlayListener(this);
		player.addLoadedDataListener(this);
		player.addLoadedMetadataListener(this);
		player.addPauseListener(this);
		player.addPlaybackEndedListener(this);
		player.addPlayingListener(this);
		player.addSeekedListener(this);
		player.addPlayListener(this);
	}
	
	private void showVideo() {
		MediaComponent newplayer = new MediaComponent(MediaComponent.Type.VIDEO);
		playerLayout.replaceComponent(player, newplayer);
		player = newplayer;
		player.addVolumeChangeListener(this);
		player.addCanPlayListener(this);
		player.addLoadedDataListener(this);
		player.addLoadedMetadataListener(this);
		player.addPauseListener(this);
		player.addPlaybackEndedListener(this);
		player.addPlayingListener(this);
		player.addSeekedListener(this);
		player.addPlayListener(this);
	}

	@Override
	public void volumeChanged(MediaComponent component) {
		Notification.show("Volume Changed!", Notification.Type.TRAY_NOTIFICATION);
		volume.setValue(String.valueOf(component.getVolume()));
	}

	@Override
	public void seeked(MediaComponent component) {
		Notification.show("Seeked!", Notification.Type.TRAY_NOTIFICATION);
		currentTime.setValue(String.valueOf(component.getCurrentTime()));
		duration.setValue(String.valueOf(component.getDuration()));
	}
	
	@Override
	public void played(MediaComponent component) {
		Notification.show("Played!", Notification.Type.TRAY_NOTIFICATION);
		currentTime.setValue(String.valueOf(component.getCurrentTime()));
		duration.setValue(String.valueOf(component.getDuration()));
	}

	@Override
	public void paused(MediaComponent component) {
		Notification.show("Paused!", Notification.Type.TRAY_NOTIFICATION);
		currentTime.setValue(String.valueOf(component.getCurrentTime()));
		duration.setValue(String.valueOf(component.getDuration()));
	}

	@Override
	public void metadataLoaded(MediaComponent component) {
		Notification.show("Metadata Loaded!", Notification.Type.TRAY_NOTIFICATION);
		currentTime.setValue(String.valueOf(component.getCurrentTime()));
		duration.setValue(String.valueOf(component.getDuration()));
	}

	@Override
	public void dataLoaded(MediaComponent component) {
		Notification.show("Data Loaded!", Notification.Type.TRAY_NOTIFICATION);
		currentTime.setValue(String.valueOf(component.getCurrentTime()));
		duration.setValue(String.valueOf(component.getDuration()));
	}

	@Override
	public void canPlay(MediaComponent component) {
		Notification.show("Can Play!", Notification.Type.TRAY_NOTIFICATION);
		currentTime.setValue(String.valueOf(component.getCurrentTime()));
		duration.setValue(String.valueOf(component.getDuration()));
	}

	@Override
	public void playing(MediaComponent component) {
		Notification.show("Playing!", Notification.Type.TRAY_NOTIFICATION);
		currentTime.setValue(String.valueOf(component.getCurrentTime()));
		duration.setValue(String.valueOf(component.getDuration()));
	}

	@Override
	public void playbackEnded(MediaComponent component) {
		Notification.show("Playback Ended!", Notification.Type.TRAY_NOTIFICATION);
		currentTime.setValue(String.valueOf(component.getCurrentTime()));
		duration.setValue(String.valueOf(component.getDuration()));
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
			inputs.addComponent(currentTime);
			inputs.addComponent(duration);
			inputs.addComponent(volume);
			inputs.addComponent(currentTimeValue);
			inputs.addComponent(volumeValue);
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
					if (SONG_ACDC.equals(key) || SONG_BONOBO.equals(key)) {
						showAudio();
					} else {
						showVideo();
					}
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
					player.setVolume(Integer.valueOf(volumeValue.getValue()));
				}
			});
			setCurrentTime = new Button("Set Current Time", new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					player.setCurrentTime(Integer.valueOf(currentTimeValue.getValue()));
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
			currentTime = new TextField("Current Time (seconds)");
			currentTime.setEnabled(false);
			duration = new TextField("Duration (seconds)");
			duration.setEnabled(false);
			volume = new TextField("Current Volume (1-10)");
			volume.setEnabled(false);
		}
		
		private void initControlInputs() {
			currentTimeValue = new TextField("Set Current Time (seconds):");
			currentTimeValue.setValue("0");
			volumeValue = new TextField("Set Volume (1-10):");
			volumeValue.setValue("10");
		}
	}
}
