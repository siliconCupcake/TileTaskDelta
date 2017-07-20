package com.curve.nandhakishore.tiletaskdelta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    LinearLayout tileGrid;
    EditText getSize;
    Button goButton;
    RelativeLayout gs;
    int size;
    Boolean newPattern = true;
    ArrayList<GameTile> undoList;
    View.OnClickListener clickGameTile;
    public GameTile[][] gameGrid, patternGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        clickGameTile = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameTile tile = (GameTile) view;
                int i = tile.getI();
                int j = tile.getJ();
                if (!tile.isPattern()) {
                    undoList.add(gameGrid[i][j]);
                    Log.e("GameGrid", "Clicked (" + String.valueOf(i) + ", " + String.valueOf(j) + ")");
                    try {
                        tile.invertState(gameGrid[i - 1][j]);
                    } catch (IndexOutOfBoundsException e) {
                    }
                    try {
                        tile.invertState(gameGrid[i + 1][j]);
                    } catch (IndexOutOfBoundsException e) {
                    }
                    try {
                        tile.invertState(gameGrid[i][j - 1]);
                    } catch (IndexOutOfBoundsException e) {
                    }
                    try {
                        tile.invertState(gameGrid[i][j + 1]);
                    } catch (IndexOutOfBoundsException e) {
                    }
                    if (patternGrid != null) {
                        gameOver();
                    }
                }
            }
        };

        undoList = new ArrayList<>();
        getSize = (EditText) findViewById(R.id.input);
        gs = (RelativeLayout) findViewById(R.id.grid_size);
        goButton = (Button) findViewById(R.id.ok_button);
        tileGrid = (LinearLayout) findViewById(R.id.tile_grid);


        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size = Integer.parseInt(getSize.getText().toString());
                tileGrid.setWeightSum((float) size);
                gameGrid = new GameTile[size][size];
                patternGrid = new GameTile[size][size];
                for (int i = 0; i < size; i++) {
                    LinearLayout tmp = new LinearLayout(getApplicationContext());
                    tmp.setWeightSum((float) size);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
                    tmp.setLayoutParams(layoutParams);

                    for (int j = 0; j < size; j++) {
                        GameTile gt = new GameTile(getApplicationContext(), true, false, i, j);
                        LinearLayout.LayoutParams bParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                        bParams.setMargins(10, 10, 10, 10);
                        gt.setLayoutParams(bParams);
                        gt.setBackgroundColor(Color.BLACK);
                        gt.setOnClickListener(clickGameTile);
                        gameGrid[i][j] = gt;
                        tmp.addView(gt);
                    }
                    tileGrid.addView(tmp);
                }
                tileGrid.setVisibility(View.VISIBLE);
                gs.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (gs.getVisibility() == View.GONE)
            switch (item.getItemId()) {
                case R.id.reset:
                    for (int i = 0; i < size; i++)
                        for (int j = 0; j < size; j++)
                            gameGrid[i][j].resetTile();
                    undoList.clear();
                    break;

                case R.id.undo:
                    try {
                        undoList.get(undoList.size() - 1).callOnClick();
                        undoList.remove(undoList.size() - 1);
                        undoList.remove(undoList.size() - 1);
                    }catch (Exception e) {
                    }
                    break;

                case R.id.view_pattern:
                    Intent viewPattern = new Intent(getApplicationContext(), PatternActivity.class);
                    viewPattern.putExtra("size", size);
                    PatternActivity.newPattern = newPattern;
                    PatternActivity.patternGrid = patternGrid;
                    startActivityForResult(viewPattern, 1);
                    break;
            }
        return super.onOptionsItemSelected(item);
    }

    public void gameOver() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("YOU WIN");
        alertBuilder.setMessage("What next?");
        alertBuilder.setPositiveButton("AGAIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int k = 0; k < size; k++)
                    for (int j = 0; j < size; j++)
                        gameGrid[k][j].resetTile();
                newPattern = true;
                Intent viewPattern = new Intent(getApplicationContext(), PatternActivity.class);
                viewPattern.putExtra("size", size);
                PatternActivity.newPattern = newPattern;
                PatternActivity.patternGrid = patternGrid;
                startActivityForResult(viewPattern, 1);
            }
        });
        alertBuilder.setNegativeButton("QUIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();

        if (gameOverCheck()) {
            alertDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            newPattern = PatternActivity.newPattern;
            patternGrid = PatternActivity.patternGrid;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean gameOverCheck(){
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                try {
                    if (gameGrid[i][j].isDark() != patternGrid[i][j].isDark())
                        return false;
                }catch (Exception e){
                }
            }
        }
        return true;
    }

}
