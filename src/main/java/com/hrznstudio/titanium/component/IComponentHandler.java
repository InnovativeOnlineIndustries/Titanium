package com.hrznstudio.titanium.component;

public interface IComponentHandler<T> {

    void add(T component);

    boolean accepts(Object component);

}
