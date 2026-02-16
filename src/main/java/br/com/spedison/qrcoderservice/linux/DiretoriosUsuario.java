package br.com.spedison.qrcoderservice.linux;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class DiretoriosUsuario {

    static final private String documentos = "DOCUMENTS";
    static final private String imagens = "PICTURES";
    static final private String home = "HOME";
    static final private String comando = "/usr/bin/xdg-user-dir";

    final private Map<String, String> cache = new HashMap<>();

    public String getData(String key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        Process p = null;
        String baseDir = "";
        try {
            String [] cmd = key.equals(home) ? new String[]{comando} : new String[]{comando, key};
            p = Runtime.getRuntime().exec(cmd);
            baseDir = new String(p.getInputStream().readAllBytes()).trim();
            log.debug("Executado o comando e retorno do baseDir = " + baseDir);
        } catch (IOException e) {
            log.warn("Não executei o comando xdg-user-dir :: " + e.getMessage());
            baseDir = System.getProperty("user.home");
            if (!key.equals(home)) { // TODO: Ajustar para ficar independete de plataforma
                baseDir = Paths.get(baseDir, key).toString();
            }
        }

        if (!baseDir.isBlank())
            cache.put(key, baseDir);

        return baseDir;
    }


    public String getHome() {
        return getData(home);
    }

    public String getDocumentos() {
        return getData(documentos);
    }

    public String getImagens() {
        return getData(imagens);
    }

    public String getCapturaDeImagens(){
        String img= getImagens();
        if (img.toLowerCase().contains("pictures")){
            return Paths.get(img, "Screenshots").toString();
        }
        return Paths.get(img, "Capturas de tela").toString();
    }
}
