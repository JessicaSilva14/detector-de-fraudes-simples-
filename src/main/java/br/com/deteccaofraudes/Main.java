package br.com.deteccaofraudes;

import br.com.deteccaofraudes.modelo.Alerta;
import br.com.deteccaofraudes.modelo.Transacao;
import br.com.deteccaofraudes.servico.DetectorFraudes;
import br.com.deteccaofraudes.servico.LeitorTransacoes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {
        Path caminho = Path.of(args.length > 0 ? args[0] : "dados/transacoes.csv");

        try {
            List<Transacao> transacoes = new LeitorTransacoes().ler(caminho);
            List<Alerta> alertas = DetectorFraudes.comRegrasPadrao().analisar(transacoes);

            System.out.println("=== DETECTOR DE FRAUDES ===");
            System.out.println("Arquivo: " + caminho);
            System.out.println("Transações analisadas: " + transacoes.size());
            System.out.println();

            if (alertas.isEmpty()) {
                System.out.println("Nenhuma transação suspeita encontrada.");
                return;
            }

            for (Alerta alerta : alertas) {
                System.out.println(alerta);
                System.out.println();
            }
            imprimirResumo(alertas);

        } catch (IOException e) {
            System.out.println("Não foi possível ler o arquivo '" + caminho + "': " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro nos dados: " + e.getMessage());
        }
    }

    private static void imprimirResumo(List<Alerta> alertas) {
        Set<String> transacoesSuspeitas = new HashSet<>();
        Map<String, Integer> porRegra = new TreeMap<>();

        for (Alerta a : alertas) {
            transacoesSuspeitas.add(a.transacao().id());
            porRegra.merge(a.regra(), 1, Integer::sum);
        }

        System.out.println("--- RESUMO ---");
        System.out.println("Transações suspeitas: " + transacoesSuspeitas.size());
        System.out.println("Alertas gerados: " + alertas.size());
        porRegra.forEach((regra, qtd) -> System.out.println(" - " + regra + ": " + qtd));
    }
}
