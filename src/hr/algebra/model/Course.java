/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author HT-ICT
 */
@Entity
@Table(name = "Course")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Course.findAll", query = "SELECT c FROM Course c")
    , @NamedQuery(name = "Course.findByIDCourse", query = "SELECT c FROM Course c WHERE c.iDCourse = :iDCourse")
    , @NamedQuery(name = "Course.findByName", query = "SELECT c FROM Course c WHERE c.name = :name")
    , @NamedQuery(name = "Course.findByEcts", query = "SELECT c FROM Course c WHERE c.ects = :ects")})
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDCourse")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer iDCourse;
    @Basic(optional = false)
    @Column(name = "Name")
    private String name;
    @Basic(optional = false)
    @Column(name = "ECTS")
    private int ects;
    @OneToMany(mappedBy = "courseID", fetch = FetchType.EAGER, orphanRemoval = true)
    private Collection<StudentCourse> studentCourseCollection;

    public Course() {
    }

    public Course(Course data) {
        updateDetails(data);
    }

    public Course(Integer iDCourse) {
        this.iDCourse = iDCourse;
    }
    public Course(Object data) {
        updateDetails(data);
    }

    public Course(Integer iDCourse, String name, int ects) {
        this.iDCourse = iDCourse;
        this.name = name;
        this.ects = ects;
    }

    public Integer getIDCourse() {
        return iDCourse;
    }

    public void setIDCourse(Integer iDCourse) {
        this.iDCourse = iDCourse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    @XmlTransient
    public Collection<StudentCourse> getStudentCourseCollection() {
        return studentCourseCollection;
    }

    public void setStudentCourseCollection(Collection<StudentCourse> studentCourseCollection) {
        this.studentCourseCollection = studentCourseCollection;
    }

    @Override
    public int hashCode() {
//        int hash = 0;
//        hash += (iDCourse != null ? iDCourse.hashCode() : 0);
//        return hash;
        return Integer.hashCode(iDCourse);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Course) {
            return ((Course) object).getIDCourse() == this.iDCourse;
        }

        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    public void updateDetails(Object data) {
        if (data instanceof Course) {
            Course course = (Course) data;
            this.name = course.name;
            this.ects = course.ects;
        }
    }

    public void updateDetails(Course data) {
        this.name = data.name;
        this.ects = data.ects;
    }

}
