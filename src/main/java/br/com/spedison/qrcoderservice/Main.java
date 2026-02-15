package br.com.spedison.qrcoderservice;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static QrCoderProcessor qrCoderProcessor = new QrCoderProcessor();

    public static void main(String[] args) {

        int tempo = args.length == 0 ? 3 : Integer.parseInt(args[0]);

        System.out.println("Monitorando área de transferência a cada " + tempo + "  segundos.");

        while (true) {
            qrCoderProcessor.processClipboard();
            try {
                Thread.sleep(3000);
            } catch (Exception e) {}
        }
    }

}

