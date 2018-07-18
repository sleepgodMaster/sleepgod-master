package com.sleepgod.sleepgod.eventbus;

/**
 * Created by cool on 2018/7/12.
 */
public class Man {
    public String name;
    public int age;

    public Man(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Man{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
