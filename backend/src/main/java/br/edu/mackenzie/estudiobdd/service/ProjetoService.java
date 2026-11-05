package br.edu.mackenzie.estudiobdd.service;

import br.edu.mackenzie.estudiobdd.dto.ApiDtos.*;
import br.edu.mackenzie.estudiobdd.dto.DtoMapper;
import br.edu.mackenzie.estudiobdd.model.Projeto;
import br.edu.mackenzie.estudiobdd.repository.ProjetoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProjetoService {
    private final ProjetoRepository projetos;
    private final Referencias refs;
    public ProjetoService(ProjetoRepository projetos, Referencias refs) {this.projetos=projetos;this.refs=refs;}
    @Transactional(readOnly=true) public List<ProjetoView> listar() {return projetos.findAll().stream().map(DtoMapper::projeto).toList();}
    @Transactional(readOnly=true) public ProjetoView buscar(Long id) {return DtoMapper.projeto(refs.projeto(id));}
    @Transactional public ProjetoView criar(ProjetoInput input) {
        if(projetos.existsByNomeIgnoreCase(input.nome().trim())) throw RegraException.conflito("Nome de projeto já utilizado.");
        return DtoMapper.projeto(projetos.saveAndFlush(new Projeto(input.nome().trim(),input.descricao(),input.contextoDominio())));
    }
    @Transactional public ProjetoView editar(Long id,ProjetoInput input) {
        Projeto p=refs.projeto(id);
        if(!p.nome.equalsIgnoreCase(input.nome().trim())&&projetos.existsByNomeIgnoreCase(input.nome().trim()))
            throw RegraException.conflito("Nome de projeto já utilizado.");
        p.nome=input.nome().trim();p.descricao=input.descricao();p.contextoDominio=input.contextoDominio();
        return DtoMapper.projeto(projetos.saveAndFlush(p));
    }
    @Transactional public void excluir(Long id) {
        var p=refs.projeto(id);
        // Impede que a exclusão do projeto contorne a regra de autoria das histórias.
        if(!p.historias.isEmpty()) throw RegraException.conflito("Para excluir o projeto, remova antes suas histórias com os respectivos autores.");
        projetos.delete(p);
    }
}
