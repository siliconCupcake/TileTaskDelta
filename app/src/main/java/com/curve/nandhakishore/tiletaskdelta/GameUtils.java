package com.curve.nandhakishore.tiletaskdelta;

import android.content.Context;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class GameUtils {

    public static GameTile[][] gameGrid;

    public static GameTile[][] patternGrid;

    public static ArrayList<GameTile> undoList;

    public static int size;

    public static Context gameContext;

    public static LinearLayout lv;

    public static boolean newPattern = true;

    public static boolean gameOverCheck(){
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (gameGrid[i][j].isDark() != patternGrid[i][j].isDark())
                    return false;
            }
        }
        return true;
    }
}
