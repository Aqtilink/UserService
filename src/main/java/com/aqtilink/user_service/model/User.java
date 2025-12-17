package com.aqtilink.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String city;

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @JsonIgnore
    private Set<User> friends = new HashSet<>();

    public User(){}

    public User(String firstName, String lastName, String email, Integer age, String city){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.city = city;
    }

    public void setFirstName(String firstName){
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
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
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

    public Set<User> getFriends(){
        return friends;
    }

    public void addFriend(User user) {
        this.friends.add(user);
    }

    public void removeFriend(User user) {
        this.friends.remove(user);
    }
    
}