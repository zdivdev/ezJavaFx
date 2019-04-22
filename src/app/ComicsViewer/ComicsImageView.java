package com.zdiv.app.ComicsViewer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

import com.zdiv.jfxlib.JFxLib.FxDrop;
import com.zdiv.jfxlib.JFxLib.FxNode;
import com.zdiv.jfxlib.JFxLib.FxRunnable;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

public class ComicsImageView implements FxNode {

	ImageView imageView;
	ScrollPane imagePane;
	Label glassText;
	StackPane glassView;
	StackPane layout;
	
	
	public ComicsImageView(int fontSize, FxRunnable keyHandle, FxRunnable dropHandle) {
	
	    imageView = new ImageView(); //new Image(new File("D:/a.png").toURI().toString())
	    imageView.setPreserveRatio(true);
	    imageView.setPickOnBounds(true);
	    imageView.setOnMouseMoved(e -> {
	        if ( e.getX() < imagePane.getWidth() / 4 || e.getX() > imagePane.getWidth() * 3 / 4 ) {
	            if ( e.getX() < imagePane.getWidth() / 4 ) {
	                Platform.runLater(() ->glassText.setText("<"));
	                StackPane.setAlignment(glassView, Pos.BOTTOM_LEFT);
	            } else {
	                Platform.runLater(() ->glassText.setText(">"));
	                StackPane.setAlignment(glassView, Pos.BOTTOM_RIGHT);
	            }
	            glassView.setMaxWidth(imagePane.getWidth() / 4);
	            glassView.setMaxHeight(imagePane.getHeight() / 4);
	            glassView.setVisible(true);
	        } else {
	            glassView.setVisible(false);
	        }
	        e.consume();
	    });
		
	    //imageView.setOnMouseClicked( e-> {
	    //	if( e.getButton().equals(MouseButton.PRIMARY) ) {
	    //		runnable.run(KeyCode.RIGHT);
	    //	}
	    //});
	    
	    imagePane = new ScrollPane();
	    imagePane.setPannable(true);
    	if( fontSize > 0 ) {
    		imagePane.setStyle("-fx-font-size: " + fontSize + " pt;");
    	}
	    imageView.fitWidthProperty().bind(imagePane.widthProperty());
	    imagePane.setContent(imageView);
	    imagePane.vvalueProperty().addListener(new ChangeListener<Number>() {
	        public void changed(ObservableValue<? extends Number> ov,
	        Number old_val, Number new_val) {
	            if ( new_val.equals(1.0) ) {
	            	//runnable.run(KeyCode.RIGHT);
	            }
	            if ( new_val.equals(0.0) ) {
	            	//runnable.run(KeyCode.LEFT);
	            }
	        }
	    });

	    imagePane.setOnKeyPressed(e -> {
	    	keyHandle.run(e.getCode());
	    });

	    glassText = new Label("");
	    glassText.setStyle("-fx-text-fill: goldenrod; -fx-font: italic 20 \"serif\"; -fx-padding: 0 0 20 0; -fx-text-alignment: center");
	
	    glassView = new StackPane();
	    StackPane.setAlignment(glassText, Pos.CENTER);
	    glassView.getChildren().addAll(glassText);
	    glassView.setStyle("-fx-background-color: rgba(0, 50, 100, 0.1); -fx-background-radius: 10;");
	    glassView.setLayoutX(0);
	    glassView.setLayoutY(0);
	    glassView.setPrefWidth(imageView.getFitWidth() / 4);
	    glassView.setPrefHeight(imageView.getFitHeight() / 4);
	    glassView.setVisible(false);
	    glassView.setOnMouseClicked( e-> {
	        if ( glassText.getText().equals(">") ) {
	        	keyHandle.run(KeyCode.RIGHT);
	        } else {
	        	keyHandle.run(KeyCode.LEFT);
	        }
	    });

	    glassView.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if( ! newValue.booleanValue() ) {
                    glassView.setVisible(false);
                }
            }
        });
	    
	    layout = new StackPane();
	    StackPane.setAlignment(glassView, Pos.BOTTOM_LEFT);
	    layout.getChildren().addAll(imagePane, glassView);
	    layout.setStyle("-fx-background-color: silver; -fx-padding: 1;");
	}	

	@Override
	public Node get() {
		return layout;
	}

	public StackPane getView() {
		return layout;
	}
	
	public void setImage(byte[] image) {
		System.out.println("ComicsImageView.setImage()");
		imageView.setImage(new Image(new ByteArrayInputStream(image)));
		imagePane.setVvalue(0.0);  
	}
	
	public void setContextMenu(ContextMenu menu) {
		System.out.println("ComicsImageView.setContextMenu()");
		imagePane.setContextMenu(menu);
	}
	
	public void hideScrollBar() {
    	imagePane.setHbarPolicy(ScrollBarPolicy.NEVER);
    	imagePane.setVbarPolicy(ScrollBarPolicy.NEVER);
	}

	public void showScrollBar() {
    	imagePane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
    	imagePane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
	}
	
	public void clearGlassView() {
		glassView.setVisible(false);
	}

}
