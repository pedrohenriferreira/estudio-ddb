package br.edu.mackenzie.estudiobdd.service;

import br.edu.mackenzie.estudiobdd.dto.ApiDtos.DecisaoInput;
import org.junit.jupiter.api.Test;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;
import static org.junit.jupiter.api.Assertions.*;

class SugestaoServiceTest {
    @Test void rejeicaoSemMotivoERejeitada() {
        var entrada=new DecisaoInput(Decisao.REJEITADA,null,"Esta sugestão não serve.");
        var erro=assertThrows(RegraException.class,()->SugestaoService.validarDecisao(entrada));
        assertTrue(erro.getMessage().contains("motivo"));
    }
    @Test void motivoOutroExigeJustificativa() {
        assertThrows(RegraException.class,()->SugestaoService.validarDecisao(new DecisaoInput(Decisao.REJEITADA,MotivoRejeicao.OUTRO,null)));
    }
    @Test void aceiteSemMotivoEPermitido() {
        assertDoesNotThrow(()->SugestaoService.validarDecisao(new DecisaoInput(Decisao.ACEITA,null,null)));
    }
}
