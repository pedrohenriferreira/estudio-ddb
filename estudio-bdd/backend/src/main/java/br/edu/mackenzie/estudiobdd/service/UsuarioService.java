package br.edu.mackenzie.estudiobdd.service;

import br.edu.mackenzie.estudiobdd.dto.ApiDtos.*;
import br.edu.mackenzie.estudiobdd.dto.DtoMapper;
import br.edu.mackenzie.estudiobdd.model.Usuario;
import br.edu.mackenzie.estudiobdd.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarios;
    public UsuarioService(UsuarioRepository usuarios) {this.usuarios=usuarios;}
    @Transactional(readOnly=true)
    public List<UsuarioView> listar() {return usuarios.findAll().stream().map(DtoMapper::usuario).toList();}
    @Transactional
    public UsuarioView criar(UsuarioInput input) {
        if(usuarios.existsByEmailIgnoreCase(input.email().trim())) throw RegraException.conflito("E-mail já utilizado.");
        return DtoMapper.usuario(usuarios.saveAndFlush(new Usuario(input.nome().trim(), input.email().trim().toLowerCase())));
    }
}
