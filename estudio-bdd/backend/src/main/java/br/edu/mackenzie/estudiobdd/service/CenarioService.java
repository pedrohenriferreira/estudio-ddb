package br.edu.mackenzie.estudiobdd.service;

import br.edu.mackenzie.estudiobdd.dto.ApiDtos.*;
import br.edu.mackenzie.estudiobdd.dto.DtoMapper;
import br.edu.mackenzie.estudiobdd.model.*;
import br.edu.mackenzie.estudiobdd.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class CenarioService {
    private final CenarioRepository cenarios;
    private final PassoRepository passos;
    private final Referencias refs;
    public CenarioService(CenarioRepository cenarios,PassoRepository passos,Referencias refs) {
        this.cenarios=cenarios;this.passos=passos;this.refs=refs;
    }
    @Transactional(readOnly=true) public List<CenarioView> listar(Long historiaId) {
        refs.historia(historiaId);
        return cenarios.findByHistoriaIdOrderByPosicaoAsc(historiaId).stream().map(DtoMapper::cenario).toList();
    }
    @Transactional(readOnly=true) public CenarioView buscar(Long id) {return DtoMapper.cenario(refs.cenario(id));}
    private void verificarPosicaoCenario(Long historiaId,Long ignorarId,int posicao) {
        if(cenarios.findByHistoriaIdOrderByPosicaoAsc(historiaId).stream()
                .anyMatch(c->c.posicao==posicao && !Objects.equals(c.id,ignorarId)))
            throw RegraException.conflito("Já existe um cenário nesta posição. Reordene os cenários sem repetir posições.");
    }
    @Transactional public CenarioView criar(Long historiaId,Long usuarioId,CenarioInput input) {
        var historia=refs.historia(historiaId);refs.exigirAutor(historia,usuarioId);
        ValidadorCenario.validar(input.passos());
        if(cenarios.existsByHistoriaIdAndTituloIgnoreCase(historiaId,input.titulo().trim())) throw RegraException.conflito("Título do cenário já existe nesta história.");
        verificarPosicaoCenario(historiaId,null,input.posicao());
        var c=new Cenario(historia,input.titulo().trim(),input.posicao(),input.origem());
        input.passos().stream().sorted(Comparator.comparingInt(PassoInput::posicao))
                .forEach(p->c.passos.add(new Passo(c,p.tipo(),p.texto().trim(),p.posicao())));
        return DtoMapper.cenario(cenarios.saveAndFlush(c));
    }
    @Transactional public CenarioView editar(Long id,Long usuarioId,CenarioInput input) {
        var c=refs.cenario(id);refs.exigirAutor(c.historia,usuarioId);
        ValidadorCenario.validar(input.passos());
        if(cenarios.existsByHistoriaIdAndTituloIgnoreCaseAndIdNot(c.historia.id,input.titulo().trim(),id)) throw RegraException.conflito("Título do cenário já existe nesta história.");
        verificarPosicaoCenario(c.historia.id,id,input.posicao());
        c.titulo=input.titulo().trim();c.posicao=input.posicao();c.origem=input.origem();
        // Substituição atômica da coleção; alterações individuais usam endpoints de passos.
        c.passos.clear();
        input.passos().stream().sorted(Comparator.comparingInt(PassoInput::posicao))
                .forEach(p->c.passos.add(new Passo(c,p.tipo(),p.texto().trim(),p.posicao())));
        return DtoMapper.cenario(cenarios.saveAndFlush(c));
    }
    @Transactional public void excluir(Long id,Long usuarioId) {
        var c=refs.cenario(id);refs.exigirAutor(c.historia,usuarioId);cenarios.delete(c);
    }
    @Transactional(readOnly=true) public List<PassoView> listarPassos(Long cenarioId) {
        return refs.cenario(cenarioId).passos.stream().sorted(Comparator.comparingInt(p->p.posicao)).map(DtoMapper::passo).toList();
    }
    private void validarProposta(List<Passo> candidatos) {ValidadorCenario.validarEntidades(candidatos);}
    @Transactional public PassoView adicionarPasso(Long cenarioId,Long usuarioId,PassoInput input) {
        var c=refs.cenario(cenarioId);refs.exigirAutor(c.historia,usuarioId);
        var candidato=new Passo(c,input.tipo(),input.texto().trim(),input.posicao());
        var proposta=new ArrayList<>(c.passos);proposta.add(candidato);validarProposta(proposta);
        c.passos.add(candidato);cenarios.saveAndFlush(c);return DtoMapper.passo(candidato);
    }
    @Transactional public PassoView editarPasso(Long id,Long usuarioId,PassoInput input) {
        var alvo=refs.passo(id);var c=alvo.cenario;refs.exigirAutor(c.historia,usuarioId);
        var proposta=new ArrayList<Passo>();
        for(var p:c.passos) proposta.add(Objects.equals(p.id,id)
                ?new Passo(c,input.tipo(),input.texto().trim(),input.posicao()):p);
        validarProposta(proposta);
        alvo.tipo=input.tipo();alvo.texto=input.texto().trim();alvo.posicao=input.posicao();
        return DtoMapper.passo(passos.saveAndFlush(alvo));
    }
    @Transactional public void excluirPasso(Long id,Long usuarioId) {
        var alvo=refs.passo(id);var c=alvo.cenario;refs.exigirAutor(c.historia,usuarioId);
        var proposta=c.passos.stream().filter(p->!Objects.equals(p.id,id)).toList();validarProposta(proposta);
        c.passos.removeIf(p->Objects.equals(p.id,id));cenarios.saveAndFlush(c);
    }
}
