package br.com.unipds;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

public class Main {

    void main(String[] args) {
        int exitCode = executar(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    int executar(String[] args) {

        boolean modoVerboso = true;

        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            var leitorCLI = container.select(LeitorCLI.class).get();
            ParametrosCotuba parametrosCotuba = leitorCLI.ler(args);

            modoVerboso = parametrosCotuba.isModoVerboso();

            var cotubaService = container.select(CotubaService.class).get();
            cotubaService.executar(parametrosCotuba);

            System.out.println("Arquivo gerado com sucesso: " + parametrosCotuba.getArquivoDeSaida());
            return 0;

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            if (modoVerboso) {
                System.err.println();
                ex.printStackTrace();
            }
            return 1;
        }
    }
}