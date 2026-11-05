package br.edu.mackenzie.estudiobdd.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;

@Entity @Table(name="cenarios", uniqueConstraints=@UniqueConstraint(name="uk_cenario_historia_titulo",columnNames={"historia_id", "titulo"}))
public class Cenario {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="historia_id") public HistoriaUsuario historia;
    @Column(nullable=false) public String titulo;
    @Column(nullable=false) public int posicao;
    @Enumerated(EnumType.STRING) @Column(nullable=false) public OrigemCenario origem;
    @OneToMany(mappedBy="cenario",cascade=CascadeType.ALL,orphanRemoval=true) @OrderBy("posicao ASC") public List<Passo> passos = new ArrayList<>();
    protected Cenario() {}
    public Cenario(HistoriaUsuario historia, String titulo, int posicao, OrigemCenario origem) {
        this.historia = historia; this.titulo = titulo; this.posicao = posicao; this.origem = origem;
    }
}
