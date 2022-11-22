package com.example.ex1hellojpa.hellojpa;

import javax.persistence.Entity;

@Entity
public class Book extends Item{
    private String author;
    private String isbn;
}
