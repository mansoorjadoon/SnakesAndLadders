package com.example.test.snakesandladders;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    Button buttonRoll;
    ImageView dice;
    ImageView board;
    ImageView you;
    ImageView comp;
    int youPos;
    int aiPos;
    private ArrayList<SnakeOrLadder> snakes = new ArrayList<>();
    private ArrayList<SnakeOrLadder> ladders = new ArrayList<>();
    private float dis;

    private boolean AIPlaying = false;

    private void locationOnScreen(int[] point, int position) {
        int row = 0;
        int col = 0;

        row = position / 10;
        col = position % 10;


        if (position % 10 == 0) {
            row--;
            col = 10;
        }
        col = row % 2 == 0 ? col - 1 : 10 - col;

        point[0] = (int) (col * dis);
        point[1] = (int) (dis - ((row + 1) * dis));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonRoll = (Button) findViewById(R.id.button);
        dice = (ImageView) findViewById(R.id.dice);
        you = (ImageView) findViewById(R.id.you);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final float width = metrics.widthPixels;
        dis = width / 10;
        youPos = 0;
        aiPos = 0;

        snakes.add(new SnakeOrLadder(16, 6));
        snakes.add(new SnakeOrLadder(46, 25));
        snakes.add(new SnakeOrLadder(49, 11));
        snakes.add(new SnakeOrLadder(62, 19));
        snakes.add(new SnakeOrLadder(99, 80));
        snakes.add(new SnakeOrLadder(95, 75));
        snakes.add(new SnakeOrLadder(74, 53));
        snakes.add(new SnakeOrLadder(92, 88));
        snakes.add(new SnakeOrLadder(89, 68));
        snakes.add(new SnakeOrLadder(64, 60));

        ladders.add(new SnakeOrLadder(38, 2));
        ladders.add(new SnakeOrLadder(14, 7));
        ladders.add(new SnakeOrLadder(31, 8));
        ladders.add(new SnakeOrLadder(26, 15));
        ladders.add(new SnakeOrLadder(42, 21));
        ladders.add(new SnakeOrLadder(44, 36));
        ladders.add(new SnakeOrLadder(84, 28));
        ladders.add(new SnakeOrLadder(67, 51));
        ladders.add(new SnakeOrLadder(98, 78));
        ladders.add(new SnakeOrLadder(94, 87));
        ladders.add(new SnakeOrLadder(91, 71));

        buttonRoll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!AIPlaying) {
                    youPos = takeTurn(R.id.you, youPos);
                    Toast.makeText(getApplicationContext(), "Player at " + String.valueOf(youPos), Toast.LENGTH_SHORT).show();

                    AIPlaying = true;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            aiPos = takeTurn(R.id.imageView5, aiPos);
                            Toast.makeText(getApplicationContext(), "AI at " + String.valueOf(aiPos), Toast.LENGTH_SHORT).show();

                            AIPlaying = false;
                        }
                    }, 1000);

                }
            }
        });
    }

    private int takeTurn(int imageID, int currentPosition) {
        int RolledNum = rollDice();

        int newPosition = currentPosition + RolledNum;

        for (SnakeOrLadder l : ladders) {
            if (l.tail() == newPosition)
                newPosition = l.head();
        }

        for (SnakeOrLadder s : snakes) {
            if (s.head() == newPosition)
                newPosition = s.tail();
        }

        if (newPosition == 100) {
            finish();
            startActivity(new Intent(MainActivity.this, GameOver.class));
        } else if (newPosition > 100) {
            newPosition = currentPosition;
        }
        int[] from = new int[2];
        locationOnScreen(from, currentPosition);

        int[] to = new int[2];
        locationOnScreen(to, newPosition);

        TranslateAnimation animation = new TranslateAnimation(from[0], to[0], from[1], to[1]);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        findViewById(imageID).startAnimation(animation);

        return newPosition;
    }

    public int rollDice() {
        Random r = new Random(); // for random num genertor
        int num = r.nextInt(6) + 1;

        switch (num) {
            case 1: {
                dice.setImageResource(R.drawable.one);
                break;
            }
            case 2: {
                dice.setImageResource(R.drawable.two);
                break;
            }
            case 3: {
                dice.setImageResource(R.drawable.three);
                break;
            }
            case 4: {
                dice.setImageResource(R.drawable.four);
                break;
            }
            case 5: {
                dice.setImageResource(R.drawable.five);
                break;
            }
            case 6: {
                dice.setImageResource(R.drawable.six);
                break;
            }


        }

        return num;
    }

    private class SnakeOrLadder {
        private int head;
        private int tail;

        private SnakeOrLadder(int head, int tail) {
            this.head = head;
            this.tail = tail;
        }

        public int head() {
            return head;
        }

        public int tail() {
            return tail;
        }
    }

}
