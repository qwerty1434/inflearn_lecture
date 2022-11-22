package com.example.ex1hellojpa.hellojpa;

import javax.persistence.Entity;

@Entity
public class Movie extends Item{
    private String director;
    private String actor;
}
