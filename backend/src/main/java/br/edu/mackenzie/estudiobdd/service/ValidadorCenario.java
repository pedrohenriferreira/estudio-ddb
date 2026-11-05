package br.edu.mackenzie.estudiobdd.service;

import br.edu.mackenzie.estudiobdd.dto.ApiDtos.PassoInput;
import br.edu.mackenzie.estudiobdd.model.Passo;
import java.util.*;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;

public final class ValidadorCenario {
    private ValidadorCenario() {}
    public static void validar(List<PassoInput> passos) {
        if (passos == null || passos.isEmpty()) throw RegraException.invalido("Cenário precisa de passos.");
        Set<Integer> posicoes=new HashSet<>();
        var ordenados=new ArrayList<>(passos);
        for(var p:ordenados) {
            if(p==null || p.tipo()==null || p.texto()==null || p.texto().isBlank() || p.posicao()==null || p.posicao()<1)
                throw RegraException.invalido("Cada passo precisa de tipo, texto e posição positiva.");
            if(!posicoes.add(p.posicao())) throw RegraException.invalido("Posições de passos não podem se repetir.");
        }
        ordenados.sort(Comparator.comparingInt(PassoInput::posicao));
        int fase=0; boolean temQuando=false,temEntao=false,comecou=false;
        for(var passo:ordenados) {
            TipoPasso tipo=passo.tipo();
            if(tipo==TipoPasso.E || tipo==TipoPasso.MAS) {
                if(!comecou) throw RegraException.invalido("Passos E e MAS não podem iniciar cenário.");
                continue;
            }
            comecou=true;
            switch(tipo) {
                case DADO -> {
                    if(fase!=0) throw RegraException.invalido("A ordem obrigatória é Dado → Quando → Então.");
                }
                case QUANDO -> {
                    if(fase==2) throw RegraException.invalido("QUANDO não pode aparecer após ENTÃO.");
                    fase=1;temQuando=true;
                }
                case ENTAO -> {
                    if(!temQuando) throw RegraException.invalido("ENTÃO deve vir depois de QUANDO.");
                    fase=2;temEntao=true;
                }
                default -> throw RegraException.invalido("Tipo de passo inválido.");
            }
        }
        if(!temQuando || !temEntao) throw RegraException.invalido("Cenário deve ter ao menos um QUANDO e um ENTÃO.");
    }
    public static void validarEntidades(List<Passo> passos) {
        validar(passos.stream().map(p->new PassoInput(p.tipo,p.texto,p.posicao)).toList());
    }
}
