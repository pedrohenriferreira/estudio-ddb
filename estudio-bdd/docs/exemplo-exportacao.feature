# language: pt

Funcionalidade: Reservar livro disponível
  Como leitor autenticado
  Quero reservar um livro do catálogo
  Para retirá-lo posteriormente

  Cenário: Reserva realizada com sucesso
    Dado que existe um exemplar disponível
    Quando o leitor solicita a reserva
    Então a reserva é registrada
