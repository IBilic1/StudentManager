/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.StudentApplication;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * FXML Controller class
 *
 * @author HT-ICT
 */
public class MenuController {

    @FXML
    private void showStudents(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(StudentApplication.class.getResource("view/Students.fxml"));
        Scene scene = new Scene(loader.load());
        StudentApplication.getStage().setScene(scene);
    }

    @FXML
    private void showCourses(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(StudentApplication.class.getResource("view/Courses.fxml"));
        Scene scene = new Scene(loader.load());
        StudentApplication.getStage().setScene(scene);
    }

}
