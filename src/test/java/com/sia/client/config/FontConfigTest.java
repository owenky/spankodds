package com.sia.client.config;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class FontConfigTest {

    private FontConfig fontConfig;
    @Before
    public void init() {
        fontConfig = new FontConfig();
    }
    @Test
    public void printAllFontName() {
        java.util.List<String> allFontNames = FontConfig.getAllFontNames();
        for (String fn: allFontNames) {
            System.out.println(fn);
        }
    }
    @Test
    public void testgetAllFontNames() {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = graphicsEnvironment.getAllFonts();
        for ( Font f: fonts) {
            System.out.println( f.getFontName(Locale.US) );
        }
    }
    @Test
    public void testGetFontStyle() {
        Font f = new Font("Arial", Font.BOLD, 11);
        String style = FontConfig.getFontStyle(f);
        assertEquals("Bold",style);
    }
    @Test
    public void testDeriveFontName() {
        String name;

        name = FontConfig.deriveFontName("Arial-Bold");
        assertEquals("Arial",name);

        name = FontConfig.deriveFontName("Arial BOLD BOLD");
        assertEquals("Arial",name);

        name = FontConfig.deriveFontName("Arial");
        assertEquals("Arial",name);

        name = FontConfig.deriveFontName("Arial-BOLD");
        assertEquals("Arial",name);

        name = FontConfig.deriveFontName("Arial Bold Italic");
        assertEquals("Arial",name);

        name = FontConfig.deriveFontName("Arial Black");
        assertEquals("Arial Black",name);

        name = FontConfig.deriveFontName("Arial.BOLD");
        assertEquals("Arial",name);

        name = FontConfig.deriveFontName("Calibri Bold");
        assertEquals("Calibri",name);

        name = FontConfig.deriveFontName("Calibri Bold");
        assertEquals("Calibri",name);
    }
}
