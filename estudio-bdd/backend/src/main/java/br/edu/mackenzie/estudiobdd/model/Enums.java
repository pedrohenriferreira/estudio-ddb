package br.edu.mackenzie.estudiobdd.model;

public final class Enums {
    private Enums() {}
    public enum Prioridade { ALTA, MEDIA, BAIXA }
    public enum OrigemCenario { HUMANO, SUGESTAO_IA }
    public enum TipoPasso { DADO, QUANDO, ENTAO, E, MAS }
    public enum Decisao { PENDENTE, ACEITA, REJEITADA }
    public enum MotivoRejeicao { INCORRETA, GENERICA, DUPLICADA, FORA_DO_ESCOPO, OUTRO }
}
