package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.nio.file.Path;
import java.util.List;

@ApplicationScoped
public class CotubaService { // Essa classe será controlada pelo CDI

    private final RenderizadorMarkdown renderizadorMarkdown;
    private final LeitorPropriedadesEbook leitorPropriedadesEbook;
    private final RepositorioMarkDowns repositorioMarkdowns;
    private final GeradorEbook geradorDePDF;
    private final GeradorEbook geradorDeEPUB;

    @Inject
    public CotubaService(RenderizadorMarkdown renderizadorMarkdown, LeitorPropriedadesEbook leitorPropriedadesEbook,
                         RepositorioMarkDowns repositorioMarkdowns, @Named("geradorDePDF") GeradorEbook geradorDePDF,
                         @Named("geradorDeEPUB") GeradorEbook geradorDeEPUB) {
        this.renderizadorMarkdown = renderizadorMarkdown;
        this.leitorPropriedadesEbook = leitorPropriedadesEbook;
        this.repositorioMarkdowns = repositorioMarkdowns;
        this.geradorDePDF = geradorDePDF;
        this.geradorDeEPUB = geradorDeEPUB;
    }

    public void executar(ParametrosCotuba parametrosCotuba) {

        Path diretorioMDs = parametrosCotuba.getDiretorioDosMD();

        List<Capitulo> capitulos = repositorioMarkdowns.buscarArquivosMD(diretorioMDs);

        renderizadorMarkdown.renderizar(capitulos);

        var ebook = new Ebook();

        leitorPropriedadesEbook.ler(parametrosCotuba.getDiretorioDosMD(), ebook);

        ebook.setCapitulos(capitulos);
        ebook.setFormato(parametrosCotuba.getFormato());
        ebook.setArquivoSaida(parametrosCotuba.getArquivoDeSaida());

        GeradorEbook geradorEbook;

        if (FormatoEbook.PDF.equals(ebook.getFormato())) {
            geradorEbook = geradorDePDF;
        } else if (FormatoEbook.EPUB.equals(ebook.getFormato())) {
            geradorEbook = geradorDeEPUB;
        } else {
            throw new IllegalArgumentException("Formato do ebook inválido: " + parametrosCotuba.getFormato());
        }

        geradorEbook.gerar(ebook);
    }
}