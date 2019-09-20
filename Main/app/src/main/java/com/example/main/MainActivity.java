package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private boolean clicked;
    private Button colorChanger;
    private ImageView imageChanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorChanger = findViewById(R.id.button);
        imageChanger = findViewById(R.id.image);
        clicked = false;
    }

        public void changeColor(View view)
        {
            if(!clicked)
                {
                colorChanger.setText(getResources().getText(R.string.btn_clicked));
                colorChanger.setBackgroundColor(getResources().getColor(R.color.colorRed,getResources().newTheme()));
                imageChanger.setImageResource(R.drawable.image1);
                clicked=true;

                }
            else
                {
                colorChanger.setText(getResources().getText(R.string.btn_not_clicked));
                colorChanger.setBackgroundColor(getResources().getColor(R.color.colorGray,getResources().newTheme()));
                imageChanger.setImageResource(R.drawable.image2);
                clicked=false;
               }
        }

    }
