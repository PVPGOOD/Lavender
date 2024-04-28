package io.justme.lavender.utility.interfaces;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Manager<T> {

    protected List<T> elements;

    public Manager(List<T> elements) {
        this.elements = elements;
    }

    public Manager() {
        this.elements = new ArrayList<>();
    }

}
