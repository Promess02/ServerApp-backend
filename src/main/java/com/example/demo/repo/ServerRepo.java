package com.example.demo.repo;

import com.example.demo.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepo extends JpaRepository<Server, Long> {
    // JPA korzysta ze schematu nazw metod typu findBy... i zwraca Server dla podanego
    //adresu IP. Powinno się używać tylko dla unikalnych kolumn - adres IP unikalny dla każdego rekordu
    Server findByIpAddress(String ipAddress);


}
