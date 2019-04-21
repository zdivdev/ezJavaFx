package com.zdiv.demo;

import com.zdiv.jfxlib.JFxApp;
import com.zdiv.jfxlib.JFxLib.FxButton;
import com.zdiv.jfxlib.JFxLib.FxChoiceBox;
import com.zdiv.jfxlib.JFxLib.FxRunnable;
import com.zdiv.jfxlib.JFxLib.FxSplitPane;
import com.zdiv.jfxlib.JFxLib.FxWebView;
import com.zdiv.richtext.FxRichText;

import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;

public class AppWeb extends JFxApp {

	FxWebView web;
	FxRichText text;
	FxChoiceBox choice;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public AppWeb() {
		
		setTitle("AppWeb");
		setIcon("/res/window.png");
		setSize(800,480);
		
		setMenubar( () -> {
			fx.addMenu("File");
			fx.addMenuItem("Exit", null, () -> System.exit(0) );
			fx.addMenuItem( null, null, null );
			fx.addMenuItem("About", null, () -> fx.alert("About", "FxAppDemo V1.0") );
			fx.addMenuItem("Toast", null, () -> fx.showToast("FxAppDemo V1.0") );
		});

		setToolbar( () -> {
			fx.addToolBarItem( new FxButton("Exit", () -> System.exit(0)));
			fx.addToolBarItem( new FxButton("Html", () -> text.setText(web.getHtml())));
			fx.addSeparator();
			fx.addToolBarItem( new FxButton("About", () -> fx.alert("About", "FxAppDemo V1.0")));
			choice = new FxChoiceBox( new String[] {"item1","item2"}, choceHandler );
			fx.addToolBarItem( choice );
		});

		setStatusbar( () -> {
			fx.setStatusBarText("Ready");
		});
		
		setContent( () -> {
			web = new FxWebView(true, webHandler);
			web.load("https://www.google.com");
			text = new FxRichText(true);
			text.setSyntaxHighlightJava(fx.getScene());
			FxSplitPane split = new FxSplitPane();
			split.add(web.get());
			split.add(text.get());
			fx.setCenter(split);
		});
	}

	FxRunnable webHandler = new FxRunnable() {
		@Override public void run(Object...objects) {
			State value = (State)objects[0];
			fx.setStatusBarText(value.name());
			if( value == Worker.State.SUCCEEDED ) {
				fx.setStatusBarText(value.name() + " - [" + web.getTitle() + "]");
			}
		}
	};	
		
	Runnable choceHandler = new Runnable() {
		@Override public void run() {
			fx.showToast(choice.getSelectedItem());
		}
	};		
}
