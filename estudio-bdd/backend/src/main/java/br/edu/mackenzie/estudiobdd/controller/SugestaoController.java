package br.edu.mackenzie.estudiobdd.controller;
import br.edu.mackenzie.estudiobdd.dto.ApiDtos.*;
import br.edu.mackenzie.estudiobdd.service.SugestaoService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;
@RestController
public class SugestaoController {
    private final SugestaoService service;
    public SugestaoController(SugestaoService service) {this.service=service;}
    @GetMapping("/api/historias/{id}/sugestoes")
    public List<SugestaoView> listar(@PathVariable Long id,@RequestParam(required=false) Decisao decisao) {return service.listar(id,decisao);}
    @PostMapping("/api/historias/{id}/sugestoes")
    public ResponseEntity<SugestaoView> registrar(@PathVariable Long id,@RequestHeader("X-Usuario-Id") Long usuarioId,
            @Valid @RequestBody SugestaoInput input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(id,usuarioId,input));
    }
    @PostMapping("/api/sugestoes/{id}/decisao")
    public SugestaoView decidir(@PathVariable Long id,@RequestHeader("X-Usuario-Id") Long usuarioId,
            @Valid @RequestBody DecisaoInput input) {return service.decidir(id,usuarioId,input);}
}
