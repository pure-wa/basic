//package com.beyond.basic.b2_board.author.domain;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
//@Entity
//@Builder
//public class Address {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String city;
//    private String street;
//    private String zipCode;
//    @Column(unique = true)
//    @JoinColumn(name = "author_id")
//    @OneToOne(fetch = FetchType.LAZY)
//    private Author author;
//}
