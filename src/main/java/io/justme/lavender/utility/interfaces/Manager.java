package io.justme.lavender.utility.interfaces;

import lombok.Getter;

import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class Manager<T> {

    protected CopyOnWriteArrayList<T> elements;

    public Manager(CopyOnWriteArrayList<T> elements) {
        this.elements = elements;
    }

    public Manager() {
        this.elements = new CopyOnWriteArrayList<>();
    }

}
