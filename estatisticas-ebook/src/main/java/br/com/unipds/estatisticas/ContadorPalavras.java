package br.com.unipds.estatisticas;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

class ContadorPalavras implements Iterable<ContadorPalavras.ContagemPalavras> {

    record ContagemPalavras(String palavra, int ocorrencias) {}

    private final Map<String, Integer> mapa = new TreeMap<>();

    public void adicionarPalavra(String palavra) {
        mapa.merge(palavra, 1, Integer::sum);
    }

    public Iterable<? extends Map.Entry<String, Integer>> entrySet() {
        return mapa.entrySet();
    }

    public Iterator<ContagemPalavras> iterator() {
        Iterator<Map.Entry<String, Integer>> iterator = mapa.entrySet().iterator();
        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public ContagemPalavras next() {
                Map.Entry<String, Integer> entry = iterator.next();
                return new ContagemPalavras(entry.getKey(), entry.getValue());
            }
        };
    }
}