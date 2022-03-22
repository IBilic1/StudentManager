/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewModel;

import hr.algebra.model.Student;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author HT-ICT
 */
public class StudentViewModel {

    private final Student student;

    public Student getStudent() {
        return student;
    }

    public StudentViewModel(Student student) {
        if (student == null) {
            student = new Student(0, "", "", "", "");
        }
        this.student = student;
    }

    public SimpleStringProperty getFirstName() {
        return new SimpleStringProperty(student.getFirstName());
    }

    public SimpleStringProperty getLastName() {
        return new SimpleStringProperty(student.getLastName());
    }

    public SimpleStringProperty getEmail() {
        return new SimpleStringProperty(student.getEmail());
    }

    public SimpleStringProperty getDateOfBirth() {
        return new SimpleStringProperty(student.getDateOfBirth());
    }

     public ObjectProperty<byte[]> getPicture() {
        return new SimpleObjectProperty<>(student.getPicture());
    }
}
