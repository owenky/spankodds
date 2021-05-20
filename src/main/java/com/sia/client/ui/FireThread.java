package com.sia.client.ui;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;

public class FireThread extends Thread {

    LinesTableData ltd;

    public FireThread(LinesTableData ltd) {
        this.ltd = ltd;

    }

    public void run() {
        try {

            Thread.sleep(30000);


            try {
                if (ltd != null) {
                    System.out.println("Suspecious fire() call in fireThread::run");
                    checkAndRunInEDT(() -> ltd.fire(null));
                }
            } catch (Exception ex) {
                log(ex);
            }
        } catch (Exception ex) {
            log(ex);
        }
    }


}
