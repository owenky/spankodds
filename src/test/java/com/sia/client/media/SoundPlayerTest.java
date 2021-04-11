package com.sia.client.media;

public class SoundPlayerTest {

    public static void main(String [] argv) throws InterruptedException {

        new SoundPlayer("halftime.wav");

        Thread.sleep(1000L);
        System.exit(0);
    }
}
