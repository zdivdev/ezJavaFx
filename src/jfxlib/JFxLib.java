package com.zdiv.jfxlib;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

public class JFxLib {

	Stage mainStage;
	FxMenuBar menubar;
	FxToolBar toolbar;
	FxStatusBar statusbar;
	FxNode mainPane;

	//////////////////////////////////////////////////////////////////////
	// Main Entry
	////////////////////////////////////////////////////////////////////// 

	public JFxLib(Stage stage) {
		this.mainStage = stage;
	}

	public Stage popupWindow(int w, int h) {
		Stage stage = new Stage();		

		menubar = new FxMenuBar();	
		toolbar = new FxToolBar();
		statusbar = new FxStatusBar();
		FxVBox topbar = new FxVBox(0);
		
		FxBorderPane pane = new FxBorderPane();
		pane.setTop(topbar);
		pane.setBottom(statusbar.get());

		stage.setScene(new Scene(pane.get(), w, h));
		return stage;
	}
	
	public Scene getScene() {
		return mainStage.getScene();
	}

	public interface FxNode {
		public Node get();
	}

	public interface FxRunnable {
		public void run(Object... obj);
	}
	
	void makeMenubar() {
		menubar = new FxMenuBar();
	}
	
	void makeToolbar() {
		toolbar = new FxToolBar();
	}
	
	void makeStatusbar() {
		statusbar = new FxStatusBar();
	}
	
	public void makeBorderPane(int w, int h) {
		FxVBox topbar;
		FxBorderPane pane = new FxBorderPane();
		pane = new FxBorderPane();
		if( menubar != null || toolbar != null ) {
			topbar = new FxVBox(0,0,0,0,0);
			if( menubar != null ) {
				topbar.add(menubar.get());
			}
			if( toolbar != null ) {
				topbar.add(toolbar.get());
			}
			pane.setTop(topbar);
		}		
		if( statusbar != null ) {
			pane.setBottom(statusbar.get());
		}
		mainStage.setScene(new Scene(pane.get(), w, h));
		mainPane = pane;
	}
	
	public void setTop(FxNode node) {
		if( mainPane instanceof FxBorderPane ) {
			FxBorderPane pane = (FxBorderPane) mainPane;
			pane.setTop(node);
		}
	}
	
	public void setLeft(FxNode node) {
		if( mainPane instanceof FxBorderPane ) {
			FxBorderPane pane = (FxBorderPane) mainPane;
			pane.setLeft(node);
		}
	}
	
	public void setCenter(FxNode node) {
		if( mainPane instanceof FxBorderPane ) {
			FxBorderPane pane = (FxBorderPane) mainPane;
			pane.setCenter(node);
		}
	}
	
	public void setRight(FxNode node) {
		if( mainPane instanceof FxBorderPane ) {
			FxBorderPane pane = (FxBorderPane) mainPane;
			pane.setRight(node);
		}
	}
	
	public void setBottom(FxNode node) {
		if( mainPane instanceof FxBorderPane ) {
			FxBorderPane pane = (FxBorderPane) mainPane;
			pane.setBottom(node);
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	// 1. Container
	//////////////////////////////////////////////////////////////////////

	public static class FxBorderPane implements FxNode {

		BorderPane pane;
		
		public FxBorderPane() {
			pane = new BorderPane();
			pane.setPadding(new Insets(0, 0, 0, 0));	
		}
		
		@Override
		public BorderPane get() {
			return pane;
		}
		
		public void setTop(Node item) {
			pane.setTop(item);
		}

		public void setBottom(Node item) {
			pane.setBottom(item);
		}
		
		public void setLeft(Node item) {
			pane.setLeft(item);
		}

		public void setRight(Node item) {
			pane.setRight(item);
		}
		
		public void setCenter(Node item) {
			pane.setCenter(item);
		}	
		
		public void setTop(FxNode item) {
			pane.setTop(item.get());
		}

		public void setBottom(FxNode item) {
			pane.setBottom(item.get());
		}
		
		public void setLeft(FxNode item) {
			pane.setLeft(item.get());
		}

		public void setRight(FxNode item) {
			pane.setRight(item.get());
		}
		
		public void setCenter(FxNode item) {
			pane.setCenter(item.get());
		}	
	}
	
	public static class FxSplitPane implements FxNode {
	
		SplitPane pane;
		
		public FxSplitPane() {
			pane = new SplitPane();
			pane.setOrientation(Orientation.HORIZONTAL);
			pane.setDividerPositions(0.5, 0.5);
		}
		
		@Override
		public SplitPane get() {
			return pane;
		}
		
		public void setVertical() {
			pane.setOrientation(Orientation.VERTICAL);
		}
		
		public void add(Node item) {
			pane.getItems().add(item);
		}
		
		public void add(FxNode item) {
			pane.getItems().add(item.get());
		}
	}

	public class FxTabPane implements FxNode {

		TabPane pane;
		
		public FxTabPane() {
			pane = new TabPane();
		}
		
		@Override
		public TabPane get() {
			return pane;
		}
		
		public void add(String title, Node item) {
			Tab tab = new Tab();
			tab.setText(title);
			tab.setContent(item);
			pane.getTabs().add(tab);
		}
		
		public void add(String title, FxNode item) {
			add(title, item.get());
		}
	}
	
	
	public class FxTitledPane implements FxNode {
	
		TitledPane pane;
			    
		public FxTitledPane() {
			pane = new TitledPane();
		}
		
		public FxTitledPane(String title, Node item, boolean scrollable) {
			this();
			add( title, item, scrollable );
		}
		
		@Override
		public TitledPane get() {
			return pane;
		}
	
		public void add(String title, Node item, boolean scrollable) {
			pane.setText(title);
			pane.setContent(item);
			pane.setExpanded(true);
			pane.setCollapsible(scrollable);
		}	
	}
	
	public class FxTitlePanes implements FxNode {
	
		Accordion pane;

		public FxTitlePanes() {
			pane = new Accordion();
		}
		
		@Override
		public Accordion get() {
			return pane;
		}
		
		public void add(String title, Node item, boolean scrollable) {
			TitledPane titledPane = new TitledPane();
			titledPane.setText(title);
			titledPane.setContent(item);
			titledPane.setExpanded(true);
			titledPane.setCollapsible(scrollable);
			pane.getPanes().add(titledPane);
		}
	
		public void add(String title, Node item) {
			add( title, item, true );
		}	
	}
	
	public class FxFlowPane implements FxNode {
	
		FlowPane pane;
			    
		public FxFlowPane() {
			pane = new FlowPane();
			pane.setPadding(new Insets(0, 0, 0, 0));
			pane.setVgap(8);
			pane.setHgap(4);
		}
		
		@Override
		public FlowPane get() {
			return pane;
		}
	
		public void add(Node item) {
			pane.getChildren().add(item);
		}
		
		public void add(FxNode item) {
			add(item.get());
		}	
		
		public void vertical() {
			pane.setOrientation(Orientation.VERTICAL);
		}
	}
		
	public class FxTilePane implements FxNode {
	
		TilePane pane;
		
		public FxTilePane() {
			pane = new TilePane();
			pane.setPadding(new Insets(0, 0, 0, 0));
			pane.setVgap(8);
			pane.setHgap(4);
		}
		
		@Override
		public TilePane get() {
			return pane;
		}
	
		public void add(Node item) {
			pane.getChildren().add(item);
		}
		
		public void add(FxNode item) {
			add(item.get());
		}	
		
		public void vertical() {
			pane.setOrientation(Orientation.VERTICAL);
		}
	}
	
	public static class FxVBox implements FxNode {
		
		VBox box;
		int gap = 0;
		Insets padding = new Insets(0, 0, 0, 0);  //T,R,B,L

		public FxVBox() {
			this.init();
		}
		public FxVBox(int gap) { //gap, padding
			this.gap = gap;
			this.init();
		}
		public FxVBox(int gap, int t, int r, int b, int l) { //gap, padding
			this.gap = gap;
			this.padding = new Insets( t, r, b, l );
			this.init();
		}

		void init() {
			this.box = new VBox(gap);
			this.box.setAlignment(Pos.CENTER);
			this.box.setSpacing(gap);
			this.box.setPadding(padding);
		}
		
		@Override
		public VBox get() {
			return this.box;
		}
		
		public Node getItem(int index) {
			return this.box.getChildren().get(index);
		}
		
		public int size() {
			return this.box.getChildren().size();
		}
		
		public void add(Node item) {
			this.box.getChildren().add(item);
		}
		
		public void add(FxNode item) {
			this.box.getChildren().add(item.get());
		}
		
		public void addSeparator() {
			Separator sep = new Separator();
			sep.setOrientation(Orientation.HORIZONTAL);
			this.box.getChildren().add(sep);
		}
	}
	
	public static class FxHBox implements FxNode {
		
		HBox box;
		int gap = 0;
		Insets padding = new Insets(0, 0, 0, 0);  //T,R,B,L
		
		public FxHBox() {
			this.init();
		}	
		public FxHBox(int gap) {
			this.gap = gap;
			this.init();
		}
		public FxHBox(int gap, int t, int r, int b, int l) { //gap, padding
			this.gap = gap;
			this.padding = new Insets( t, r, b, l );
			this.init();
		}
		
		void init() {
			this.box = new HBox(gap);
			this.box.setAlignment(Pos.CENTER);
			this.box.setSpacing(gap);
			this.box.setPadding(padding);
		}

		@Override
		public HBox get() {
			return this.box;
		}
		
		public int size() {
			return this.box.getChildren().size();
		}
		
		public void add(FxNode item) {
			this.box.getChildren().add(item.get());
		}
	
		public void add(Node item) {
			this.box.getChildren().add(item);
		}
	
		public void addSeparator() {
			Separator sep = new Separator();
			sep.setOrientation(Orientation.VERTICAL);
			this.box.getChildren().add(sep);
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	// 2. Menu, Toolbar, Statusbar
	//////////////////////////////////////////////////////////////////////	
	public static class FxMenuBar {
		
		MenuBar menuBar;
		Menu currMenu;
		
		public FxMenuBar() {
			menuBar = new MenuBar();
		}
	
		public MenuBar get() {
			return menuBar;
		}
		
		public void addMenu(String title) {
			currMenu = new Menu(title);
			menuBar.getMenus().add(currMenu);
		}
	
		public MenuItem addMenuItem(String title, String icon, Runnable runnable) {
			if( title == null ) {
				SeparatorMenuItem item = new SeparatorMenuItem();
				currMenu.getItems().add(item);
				return item;
			} else {
				MenuItem item = new MenuItem( title);
				if( icon != null ) {
					item.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(icon))));
				}			
		        item.setOnAction(new EventHandler<ActionEvent>() {
		            public void handle(ActionEvent t) {
		            	runnable.run();
		            }
		        });
		        currMenu.getItems().add(item);
				return item;
			}
		}		
	}
	
	public static class FxToolBar {
		FxVBox vbox;
		List<ToolBar> toolBar;
		ToolBar currToolBar;
		
		public FxToolBar() {
			vbox = new FxVBox();
			toolBar = new ArrayList<>();
		}
		
		public int size() {
			return toolBar.size();
		}
	
		public Node get() {
			return vbox.get();
		}
		
		public ToolBar getCurrToolbar() {
			return currToolBar;
		}

		public ToolBar get(int i) {
			return toolBar.get(i);
		}
		
		public void addToolBar() {
			currToolBar = new ToolBar();
			toolBar.add(currToolBar);
			VBox.setVgrow(currToolBar, Priority.NEVER);
			//HBox.setHgrow(currToolBar, Priority.ALWAYS);
			vbox.add(currToolBar);			
		}
		
		public void addToolBarItem(Node item, boolean extend) {
			if( item == null ) {
				currToolBar.getItems().add(new Separator());
			} else {
				VBox.setVgrow(currToolBar, Priority.NEVER);
				if( extend ) {
					HBox.setHgrow(item, Priority.ALWAYS);	
				}
				currToolBar.getItems().add(item);
			}
		}	
		
		public void addSeparator() {
			currToolBar.getItems().add(new Separator());
		}	
			
		public void addToolBarItem(Node item) {
			addToolBarItem(item, false);
		}	
		
		public void addToolBarItem(FxNode item, boolean extend) {
			addToolBarItem(item.get(), extend);
		}
		
		public void addToolBarItem(FxNode item) {
			addToolBarItem(item.get(), false);
		}	
		
		public void addToolBarItem(int index, Node item, boolean extend) {
			if( item == null ) {
				currToolBar.getItems().add(new Separator());
			} else {
				if( extend ) {
					HBox.setHgrow(item, Priority.ALWAYS);
				}	
		        toolBar.get(index).getItems().add(item);
			}
		}
		
		public void addToolBarItem(int index, Node item) {
			addToolBarItem(index, item, false);
		}	
	
		public void add(Node item) {
			addToolBarItem(item, false);
		}	
		
		public void add(FxNode item) {
			addToolBarItem(item.get(), false);
		}	

	}
	
	public static class FxStatusBar {
	
		HBox hbox;
		Label defaultLabel;
		
		public FxStatusBar() {
			hbox = new HBox(16);
			//hbox.setAlignment(Pos.CENTER_LEFT);
			hbox.setPadding(new Insets(4, 4, 4, 4));
			//HBox.setHgrow(hbox, Priority.ALWAYS);
		}
		
		public HBox get() {
			return hbox;
		}
	
		public void addStatusBarItem(Node item, boolean extend) {
			if( extend ) {
				HBox.setHgrow(item, Priority.ALWAYS);
			}		
			hbox.getChildren().add(item);
		}

		public void addStatusBarItem(FxNode item, boolean extend) {
			addStatusBarItem(item.get(), extend);
		}

		public void setDefault() {
			defaultLabel = new Label();
			addStatusBarItem( defaultLabel, true);
		}
		
		public void setText(String text) {
			if( defaultLabel != null ) {
				defaultLabel.setText(text);
			}
		}
		
		public void setFontSize( int size ) {
			defaultLabel.setStyle("-fx-font: " + size + " arial;");
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	// Controls
	//////////////////////////////////////////////////////////////////////
	

	public static class FxClipboard {
	
		public static void putString(String s) {
	        final Clipboard clipboard = Clipboard.getSystemClipboard();
	        final ClipboardContent content = new ClipboardContent();
	        content.putString(s);
	        clipboard.setContent(content);
		}
		
		public static void putHtml(String s) {
	        final Clipboard clipboard = Clipboard.getSystemClipboard();
	        final ClipboardContent content = new ClipboardContent();
	        content.putString(s);
	        content.putHtml(s);
	        clipboard.setContent(content);
		}
	
		public static void putImage(Image image) {
	        final Clipboard clipboard = Clipboard.getSystemClipboard();
	        final ClipboardContent content = new ClipboardContent();
	        content.putImage(image);
	        clipboard.setContent(content);
		}
	
		public static void putFiles(List<File> files) {
	        final Clipboard clipboard = Clipboard.getSystemClipboard();
	        final ClipboardContent content = new ClipboardContent();
	        content.putFiles(files);
	        clipboard.setContent(content);
		}
		
		public static String getString() {
	        return Clipboard.getSystemClipboard().getString();
		}
		
		public static String getHtml() {
	        return Clipboard.getSystemClipboard().getHtml();
		}
	
		public static Image getImage() {
	        return Clipboard.getSystemClipboard().getImage();
		}
	
		public static List<File> getFiles() {
	        return Clipboard.getSystemClipboard().getFiles();
		}
	
		public static String getUrl() {
	        return Clipboard.getSystemClipboard().getUrl();
		}
	}

	public static class FxContextMenu {
	
		ContextMenu menu;
		
		public FxContextMenu() {
			menu = new ContextMenu();
		}
		
		public void addMenuItem(String title, Runnable runnable) {
			MenuItem item = new MenuItem();
			item.setText(title);
			item.setOnAction(e -> runnable.run());
			menu.getItems().add(item);
		}
	
		public void addSeparator() {
			menu.getItems().add(new SeparatorMenuItem());
		}
		
		public ContextMenu get() {
			return menu;
		}
	}
	
	public static class FxDrop {
	
		public static void setDropHandler(Node node, FxRunnable runnable) {
			node.setOnDragOver(new EventHandler<DragEvent>() {
	            @Override public void handle(DragEvent event) {
	                if( event.getDragboard().hasFiles() ) {
	                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
	                }
	                event.consume();
	            }
	        });
			
			node.setOnDragDropped(new EventHandler<DragEvent>() {
	            @Override public void handle(DragEvent event) {
	                Dragboard db = event.getDragboard();
	                boolean success = false;
	                if( db.hasFiles() ) {
	                    success = true;
	                    List<File> files = db.getFiles();
	                    runnable.run(files);
	                }
	                event.setDropCompleted(success);
	                event.consume();
	            }
	        });
		}
	}

	public static class FxLabel implements FxNode {
		
		Label ctrl;
		
		public FxLabel(String text, int fontSize) {
			ctrl = new Label(text);
			setFontSize(fontSize);
		}
		
		public FxLabel(String text) {
			ctrl = new Label(text);
		}
		
		@Override
		public Node get() {
			return ctrl;
		}
		public void setFontSize(int size) {
			ctrl.setStyle("-fx-font: " + size + " arial;");
		}
	}
	
	public static class FxButton implements FxNode {
	
		ButtonBase ctrl;
		int fontSize = 12;
		
		public FxButton(String text, String icon, String tooltip, boolean vert, Runnable runnable) {
			ctrl = new Button();
			if( text != null ) {
				setText(text);
			}
			if( icon != null ) {
				setIcon(icon);
			}
			if( tooltip != null ) {
				setTooltip(tooltip);
			}
			if( runnable != null ) {
				setHandler(runnable);
			}
			if( vert ) {
				setVertical();
			}
		}
		
		public FxButton() {
			this( null, null, null, false, null);
		}
		
		public FxButton(String text) {
			this( text, null, null, false, null );
		}
		
		public FxButton(String text, Runnable runnable) {
			this( text, null, null, false, runnable );
		}
	
		public FxButton(String text, String icon, Runnable runnable) {
			this( text, icon, null, false, runnable );
		}

		public FxButton(String text, String icon, String tooltip, Runnable runnable) {
			this( text, icon, tooltip, false, runnable );
		}

		@Override
		public Node get() {
			return ctrl;
		}
	
		public void setBackground() {
			ctrl.setStyle("-fx-background-color: #D8BFD8;");
		}
	
		public void setFontSize(int size) {
			fontSize = size;
			ctrl.setStyle("-fx-font: " + fontSize + " arial;");
		}
		
		public void setText(String text) {
			ctrl.setText(text);
		}
	
		public void setIcon(String icon) {
			ctrl.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(icon))));
		}
		
		public void setTooltip(String tooltip) {
			ctrl.setTooltip(new Tooltip(tooltip));
		}
		
		public void setHandler(Runnable runnable) {
			ctrl.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {
					runnable.run();
				}
			}); 
		}
		
		public void setVertical() {
			ctrl.setContentDisplay(ContentDisplay.TOP);
		}
	}
	

	public static class FxToggleButton implements FxNode {
	
		ToggleButton ctrl;
		int fontSize = 12;
		
		public FxToggleButton(String text, String icon, String tooltip, boolean vert, Runnable runnable) {
			ctrl = new ToggleButton();
			if( text != null ) {
				setText(text);
			}
			if( icon != null ) {
				setIcon(icon);
			}
			if( tooltip != null ) {
				setTooltip(tooltip);
			}
			if( runnable != null ) {
				setHandler(runnable);
			}
			if( vert ) {
				setVertical();
			}
		}
		
		public FxToggleButton() {
			this( null, null, null, false, null);
		}
		
		public FxToggleButton(String text, Runnable runnable) {
			this( text, null, null, false, runnable );
		}
	
		public FxToggleButton(String text, String icon, Runnable runnable) {
			this( text, icon, null, false, runnable );
		}

		public FxToggleButton(String text, String icon, String tooltip, Runnable runnable) {
			this( text, icon, tooltip, false, runnable );
		}

		@Override
		public Node get() {
			return ctrl;
		}
	
		public boolean getState() {
			return ctrl.isSelected();
		}
		public void setBackground() {
			ctrl.setStyle("-fx-background-color: #D8BFD8;");
		}
	
		public void setFontSize(int size) {
			fontSize = size;
			ctrl.setStyle("-fx-font: " + fontSize + " arial;");
		}
		
		public void setText(String text) {
			ctrl.setText(text);
		}
	
		public void setIcon(String icon) {
			ctrl.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(icon))));
		}
		
		public void setTooltip(String tooltip) {
			ctrl.setTooltip(new Tooltip(tooltip));
		}
		
		public void setHandler(Runnable runnable) {
			ctrl.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {
					runnable.run();
				}
			}); 
		}
		
		public void setVertical() {
			ctrl.setContentDisplay(ContentDisplay.TOP);
		}
	}
	
	public static class FxChoiceBox implements FxNode {
	
		ChoiceBox<String> ctrl;
		int fontSize = 12;

		public FxChoiceBox(String[] items, Runnable runnable) {
			ctrl = new ChoiceBox<>();
			ctrl.setStyle("-fx-font: " + fontSize + " arial;");
			if( items != null ) {
				ctrl.getItems().addAll(items);
				selectFirst();
			}
			if( runnable != null ) {
				setHandler(runnable);
			}
		}

		@Override
		public ChoiceBox<String> get() {
			return ctrl;
		}
	
		public void setBackground() {
			ctrl.setStyle("-fx-background-color: #D8BFD8;");
		}
	
		public void setFontSize(int size) {
			fontSize = size;
			ctrl.setStyle("-fx-font-size: " + fontSize + ";");
		}
		
		public void add(String text) {
			ctrl.getItems().add(text);
		}
	
		public void selectFirst() {
			ctrl.getSelectionModel().selectFirst();
		}
		
		public String getSelectedItem() {
			return ctrl.getSelectionModel().getSelectedItem();
		}
		
		public int getSelectedIndex() {
			return ctrl.getSelectionModel().getSelectedIndex();
		}
		
		public void setHandler(Runnable runnable) {
			ctrl.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					runnable.run(); 
				}
	        });
		}
	}
	

	public static class FxComboBox implements FxNode {
	
		ComboBox<String> ctrl;
		int fontSize = 12;

		public FxComboBox(String[] items, Runnable runnable) {
			ctrl = new ComboBox<>();
			ctrl.setStyle("-fx-font: " + fontSize + " arial;");
			if( items != null ) {
				ctrl.getItems().addAll(items);
				selectFirst();
			}
			if( runnable != null ) {
				setHandler(runnable);
			}
		}

		public FxComboBox(Runnable runnable) {
			this(null, runnable);
		}
		
		public FxComboBox() {
			this(null, null);
		}
		
		@Override
		public ComboBox<String> get() {
			return ctrl;
		}
	
		public void setBackground() {
			ctrl.setStyle("-fx-background-color: #D8BFD8;");
		}
	
		public void setFontSize(int size) {
			fontSize = size;
			ctrl.setStyle("-fx-font-size: " + fontSize + ";");
		}
		
		public void add(String text) {
			ctrl.getItems().add(text);
		}
	
		public void selectFirst() {
			ctrl.getSelectionModel().selectFirst();
		}
		
		public String getSelectedItem() {
			return ctrl.getSelectionModel().getSelectedItem();
		}
		
		public int getSelectedIndex() {
			return ctrl.getSelectionModel().getSelectedIndex();
		}
		
		public void setHandler(Runnable runnable) {
			ctrl.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					runnable.run(); 
				}
	        });
		}
	}
	
	public static class FxImageView implements FxNode {
	
		ImageView imageView;
		String imageFile;
	
		public FxImageView() {
			imageView = new ImageView();
		}
	
		public FxImageView(String image) {
			imageFile = new File(image).toURI().toString();
			imageView = new ImageView(new Image(imageFile));
		}
	
		public FxImageView(int width, int height) {
			imageView = new ImageView();
			imageView.setPreserveRatio(true);
			imageView.setFitWidth(width);
			imageView.setFitHeight(height);
		}
	
		public FxImageView(String imageFile, int width, int height) {
			imageView = new ImageView(new Image(new File(imageFile).toURI().toString(), width, height, true, true));
			imageView.setPreserveRatio(true);
			//imageView.setFitWidth(width);
			//imageView.setFitHeight(height);
		}
	
		@Override public Node get() {
			return imageView;
		}
	
		public void load(String imageFile) {
			imageView.setImage(new Image(new File(imageFile).toURI().toString()));
		}
	}
	
	
	public class FxScrollImageView implements FxNode {
	
		ScrollPane scroll;
		FxHBox box;
		
		public FxScrollImageView() {
			box = new FxHBox(4);
			//box.get().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
	
			scroll = new ScrollPane();
			//scroll.setHmin(32);
			//scroll.setHmax(32);
			scroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
			scroll.setHbarPolicy(ScrollBarPolicy.ALWAYS);
			scroll.setPannable(true);
			scroll.setFitToHeight(true);
			scroll.setContent(box.get());
		}
	
		@Override public Node get() {
			return scroll;
		}
		
		public void add(String imageFile) {
			FxImageView iv = new FxImageView(imageFile, 64, 64);
			box.add(iv);
			//scroll.setContent(box.get());
		}
	}
	
	public static class FxListView implements FxNode {
	
		ListView<String> ctrl;
		int fontSize = 11;

		public FxListView(String[] items, Runnable runnable) {
			ctrl = new ListView<>();
			ctrl.setStyle("-fx-font: " + fontSize + " arial;");

			if( items != null ) {
				ctrl.getItems().addAll(items);
				selectFirst();
			}
			if( runnable != null ) {
				setHandler(runnable);
			}
		}
	    
		public FxListView() {
			this(null,null);
		}

		public FxListView(String[] items) {
			this(items,null);
		}
		
		public FxListView(Runnable runnable) {
			this(null,runnable);
		}
		
		@Override
		public Node get() {
			return ctrl;
		}
		
		public void setHandler(Runnable runnable) {
			ctrl.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					runnable.run(); 
				}
	        });
		}
		
		public void clear() {
			ctrl.getItems().clear();
		}
		
		public void add(String data) {
			ctrl.getItems().add(data);
		}
		
		public String get(int index) {
			return ctrl.getItems().get(index);
		}
		
		public String getText() {
			return String.join("\n",  ctrl.getItems());
		}
		
		public void fontDown() {
			if( fontSize > 1 ) {
				fontSize--;
				ctrl.setStyle("-fx-font-size: " + fontSize + ";");
			}
		}
	
		public void fontUp() {
			if( fontSize < 127 ) {
				fontSize++;
				ctrl.setStyle("-fx-font-size: " + fontSize + ";");
			}		
		}

      public void selectFirst() {
    	  ctrl.getSelectionModel().selectFirst();
		}
		
		public String getSelectedItem() {
			return ctrl.getSelectionModel().getSelectedItem();
		}
		
		public int getSelectedIndex() {
			return ctrl.getSelectionModel().getSelectedIndex();
		}
		
		public String getUserData() {
			return (String)ctrl.getUserData(); 
		}
	}
	
	public static class FxFileListView implements FxNode {
		
		/*
		FxVBox vbox;
		FxToolBar hbox;
		*/
		ListView<String> ctrl;
		
	    private static class FileListCell extends ListCell<String> {
	        @Override public void updateItem(String item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty) {
	                setGraphic(null);
	                setText(null);
	            } else {
	            	File file = new File(item);
	            	Image fxImage;
	            	if( file.exists() && file.isDirectory() ) {
	            		fxImage = new Image(getClass().getResourceAsStream("/res/folder.png"));
	            	} else {
	            		fxImage = new Image(getClass().getResourceAsStream("/res/file.png"));
	            	}
	                setGraphic(new ImageView(fxImage));
	                setText(file.getName());
	                setTooltip(new Tooltip(item));
	                setUserData(item);
	            }
	            
	        }
	    }
	    
		public FxFileListView(String folder, Runnable runnable) {
			ctrl = new ListView<>();
			ctrl.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
	            @Override public ListCell<String> call(ListView<String> list) {
	                return new FileListCell();
	            }
	        });
			if( folder != null ) {
				File dir = new File(folder);
				if( dir.exists() && dir.isDirectory() ) {
					loadDirectory(dir);
				}
			}
			if( runnable != null ) {
				setHandler(runnable);
			}
			FxDrop.setDropHandler(ctrl, new FxRunnable() {
				@SuppressWarnings("unchecked")
				@Override public void run(Object... object) {
					List<File> list = (List<File>) object[0];
					for( File file : list ) {
						ctrl.getItems().add(file.getPath());
					}
				}
			});
		}
		
		public FxFileListView() {
			this(null,null);
		}
		
		/*
		public FxFileListView(boolean toolbar) {
			this();
			if( toolbar ) {
				hbox = new FxToolBar();
				hbox.addToolBar();
				hbox.add(new FxButton("Clear", () -> clear() ));
				hbox.add(new FxButton("Copy", () -> copy() ));
				VBox.setVgrow(hbox.get(), Priority.NEVER);
				VBox.setVgrow(ctrl, Priority.ALWAYS);
				vbox = new FxVBox();
				vbox.add(hbox.get());
				vbox.add(ctrl);
			}
		}
		*/
		
		public ListView<String> getListView() {
			return ctrl;
		}

		@Override
		public Node get() {
			//return vbox.get();
			return ctrl;
		}
		
		public void setHandler(Runnable runnable) {
			ctrl.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					File file = new File(getSelectedItem());
					if( file.isDirectory() ) {
						loadDirectory(file.getAbsoluteFile());
					} else {
						runnable.run(); 
					}
				}
	        });
		}
		
		public void loadDirectory(File dir) {
			if( dir.exists() && dir.isDirectory() ) {
				ctrl.getSelectionModel().clearSelection();
				ctrl.getItems().clear();
				if( dir.getParentFile().exists() ) {
					ctrl.getItems().add( dir.getPath() + "/.." );
				}
				Collections.sort(Arrays.asList(dir.listFiles()));
				for( File file : dir.listFiles() ) {
					if( file.isDirectory() ) {
						ctrl.getItems().add(file.getPath());
					}
				}
				for( File file : dir.listFiles() ) {
					if( ! file.isDirectory() ) {
						ctrl.getItems().add(file.getPath());
					}
				}
				ctrl.scrollTo(0);
			}
		}
		
		public void loadDirectory(String folder) {
			loadDirectory(folder);
		}
		
		public String getSelectedItem() {
			return ctrl.getSelectionModel().getSelectedItem();
		}
		
		public int getSelectedIndex() {
			return ctrl.getSelectionModel().getSelectedIndex();
		}
		
		public String getUserData() {
			return (String)ctrl.getUserData(); 
		}
		
		public void clear() {
			ctrl.getItems().clear();
		}
		
		public void copy() {
			FxClipboard.putString(String.join("\n", ctrl.getItems()));
		}
				
		public void add(String text) {
			ctrl.getItems().add(text);
		}
		
		public void add(String text, String icon) {
			ctrl.getItems().add(text);
		}

	}
	

	public static class FxFileTreeView implements FxNode {

		TreeView<String> ctrl;
		int fontSize = 12;

		public FxFileTreeView(String folder, Runnable runnable) {
			ctrl = new TreeView<>();
			ctrl.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
	            @Override
	            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
	            	runnable.run();
	            }
	        }); 
	    	Image icon = new Image(getClass().getResourceAsStream("/res/16/folder.png"));
	    	TreeItem<String> root = new TreeItem<> (new File(folder).getName(), new ImageView(icon));
	    	addToTreeView(root, folder, fontSize, runnable);
	    	ctrl.setRoot(root);
	    }
		
		@Override
		public Node get() {
			// TODO Auto-generated method stub
			return null;
		}
		
		public void setFontSize( int size ) {
			ctrl.setStyle("-fx-font-size: " + size + " pt;");
		}

	    public TreeItem<String> getTreeRootItem() {
	    	return ctrl.getRoot();
	    }
	    
	    public void setShowRoot(boolean show) {
	    	ctrl.setShowRoot(show);
	    }
	    
	    public String getSelectedTreePath() {
	    	TreeItem<String> item = ctrl.getSelectionModel().getSelectedItem();
	    	StringBuilder sb = new StringBuilder();
	    	sb.insert( 0, item.getValue() );
	    	item = item.getParent();
	    	while( item != null ) {
	    		sb.insert(0,  "/");
	    		sb.insert(0, item.getValue());
	    		item = item.getParent();
	    	}
	    	return sb.toString();
	    }
	    
	    private void addToTreeView(TreeItem<String> node, String folder, int fontSize, Runnable runnable) {
	    	File dir = new File(folder);
	    	File[] files = dir.listFiles();
	    	for( File file : files ) {
	    		if( file.isDirectory() ) {
	    			Image icon = new Image(getClass().getResourceAsStream("/res/16/folder.png"));
	    			TreeItem<String> item = new TreeItem<> ( file.getName(), new ImageView(icon));
	    			//item.setExpanded(true);
	    			node.getChildren().add(item);
	    			addToTreeView( item, file.getPath(), fontSize, runnable );
	    		} else {
	    			Image icon = new Image(getClass().getResourceAsStream("/res/16/file.png"));
	    			TreeItem<String> item = new TreeItem<> ( file.getName(), new ImageView(icon));
	    			node.getChildren().add(item);
	    		}
	    	}
	    }
	}
	
	public static class FxProgressBar implements FxNode {

		ProgressBar ctrl;
		
		public FxProgressBar() {
			ctrl = new ProgressBar();
		}
		
		public FxProgressBar(double value) {
			ctrl = new ProgressBar();
			ctrl.setProgress(value);
		}
		
		@Override
		public Node get() {
			return ctrl;
		}
	
		public double getValue() {
			return ctrl.getProgress();
		}
		
		public void setValue(double value) {
			ctrl.setProgress(value);
		}
	}

	public static class FxTextArea implements FxNode {
	
		FxVBox vbox;
		FxToolBar hbox;
		TextArea text;
		int fontSize = 11;
		
		public FxTextArea() {
			text = new TextArea();
			text.setStyle("-fx-font: " + fontSize + " arial;");
		}
	
		public FxTextArea(boolean toolbar) {
			this();
			if( toolbar ) {
				hbox = new FxToolBar();
				hbox.addToolBar();
				//hbox.get().setAlignment(Pos.CENTER_LEFT);
				hbox.add(new FxButton("Clear", () -> clear() ));
				hbox.add(new FxButton("Copy", () -> copyAll() ));
				hbox.add(new FxButton("Paste", () -> pasteAll() ));
				hbox.add(new FxButton("Paste Html", () -> pasteHtml() ));
				hbox.addSeparator();
				hbox.add(new FxButton("Wrap", () -> wrapToggle() ));
				hbox.add(new FxButton("Font(-)", () -> fontDown() ));
				hbox.add(new FxButton("Font(+)", () -> fontUp() ));
				VBox.setVgrow(hbox.get(), Priority.NEVER);
				VBox.setVgrow(text, Priority.ALWAYS);
				vbox = new FxVBox();
				vbox.add(hbox.get());
				vbox.add(text);
			}
		}
	
		@Override
		public Node get() {
			if( vbox != null ) {
				return vbox.get();
			} else {
				return text;
			}
		}
		
		public String getText() {
			return text.getText();
		}
		
		public void setText(String data) {
			text.setText(data);
		}
		
		public void insert(int index, String data) {
			text.insertText(index, data);
		}
		
		public void append(String data) {
			text.appendText(data);
		}
		
		public void clear() {
			text.clear();
		}
		
		public void copy() {
			text.copy();
		}
		
		public void paste() {
			text.paste();
		}
		
		public void pasteHtml() {
			if( Clipboard.getSystemClipboard().hasHtml() ) {
				text.setText( Clipboard.getSystemClipboard().getHtml() );
			}
		}
		
		public void copyAll() {
			text.selectAll();
			text.copy();
			text.deselect();
		}
		
		public void pasteAll() {
			text.clear();
			text.paste();
		}
	
		public void pasteHtmlAll() {
			if( Clipboard.getSystemClipboard().hasHtml() ) {
				text.clear();
				text.setText( Clipboard.getSystemClipboard().getHtml() );
			}
		}
		
		public void wrapToggle() {
			text.setWrapText( ! text.isWrapText() );
		}
		
		public void setFontSize(int size) {
			if( size > 0 ) {
				fontSize = size;
				text.setStyle("-fx-font-size: " + size + ";"); //1em = 50px
			}
		}
		
		public void fontDown() {
			if( fontSize > 1 ) {
				fontSize--;
				setFontSize(fontSize);
			}
		}
	
		public void fontUp() {
			if( fontSize < 127 ) {
				fontSize++;
				setFontSize(fontSize);
			}		
		}

	}

	public static class FxTextField implements FxNode {
	
		TextField text;
		
		public FxTextField() {
			text = new TextField();
			FxDrop.setDropHandler(text, new FxRunnable() {
				@SuppressWarnings("unchecked")
				@Override public void run(Object... object) {
					if( object[0] instanceof List<?> ) {
						List<File> files = (List<File>) object[0];
						text.setText( files.get(0).getPath() );
					}
				}
			});
		}
		
		public FxTextField(String value, Runnable runnable) {
			this();
			if( value != null ) {
				text.setText(value);
			}
			if( runnable != null ) {
				text.textProperty().addListener((observable, oldValue, newValue) -> {
				    runnable.run();
				});
			}
		}
		
		public FxTextField(String value) {
			this(value, null);
		}
		
		public FxTextField(Runnable runnable) {
			this(null, runnable);
		}
	
		@Override
		public TextField get() {
			return text;
		}

		public void setOnEnterHandler(Runnable runnable) {
			text.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override public void handle(KeyEvent event) {
		            if (event.getCode().equals( KeyCode.ENTER) ) {
						runnable.run();
		            }				
				}
			});
		}
		
		public String getText() {
			return text.getText();
		}
	
		public void setText(String value) {
			text.setText(value);
		}

		public void insert(int index, String data) {
			text.insertText(index, data);
		}
		
		public void append(String data) {
			text.appendText(data);
		}
		
		public void clear() {
			text.clear();
		}
		
		public void copy() {
			text.copy();
		}
		
		public void paste() {
			text.paste();
		}
		
		public void copyAll() {
			text.selectAll();
			text.copy();
			text.deselect();
		}
		
		public void pasteAll() {
			text.clear();
			text.paste();
		}
	}
	
	public static class FxWebView implements FxNode {
		
		FxVBox vbox;
		public FxTextField addrText;
		public WebView webView;
		public WebEngine webEngine;
		public String webViewHome = "https://www.google.com";
		public Runnable handler;
		
		public FxWebView(FxRunnable runnable) {
			webView = new WebView();
			webEngine = webView.getEngine();
			webEngine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
			webEngine.setJavaScriptEnabled(true);
	        webEngine.getLoadWorker().stateProperty().addListener( new ChangeListener<State>() {
				@Override
				public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
	                //if (newValue == Worker.State.SUCCEEDED) {
					if( addrText != null ) {
						addrText.get().setText(webEngine.getLocation());
		            	if( runnable != null ) {
		            		runnable.run(newValue);
		            	}
					}
	                //}
				}
	        });
		}
	
		public FxWebView(boolean toolbar, FxRunnable runnable) {
			this(runnable);
			if( toolbar ) {
				FxToolBar hbox1 = new FxToolBar();
				hbox1.addToolBar();
				//hbox1.get().setAlignment(Pos.CENTER_LEFT);
				hbox1.add( new FxButton("Home", () -> home()));
				hbox1.add( new FxButton("Back", () -> back()) );
				hbox1.add( new FxButton("Forward", () -> forward()) );
				hbox1.add( new FxButton("Stop", () -> stop()) );
				hbox1.add( new FxButton("Copy", () -> copySourceToClipboard()) );
	
				FxToolBar hbox2 = new FxToolBar();
				hbox2.addToolBar();
				//hbox2.get().setAlignment(Pos.CENTER_LEFT);
				addrText = new FxTextField();
				hbox2.addToolBarItem(addrText.get(), true);
				addrText.setOnEnterHandler(() -> webEngine.load(addrText.get().getText())); 
				hbox2.add(new FxButton( "Go", () -> webEngine.load(addrText.get().getText())));
				
				VBox.setVgrow(hbox1.get(), Priority.NEVER);
				VBox.setVgrow(hbox2.get(), Priority.NEVER);
				VBox.setVgrow(webView, Priority.ALWAYS);
				vbox = new FxVBox();
				vbox.add(hbox1.get());
				vbox.add(hbox2.get());
				vbox.add(webView);
			}		
		}
	
		@Override
		public Node get() {
			if( vbox != null ) {
				return vbox.get();
			} else {
				return webView;
			}
		}
		
		public void addToolButton(FxHBox hbox, String text, Runnable runnable) {
			FxButton button = new FxButton(text, runnable );
			HBox.setHgrow(button.get(), Priority.NEVER);
			hbox.add(button);
		}
		
		public FxTextField addToolTextField(FxHBox hbox) {
			FxTextField text = new FxTextField();
			HBox.setHgrow(text.get(), Priority.ALWAYS);
			hbox.add(text);
			return text;
		}
	
		
		public void setProxy(String ip, int port) {
			System.setProperty("java.net.useSystemProxies", "true");
		    System.setProperty("http.proxy", ip);
		    System.setProperty("http.proxyHost", ip);
		    System.setProperty("http.proxyPort", String.valueOf(port));
		    System.setProperty("https.proxy", ip);
		    System.setProperty("https.proxyHost", ip);
		    System.setProperty("https.proxyPort", String.valueOf(port));
		}
		
		public void stop() {
			webEngine.getLoadWorker().cancel();
		}
		public void load(String url) {
			if( addrText != null ) {
				addrText.get().setText(url);
			}
			webEngine.load(url);
		}
	
		public void loadHtml(String html) {
			webEngine.loadContent(html);
		}
		
		public void reload() {
			webEngine.reload();
		}
		
		public void setHome(String url) {
			webViewHome = url;
		}
		
		public void home() {
			WebHistory hist = webEngine.getHistory();
			hist.go(-hist.getCurrentIndex());
		}
	
		public void back() {
			WebHistory hist = webEngine.getHistory();
			int index = hist.getCurrentIndex();
			if( index > 0 ) {
				hist.go(-1);
			}
		}
		
		public void forward() {
			WebHistory hist = webEngine.getHistory();
			int index = hist.getCurrentIndex();
			if( index < (hist.getEntries().size()-1) ) {
				hist.go(1);
			}
		}
	
		public String getTitle() {
			return webEngine.getTitle();
		}
	
		public String getLocation() {
			return webEngine.getLocation();
		}
		
		public String getUserAgent() {
			return webEngine.getUserAgent();
		}
		
		public String getHtml() {
			Document doc = webEngine.getDocument();
	        try {
	            Transformer transformer = TransformerFactory.newInstance().newTransformer();
	            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	            //transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(System.out, "UTF-8")));
	            StringWriter sw = new StringWriter();
	            transformer.transform(new DOMSource(doc), new StreamResult(sw));
	            return sw.toString();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            return null;
	        }
			//return (String) webEngine.executeScript("document.documentElement.outerHTML");
		}
		
		public void copySourceToClipboard() {
	        final Clipboard clipboard = Clipboard.getSystemClipboard();
	        final ClipboardContent content = new ClipboardContent();
	        String source = getHtml();
	        content.putString(source);
	        content.putHtml(source);
	        clipboard.setContent(content);
		}
		
		public List<String> getImageLinks() {
	        Document doc = webEngine.getDocument();
	        try {
	        	List<String> list = new ArrayList<>();
	            NodeList nodes = doc.getElementsByTagName("img");
	            for( int i = 0; i < nodes.getLength(); i++ ) {
	            	String img_url = webEngine.getLocation() + nodes.item(i).getAttributes().getNamedItem("src").getNodeValue();
	            	list.add(img_url);
	            }
	            return list;
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            return null;
	        }
		}
		
		public void snapshot(String fileName) throws IOException {
			WritableImage image = webView.snapshot(new SnapshotParameters(), null);
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File(fileName));
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	// JFxLib Members
	//////////////////////////////////////////////////////////////////////

	public void addMenu(String title) {
		menubar.addMenu(title);
	}

	public void addMenuItem(String title, String icon, Runnable runnable) {
		menubar.addMenuItem(title, icon, runnable);
	}
	
	public void addToolBar() {
		toolbar.addToolBar();
	}

	public void addToolBarItem(FxNode item) {
		toolbar.addToolBarItem(item,false);
	}
	
	public void addToolBarItem(Node item) {
		toolbar.addToolBarItem(item,false);
	}

	public void addToolBarItem(FxNode item, boolean extend) {
		toolbar.addToolBarItem(item,extend);
	}

	public void addToolBarItem(Node item, boolean extend) {
		toolbar.addToolBarItem(item,extend);
	}

	public void addSeparator() {
		toolbar.addSeparator();
	}
	
	public void addDefaultStatusText() {
		statusbar.setDefault();
	}
	
	public void addStatusBarItem(FxNode item) {
		statusbar.addStatusBarItem(item,false);
	}
	
	public void addStatusBarItem(Node item) {
		statusbar.addStatusBarItem(item,false);
	}

	public void addStatusBarItem(FxNode item, boolean extend) {
		statusbar.addStatusBarItem(item,extend);
	}

	public void addStatusBarItem(Node item, boolean extend) {
		statusbar.addStatusBarItem(item,extend);
	}

	public void setStatusBarText(String text) {
		statusbar.setText(text);
	}

	public void setStatusBarTextSize(int size) {
		
	}
	
	//////////////////////////////////////////////////////////////////////
	// JFxLib Members
	//////////////////////////////////////////////////////////////////////

	public void show(Stage stage) {
		stage.show();
	}

	public void show() {
		this.show(this.mainStage);
	}

	public void setTitle(Stage stage, String title) {
        stage.setTitle(title);
	}

	public void setTitle(String title) {
		this.setTitle(this.mainStage, title);
	}

	public Image getImage(String imageName, Class<?> cls) {
		if( cls != null ) {
			return new Image(cls.getResourceAsStream(imageName));
		} else {
			return new Image(getClass().getResourceAsStream(imageName));
		}
	}

	public Image getImage(String imageName) {
		return getImage(imageName,null);
	}

	public void setIcon(Stage stage, String icon) { // /res/icon.png
		stage.getIcons().add(getImage(icon));
	}

	public void setIcon(Stage stage, Class<?> cls, String icon) { // icon.png
		stage.getIcons().add(getImage(icon, cls));
	}

	public void setIcon(String icon) {
		this.setIcon(this.mainStage,icon);
	}
	
	public void setIcon(Class<?> cls, String icon) {
		this.setIcon(this.mainStage,cls,icon);
	}
	
	public void setExitOnQuit(Stage stage, EventHandler<WindowEvent> handler) {
		stage.setOnCloseRequest(handler);
		/*
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.exit(0);
            }
        });
        */  
	}
	
	public void setExitOnQuit(EventHandler<WindowEvent> handler) {
		this.setExitOnQuit(this.mainStage, handler);
	}
	
	Scene getScene(Stage stage) {
		return stage.getScene();
	}
	
	Node getRootNode(Stage stage) {
		return stage.getScene().getRoot();
	}
	
	public void setWindowBorderDecorated(Stage stage) {
		stage.initStyle(StageStyle.DECORATED);
	}
	
	public void setWindowBorderUndecorated(Stage stage) {
		stage.initStyle(StageStyle.UNDECORATED);
	}
	
	public void setWindowBorderUtility(Stage stage) {
		stage.initStyle(StageStyle.UTILITY);
	}
	
	public void setWindowBorderTransparent(Stage stage) {
		stage.initStyle(StageStyle.TRANSPARENT);
	}

	public void showWindow(Stage stage) {
		stage.show();
	}

	public void showNewWindow() {
		Stage stage = new Stage();
		BorderPane pane = new BorderPane();
		Scene scene = new Scene(pane, 320, 240);
		stage.setScene(scene);
		stage.show();
	}
	
    public Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

	public void setWindowPosition(Stage stage, int x, int y) {
		stage.setX(x);
		stage.setY(y);
	}

	public void setWindowPositionBottomRight(Stage stage) {
		Rectangle2D screenBound = Screen.getPrimary().getBounds();
		stage.setX(screenBound.getWidth() - stage.getWidth());
		stage.setY(screenBound.getHeight() - stage.getHeight());
	}

	public void setWindowPositionCenter(Stage stage) {
		Rectangle2D screenBound = Screen.getPrimary().getBounds();
		stage.setX((screenBound.getWidth() - stage.getWidth()) / 2);
		stage.setY((screenBound.getHeight() - stage.getHeight()) / 2);
	}
	
	public void moveWindow(Stage stage) {
		KeyFrame keyFrame = new KeyFrame(Duration.millis(10), e -> stage.setX(stage.getX()+1));		
		Timeline timeline  = new Timeline(); 
		timeline.setCycleCount(1000); 
		//timeline.setCycleCount(Timeline.INDEFINITE);
		//timeline.setAutoReverse(true); 
		timeline.getKeyFrames().addAll(keyFrame); 
		timeline.play();
	}
	
	public void clearClipboard() {
		Clipboard.getSystemClipboard().clear();
	}
	
	public String getClipboardText() {
		if( Clipboard.getSystemClipboard().hasString() ) {
			return Clipboard.getSystemClipboard().getString();
		}
		return null;
	}

	public String getClipboardHtmlText() {
		if( Clipboard.getSystemClipboard().hasHtml() ) {
			return Clipboard.getSystemClipboard().getHtml();
		}
		return null;
	}

	public BufferedImage getClipboardImage() {
		return SwingFXUtils.fromFXImage( Clipboard.getSystemClipboard().getImage(), null );
	}
	
	public List<File> getClipboardFiles() {
		if( Clipboard.getSystemClipboard().hasFiles() ) {
			return Clipboard.getSystemClipboard().getFiles();
		} 
		return null;
	}
	
	public void putClipboardText(String text) {
		ClipboardContent content = new ClipboardContent();
		content.putString(text);
		Clipboard.getSystemClipboard().setContent(content);
	}
	
	public void putClipboardHtmlText(String text) {
		ClipboardContent content = new ClipboardContent();
		content.putString(text);
		content.putHtml(text);
		Clipboard.getSystemClipboard().setContent(content);
	}
	
	public void putClipboardImage(BufferedImage image) {
		ClipboardContent content = new ClipboardContent();
		content.putImage(SwingFXUtils.toFXImage(image, null));
		Clipboard.getSystemClipboard().setContent(content);
	}	
	
	public void putClipboardFiles(List<File> files) {
		ClipboardContent content = new ClipboardContent();
		content.putFiles(files);
		Clipboard.getSystemClipboard().setContent(content);
	}
	
	//------------------------------------------------------------
	// System
	//------------------------------------------------------------

    public void runLater(Runnable runnable) {
        Platform.runLater(() -> runnable.run());
    }
    
    public Timeline runAfter(int msec, Runnable runnable) {
        Timeline timeline = new Timeline(new KeyFrame( Duration.millis(msec), e -> runnable.run()));
        timeline.play();
        return timeline;
    }
    
    public Timeline runPeriodic(int msec, Runnable runnable) {
        Timeline timeline = new Timeline(new KeyFrame( Duration.millis(msec), e -> runnable.run()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        return timeline;
    }
    
    public void stopPeriodic(Timeline timeline) {
        timeline.stop();
    }
    
    public String getHomeFolder() {
        return System.getProperty("user.home");
    }
    
    public String getWorkingFolder() {
        return System.getProperty("user.dir");
    }
    
    public File[] getRootLists() {
        return File.listRoots();
    }
    
	public double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}

	public double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}
	
	public void desktopOpen(String file) throws IOException {
		if( Desktop.getDesktop().isSupported(Action.OPEN) ) {
			Desktop.getDesktop().open(new File(file));
		}
	}

	public void desktopEdit(String file) throws IOException {
		if( Desktop.getDesktop().isSupported(Action.EDIT) ) {
			Desktop.getDesktop().edit(new File(file));
		}
	}

	public void desktopPrint(String file) throws IOException {
		if( Desktop.getDesktop().isSupported(Action.PRINT) ) {
			Desktop.getDesktop().print(new File(file));
		}
	}

	public void desktopBrowse(String url) throws URISyntaxException, IOException {
		if( Desktop.getDesktop().isSupported(Action.BROWSE) ) {
			Desktop.getDesktop().browse(new URI(url));
		}
	}
	
	public void desktopMail() throws IOException {
		if( Desktop.getDesktop().isSupported(Action.MAIL) ) {
			Desktop.getDesktop().mail();
		}
	}

	public void desktopMailTo(String url) throws URISyntaxException, IOException {
		if( Desktop.getDesktop().isSupported(Action.MAIL) ) {
			Desktop.getDesktop().mail(new URI(url));
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	// Dialog
	////////////////////////////////////////////////////////////////////// 
	
    public void alert(Stage parentStage, String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(parentStage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    } 
    
    public void alert(String title, String message) {
    	this.alert(mainStage, title, message);
    } 
    
    
    public boolean yesno(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainStage);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK ){
            return true;
        } else {
            return false;
        }
    } 
    
    public File getFileDialog() {
    	FileChooser dialog = new FileChooser();
    	dialog.setTitle("Select File");
    	return dialog.showOpenDialog(null);
    }
    
    public File getDirectoryDialog(String initialDirectory) {
    	DirectoryChooser dialog = new DirectoryChooser();
    	if( initialDirectory != null ) {
    		File f = new File(initialDirectory);
    		if( f.exists() && f.isDirectory() ) {
    			dialog.setInitialDirectory(f);
    		}
    	}
    	dialog.setTitle("Select Folder");
    	return dialog.showDialog(null);
    }
    
    public File getDirectoryDialog() {
    	return getDirectoryDialog(null);
    }    
	
	public static class FxDialog {
		
	    public static void alert(Stage ownerStage, String title, String message) {
	        Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.initOwner(ownerStage);
	        alert.initModality(Modality.APPLICATION_MODAL);
	        alert.showAndWait();
	    } 
	    
	    public static void alert(String iconRes, String title, String message) {
	        Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.initModality(Modality.APPLICATION_MODAL);
	        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
	        stage.getIcons().add(new Image(FxDialog.class.getResourceAsStream(iconRes)));
	        alert.showAndWait();
	    }
	    
	    public static boolean yesno(Stage ownerStage, String title, String message) {
	        Alert alert = new Alert(AlertType.CONFIRMATION);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.initOwner(ownerStage);
	        alert.initModality(Modality.APPLICATION_MODAL);
	        Optional<ButtonType> result = alert.showAndWait();
	        if (result.get() == ButtonType.OK ){
	            return true;
	        } else {
	            return false;
	        }
	    }    
	    
	    public static String getText(Stage ownerStage, String title, String message, String def_value) {
	        TextInputDialog dialog = new TextInputDialog(def_value);
	        dialog.setTitle(title);
	        dialog.setHeaderText(null);
	        dialog.setContentText(message);
	        dialog.initOwner(ownerStage);
	        dialog.initModality(Modality.APPLICATION_MODAL);
	        Optional<String> result = dialog.showAndWait();
	        if (result.isPresent()){
	            return result.get();
	        }
	        return null;
	    }  	
	    
	    public static String choice(Stage ownerStage, String title, String message, List<String> values) {
	        ChoiceDialog<String> dialog = new ChoiceDialog<>(values.get(0), values);
	        dialog.setTitle(title);
	        dialog.setHeaderText(null);
	        dialog.setContentText(message);
	        dialog.initOwner(ownerStage);
	        dialog.initModality(Modality.APPLICATION_MODAL);
	        Optional<String> result = dialog.showAndWait();
	        if (result.isPresent()){
	            return result.get();
	        }
	        return null;
	    } 
	    
	    public static int custonButton(Stage ownerStage, String title, String message) {
	        Alert alert = new Alert(AlertType.CONFIRMATION);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.initOwner(ownerStage);
	        alert.initModality(Modality.APPLICATION_MODAL);
	
	        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonData.YES);
	        ButtonType buttonTypeNo = new ButtonType("No", ButtonData.NO);
	        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
	        ButtonType buttonTypeCustom = new ButtonType("Custom");
	        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel, buttonTypeCustom);
	
	        Optional<ButtonType> result = alert.showAndWait();
	        if (result.get() == buttonTypeYes){
	            return 0;
	        } 
	        if (result.get() == buttonTypeNo) {
	            return -1;
	        } 
	        if (result.get() == buttonTypeCancel) {
	            return 1;
	        }       
	        return 2;
	    }  
	}
    
	//////////////////////////////////////////////////////////////////////
	// Toast
	////////////////////////////////////////////////////////////////////// 
	

    public void showToast(Stage ownerStage, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {
        Stage toastStage=new Stage();
        toastStage.initOwner(ownerStage);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Text text = new Text(toastMsg);
        text.setFont(Font.font("Verdana", 40));
        text.setFill(Color.RED);

        StackPane root = new StackPane(text);
        root.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(0, 0, 0, 0.2); -fx-padding: 50px;");
        root.setOpacity(0);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setScene(scene);
        toastStage.show();

        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue (toastStage.getScene().getRoot().opacityProperty(), 1)); 
        fadeInTimeline.getKeyFrames().add(fadeInKey1);   
        fadeInTimeline.setOnFinished((ae) -> {
            new Thread(() -> {
                try {
                    Thread.sleep(toastDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Timeline fadeOutTimeline = new Timeline();
                KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue (toastStage.getScene().getRoot().opacityProperty(), 0)); 
                fadeOutTimeline.getKeyFrames().add(fadeOutKey1);   
                fadeOutTimeline.setOnFinished((aeb) -> toastStage.close()); 
                fadeOutTimeline.play();
            }).start();
        }); 
        fadeInTimeline.play();
    }

    public void showToast(String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {
    	showToast(mainStage, toastMsg, toastDelay, fadeInDelay, fadeOutDelay);
    }
    
    public void showToast(String toastMsg) {
    	showToast(mainStage, toastMsg, 3500, 500, 500);
    }
    
    public void showToast(String toastMsg, int toastDelay) {
    	showToast(mainStage, toastMsg, toastDelay, 500, 500);
    }
    
    /*
    @Override
    public void start(Stage stage) {
    	String toastMsg = "some text...";
    	int toastMsgTime = 3500; //3.5 seconds
    	int fadeInTime = 500; //0.5 seconds
    	int fadeOutTime= 500; //0.5 seconds
    	FxToast.makeText(stage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    */

	
}
