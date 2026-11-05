package br.edu.mackenzie.estudiobdd.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;

@Entity @Table(name = "historias", uniqueConstraints = @UniqueConstraint(name="uk_historia_projeto_titulo", columnNames={"projeto_id", "titulo"}))
public class HistoriaUsuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name="projeto_id") public Projeto projeto;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name="autor_id") public Usuario autor;
    @Column(nullable = false) public String titulo;
    @Column(nullable = false) public String papel;
    @Column(nullable = false, length = 2000) public String objetivo;
    @Column(nullable = false, length = 2000) public String beneficio;
    @Enumerated(EnumType.STRING) @Column(nullable = false) public Prioridade prioridade;
    @CreationTimestamp @Column(updatable = false) public Instant criadoEm;
    @UpdateTimestamp public Instant atualizadoEm;
    @OneToMany(mappedBy="historia", cascade=CascadeType.ALL, orphanRemoval=true) @OrderBy("posicao ASC") public List<Cenario> cenarios = new ArrayList<>();
    @OneToMany(mappedBy="historia", cascade=CascadeType.ALL, orphanRemoval=true) public List<Sugestao> sugestoes = new ArrayList<>();
    protected HistoriaUsuario() {}
    public HistoriaUsuario(Projeto projeto, Usuario autor, String titulo, String papel, String objetivo, String beneficio, Prioridade prioridade) {
        this.projeto = projeto; this.autor = autor; this.titulo = titulo; this.papel = papel;
        this.objetivo = objetivo; this.beneficio = beneficio; this.prioridade = prioridade;
    }
}
