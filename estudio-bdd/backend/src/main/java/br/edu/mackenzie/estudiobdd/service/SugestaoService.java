package br.edu.mackenzie.estudiobdd.service;

import br.edu.mackenzie.estudiobdd.dto.ApiDtos.*;
import br.edu.mackenzie.estudiobdd.dto.DtoMapper;
import br.edu.mackenzie.estudiobdd.model.Sugestao;
import br.edu.mackenzie.estudiobdd.repository.SugestaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;

@Service
public class SugestaoService {
    private final SugestaoRepository sugestoes;
    private final Referencias refs;
    public SugestaoService(SugestaoRepository sugestoes, Referencias refs) {this.sugestoes=sugestoes;this.refs=refs;}
    @Transactional(readOnly=true) public List<SugestaoView> listar(Long historiaId, Decisao decisao) {
        refs.historia(historiaId);
        var lista=decisao==null?sugestoes.findByHistoriaIdOrderByIdAsc(historiaId):sugestoes.findByHistoriaIdAndDecisaoOrderByIdAsc(historiaId,decisao);
        return lista.stream().map(DtoMapper::sugestao).toList();
    }
    @Transactional public SugestaoView registrar(Long historiaId, Long usuarioId, SugestaoInput input) {
        var h=refs.historia(historiaId);var u=refs.usuario(usuarioId);
        var s=new Sugestao(h,u,input.ferramenta().trim(),input.prompt().trim(),input.conteudo().trim());
        return DtoMapper.sugestao(sugestoes.saveAndFlush(s));
    }
    public static void validarDecisao(DecisaoInput input) {
        if(input==null || input.decisao()==null || input.decisao()==Decisao.PENDENTE)
            throw RegraException.invalido("A decisão deve ser ACEITA ou REJEITADA.");
        if(input.decisao()==Decisao.REJEITADA && input.motivoRejeicao()==null)
            throw RegraException.invalido("Rejeição exige motivo.");
        if(input.decisao()==Decisao.REJEITADA && input.motivoRejeicao()==MotivoRejeicao.OUTRO
                && (input.justificativa()==null||input.justificativa().isBlank()))
            throw RegraException.invalido("Informe a justificativa quando o motivo for OUTRO.");
        if(input.decisao()==Decisao.ACEITA && input.motivoRejeicao()!=null)
            throw RegraException.invalido("Sugestão aceita não pode ter motivo de rejeição.");
    }
    @Transactional public SugestaoView decidir(Long sugestaoId,Long usuarioId,DecisaoInput input) {
        var s=refs.sugestao(sugestaoId);
        if(s.decisao!=Decisao.PENDENTE) throw RegraException.conflito("Sugestão já decidida não pode ser alterada.");
        validarDecisao(input);
        s.decisao=input.decisao();s.motivoRejeicao=input.motivoRejeicao();
        s.justificativa=input.justificativa()==null?null:input.justificativa().trim();
        s.decididaPor=refs.usuario(usuarioId);s.decididaEm=Instant.now();
        // Aceitar não cria cenários nem altera passos automaticamente.
        return DtoMapper.sugestao(sugestoes.saveAndFlush(s));
    }
}
