package com.hksz.demo.ui.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class InitSceneController implements Initializable {
    @FXML
    private Button btnOpenFile;


    public InitSceneController() {
        System.out.println("");
//        btnOpenFile.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("");
//            }
//        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("");
        btnOpenFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("openFile clicked.");
            }
        });
    }
}
