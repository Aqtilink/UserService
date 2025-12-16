package com.aqtilink.user_service.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private Integer age;
    private String city;

    public User(){}

    public User(String firstName, String lastName, String email, Integer age, String city){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.city = city;
    }

    public void setfirstName(String firstName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setAge(Integer age){
        this.age = age;
    }
    public void setCity(String city){
        this.city = city;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public UUID getId(){
        return id;
    }
    public String getfirstName(){
        return firstName;
    }
    public String getlastName(){
        return lastName;
    }
    public Integer getAge(){
        return age;
    }
    public String getCity(){
        return city;
    }
    public String getEmail(){
        return email;
    }
    
}