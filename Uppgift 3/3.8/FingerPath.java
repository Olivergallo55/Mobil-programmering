package com.example.uppgift3;

import android.graphics.Path;

/**
 *  Denna klass representerar ritandet, genom olika sorters pennor och deras bredd
 *
 * @author Oliver Gallo, olga7031
 */


public class FingerPath {

    public int color;
    public boolean emboss;
    public boolean blur;
    public int strokeWidth;
    public Path path;

    //Konstruktor
    public FingerPath(int color, boolean emboss, boolean blur, int strokeWidth, Path path) {
        this.color = color;
        this.emboss = emboss;
        this.blur = blur;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }
}