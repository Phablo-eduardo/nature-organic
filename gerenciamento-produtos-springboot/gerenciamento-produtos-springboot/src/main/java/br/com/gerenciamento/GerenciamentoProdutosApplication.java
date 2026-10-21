package br.com.gerenciamento;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class GerenciamentoProdutosApplication {

    private static final Logger logger = LoggerFactory.getLogger(GerenciamentoProdutosApplication.class);

    @Value("${server.port:8080}")
    private int serverPort;

    public static void main(String[] args) {
        SpringApplication.run(GerenciamentoProdutosApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowserWhenReady() {
        if (GraphicsEnvironment.isHeadless()) {
            logger.info("Ambiente headless detectado; navegador automatizado nao sera aberto.");
            return;
        }

        String url = "http://localhost:" + serverPort;
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create(url));
                return;
            }

            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "start", "", url).start();
                return;
            }

            logger.warn("Navegador nao aberto automaticamente: o ambiente nao oferece suporte a Desktop.");
        } catch (IOException | RuntimeException exception) {
            logger.warn("Nao foi possivel abrir o navegador automaticamente.", exception);
        }
    }
}
