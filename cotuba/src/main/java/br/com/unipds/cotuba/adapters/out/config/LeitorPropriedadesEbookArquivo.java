package br.com.unipds.cotuba.adapters.out.config;

import br.com.unipds.cotuba.dto.PropriedadesEbook;
import br.com.unipds.cotuba.ports.out.LeitorPropriedadesEbook;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@ApplicationScoped
public class LeitorPropriedadesEbookArquivo implements LeitorPropriedadesEbook {

    @Override
    public PropriedadesEbook ler(Path diretorioMD) {

        Path arquivoProperties = diretorioMD.resolve("ebookBuilder.properties");

        if (!Files.exists(arquivoProperties)) {
            throw new IllegalStateException("Arquivo ebook.properties não encontrado em: " + diretorioMD);
        }

        Properties prop = new Properties();

        try (InputStream input = Files.newInputStream(arquivoProperties);
             InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {

            prop.load(reader);

        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler arquivo: " + arquivoProperties, e);
        }

        /**
         * O método load(InputStream) interpreta os bytes usando ISO-8859-1.
         * Para ler o arquivo ebookBuilder.properties em UTF-8, troquei o InputStream por um Reader, que usa
         * StandardCharsets.UTF_8, preservando os caracteres acentuados.
         * try (InputStream input = Files.newInputStream(arquivoProperties)) {
         *   // Esse método por ser muito antigo ele não tem retorno e sim muda o status do objeto;
         *   prop.load(input);
         * } catch (IOException e) {
         *   throw new IllegalStateException("Erro ao ler arquivo: " + arquivoProperties, e);
         * }
         */

        String propriedadeTitulo = "cotuba.ebookBuilder.titulo";
        String titulo = prop.getProperty(propriedadeTitulo);
        validarPropriedade(titulo, propriedadeTitulo);

        String propriedadeAutor = "cotuba.ebookBuilder.autor";
        String autor = prop.getProperty(propriedadeAutor);
        validarPropriedade(autor, propriedadeAutor);

       return new PropriedadesEbook(titulo, autor);
    }

    private void validarPropriedade(String valor, String propriedade) {
        if(valor == null || valor.isBlank()) {
            throw new IllegalStateException("Propriedade inválida: "  + propriedade);
        }
    }
}