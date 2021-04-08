package com.sia.client.config;

import java.net.URL;

public class UtilsTest {

    public static void main(String [] argv) {
        URL url = Utils.getMediaResource("boxing.png");
        System.out.println("url="+url);

        url = Utils.getConfigResource("spankoddsurlinfo.txt");
        System.out.println("url="+url);
    }
}
