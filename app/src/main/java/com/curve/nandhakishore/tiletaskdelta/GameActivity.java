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
    static int gameQuit = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        GameUtils.undoList = new ArrayList<>();
        getSize = (EditText) findViewById(R.id.input);
        gs = (RelativeLayout) findViewById(R.id.grid_size);
        goButton = (Button) findViewById(R.id.ok_button);
        tileGrid = (LinearLayout) findViewById(R.id.tile_grid);
        tileGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameQuit == 1)
                    finish();
                else if(gameQuit == 0)
                    startActivity(new Intent(getApplicationContext(), PatternActivity.class));
            }
        });

        GameUtils.lv = tileGrid;
        GameUtils.gameContext = this;

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameUtils.size = Integer.parseInt(getSize.getText().toString());
                tileGrid.setWeightSum((float) GameUtils.size);
                GameUtils.gameGrid = new GameTile[GameUtils.size][GameUtils.size];
                for (int i = 0; i < GameUtils.size; i++) {
                    LinearLayout tmp = new LinearLayout(getApplicationContext());
                    tmp.setWeightSum((float) GameUtils.size);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
                    tmp.setLayoutParams(layoutParams);

                    for (int j = 0; j < GameUtils.size; j++) {
                        Button tile = new Button(getApplicationContext());
                        LinearLayout.LayoutParams bParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                        bParams.setMargins(10, 10, 10, 10);
                        tile.setLayoutParams(bParams);
                        tile.setBackgroundColor(Color.BLACK);
                        GameTile gt = new GameTile(tile, true, false);
                        GameUtils.gameGrid[i][j] = gt;
                        gt.setI(i);
                        gt.setJ(j);
                        tmp.addView(tile);
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
                    for (int i = 0; i < GameUtils.size; i++)
                        for (int j = 0; j < GameUtils.size; j++)
                            GameUtils.gameGrid[i][j].resetTile();
                    GameUtils.undoList.clear();
                    break;

                case R.id.undo:
                    try {
                        GameUtils.undoList.get(GameUtils.undoList.size() - 1).getTile().callOnClick();
                        GameUtils.undoList.remove(GameUtils.undoList.size() - 1);
                        GameUtils.undoList.remove(GameUtils.undoList.size() - 1);
                    }catch (Exception e) {
                    }
                    break;

                case R.id.view_pattern:
                    startActivity(new Intent(getApplicationContext(), PatternActivity.class));
                    break;
            }
        return super.onOptionsItemSelected(item);
    }

    public static void gameOver() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(GameUtils.gameContext);
        alertBuilder.setTitle("YOU WIN");
        alertBuilder.setMessage("What next?");
        alertBuilder.setPositiveButton("AGAIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int k = 0; k < GameUtils.size; k++)
                    for (int j = 0; j < GameUtils.size; j++)
                        GameUtils.gameGrid[k][j].resetTile();
                GameUtils.newPattern = true;
                gameQuit = 0;
                GameUtils.lv.callOnClick();
            }
        });
        alertBuilder.setNegativeButton("QUIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gameQuit = 1;
                GameUtils.lv.callOnClick();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();

        if (GameUtils.gameOverCheck()) {
            alertDialog.show();
        }
    }

    @Override
    protected void onResume() {
        gameQuit = 2;
        super.onResume();
    }
}
