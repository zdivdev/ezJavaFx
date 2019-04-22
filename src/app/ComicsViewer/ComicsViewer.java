package com.zdiv.app.ComicsViewer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.zdiv.java.UnzipUtilApache;
import com.zdiv.jfxlib.JFxApp;
import com.zdiv.jfxlib.JFxLib.FxButton;
import com.zdiv.jfxlib.JFxLib.FxComboBox;
import com.zdiv.jfxlib.JFxLib.FxLabel;
import com.zdiv.jfxlib.JFxLib.FxRunnable;
import com.zdiv.jfxlib.JFxLib.FxTextField;

import javafx.scene.input.KeyCode;

public class ComicsViewer extends JFxApp {

	final String title = "ComicsViewer V1.0";
	ComicsImageView comics;
	UnzipUtilApache images;
	FxComboBox pages;
	FxTextField file;
	int imagesIndex;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public ComicsViewer() {
		
    	images = new UnzipUtilApache();
    	imagesIndex = 0;
		
		setTitle(title);
		setIcon("/res/window.png");
		setSize(800,480);
		
		setMenubar( () -> {
			fx.addMenu("File");
			fx.addMenuItem("Exit", null, () -> System.exit(0) );
			fx.addMenuItem( null, null, null );
			fx.addMenuItem("About", null, () -> fx.alert("About", title) );
		});

		setToolbar( () -> {
			fx.addToolBarItem( new FxLabel("File: "));
			file = new FxTextField(() -> loadComicsFile( file.getText()) );
			fx.addToolBarItem( file, true  );
			fx.addToolBarItem( new FxButton("Browse", () -> {
				File f = fx.getFileDialog();
				if( f != null ) {
					file.setText(f.getAbsolutePath());
				}
			}));
		});

		setStatusbar( () -> {
			pages = new FxComboBox( () -> gotoPage() );
			fx.addStatusBarItem( pages );
			fx.setStatusBarText("Ready");
		});
		
		setContent( () -> {
			comics = new ComicsImageView(20, keyHandle, dropHandle );
			fx.setCenter(comics);
		});
	}

	Runnable runnableExit = new Runnable() {
		@Override public void run() {
			boolean result = fx.yesno("Exit", "Do you want exit progrma ?");
			if( result ) { System.exit(0); }
		}
	};

	FxRunnable keyHandle = new FxRunnable() {
		@Override public void run(Object... object) {
			if( images != null ) {
				KeyCode e = (KeyCode)object[0];		

		        if ( e.equals(KeyCode.UP) ) {
		            System.out.println("Up Pressed");
		        }
		        if ( e.equals(KeyCode.DOWN) ) {
		            System.out.println("Down Pressed");
		        }
		        if ( e.equals(KeyCode.RIGHT) ) {
		            System.out.println("Right Pressed");
		            nextPage();
		        }
		        if ( e.equals(KeyCode.LEFT) ) {
		            System.out.println("Left Pressed");
		            prevPage();
		        }
		        if ( e.equals(KeyCode.PAGE_UP) ) {
		            System.out.println("PageUp Pressed");
		        }
		        if ( e.equals(KeyCode.PAGE_DOWN) ) {
		            System.out.println("PageDown Pressed");
		        }
		        if ( e.equals(KeyCode.HOME) ) {
		            System.out.println("Home Pressed");
		        }
		        if ( e.equals(KeyCode.END) ) {
		            System.out.println("End Pressed");
		        }
		        if ( e.equals(KeyCode.ENTER) ) {
		            System.out.println("Enter Pressed");
		        }

			}
		}
	};
	
	FxRunnable dropHandle = new FxRunnable() {
		@SuppressWarnings("unchecked")
		@Override public void run(Object... object) {
			if( object[0] instanceof List<?> ) {
				List<File> f = (List<File>)object[0];
				try {
					loadComicsFile(f.get(0).getAbsolutePath());
				} catch (Exception e) {
				}
			}
		}
	};

    public void loadComicsFile(String filePath) {
		System.out.println(filePath);
		if( new File(filePath).isFile() ) {
			try {
				images.load(filePath, "EUC_KR");
				pages.get().getItems().clear();
				for( int i = 0; i < images.size(); i++ ) {
					pages.get().getItems().add(images.getName(i));
				}
				firstPage();
			} catch (IOException e) {
				e.printStackTrace();
			} 		
		}
    }

    public void setImage(int index) {
		try {
			comics.setImage(images.unzipEntry(index));
			fx.setStatusBarText(" [ " + (index+1) + " / " + images.size() + " ] ");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	public void firstPage() {
		imagesIndex = 0;
		pages.get().getSelectionModel().selectFirst();
	}
	
	public void lastPage() {
		imagesIndex = images.size() - 1;
		pages.get().getSelectionModel().selectLast();
	}
		
	public void nextPage() {
		if( imagesIndex < (images.size()-1) ) {
			imagesIndex++;
			pages.get().getSelectionModel().select(imagesIndex);
		} else {
			fx.showToast("마지막 페이지 입니다", 1000);
		}
	}
	
	public void prevPage() {
		if( imagesIndex > 0 ) {
			imagesIndex--;
			pages.get().getSelectionModel().select(imagesIndex);
		} else {
			fx.showToast("첫 페이지 입니다", 1000);
		}
	}
	
	public void gotoPage() {
		int index  = pages.get().getSelectionModel().getSelectedIndex();
		if( index >= 0 && index <= (images.size()-1) ) {
			imagesIndex = index;
			setImage(imagesIndex);
		}
	}
	
}
