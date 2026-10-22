package br.com.unipds;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.nio.file.Path;
import java.util.List;

public class RenderizadorMarkdown {

    public List<Capitulo> renderizar(Path diretorioMDs) {

            var repositorioMarkdowns = new RepositorioMarkDowns();
            List<Capitulo> capitulos = repositorioMarkdowns.buscarArquivosMD(diretorioMDs);

            return capitulos.stream().map(arquivoMD -> {

                var capitulo = new Capitulo();

                Parser parser = Parser.builder().build();
                Node document = null;
                try {
                    // Obtém o Markdown do objeto recebido pelo Stream
                    String markdown = arquivoMD.getMarkdown();

                    document = parser.parse(markdown);

                    document.accept(new AbstractVisitor() {
                        @Override
                        public void visit(Heading heading) {
                            if (heading.getLevel() == 1) {
                                // capítulo
                                String tituloDoCapitulo = ((Text) heading.getFirstChild()).getLiteral();
                                capitulo.setTitulo(tituloDoCapitulo);
                            } else if (heading.getLevel() == 2) {
                                // seção
                            } else if (heading.getLevel() == 3) {
                                // título
                            }

                            // Permite percorrer os nós filhos do título, possibilitando o processamento de outros
                            // elementos do Markdown.
                            visitChildren(heading);
                        }
                    });
                } catch (Exception ex) {
                    throw new IllegalStateException("Erro ao fazer parse do arquivo "
                            + capitulo.getArquivoMarkdown(), ex);
                }

                try {
                    HtmlRenderer renderer = HtmlRenderer.builder().build();
                    String html = renderer.render(document);
                    capitulo.setHtml(html);

                    return capitulo;
                } catch (Exception ex) {
                    throw new IllegalStateException("Erro ao renderizar para HTML o arquivo "
                            + capitulo.getArquivoMarkdown(), ex);
                }
            }).toList();
    }
}
