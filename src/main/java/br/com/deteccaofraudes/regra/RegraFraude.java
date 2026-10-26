package br.com.deteccaofraudes.regra;

import br.com.deteccaofraudes.modelo.Transacao;

import java.util.List;
import java.util.Optional;

public interface RegraFraude {

    String nome();

    /**
     * Avalia a transação atual com base no histórico do mesmo cliente
     * (apenas transações anteriores). Retorna o motivo se for suspeita.
     */
    Optional<String> avaliar(Transacao atual, List<Transacao> historico);
}
