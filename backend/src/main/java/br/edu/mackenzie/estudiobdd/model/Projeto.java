package br.edu.mackenzie.estudiobdd.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "projetos")
public class Projeto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @Column(nullable = false, unique = true) public String nome;
    @Column(length = 4000) public String descricao;
    @Column(name = "contexto_dominio", length = 20000) public String contextoDominio;
    @CreationTimestamp @Column(updatable = false) public Instant criadoEm;
    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true) public List<HistoriaUsuario> historias = new ArrayList<>();
    protected Projeto() {}
    public Projeto(String nome, String descricao, String contextoDominio) {
        this.nome = nome; this.descricao = descricao; this.contextoDominio = contextoDominio;
    }
}
