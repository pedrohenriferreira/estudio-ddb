package br.edu.mackenzie.estudiobdd.config;

import br.edu.mackenzie.estudiobdd.model.*;
import br.edu.mackenzie.estudiobdd.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;

@Configuration @Profile("demo")
public class DadosDemo {
    @Bean CommandLineRunner carregarDemo(UsuarioRepository usuarios,ProjetoRepository projetos,HistoriaRepository historias,
                                         CenarioRepository cenarios,SugestaoRepository sugestoes) {
        return args -> {
            if(projetos.count()>0 || usuarios.count()>0) return; // Não duplica em reinicializações.
            Usuario ana=usuarios.save(new Usuario("Ana Analista","ana@example.com"));
            Usuario bruno=usuarios.save(new Usuario("Bruno Revisor","bruno@example.com"));
            Projeto projeto=projetos.save(new Projeto("Biblioteca digital", "Empréstimos, reservas e catálogo de livros", "Leitor: usuário que consulta livros. Exemplar: cópia física. Reserva: solicitação de empréstimo."));
            HistoriaUsuario h=historias.save(new HistoriaUsuario(projeto,ana,"Reservar livro disponível", "leitor autenticado",
                    "reservar um livro do catálogo", "retirá-lo posteriormente",Prioridade.ALTA));
            Cenario c=new Cenario(h,"Reserva realizada com sucesso",1,OrigemCenario.HUMANO);
            c.passos.add(new Passo(c,TipoPasso.DADO,"que existe um exemplar disponível",1));
            c.passos.add(new Passo(c,TipoPasso.QUANDO,"o leitor solicita a reserva",2));
            c.passos.add(new Passo(c,TipoPasso.ENTAO,"a reserva é registrada",3));
            cenarios.save(c);
            sugestoes.save(new Sugestao(h,ana,"Assistente de IA", "Sugira cenários alternativos para reserva de livros", "Adicionar cenário de livro indisponível."));
            historias.save(new HistoriaUsuario(projeto,bruno,"Consultar histórico de empréstimos", "leitor autenticado",
                    "visualizar meus empréstimos", "acompanhar devoluções",Prioridade.MEDIA));
        };
    }
}
