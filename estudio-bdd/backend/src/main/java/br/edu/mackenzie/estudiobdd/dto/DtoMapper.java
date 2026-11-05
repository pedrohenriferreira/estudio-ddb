package br.edu.mackenzie.estudiobdd.dto;

import br.edu.mackenzie.estudiobdd.model.*;
import java.util.Comparator;
import static br.edu.mackenzie.estudiobdd.dto.ApiDtos.*;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;

public final class DtoMapper {
    private DtoMapper() {}
    public static UsuarioView usuario(Usuario u) { return new UsuarioView(u.id,u.nome,u.email); }
    public static ProjetoView projeto(Projeto p) { return new ProjetoView(p.id,p.nome,p.descricao,p.contextoDominio,p.criadoEm); }
    public static HistoriaView historia(HistoriaUsuario h) {
        int pendentes=(int)h.sugestoes.stream().filter(s->s.decisao==Decisao.PENDENTE).count();
        return new HistoriaView(h.id,h.projeto.id,h.autor.id,h.autor.nome,h.titulo,h.papel,h.objetivo,h.beneficio,
                h.prioridade,h.criadoEm,h.atualizadoEm,h.cenarios.size(),pendentes);
    }
    public static PassoView passo(Passo p) { return new PassoView(p.id,p.tipo,p.texto,p.posicao); }
    public static CenarioView cenario(Cenario c) {
        return new CenarioView(c.id,c.historia.id,c.titulo,c.posicao,c.origem,
                c.passos.stream().sorted(Comparator.comparingInt(p->p.posicao)).map(DtoMapper::passo).toList());
    }
    public static SugestaoView sugestao(Sugestao s) {
        return new SugestaoView(s.id,s.historia.id,s.ferramenta,s.prompt,s.conteudo,s.decisao,s.motivoRejeicao,
                s.justificativa,s.registradaPor.id,s.decididaPor==null?null:s.decididaPor.id,s.registradaEm,s.decididaEm);
    }
}
