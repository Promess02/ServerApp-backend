package com.example.demo.repo;

import com.example.demo.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

// custom repo interface must inherit after JpaRepository<T,V> where
// T-entity, V - primary Key type
public interface ServerRepo extends JpaRepository<Server, Long> {
    // JPA implements the method naming scheme - ReturnType findBy[FieldName](fieldType fieldName) and finds the object
    //with given field object. Works only with given objects with unique values
    Server findByIpAddress(String ipAddress);


}
