package com.example.ex1hellojpa.hellojpa;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
