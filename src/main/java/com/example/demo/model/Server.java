package com.example.demo.model;
import com.example.demo.enumeration.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.AUTO;

// data model of an SQL entity
@Entity
//setters, getters, toString etc.
@Data
//generates a constructor with no arguments
@NoArgsConstructor
//generates a constructor with all arguments in a class
@AllArgsConstructor
public class Server {
    //annotations ensure that JPA treats the id field as a primary key in an SQL table, @Generated value ensures that after adding new records this field will
    // have automatically assigned values
    @Id @GeneratedValue(strategy = AUTO)
    private Long id;
    // ensures that ipAddress field will be treated as a column with unique values in the table
    @Column(unique = true)
    // throws exception when the value in ipAddress is empty or null
    @NotEmpty(message = "IP Address cannot be empty or null")
    private String ipAddress;
    private String name;
    private String memory;
    private String type;
    private String imageURL;
    private Status status;
}
