package br.edu.mackenzie.estudiobdd.service;

import br.edu.mackenzie.estudiobdd.model.Cenario;
import br.edu.mackenzie.estudiobdd.model.Passo;
import org.junit.jupiter.api.Test;
import java.util.List;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;
import static org.junit.jupiter.api.Assertions.*;

class ExportacaoServiceTest {
    private Cenario valido() {
        var c=new Cenario(null,"Reserva realizada",1,OrigemCenario.HUMANO);
        c.passos.add(new Passo(c,TipoPasso.DADO,"um livro disponível",1));
        c.passos.add(new Passo(c,TipoPasso.QUANDO,"reservo o livro",2));
        c.passos.add(new Passo(c,TipoPasso.ENTAO,"reserva confirmada",3));
        return c;
    }
    @Test void bloqueiaExportacaoComSugestaoPendente() {
        var erro=assertThrows(RegraException.class,()->ExportacaoService.validarExportacao(List.of(valido()),true));
        assertTrue(erro.getMessage().contains("pendentes"));
    }
    @Test void bloqueiaExportacaoSemCenarios() {
        assertThrows(RegraException.class,()->ExportacaoService.validarExportacao(List.of(),false));
    }
    @Test void permiteExportacaoComCenarioValidoESemPendencias() {
        assertDoesNotThrow(()->ExportacaoService.validarExportacao(List.of(valido()),false));
    }
}
