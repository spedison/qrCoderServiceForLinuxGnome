package br.com.spedison.qrcoderservice.linux;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class DiretoriosUsuarioTest {

    @Test
    void getHome() {
        String dirHome = System.getProperty("user.home");
        DiretoriosUsuario diretoriosUsuario = new DiretoriosUsuario();
        String dirHomeClass = diretoriosUsuario.getHome();
        Assertions.assertNotNull(dirHomeClass,dirHome);
    }

    @Test
    void getDocumentos() {

        String dirDocumentos = Paths.get(System.getProperty("user.home"),"Documentos").toString();
        DiretoriosUsuario diretoriosUsuario = new DiretoriosUsuario();
        String dirImagesClass = diretoriosUsuario.getDocumentos();
        Assertions.assertNotNull(dirImagesClass,dirDocumentos);

    }

    @Test
    void getImagens() {

        String dirImagens = Paths.get(System.getProperty("user.home"),"Imagens").toString();
        DiretoriosUsuario diretoriosUsuario = new DiretoriosUsuario();
        String dirImagesClass = diretoriosUsuario.getImagens();
        Assertions.assertNotNull(dirImagesClass,dirImagens);
    }
}