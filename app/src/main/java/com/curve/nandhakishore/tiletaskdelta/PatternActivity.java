package com.curve.nandhakishore.tiletaskdelta;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Random;

public class PatternActivity extends AppCompatActivity {

    LinearLayout patternLV;
    public static GameTile[][] patternGrid;
    int size;
    public static Boolean newPattern;
    View.OnClickListener clickPatternTile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern_activity);
        setTitle("Pattern");

        size = getIntent().getIntExtra("size", 0);
        clickPatternTile = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameTile tile = (GameTile) view;
                int i = tile.getI();
                int j = tile.getJ();
                if (tile.isPattern()) {
                    try {
                        tile.invertState(patternGrid[i - 1][j]);
                    } catch (IndexOutOfBoundsException e) {
                    }
                    try {
                        tile.invertState(patternGrid[i + 1][j]);
                    } catch (IndexOutOfBoundsException e) {
                    }
                    try {
                        tile.invertState(patternGrid[i][j - 1]);
                    } catch (IndexOutOfBoundsException e) {
                    }
                    try {
                        tile.invertState(patternGrid[i][j + 1]);
                    } catch (IndexOutOfBoundsException e) {
                    }
                    }
                }
            };

        patternLV = (LinearLayout) findViewById(R.id.pattern_grid);
        patternLV.setWeightSum((float) size);
        if(newPattern)
            patternGrid = new GameTile[size][size];

        for(int i = 0; i < size; i++) {
            LinearLayout tLayout = new LinearLayout(getApplicationContext());
            tLayout.setWeightSum((float) size);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
            tLayout.setLayoutParams(layoutParams);
            for (int j = 0; j < size; j++) {
                GameTile gt = new GameTile(getApplicationContext(), true, true, i, j);
                LinearLayout.LayoutParams bParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                bParams.setMargins(10, 10, 10, 10);
                if(newPattern) {
                    gt.setLayoutParams(bParams);
                    gt.setBackgroundColor(Color.BLACK);
                    gt.setOnClickListener(clickPatternTile);
                    patternGrid[i][j] = gt;
                }
                else
                    gt = patternGrid[i][j];
                if(gt.getParent() != null)
                    ((ViewGroup) gt.getParent()).removeView(gt);
                tLayout.addView(gt);
            }
            patternLV.addView(tLayout);
        }
        if(newPattern) {
            createPattern();
            newPattern = false;
        }
        enableTiles(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pattern_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_pattern:
                newPattern = true;
                reload();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createPattern(){
        Random generator = new Random();
        int k=0, l=0;
        int k1, l1;
        for(int i = 0; i < 3; i++){
            k1 = generator.nextInt(size);
            l1 = generator.nextInt(size);
            while (k == k1) {
                k1 = generator.nextInt(size);
            }
            k = k1;
            while (l == l1) {
                l1 = generator.nextInt(size);
            }
            l = l1;
            Log.e("CreatePattern", "Click (" + String.valueOf(k) + ", " + String.valueOf(l) + ")");
            patternGrid[k][l].callOnClick();
        }
    }

    public void enableTiles(boolean enable){
        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++)
                patternGrid[i][j].setEnabled(enable);
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        patternLV.removeAllViews();
        super.onStop();
    }
}
