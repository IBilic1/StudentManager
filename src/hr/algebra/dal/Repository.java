/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import java.util.List;

/**
 *
 * @author HT-ICT
 */
public interface Repository {
    <T> T add(T t) throws Exception;
    <T> void update(T t,int id) throws Exception;
    <T> void delete(T t) throws Exception;
    <T> T get(int id,Class<T> clazz) throws Exception;
    <T> List<T> get(String namedQuery) throws Exception;
    
    default void relese() throws Exception{};
}
