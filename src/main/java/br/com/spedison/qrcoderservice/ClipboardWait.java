package br.com.spedison.qrcoderservice;

import br.com.spedison.qrcoderservice.linux.DiretoriosUsuario;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class ClipboardWait {

    private WatchService watchService;
    private DiretoriosUsuario diretoriosUsuario;
    private Path path;

    public ClipboardWait() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            diretoriosUsuario = new DiretoriosUsuario();
            path = Paths.get(diretoriosUsuario.getCapturaDeImagens());
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        } catch (IOException e) {
            log.debug("Erro ao criar o WatchService no diretório : " + diretoriosUsuario.getCapturaDeImagens(), e);
            throw new RuntimeException(e);
        }
    }

    public String waitChangeFiles() {
        try {
            WatchKey key = watchService.take(); // TRAVA AQUI - Consumo de CPU Zero
            String fileName = null;
            for (WatchEvent<?> event : key.pollEvents()) {
                Path newFile = path.resolve((Path) event.context());
                if (newFile.toString().endsWith(".png")) {
                    // Pequeno delay para o GNOME liberar o descritor do arquivo
                    Thread.sleep(200);
                    log.debug("Arquivo detectado : " + newFile);
                    fileName = newFile.toString();
                    break;
                }
            }
            key.reset();
            return fileName;
        } catch (InterruptedException e) {
            log.error("Problemas enquanto espera o arquivo : " + e.getMessage());
            return null;
        }
    }

    public void fechar(){
        try {
            watchService.close();
        } catch (IOException e) {}
    }
}