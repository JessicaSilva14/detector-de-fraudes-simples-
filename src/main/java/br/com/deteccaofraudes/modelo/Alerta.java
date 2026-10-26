package br.com.deteccaofraudes.modelo;

import br.com.deteccaofraudes.util.Moeda;

public record Alerta(Transacao transacao, String regra, String motivo) {

    @Override
    public String toString() {
        return String.format("[%s] Cliente %s | %s | %s%n      Regra:  %s%n      Motivo: %s",
                transacao.id(), transacao.clienteId(), Moeda.formatar(transacao.valor()),
                transacao.dataHora(), regra, motivo);
    }
}
