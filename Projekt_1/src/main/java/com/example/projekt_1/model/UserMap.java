package com.example.projekt_1.model;

import java.util.HashMap;
import java.util.Map;

public class UserMap <K, T extends User> {
    private Map<K, T> users;

    public UserMap() {
        this.users = new HashMap<>();
    }

    public void addUser(K key, T user) {
        users.put(key, user);
    }

    public User getUser(K username){
        return users.get(username);
    }

    public void removeUser(K key) {
        users.remove(key);
    }

    public boolean containsKey(K username){
        return users.containsKey(username);
    }
}
