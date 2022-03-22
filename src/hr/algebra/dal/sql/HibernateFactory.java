/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author HT-ICT
 */
public class HibernateFactory {

    private static final String PERSISTANCE_UNIT = "StudentManagerPU";
    public static final String SELECT_ALL = "Student.findAll";
    public static final String SELECT_ALL_COURSES = "Course.findAll";
    public static final String SELECT_ALL_STUDENT_COURSES= "StudentCourse.findAll";
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory(PERSISTANCE_UNIT);

    public static EntityManagerWrapper getEntitiyManager() {
        return new EntityManagerWrapper(EMF.createEntityManager());
    }

    public static void relese() {
        EMF.close();
    }
}
