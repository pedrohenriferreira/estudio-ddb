package br.edu.mackenzie.estudiobdd.model;

import jakarta.persistence.*;

@Entity @Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @Column(nullable = false) public String nome;
    @Column(nullable = false, unique = true) public String email;
    protected Usuario() {}
    public Usuario(String nome, String email) { this.nome = nome; this.email = email; }
}
