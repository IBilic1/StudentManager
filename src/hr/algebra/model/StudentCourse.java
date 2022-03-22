/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HT-ICT
 */
@Entity
@Table(name = "StudentCourse")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentCourse.findAll", query = "SELECT s FROM StudentCourse s")
    , @NamedQuery(name = "StudentCourse.findByIDStudentCourse", query = "SELECT s FROM StudentCourse s WHERE s.iDStudentCourse = :iDStudentCourse")})
public class StudentCourse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDStudentCourse")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer iDStudentCourse;
    @JoinColumn(name = "CourseID", referencedColumnName = "IDCourse")
    @ManyToOne
    private Course courseID;
    @JoinColumn(name = "StudentID", referencedColumnName = "IDStudent")
    @ManyToOne
    private Student studentID;

    public StudentCourse(Course courseID, Student studentID) {
        this.courseID = courseID;
        this.studentID = studentID;
    }

    public StudentCourse(Object data) {
        updateDetails(data);
    }

    public StudentCourse() {
    }

    public StudentCourse(Integer iDStudentCourse) {
        this.iDStudentCourse = iDStudentCourse;
    }

    public Integer getIDStudentCourse() {
        return iDStudentCourse;
    }

    public void setIDStudentCourse(Integer iDStudentCourse) {
        this.iDStudentCourse = iDStudentCourse;
    }

    public Course getCourseID() {
        return courseID;
    }

    public void setCourseID(Course courseID) {
        this.courseID = courseID;
    }

    public Student getStudentID() {
        return studentID;
    }

    public void setStudentID(Student studentID) {
        this.studentID = studentID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.getStudentID().getIDStudent() * this.getCourseID().getIDCourse() + 15);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof StudentCourse) {
            return ((StudentCourse) object).getStudentID().getIDStudent() == this.getStudentID().getIDStudent()
                    && ((StudentCourse) object).getCourseID().getIDCourse() == this.getCourseID().getIDCourse();
        }
        // za svaki slucaj ovo necu brisati
        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof StudentCourse)) {
//            return false;
//        }
//        StudentCourse other = (StudentCourse) object;
//        if ((this.iDStudentCourse == null && other.iDStudentCourse != null) || (this.iDStudentCourse != null && !this.iDStudentCourse.equals(other.iDStudentCourse))) {
//            return false;
//        }
//        return true;
        return false;
    }

    @Override
    public String toString() {
        return courseID.getName();
    }

    private void updateDetails(Object data) {
        if (data instanceof StudentCourse) {
            StudentCourse sc = (StudentCourse) data;
            this.courseID=sc.courseID;
            this.studentID=sc.studentID;
        }
    }

}
