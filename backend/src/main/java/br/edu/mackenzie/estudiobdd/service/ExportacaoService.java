package br.edu.mackenzie.estudiobdd.service;

import br.edu.mackenzie.estudiobdd.model.Cenario;
import br.edu.mackenzie.estudiobdd.model.Passo;
import br.edu.mackenzie.estudiobdd.repository.CenarioRepository;
import br.edu.mackenzie.estudiobdd.repository.SugestaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;

@Service
public class ExportacaoService {
    private final Referencias refs;
    private final CenarioRepository cenarios;
    private final SugestaoRepository sugestoes;
    public ExportacaoService(Referencias refs,CenarioRepository cenarios,SugestaoRepository sugestoes) {
        this.refs=refs;this.cenarios=cenarios;this.sugestoes=sugestoes;
    }
    private static String linha(String texto) {return texto==null?"":texto.replaceAll("[\\r\\n]+"," ").trim();}
    private static String marcador(TipoPasso tipo) {
        return switch(tipo) {case DADO->"Dado";case QUANDO->"Quando";case ENTAO->"Então";case E->"E";case MAS->"Mas";};
    }
    public static void validarExportacao(List<Cenario> cenarios,boolean pendentes) {
        if(cenarios.isEmpty()) throw RegraException.invalido("Exportação exige ao menos um cenário.");
        if(pendentes) throw RegraException.invalido("Exportação bloqueada: existem sugestões pendentes.");
        for(var c:cenarios) ValidadorCenario.validarEntidades(c.passos);
    }
    @Transactional(readOnly=true) public String exportar(Long historiaId) {
        var h=refs.historia(historiaId);
        var lista=cenarios.findByHistoriaIdOrderByPosicaoAsc(historiaId);
        validarExportacao(lista,sugestoes.existsByHistoriaIdAndDecisao(historiaId,Decisao.PENDENTE));
        StringBuilder sb=new StringBuilder("# language: pt\n\n");
        sb.append("Funcionalidade: ").append(linha(h.titulo)).append("\n");
        sb.append("  Como ").append(linha(h.papel)).append("\n");
        sb.append("  Quero ").append(linha(h.objetivo)).append("\n");
        sb.append("  Para ").append(linha(h.beneficio)).append("\n");
        for(var c:lista) {
            sb.append("\n  Cenário: ").append(linha(c.titulo)).append("\n");
            c.passos.stream().sorted(Comparator.comparingInt(p->p.posicao)).forEach(p->
                    sb.append("    ").append(marcador(p.tipo)).append(" ").append(linha(p.texto)).append("\n"));
        }
        return sb.toString();
    }
}
