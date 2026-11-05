package br.edu.mackenzie.estudiobdd.service;

import br.edu.mackenzie.estudiobdd.dto.ApiDtos.DecisaoInput;
import br.edu.mackenzie.estudiobdd.model.*;
import br.edu.mackenzie.estudiobdd.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.util.*;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegrasComMocksTest {
    @Mock UsuarioRepository usuarios;
    @Mock ProjetoRepository projetos;
    @Mock HistoriaRepository historias;
    @Mock CenarioRepository cenarios;
    @Mock PassoRepository passos;
    @Mock SugestaoRepository sugestoes;

    Referencias refs() {return new Referencias(usuarios,projetos,historias,cenarios,passos,sugestoes);}

    @Test void autorDiferenteNaoPodeEditarHistoria() {
        var autor=new Usuario("Ana","ana@example.com");autor.id=1L;
        var outro=new Usuario("Bruno","bruno@example.com");outro.id=2L;
        var projeto=new Projeto("Biblioteca", "", "");projeto.id=1L;
        var historia=new HistoriaUsuario(projeto,autor,"Reserva","leitor","reservar","ler",Prioridade.ALTA);historia.id=3L;
        when(usuarios.findById(2L)).thenReturn(Optional.of(outro));
        var exc=assertThrows(RegraException.class,()->refs().exigirAutor(historia,2L));
        assertEquals(HttpStatus.FORBIDDEN,exc.status);
    }

    @Test void decisaoJaTomadaNaoPodeSerAlterada() {
        var sug=new Sugestao(null,null,"ChatGPT","prompt","texto");sug.decisao=Decisao.REJEITADA;
        when(sugestoes.findById(7L)).thenReturn(Optional.of(sug));
        var service=new SugestaoService(sugestoes,refs());
        var exc=assertThrows(RegraException.class,()->service.decidir(7L,1L,new DecisaoInput(Decisao.ACEITA,null,null)));
        assertEquals(HttpStatus.CONFLICT,exc.status);
    }

    @Test void exportacaoConsultaPendenciasNoRepositorioEBloqueia() {
        var autor=new Usuario("Ana","ana@example.com");autor.id=1L;
        var projeto=new Projeto("Biblioteca", "", "");projeto.id=1L;
        var h=new HistoriaUsuario(projeto,autor,"Reservar livro","leitor","reservar","retirar",Prioridade.ALTA);h.id=5L;
        var c=new Cenario(h,"Reserva feita",1,OrigemCenario.HUMANO);
        c.passos.add(new Passo(c,TipoPasso.QUANDO,"peço reserva",1));
        c.passos.add(new Passo(c,TipoPasso.ENTAO,"reserva registrada",2));
        when(historias.findById(5L)).thenReturn(Optional.of(h));
        when(cenarios.findByHistoriaIdOrderByPosicaoAsc(5L)).thenReturn(List.of(c));
        when(sugestoes.existsByHistoriaIdAndDecisao(5L,Decisao.PENDENTE)).thenReturn(true);
        var service=new ExportacaoService(refs(),cenarios,sugestoes);
        var exc=assertThrows(RegraException.class,()->service.exportar(5L));
        assertTrue(exc.getMessage().contains("pendentes"));
    }

    @Test void exportacaoGeraGherkinPortuguesSemPendencias() {
        var autor=new Usuario("Ana","ana@example.com");autor.id=1L;
        var projeto=new Projeto("Biblioteca", "", "");projeto.id=1L;
        var h=new HistoriaUsuario(projeto,autor,"Reservar livro","leitor","reservar","retirar",Prioridade.ALTA);h.id=5L;
        var c=new Cenario(h,"Reserva feita",1,OrigemCenario.HUMANO);
        c.passos.add(new Passo(c,TipoPasso.QUANDO,"peço reserva",1));
        c.passos.add(new Passo(c,TipoPasso.ENTAO,"reserva registrada",2));
        when(historias.findById(5L)).thenReturn(Optional.of(h));
        when(cenarios.findByHistoriaIdOrderByPosicaoAsc(5L)).thenReturn(List.of(c));
        var texto=new ExportacaoService(refs(),cenarios,sugestoes).exportar(5L);
        assertTrue(texto.startsWith("# language: pt"));
        assertTrue(texto.contains("Funcionalidade: Reservar livro"));
        assertTrue(texto.contains("Cenário: Reserva feita"));
        assertTrue(texto.contains("Quando peço reserva"));
        assertTrue(texto.contains("Então reserva registrada"));
    }
}
