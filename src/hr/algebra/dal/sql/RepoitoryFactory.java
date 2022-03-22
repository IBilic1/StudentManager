/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.utilities.HibernateRepository;
import hr.algebra.dal.Repository;

/**
 *
 * @author HT-ICT
 */
public class RepoitoryFactory {

    private RepoitoryFactory() {
    }
    
    private static final Repository REPOSITORY= new HibernateRepository();
    
    public static Repository getRepository(){
        return REPOSITORY;
    }
    
}
