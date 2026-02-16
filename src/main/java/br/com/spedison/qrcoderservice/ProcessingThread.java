package br.com.spedison.qrcoderservice;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
public class ProcessingThread extends Thread {

    ClipboardWait clipboard;
    public AtomicBoolean continueProcessing = new AtomicBoolean(true);

    public ProcessingThread(ClipboardWait clipboard) {
        this.clipboard = clipboard;
    }

    @Override
    public void run() {
        try {
            while (continueProcessing.get()) {
                String fileName = clipboard.waitChangeFiles();
                ByteArrayInputStream bais = new ByteArrayInputStream(Files.readAllBytes(Paths.get(fileName)));
                BufferedImage image = ImageIO.read(bais);
                String decodedText = decodeQRCode(image);
                if (decodedText != null) {
                    System.out.println("QR Code detectado: " + decodedText);
                    copyToClipboard(decodedText);
                }
            }
        } catch (IOException | ClosedWatchServiceException e) {
            log.info("fechando o processo de monitoramento");
            return;
        }
    }

    public void fechar() {
        clipboard.fechar();
        continueProcessing.set(false);
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
            log.info("Colocado Texto com sucesso:" + text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
