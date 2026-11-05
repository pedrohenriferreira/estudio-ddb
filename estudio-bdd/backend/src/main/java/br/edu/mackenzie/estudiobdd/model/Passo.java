package br.edu.mackenzie.estudiobdd.model;

import jakarta.persistence.*;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;

@Entity @Table(name="passos")
public class Passo {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="cenario_id") public Cenario cenario;
    @Enumerated(EnumType.STRING) @Column(nullable=false) public TipoPasso tipo;
    @Column(nullable=false,length=2000) public String texto;
    @Column(nullable=false) public int posicao;
    protected Passo() {}
    public Passo(Cenario cenario, TipoPasso tipo, String texto, int posicao) {
        this.cenario = cenario; this.tipo = tipo; this.texto = texto; this.posicao = posicao;
    }
}
