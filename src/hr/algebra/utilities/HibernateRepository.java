/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utilities;

import hr.algebra.dal.Repository;
import hr.algebra.dal.sql.EntityManagerWrapper;
import hr.algebra.dal.sql.HibernateFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import javax.persistence.EntityManager;

public class HibernateRepository implements Repository {

    @Override
    public <T> T add(T t) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntitiyManager()) {
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            Class<?> aClass = t.getClass();
            Constructor<?> declaredConstructor = aClass.getDeclaredConstructor(new Class[]{Object.class});
            T l=(T)declaredConstructor.newInstance(new Object[]{t});
            
            em.persist(l);
            em.getTransaction().commit();
            return l;
        }
    }

    @Override
    public <T> void update(T t, int id) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntitiyManager()) {
            EntityManager em = wrapper.get();
            em.getTransaction().begin();

            Object targetObject = em.find(t.getClass(), id);
            updateDetils(targetObject, t);

            em.getTransaction().commit();
        }
    }

    @Override
    public <T> void delete(T t) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntitiyManager()) {
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            em.remove(em.contains(t) ? t : em.merge(t));
            em.getTransaction().commit();
        }
    }

    @Override
    public <T> T get(int id, Class<T> clazz) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntitiyManager()) {
            EntityManager em = wrapper.get();
            return (T) em.find(clazz.getClass(), id);
        }
    }

    @Override
    public <T> List<T> get(String namedQuery) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntitiyManager()) {
            EntityManager em = wrapper.get();
            return (List<T>) em.createNamedQuery(namedQuery).getResultList();
        }
    }

    @Override
    public void relese() throws Exception {
        HibernateFactory.relese();
    }

    private void updateDetils(Class find, Class clazz) throws IllegalAccessException {
        // 1. zabiljezi mi sve propertye
        List<Field> fields = ReflectionUtils.getFields(find);
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            field.setAccessible(true);
            System.out.println(clazz);
            System.out.println("111111111111111111111111111111111111111111111111111111111111111");
            field.set(find, field.get(clazz));
        }
        // 2. svakom propertiu promijeni vrijednost i tjt
    }

    private <T> void updateDetils(Object targetObject, T t) throws Exception {
        List<Field> fields = hr.algebra.utilities.ReflectionUtils.getFields(targetObject.getClass());
        List<Field> Ttt = hr.algebra.utilities.ReflectionUtils.getFields(t.getClass());
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            Field field2 = Ttt.get(i);
            if (!field.getName().toLowerCase().contains("id")) {
                
                field.setAccessible(true);
                field2.setAccessible(true);
                field.set(targetObject, field2.get(t));
            }
        }
    }


}
