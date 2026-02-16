package br.com.spedison.qrcoderservice;

import lombok.extern.log4j.Log4j2;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.concurrent.Semaphore;

@Log4j2
public class Main {

    final static Semaphore semaphore = new Semaphore(0);

    public static void main(String[] args) {

//        // 2. REGISTRO DO SHUTDOWN HOOK (O tratamento do ^C ou SIGTERM)
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            log.debug("Sinal de interrupção recebido!");
//            Main.semaphore.release();
//        }));


        SignalHandler handler = sig -> {
            log.debug("Sinal " + sig.getName() + " interceptado pelo Handler!");
            // Aqui liberamos o semáforo na thread main
            Main.semaphore.release();
            // A JVM NÃO vai fechar sozinha aqui. Ela espera o main continuar.
        };

        // 1. Capturamos o sinal SIGINT (2) ou SIGTERM (15) diretamente
        Signal.handle(new Signal("INT"), handler);
        Signal.handle(new Signal("TERM"), handler);
        Signal.handle(new Signal("HUP"), handler);

        // Thread de processamento.
        ProcessingThread pth = new ProcessingThread(new ClipboardWait());
        pth.start();

        log.debug("Iniciando processo");

        try {
            log.debug("rodando o processo =>> PID = " + ProcessHandle.current().pid());
            Main.semaphore.acquire();
            log.debug("Parando a thread de leitura do diretório de imagens");
            pth.fechar();
            log.debug("Finalizando processo");
        } catch (InterruptedException e) {
        }
    }


    public static void main2(String[] args) {

        int tempo = args.length == 0 ? 3 : Integer.parseInt(args[0]);

        log.info("Monitorando área de transferência a cada " + tempo + "  segundos.");

        while (true) {
            log.debug("Iniciando processamento da área de transferência.");
            //qrCoderProcessor.processClipboard();
            log.debug("Processamento finalizado. Aguardando o intervalo de " + tempo + " segundos.");
            try {
                Thread.sleep(tempo * 1000);
            } catch (Exception e) {
            }
        }
    }
}

