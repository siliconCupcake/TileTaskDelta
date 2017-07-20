package com.curve.nandhakishore.tiletaskdelta;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Random;

public class PatternActivity extends AppCompatActivity {

    LinearLayout patternGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern_activity);
        setTitle("Pattern");

        patternGrid = (LinearLayout) findViewById(R.id.pattern_grid);
        patternGrid.setWeightSum((float) GameUtils.size);
        if(GameUtils.newPattern)
            GameUtils.patternGrid = new GameTile[GameUtils.size][GameUtils.size];

        for(int i = 0; i < GameUtils.size; i++) {
            LinearLayout tLayout = new LinearLayout(getApplicationContext());
            tLayout.setWeightSum((float) GameUtils.size);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
            tLayout.setLayoutParams(layoutParams);
            for (int j = 0; j < GameUtils.size; j++) {
                Button b = new Button(this);
                LinearLayout.LayoutParams bParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                bParams.setMargins(10, 10, 10, 10);
                b.setLayoutParams(bParams);
                b.setBackgroundColor(Color.BLACK);
                if(GameUtils.newPattern) {
                    GameTile gt = new GameTile(b, true, true);
                    GameUtils.patternGrid[i][j] = gt;
                    gt.setI(i);
                    gt.setJ(j);
                }
                else
                    b = GameUtils.patternGrid[i][j].getTile();
                if(b.getParent() != null)
                    ((ViewGroup) b.getParent()).removeView(b);
                tLayout.addView(b);
            }
            patternGrid.addView(tLayout);
        }
        if(GameUtils.newPattern) {
            createPattern();
            GameUtils.newPattern = false;
        }
        enableTiles(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void createPattern(){
        Random generator = new Random();
        int k=0, l=0;
        int k1, l1;
        for(int i = 0; i < 10; i++){
            k1 = generator.nextInt(GameUtils.size);
            l1 = generator.nextInt(GameUtils.size);
            while (k != k1) {
                k = k1;
                k1 = generator.nextInt(GameUtils.size);
            }
            while (l != l1) {
                l = l1;
                l1 = generator.nextInt(GameUtils.size);
            }
            Log.e("CreatePattern", "Click (" + String.valueOf(k) + ", " + String.valueOf(l) + ")");
            GameUtils.patternGrid[k][l].getTile().callOnClick();
        }
    }

    public void enableTiles(boolean enable){
        for(int i = 0; i < GameUtils.size; i++)
            for(int j = 0; j < GameUtils.size; j++)
                GameUtils.patternGrid[i][j].getTile().setEnabled(enable);
    }

    @Override
    protected void onStop() {
        patternGrid.removeAllViews();
        super.onStop();
    }
}
