# language: pt
# Atividade inicial: modelo textual. Transcrever os cenários à mão para comprovar a etapa solicitada.

Funcionalidade: Rejeitar sugestões de IA inadequadas
  Como analista de requisitos
  Quero analisar e rejeitar sugestões de IA
  Para manter a documentação confiável e rastreável

  Cenário: Rejeitar sugestão com motivo válido
    Dado que há uma sugestão pendente para uma história
    Quando o analista rejeita a sugestão com motivo INCORRETA
    Então a sugestão passa ao estado REJEITADA
    E o motivo da rejeição fica registrado

  Cenário: Impedir rejeição sem motivo
    Dado que há uma sugestão pendente para uma história
    Quando o analista tenta rejeitar a sugestão sem informar motivo
    Então o sistema impede a decisão
    E a sugestão permanece PENDENTE

  Cenário: Impedir alteração após decisão
    Dado que uma sugestão já foi REJEITADA
    Quando o analista tenta alterar a decisão para ACEITA
    Então o sistema rejeita a alteração
    E mantém a decisão original
