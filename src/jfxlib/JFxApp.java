package com.zdiv.jfxlib;

import javafx.application.Application;
import javafx.stage.Stage;

public class JFxApp extends Application {
    
	//main() -> JFxApp() -> start()

	protected JFxLib fx;

	String title;
	Class<?> icon_cls;
	String icon;
	int width = 800;
	int height = 600;
	
	Runnable _menu;
	Runnable _tool;
	Runnable _status;
	Runnable _content;
	
	public JFxApp() {
	}

	protected void setTitle(String title) {
		this.title = title;
	}

	protected void setIcon(String icon) {
		this.icon_cls = null;
		this.icon = icon;
	}

	protected void setIcon(Class<?> cls, String icon) {
		this.icon_cls = cls;
		this.icon = icon;
	}

	protected void setSize(int w, int h) {
		this.width = w;
		this.height = h;
	}
	
	protected void setMenubar(Runnable runnable) {
		_menu = runnable;
	}

	protected void setToolbar(Runnable runnable) {
		_tool = runnable;
	}

	protected void setStatusbar(Runnable runnable) {
		_status = runnable;
	}

	protected void setContent(Runnable runnable) {
		_content = runnable;
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override public void start(Stage stage) throws Exception {

		fx = new JFxLib(stage);
		
        if( this.title != null ) {
        	fx.setTitle( this.title );
        }
        if( this.icon != null ) {
        	if( this.icon_cls == null ) {
        		fx.setIcon( this.icon );
        	} else {
        		fx.setIcon( this.icon_cls, this.icon );
        	}
        }
		if( _menu != null ) {
	        fx.makeMenubar();
			_menu.run();
		}
		if( _tool != null ) {
	        fx.makeToolbar();
	        fx.addToolBar();
			_tool.run();
		}
		if( _status != null ) {
	        fx.makeStatusbar();
			_status.run();
		}
		if( _content != null ) {
			fx.makeBorderPane(width,height);
			_content.run();
		}

		fx.show();
	}
}
