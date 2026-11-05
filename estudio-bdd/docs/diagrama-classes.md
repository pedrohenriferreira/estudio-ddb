# Diagrama de classes — Estúdio BDD

Diagrama correspondente às seis entidades obrigatórias do enunciado. O bônus com Fluxo/Etapa/Transição não entra até a Semana 3.

```mermaid
classDiagram
  direction LR
  class Usuario {
    +Long id
    +String nome
    +String email [único]
  }
  class Projeto {
    +Long id
    +String nome [único]
    +String descricao
    +String contextoDominio
    +Instant criadoEm
  }
  class HistoriaUsuario {
    +Long id
    +String titulo [único no projeto]
    +String papel
    +String objetivo
    +String beneficio
    +Prioridade prioridade
    +Instant criadoEm
    +Instant atualizadoEm
  }
  class Cenario {
    +Long id
    +String titulo [único na história]
    +int posicao
    +OrigemCenario origem
  }
  class Passo {
    +Long id
    +TipoPasso tipo
    +String texto
    +int posicao
  }
  class Sugestao {
    +Long id
    +String ferramenta
    +String prompt
    +String conteudo
    +Decisao decisao
    +MotivoRejeicao motivoRejeicao
    +String justificativa
    +Instant registradaEm
    +Instant decididaEm
  }
  Projeto "1" *-- "0..*" HistoriaUsuario : histórias
  Usuario "1" <-- "0..*" HistoriaUsuario : autor
  HistoriaUsuario "1" *-- "0..*" Cenario : cenários
  Cenario "1" *-- "0..*" Passo : passos ordenados
  HistoriaUsuario "1" *-- "0..*" Sugestao : sugestões
  Usuario "1" <-- "0..*" Sugestao : registradaPor
  Usuario "0..1" <-- "0..*" Sugestao : decididaPor
```

## Enums

- `Prioridade`: `ALTA`, `MEDIA`, `BAIXA`.
- `OrigemCenario`: `HUMANO`, `SUGESTAO_IA`.
- `TipoPasso`: `DADO`, `QUANDO`, `ENTAO`, `E`, `MAS`.
- `Decisao`: `PENDENTE`, `ACEITA`, `REJEITADA`.
- `MotivoRejeicao`: `INCORRETA`, `GENERICA`, `DUPLICADA`, `FORA_DO_ESCOPO`, `OUTRO`.

**Estratégia JPA:** `ManyToOne(fetch=LAZY)` evita carregar pais automaticamente; `OneToMany` com `orphanRemoval=true` e `CascadeType.ALL` mantém composição de cenários e passos; serviços são transacionais, de modo que os DTOs são convertidos antes do encerramento da sessão. A ordem é definida pelas posições persistidas, não pela ordem casual de retorno do banco.
