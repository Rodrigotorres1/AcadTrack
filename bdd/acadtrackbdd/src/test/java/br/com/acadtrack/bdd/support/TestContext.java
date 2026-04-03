package br.com.acadtrack.bdd.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestContext {

    public final Map<String, String> vinculosAlunoTurma = new HashMap<>();
    public final Map<String, List<String>> simuladoDisciplinas = new HashMap<>();
    public final Map<String, Double> pesosDisciplinas = new HashMap<>();
    public final Map<String, Double> notasAluno = new HashMap<>();
    public final Map<String, String> responsaveisAluno = new HashMap<>();
    public final List<String> ranking = new ArrayList<>();

    public String mensagem;
    public Double mediaCalculada;
    public boolean operacaoExecutada;
}
