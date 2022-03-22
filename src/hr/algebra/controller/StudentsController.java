/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dal.sql.HibernateFactory;
import hr.algebra.dal.sql.RepoitoryFactory;
import hr.algebra.model.Course;
import hr.algebra.model.Student;
import hr.algebra.model.StudentCourse;
import hr.algebra.utilities.FileUtils;
import hr.algebra.utilities.ImageUtils;
import hr.algebra.utilities.ValidationUtils;
import hr.algebra.viewModel.StudentViewModel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author HT-ICT
 */
public class StudentsController implements Initializable {

    private Map<TextField, Label> validationMap;
    private StudentViewModel selectedStudentViewModel;
    private final ObservableList<StudentViewModel> list = FXCollections.observableArrayList();
    private final ObservableList<StudentCourse> allCourses = FXCollections.observableArrayList();
    private ObservableList<StudentCourse> studentsCourses = FXCollections.observableArrayList();
    @FXML
    private Tab tabList;
    @FXML
    private TableColumn<StudentViewModel, String> tcFirstName;
    @FXML
    private TableColumn<StudentViewModel, String> tcLastName;
    @FXML
    private TableColumn<StudentViewModel, String> tcEmail;
    @FXML
    private TableColumn<StudentViewModel, String> tcDateOfBirth;
    @FXML
    private Tab tabAdd;
    @FXML
    private ImageView ivStudent;
    @FXML
    private TextField tfLastName, tfDateOfBirth, tfFirstName, tfEmail;
    @FXML
    private Label lbLastName, lbFirstName, lbEmail, lbDateOfBirth, lbIcon;
    @FXML
    private TableView<StudentViewModel> tvStudents;
    @FXML
    private TabPane tpContent;
    @FXML
    private ListView<StudentCourse> lvCourses, lvAllCourses;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initValidations();
            initStudents();
            inittable();
            setUpListeners();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void delete(ActionEvent event) throws Exception {
        if (tvStudents.getSelectionModel().getSelectedItem() != null) {
            list.remove(tvStudents.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void edit() {
        if (tvStudents.getSelectionModel().getSelectedItem() != null) {
            bindStudent(tvStudents.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabAdd);
        }
    }

    private void initValidations() {
        validationMap = Stream.of(
                new SimpleImmutableEntry<>(tfFirstName, lbFirstName),
                new SimpleImmutableEntry<>(tfLastName, lbLastName),
                new SimpleImmutableEntry<>(tfEmail, lbEmail),
                new SimpleImmutableEntry<>(tfDateOfBirth, lbDateOfBirth)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    private void initStudents() throws ClassNotFoundException {

        try {
            RepoitoryFactory.getRepository().<Student>get(HibernateFactory.SELECT_ALL).forEach(p
                    -> {
                try {
                    list.add(new StudentViewModel(p));
                } catch (Exception ex) {
                    Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void inittable() {
        tcFirstName.setCellValueFactory(student -> student.getValue().getFirstName());
        tcLastName.setCellValueFactory(student -> student.getValue().getLastName());
        tcEmail.setCellValueFactory(student -> student.getValue().getEmail());
        tcDateOfBirth.setCellValueFactory(student -> student.getValue().getDateOfBirth());

        tvStudents.setItems(list);
    }

    private Tab priorTab;

    private void setUpListeners() {
        tpContent.setOnMouseClicked(event -> {
            if (tpContent.getSelectionModel().getSelectedItem().equals(tabAdd) && priorTab == tabList) {
                bindStudent(null);
            }
            priorTab = tpContent.getSelectionModel().getSelectedItem();
        });
        list.addListener(new ListChangeListener<StudentViewModel>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends StudentViewModel> change) {
                if (change.next()) {
                    if (change.wasRemoved()) {
                        change.getRemoved().forEach(svm -> {
                            try {
                                RepoitoryFactory.getRepository().<Student>delete(svm.getStudent());
                            } catch (Exception ex) {
                                Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    } else if (change.wasAdded()) {
                        change.getAddedSubList().forEach(pvm -> {
                            try {
                                Student student = RepoitoryFactory.getRepository().<Student>add(pvm.getStudent());
                                pvm.getStudent().setIDStudent(student.getIDStudent());
                            } catch (Exception ex) {
                                Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    };
                }
            }

        }
        );

    }

    private void bindStudent(StudentViewModel studentViewModel) {
        resetForm();
        studentsCourses.clear();
        allCourses.clear();
        selectedStudentViewModel = studentViewModel != null ? studentViewModel : new StudentViewModel(null);

        if (selectedStudentViewModel.getStudent().getStudentCourseCollection() != null) {
            initCourses();
            initAllCourses();
        } else {
            lvCourses.setItems(studentsCourses);
            initAllCourses();
        }
        tfFirstName.setText(selectedStudentViewModel.getFirstName().get());
        tfLastName.setText(selectedStudentViewModel.getLastName().get());
        tfEmail.setText(selectedStudentViewModel.getEmail().get());
        tfDateOfBirth.setText(selectedStudentViewModel.getDateOfBirth().get());

        ivStudent.setImage(
                selectedStudentViewModel.getPicture().get() != null
                ? new Image(new ByteArrayInputStream(selectedStudentViewModel.getPicture().get()))
                : new Image(new File("src/assets/no_image.png").toURI().toString()));

    }

    @FXML
    private void add(ActionEvent event) {
        if (formValid()) {
            selectedStudentViewModel.getStudent().setFirstName(tfFirstName.getText().trim());
            selectedStudentViewModel.getStudent().setLastName(tfLastName.getText().trim());
            selectedStudentViewModel.getStudent().setEmail(tfEmail.getText().trim());
            selectedStudentViewModel.getStudent().setDateOfBirth(tfDateOfBirth.getText().trim());

            if (selectedStudentViewModel.getStudent().getIDStudent() == 0) {
                list.add(selectedStudentViewModel);
                studentsCourses.forEach(c -> {
                    try {
                        c.setStudentID(selectedStudentViewModel.getStudent());
                        RepoitoryFactory.getRepository().<StudentCourse>add(c);
                    } catch (Exception ex) {
                        Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            } else {
                try {
                    RepoitoryFactory.getRepository().<Student>update(selectedStudentViewModel.getStudent(), selectedStudentViewModel.getStudent().getIDStudent());
                    tvStudents.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedStudentViewModel = null;
            tpContent.getSelectionModel().select(tabList);
            resetForm();
        }
    }

    @FXML
    private void upload() {
        File file = FileUtils.uploadFileDialog(tfFirstName.getScene().getWindow(), "jpeg", "jpg", "png");
        if (file != null) {
            try {
                Image image = new Image(file.toURI().toString());
                String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                selectedStudentViewModel.getStudent().setPicture(ImageUtils.imageToByteArray(image, ext));
                ivStudent.setImage(image);

            } catch (IOException ex) {
                Logger.getLogger(StudentsController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void resetForm() {
        validationMap.values().forEach(l -> l.setVisible(false));
        lbIcon.setVisible(false);
    }

    private boolean formValid() {
        final AtomicBoolean ok = new AtomicBoolean(true);

        validationMap.keySet().forEach(tf -> {
            if (tf.getText().trim().isEmpty()
                    || tf.getId().contains("Email") && !ValidationUtils.isValidEmail(tf.getText().trim())
                    || tf.getId().contains("Date") && !ValidationUtils.isValidDate(tf.getText().trim())) {
                ok.set(false);
                validationMap.get(tf).setVisible(true);
            } else {
                validationMap.get(tf).setVisible(false);
            }
        });

        if (selectedStudentViewModel.getPicture().get() == null) {
            lbIcon.setVisible(true);
            ok.set(false);
        } else {
            lbIcon.setVisible(false);
        }
        return ok.get();
    }

    @FXML
    private void moveToAllCourses() {
        if (lvCourses.getSelectionModel().getSelectedItem() != null) {
            try {
                allCourses.add(lvCourses.getSelectionModel().getSelectedItem());

                RepoitoryFactory.getRepository().<StudentCourse>delete(lvCourses.getSelectionModel().getSelectedItem());
                studentsCourses.remove(lvCourses.getSelectionModel().getSelectedItem());

                // i remove from database
            } catch (Exception ex) {
                Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void moveToStudentCourses() {
        if (lvAllCourses.getSelectionModel().getSelectedItem() != null) {
            try {
                studentsCourses.add(lvAllCourses.getSelectionModel().getSelectedItem());
                if (selectedStudentViewModel.getStudent().getIDStudent() != 0) {
                    RepoitoryFactory.getRepository().<StudentCourse>add(lvAllCourses.getSelectionModel().getSelectedItem());
                }
                allCourses.remove(lvAllCourses.getSelectionModel().getSelectedItem());
                // i remove from database
            } catch (Exception ex) {
                Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void initCourses() {
        new Thread(() -> {
            selectedStudentViewModel.getStudent().getStudentCourseCollection().forEach(c -> {
                studentsCourses.add(c);
            });
            Platform.runLater(() -> lvCourses.setItems(studentsCourses));
        }).start();

    }

    private void initAllCourses() {
        new Thread(() -> {
            try {
                List<Course> all;
                if (selectedStudentViewModel.getStudent().getStudentCourseCollection() == null) {
                    all = RepoitoryFactory.getRepository().<Course>get(HibernateFactory.SELECT_ALL_COURSES);
                } else {
                    all = RepoitoryFactory.getRepository().<Course>get(HibernateFactory.SELECT_ALL_COURSES).stream()
                            .filter(c -> !selectedStudentViewModel.getStudent().getStudentCourseCollection().contains(new StudentCourse(c, selectedStudentViewModel.getStudent()))
                            ).collect(Collectors.toList());
                }
                
                Platform.runLater(() ->{ 
                    all.forEach(l -> allCourses.add(new StudentCourse(l, selectedStudentViewModel.getStudent())));
                    lvAllCourses.setItems(allCourses);});
            } catch (Exception ex) {
                Logger.getLogger(StudentsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
}
