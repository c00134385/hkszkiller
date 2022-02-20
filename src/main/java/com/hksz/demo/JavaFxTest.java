package com.hksz.demo;

import com.hksz.demo.ui.view.InitSceneView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaFxTest extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(JavaFxTest.class, InitSceneView.class, args);
    }
}
