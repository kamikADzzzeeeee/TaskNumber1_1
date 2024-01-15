package ru.yamshikov.servlet.rest_servlet_example.logic;

import lombok.Getter;

import java.io.Serializable;
import java.util.*;

public class Model implements Serializable {

    @Getter
    private static final Model instance = new Model();
    private final Map<Integer, User> model;

    private Model(){
        model = new HashMap<>();
        model.put(1,new User("Denis","Yamshikov", 60000D));
        model.put(2,new User("Aliya","Valeeva", 77010D));
        model.put(3,new User("Artem","Kardashyan", 60500D));
        model.put(4,new User("Bulat","Belgin", 65000D));
        model.put(5,new User("Valentin","Strykalo", 10100D));

    }

    public void add(Integer userId, User user){
        model.put(userId,user);
    }

    public Map<Integer, User> getAllUser(){
        return model;
    }


    public int getCountUsers(){
        return model.size();
    }

    public User getUserById(Integer userId){
        return model.get(userId);
    }

    public void removeUserById(Integer userId){
        model.remove(userId);
    }

    public void removeAllUsers(){
        model.clear();
    }

    public User updateUserById(Integer userId, User newUser){
        model.replace(userId, newUser);
        return model.get(userId);
    }

    public boolean findUserById(Integer userId){
        return model.containsKey(userId);
    }

}
