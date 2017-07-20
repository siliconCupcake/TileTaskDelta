package com.curve.nandhakishore.tiletaskdelta;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class GameTile {

    private Button tile;
    private boolean dark;
    private boolean pattern;
    private int i,j;

    public GameTile(Button b, boolean d, boolean p) {
        tile = b;
        dark = d;
        pattern = p;
        if(pattern){
            tile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        invertState(GameUtils.patternGrid[i - 1][j]);
                    }catch (IndexOutOfBoundsException e){
                    }
                    try {
                        invertState(GameUtils.patternGrid[i + 1][j]);
                    }catch (IndexOutOfBoundsException e){
                    }
                    try {
                        invertState(GameUtils.patternGrid[i][j - 1]);
                    }catch (IndexOutOfBoundsException e){
                    }
                    try {
                        invertState(GameUtils.patternGrid[i][j + 1]);
                    }catch (IndexOutOfBoundsException e){
                    }
                }
            });
        }
        else {
            tile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GameUtils.undoList.add(GameUtils.gameGrid[i][j]);
                    Log.e("UndoList", "Size: " + String.valueOf(GameUtils.undoList.size()));
                    Log.e("GameGrid", "Clicked (" + String.valueOf(i) + ", " + String.valueOf(j) + ")");
                    try {
                        invertState(GameUtils.gameGrid[i - 1][j]);
                    } catch (IndexOutOfBoundsException e) {
                    }
                    try {
                        invertState(GameUtils.gameGrid[i + 1][j]);
                    } catch (IndexOutOfBoundsException e) {
                    }
                    try {
                        invertState(GameUtils.gameGrid[i][j - 1]);
                    } catch (IndexOutOfBoundsException e) {
                    }
                    try {
                        invertState(GameUtils.gameGrid[i][j + 1]);
                    } catch (IndexOutOfBoundsException e) {
                    }
                    if(GameUtils.patternGrid != null) {
                        GameActivity.gameOver();
                    }
                }
            });
        }
    }

    private void invertState(GameTile g){
        if (g.isDark()){
            g.tile.setBackgroundColor(Color.LTGRAY);
            g.setDark(false);
        }
        else{
            g.tile.setBackgroundColor(Color.BLACK);
            g.setDark(true);
        }
    }

    public Button getTile() {
        return tile;
    }

    public void setTile(Button tile) {
        this.tile = tile;
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

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public void resetTile(){
        dark = true;
        tile.setBackgroundColor(Color.BLACK);
    }
}
