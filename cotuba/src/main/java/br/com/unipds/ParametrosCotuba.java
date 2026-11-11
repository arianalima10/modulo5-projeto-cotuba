package br.com.unipds;

import java.nio.file.Path;

public record ParametrosCotuba(Path diretorioDosMD, FormatoEbook formato, Path arquivoDeSaida, boolean modoVerboso) {

    public ParametrosCotuba(Path diretorioDosMD, FormatoEbook formato, Path arquivoDeSaida, boolean modoVerboso) {
        this.diretorioDosMD = diretorioDosMD;
        this.formato = formato;
        this.arquivoDeSaida = arquivoDeSaida;
        this.modoVerboso = false;
    }
}