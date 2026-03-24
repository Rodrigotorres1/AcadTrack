package br.com.acadtrack.bdd;

import io.cucumber.java.pt.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class RankingSteps {

    List<Integer> notas;
    List<Integer> ordenado;

    @Dado("que existem alunos com notas")
    public void dado() {
        notas = Arrays.asList(7, 9, 5);
    }

    @Quando("gerar ranking")
    public void quando() {
        ordenado = new ArrayList<>(notas);
        ordenado.sort(Collections.reverseOrder());
    }

    @Então("o maior deve ser o primeiro")
    public void entao() {
        assertEquals(9, ordenado.get(0));
    }
}
