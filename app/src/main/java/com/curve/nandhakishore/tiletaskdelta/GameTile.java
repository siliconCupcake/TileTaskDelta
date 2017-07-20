package com.curve.nandhakishore.tiletaskdelta;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;


public class GameTile extends AppCompatButton {

    private boolean dark;
    private boolean pattern;
    private int i,j;

    public GameTile(Context context, boolean dark, boolean pattern, int i, int j) {
        super(context);
        this.dark = dark;
        this.pattern = pattern;
        this.i = i;
        this.j = j;
    }

    public void invertState(GameTile g){
        if (g.isDark()){
            g.setBackgroundColor(Color.LTGRAY);
            g.setDark(false);
        }
        else{
            g.setBackgroundColor(Color.BLACK);
            g.setDark(true);
        }
    }


    public boolean isDark() {
        return dark;
    }

    public void setDark(boolean dark) {
        this.dark = dark;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public boolean isPattern() {
        return pattern;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public void resetTile(){
        dark = true;
        this.setBackgroundColor(Color.BLACK);
    }
}
