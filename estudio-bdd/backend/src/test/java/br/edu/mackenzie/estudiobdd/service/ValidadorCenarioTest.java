package br.edu.mackenzie.estudiobdd.service;

import br.edu.mackenzie.estudiobdd.dto.ApiDtos.PassoInput;
import org.junit.jupiter.api.Test;
import java.util.List;
import static br.edu.mackenzie.estudiobdd.model.Enums.TipoPasso.*;
import static org.junit.jupiter.api.Assertions.*;

class ValidadorCenarioTest {
    @Test void cenarioSemEntaoERejeitado() {
        var passos=List.of(new PassoInput(DADO,"há livro disponível",1),new PassoInput(QUANDO,"solicita reserva",2));
        var erro=assertThrows(RegraException.class,()->ValidadorCenario.validar(passos));
        assertTrue(erro.getMessage().contains("ENTÃO"));
    }
    @Test void cenarioIniciadoPorERejeitado() {
        var passos=List.of(new PassoInput(E,"outra condição",1),new PassoInput(QUANDO,"age",2),new PassoInput(ENTAO,"resultado",3));
        assertThrows(RegraException.class,()->ValidadorCenario.validar(passos));
    }
    @Test void cenarioForaDeOrdemERejeitado() {
        var passos=List.of(new PassoInput(QUANDO,"age",1),new PassoInput(ENTAO,"resultado",2),new PassoInput(DADO,"condição",3));
        assertThrows(RegraException.class,()->ValidadorCenario.validar(passos));
    }
    @Test void passosOrdenadosValidosSaoAceitos() {
        assertDoesNotThrow(()->ValidadorCenario.validar(List.of(
            new PassoInput(ENTAO,"resultado",4),new PassoInput(DADO,"condição",1),
            new PassoInput(QUANDO,"ação",3),new PassoInput(E,"outra condição",2))));
    }
}
