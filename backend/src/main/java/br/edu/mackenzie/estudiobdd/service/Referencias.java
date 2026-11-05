package br.edu.mackenzie.estudiobdd.service;

import br.edu.mackenzie.estudiobdd.model.*;
import br.edu.mackenzie.estudiobdd.repository.*;
import org.springframework.stereotype.Component;

@Component
public class Referencias {
    private final UsuarioRepository usuarios;
    private final ProjetoRepository projetos;
    private final HistoriaRepository historias;
    private final CenarioRepository cenarios;
    private final PassoRepository passos;
    private final SugestaoRepository sugestoes;
    public Referencias(UsuarioRepository usuarios,ProjetoRepository projetos,HistoriaRepository historias,
                       CenarioRepository cenarios,PassoRepository passos,SugestaoRepository sugestoes) {
        this.usuarios=usuarios;this.projetos=projetos;this.historias=historias;
        this.cenarios=cenarios;this.passos=passos;this.sugestoes=sugestoes;
    }
    public Usuario usuario(Long id) {return usuarios.findById(id).orElseThrow(()->RegraException.naoEncontrado("Usuário"));}
    public Projeto projeto(Long id) {return projetos.findById(id).orElseThrow(()->RegraException.naoEncontrado("Projeto"));}
    public HistoriaUsuario historia(Long id) {return historias.findById(id).orElseThrow(()->RegraException.naoEncontrado("História"));}
    public Cenario cenario(Long id) {return cenarios.findById(id).orElseThrow(()->RegraException.naoEncontrado("Cenário"));}
    public Passo passo(Long id) {return passos.findById(id).orElseThrow(()->RegraException.naoEncontrado("Passo"));}
    public Sugestao sugestao(Long id) {return sugestoes.findById(id).orElseThrow(()->RegraException.naoEncontrado("Sugestão"));}
    public void exigirAutor(HistoriaUsuario h, Long idUsuario) {
        usuario(idUsuario);
        if (!h.autor.id.equals(idUsuario)) throw RegraException.proibido("Somente o autor pode alterar ou excluir a história e seus cenários.");
    }
}
