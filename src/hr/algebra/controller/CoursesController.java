/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dal.sql.HibernateFactory;
import hr.algebra.dal.sql.RepoitoryFactory;
import hr.algebra.model.Course;
import hr.algebra.utilities.MessageUtils;
import hr.algebra.viewModel.CourseViewModel;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author HT-ICT
 */
public class CoursesController implements Initializable {

    private Map<TextField, Label> validationMap;
    private CourseViewModel selectedStudentViewModel;
    private final ObservableList<CourseViewModel> list = FXCollections.observableArrayList();

    @FXML
    private TabPane tpContent;
    @FXML
    private Tab tabList;
    @FXML
    private Tab tabEdit;
    @FXML
    private TableColumn<CourseViewModel, String> tcName;
    @FXML
    private TableColumn<CourseViewModel, Integer> tcECTS;
    @FXML
    private TextField tfName, tfEcts;
    @FXML
    private Label lbName, lbECTS;
    @FXML
    private TableView<CourseViewModel> tvCourses;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initValidation();
        initCourses();
        initTable();
        addIntegerMask(tfEcts);
        setUpListeners();
    }

    private void initValidation() {
        validationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfName, lbName),
                new AbstractMap.SimpleImmutableEntry<>(tfEcts, lbECTS))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void initCourses() {
        try {
            RepoitoryFactory.getRepository().<Course>get(HibernateFactory.SELECT_ALL_COURSES).forEach(course -> list.add(new CourseViewModel(course)));
        } catch (Exception ex) {
            Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showMessage(Alert.AlertType.ERROR, "Unable to load the form. Exiting...");
        }

    }

    private void initTable() {
        tcName.setCellValueFactory(course -> course.getValue().getName());
        tcECTS.setCellValueFactory(course -> course.getValue().getECTS().asObject());
        tvCourses.setItems(list);

    }

    private void addIntegerMask(TextField tfEcts) {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("\\d*")) {
                return change;
            }
            return null;
        };

        tfEcts.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));

    }
    private Tab priorTab;

    private void setUpListeners() {
        tpContent.setOnMouseClicked(event -> {
            if (tpContent.getSelectionModel().getSelectedItem().equals(tabEdit) && priorTab == tabList) {
                bindCourse(null);
            }
            priorTab = tpContent.getSelectionModel().getSelectedItem();
        });
        list.addListener(new ListChangeListener<CourseViewModel>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends CourseViewModel> change) {
                if (change.next()) {
                    if (change.wasRemoved()) {
                        change.getRemoved().forEach(cvm -> {
                            try {
                                RepoitoryFactory.getRepository().<Course>delete(cvm.getCourse());
                            } catch (Exception ex) {
                                Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    } else if (change.wasAdded()) {
                        change.getAddedSubList().forEach(cvm -> {
                            try {
                                Course add = RepoitoryFactory.getRepository().<Course>add(cvm.getCourse());
                                cvm.getCourse().setIDCourse(add.getIDCourse());
                            } catch (Exception ex) {
                                Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                }
            }
        });
    }

    private void bindCourse(CourseViewModel courseViewModel) {
        resetForm();
        selectedStudentViewModel = courseViewModel != null ? courseViewModel : new CourseViewModel(null);
        tfName.setText(selectedStudentViewModel.getName().get());
        tfEcts.setText(String.valueOf(selectedStudentViewModel.getECTS().get()));
    }

    @FXML
    private void delete(ActionEvent event) {
        if (tvCourses.getSelectionModel().getSelectedItem() != null) {
            list.remove(tvCourses.getSelectionModel().getSelectedItem());

        }
    }

    @FXML
    private void edit(ActionEvent event) {
        if (tvCourses.getSelectionModel().getSelectedItem() != null) {
            bindCourse(tvCourses.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabEdit);
        }
    }

    @FXML
    private void commit(ActionEvent event) {
        if (formValid()) {
            selectedStudentViewModel.getCourse().setEcts(Integer.valueOf(tfEcts.getText().trim()));
            selectedStudentViewModel.getCourse().setName(tfName.getText().trim());
            if (selectedStudentViewModel.getCourse().getIDCourse() == 0) {
                list.add(selectedStudentViewModel);
            } else {
                try {
                    RepoitoryFactory.getRepository().<Course>update(selectedStudentViewModel.getCourse(), selectedStudentViewModel.getCourse().getIDCourse());
                    tvCourses.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedStudentViewModel = null;
            tpContent.getSelectionModel().select(tabList);
            resetForm();
        }
    }

    private boolean formValid() {
        final AtomicBoolean ok = new AtomicBoolean(true);
        validationMap.keySet().forEach(tf -> {
            if (tf.getText().trim().isEmpty()) {
                validationMap.get(tf).setVisible(true);
                ok.set(false);
            } else {
                validationMap.get(tf).setVisible(false);
            }
        });
        return ok.get();
    }

    private void resetForm() {
        validationMap.values().forEach(lb -> lb.setVisible(false));
    }

}
