package br.com.spedison.qrcoderservice;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;

public class QrCoderProcessor {

    private byte[] lastHash;

    public void processClipboard() {
        try {
            // No Wayland/Fedora, o Java AWT às vezes falha em pegar imagens do sistema.
            // Usamos o wl-paste para garantir a compatibilidade com o GNOME.
            Process process = new ProcessBuilder("/usr/bin/wl-paste", "w", "-t", "image/png").start();

            try (InputStream is = process.getInputStream()) {
                byte[] imageData = is.readAllBytes();
                ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
                BufferedImage image = ImageIO.read(bais);
                byte[] hashCurrent = computeHash(imageData);

                if (image != null) {

                    if (lastHash != null) {
                        if (Arrays.equals(lastHash, hashCurrent)) {
                            return;
                        }
                    }
                    lastHash = hashCurrent;

                    String decodedText = decodeQRCode(image);
                    if (decodedText != null) {
                        System.out.println("QR Code detectado: " + decodedText);
                        copyToClipboard(decodedText);
                    }
                }
            }
        } catch (Exception e) {
            // Erro esperado se o clipboard não contiver uma imagem PNG
        }
    }

    private String decodeQRCode(BufferedImage image) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            return null; // Não é um QR Code
        }
    }

    private void copyToClipboard(String text) {
        try {
            // Devolve para o clipboard usando wl-copy para total integração com GNOME
            ProcessBuilder pb = new ProcessBuilder("/usr/bin/wl-copy");
            Process p = pb.start();
            p.getOutputStream().write(text.getBytes());
            p.getOutputStream().close();
            System.out.println("Colado Texto com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] computeHash(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(data);
    }
}