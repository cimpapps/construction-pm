
package com.cimpapps.construction.pm.service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactorySingleton {

    private static final String PU_NAME = "com.cimpapps_construction-pm-db_jar_1.0-SNAPSHOTPU";
    
    private EntityManagerFactory emf;
    
    private EntityManagerFactorySingleton(){
        emf = Persistence.createEntityManagerFactory(PU_NAME);
    }
    
    private static final class SingletonHolder{
        private static final EntityManagerFactorySingleton INSTANCE = new EntityManagerFactorySingleton();
    }
    
    public static EntityManagerFactorySingleton getInstance(){
        return SingletonHolder.INSTANCE;
    }
    
    public EntityManagerFactory getEntityMangerFactory(){
        return emf;
    }
    
}
