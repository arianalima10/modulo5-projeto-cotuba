package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

public class Main {

    void main(String[] args) {
        int exitCode = executar(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    int executar(String[] args) {

        boolean modoVerboso = true;

        try {
            var leitorCLI = new LeitorCLI();
            leitorCLI.ler(args);

            Path diretorioMD = leitorCLI.getDiretorioDosMD();
            String formato = leitorCLI.getFormato();
            Path arquivoSaida = leitorCLI.getArquivoDeSaida();
            modoVerboso = leitorCLI.isModoVerboso();

            var renderizadorMarkdown = new RenderizadorMarkdown();
            List<String> htmls = renderizadorMarkdown.renderizar(diretorioMD);

            if ("pdf".equals(formato)) {
                var geradorDePDF = new GeradorDePDF();
                geradorDePDF.gerarPDF(htmls, arquivoSaida);
            } else if ("epub".equals(formato)) {
                var geradorDeEPUB = new GeradorDeEPUB();
                geradorDeEPUB.gerarEPUB(htmls, arquivoSaida);
            } else {
                throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
            }

            System.out.println("Arquivo gerado com sucesso: " + arquivoSaida);
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