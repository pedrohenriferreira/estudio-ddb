package br.edu.mackenzie.estudiobdd.service;

import br.edu.mackenzie.estudiobdd.dto.ApiDtos.*;
import br.edu.mackenzie.estudiobdd.dto.DtoMapper;
import br.edu.mackenzie.estudiobdd.model.HistoriaUsuario;
import br.edu.mackenzie.estudiobdd.repository.HistoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;

@Service
public class HistoriaService {
    private final HistoriaRepository historias;
    private final Referencias refs;
    public HistoriaService(HistoriaRepository historias, Referencias refs) {this.historias=historias;this.refs=refs;}
    @Transactional(readOnly=true)
    public List<HistoriaView> listar(Long projetoId, Prioridade filtro) {
        refs.projeto(projetoId);
        var lista=filtro==null?historias.findByProjetoIdOrderByIdAsc(projetoId):historias.findByProjetoIdAndPrioridadeOrderByIdAsc(projetoId,filtro);
        return lista.stream().map(DtoMapper::historia).toList();
    }
    @Transactional(readOnly=true) public HistoriaView buscar(Long id) {return DtoMapper.historia(refs.historia(id));}
    @Transactional public HistoriaView criar(Long projetoId,Long usuarioId,HistoriaInput input) {
        var projeto=refs.projeto(projetoId);var usuario=refs.usuario(usuarioId);
        if(historias.existsByProjetoIdAndTituloIgnoreCase(projetoId,input.titulo().trim())) throw RegraException.conflito("Título da história já existe neste projeto.");
        var h=new HistoriaUsuario(projeto,usuario,input.titulo().trim(),input.papel().trim(),input.objetivo().trim(),input.beneficio().trim(),input.prioridade());
        return DtoMapper.historia(historias.saveAndFlush(h));
    }
    @Transactional public HistoriaView editar(Long id,Long usuarioId,HistoriaInput input) {
        var h=refs.historia(id);refs.exigirAutor(h,usuarioId);
        if(historias.existsByProjetoIdAndTituloIgnoreCaseAndIdNot(h.projeto.id,input.titulo().trim(),id)) throw RegraException.conflito("Título da história já existe neste projeto.");
        h.titulo=input.titulo().trim();h.papel=input.papel().trim();h.objetivo=input.objetivo().trim();
        h.beneficio=input.beneficio().trim();h.prioridade=input.prioridade();
        return DtoMapper.historia(historias.saveAndFlush(h));
    }
    @Transactional public void excluir(Long id,Long usuarioId) {
        var h=refs.historia(id);refs.exigirAutor(h,usuarioId);historias.delete(h);
    }
}
