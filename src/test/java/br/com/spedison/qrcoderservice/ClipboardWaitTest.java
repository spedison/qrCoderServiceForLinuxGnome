package br.com.spedison.qrcoderservice;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class ClipboardWaitTest {

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    public void testWaitChangeFiles() {
        ClipboardWait clipboardWait = new ClipboardWait();
        //String ret = clipboardWait.waitChangeFiles();
        //assertNotEquals("", ret);
        //log.debug("Adicionado o arquivo : " + ret);
        clipboardWait.fechar();
    }


}