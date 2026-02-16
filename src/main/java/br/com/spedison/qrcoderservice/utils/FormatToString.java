package br.com.spedison.qrcoderservice.utils;

import java.util.Arrays;

public class FormatToString {
    public static String toString(byte[] byteArray){
        return "[[ "+Arrays.toString(byteArray)+" ]]";
    }
}
