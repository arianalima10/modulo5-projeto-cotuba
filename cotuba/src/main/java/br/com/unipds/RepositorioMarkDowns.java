package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

public interface RepositorioMarkDowns {
    List<Capitulo> buscarArquivosMD(Path diretorioMD);
}