package com.example.projekt_1.interfaces;

import com.example.projekt_1.model.User;
import com.example.projekt_1.model.UserMap;

public interface UsersUtilInterface {

    UserMap<String, User> getAllUsers();
}
