package com.sia.client.ui.comps;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.*;
import java.util.Base64;

public class WebViewPane extends StackPane {

    private Dimension loadingIndicatorSize = new Dimension(40, 40);
    private WebView webView;
    private ProgressIndicator pi;

    public WebViewPane() {
        init();
    }

    private void init() {

        pi = new ProgressIndicator();
        pi.setMaxSize(loadingIndicatorSize.getWidth(), loadingIndicatorSize.getHeight());

        webView = new WebView();
        getChildren().addAll(webView, pi);

        WebEngine webEngine = webView.getEngine();
        // updating progress bar using binding
//            pi.progressProperty().bind(webEngine.getLoadWorker().progressProperty());
        webEngine.setUserStyleSheetLocation(getClass().getResource("/config/WebViewStyle.css").toString());

        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString(SiaConst.up.getBytes());

        webEngine.setUserAgent("foo\nAuthorization: "+basicAuthPayload);
        webEngine.getLoadWorker().stateProperty().addListener(
                (ov, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED || newState == Worker.State.FAILED || newState == Worker.State.CANCELLED) {
                        // hide progress bar then page is ready
                        pi.setVisible(false);
                    }
                });

    }

    public void setLoadingIndicatorSize(Dimension loadingIndicatorSize) {
        this.loadingIndicatorSize = loadingIndicatorSize;
    }

    public void loadUrl(String url) {
        try {
            pi.setVisible(true);
            WebEngine webEngine = webView.getEngine();
            webEngine.load(url);
        } catch (Exception e) {
            Utils.log(e);
        }
    }

    public void clear() {
        Runnable clearJob = () -> {

            getChildren().removeAll(webView, pi);
            // Delete cache for navigate back
            WebEngine webEngine = webView.getEngine();
            webEngine.load("about:blank");
            // Delete cookies
            java.net.CookieHandler.setDefault(new java.net.CookieManager());
            webView = null;
            pi = null;
        };
        Platform.runLater(clearJob);
    }
}
