package br.edu.mackenzie.estudiobdd.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import static br.edu.mackenzie.estudiobdd.model.Enums.*;

public final class ApiDtos {
    private ApiDtos() {}
    public record UsuarioInput(@NotBlank String nome, @NotBlank @Email String email) {}
    public record UsuarioView(Long id, String nome, String email) {}
    public record ProjetoInput(@NotBlank String nome, String descricao, String contextoDominio) {}
    public record ProjetoView(Long id, String nome, String descricao, String contextoDominio, Instant criadoEm) {}
    public record HistoriaInput(@NotBlank String titulo, @NotBlank String papel, @NotBlank String objetivo,
                                @NotBlank String beneficio, @NotNull Prioridade prioridade) {}
    public record HistoriaView(Long id, Long projetoId, Long autorId, String nomeAutor, String titulo,
                               String papel, String objetivo, String beneficio, Prioridade prioridade,
                               Instant criadoEm, Instant atualizadoEm, int quantidadeCenarios, int sugestoesPendentes) {}
    public record PassoInput(@NotNull TipoPasso tipo, @NotBlank String texto, @NotNull @Min(1) Integer posicao) {}
    public record PassoView(Long id, TipoPasso tipo, String texto, int posicao) {}
    public record CenarioInput(@NotBlank String titulo, @NotNull @Min(1) Integer posicao, @NotNull OrigemCenario origem,
                               @NotEmpty List<@Valid PassoInput> passos) {}
    public record CenarioView(Long id, Long historiaId, String titulo, int posicao, OrigemCenario origem, List<PassoView> passos) {}
    public record SugestaoInput(@NotBlank String ferramenta, @NotBlank String prompt, @NotBlank String conteudo) {}
    public record DecisaoInput(@NotNull Decisao decisao, MotivoRejeicao motivoRejeicao, String justificativa) {}
    public record SugestaoView(Long id, Long historiaId, String ferramenta, String prompt, String conteudo,
                               Decisao decisao, MotivoRejeicao motivoRejeicao, String justificativa,
                               Long registradaPorId, Long decididaPorId, Instant registradaEm, Instant decididaEm) {}
    public record ResumoView(Long projetoId, long quantidadeHistorias, Map<OrigemCenario,Long> cenariosPorOrigem,
                             Map<Decisao,Long> sugestoesPorDecisao, Map<MotivoRejeicao,Long> motivosRejeicao) {}
}
