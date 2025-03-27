package com.example.projekt_1.interfaces;

import com.example.projekt_1.model.ScientificWork;

public sealed interface ChangeTracker permits ScientificWork {

    String printChange();
}
