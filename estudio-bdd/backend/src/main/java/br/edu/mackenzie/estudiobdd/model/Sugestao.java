package br.edu.mackenzie.estudiobdd.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;

@Entity @Table(name="sugestoes")
public class Sugestao {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="historia_id") public HistoriaUsuario historia;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="registrada_por_id") public Usuario registradaPor;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="decidida_por_id") public Usuario decididaPor;
    @Column(nullable=false) public String ferramenta;
    @Column(nullable=false,length=10000) public String prompt;
    @Column(nullable=false,length=20000) public String conteudo;
    @Enumerated(EnumType.STRING) @Column(nullable=false) public Decisao decisao = Decisao.PENDENTE;
    @Enumerated(EnumType.STRING) public MotivoRejeicao motivoRejeicao;
    @Column(length=4000) public String justificativa;
    @CreationTimestamp @Column(updatable=false) public Instant registradaEm;
    public Instant decididaEm;
    protected Sugestao() {}
    public Sugestao(HistoriaUsuario historia, Usuario registradaPor, String ferramenta, String prompt, String conteudo) {
        this.historia=historia; this.registradaPor=registradaPor; this.ferramenta=ferramenta; this.prompt=prompt; this.conteudo=conteudo;
    }
}
