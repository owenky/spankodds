package com.sia.client.ui.comps;

import com.sia.client.config.Utils;
import javafx.concurrent.Worker;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.*;

public class WebViewPane extends StackPane {

    private Dimension loadingIndicatorSize = new Dimension(40,40);
    public void setLoadingIndicatorSize(Dimension loadingIndicatorSize) {
        this.loadingIndicatorSize = loadingIndicatorSize;
    }
    public void loadUrl(String url) {
        try {
            final ProgressIndicator pi = new ProgressIndicator();
            pi.setMaxSize(loadingIndicatorSize.getWidth(),loadingIndicatorSize.getHeight());

            WebView webView = new WebView();
            getChildren().addAll(webView, pi);

            WebEngine webEngine = webView.getEngine();
            // updating progress bar using binding
//            pi.progressProperty().bind(webEngine.getLoadWorker().progressProperty());
            webEngine.setUserStyleSheetLocation(getClass().getResource("/config/WebViewStyle.css").toString());

            webEngine.getLoadWorker().stateProperty().addListener(
                    (ov, oldState, newState) -> {
                        if (newState == Worker.State.SUCCEEDED || newState == Worker.State.FAILED || newState == Worker.State.CANCELLED) {
                            // hide progress bar then page is ready
                            pi.setVisible(false);
                        }
                    });

            webEngine.load(url);
        } catch (Exception e) {
            Utils.log(e);
        }
    }
}
