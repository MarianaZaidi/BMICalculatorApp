package com.example.bmicalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPref;

    private static DecimalFormat df = new DecimalFormat("0.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText weight, height;
        TextView result, bmi1;
        Button btn, rst;

        weight=(EditText) findViewById(R.id.weight);
        height=(EditText) findViewById(R.id.height);
        bmi1=(TextView) findViewById(R.id.bmi1);
        result=(TextView) findViewById(R.id.result);
        btn=(Button) findViewById(R.id.btn);
        rst=(Button) findViewById(R.id.rst);

        sharedPref = this.getSharedPreferences("height", Context.MODE_PRIVATE);
        sharedPref = this.getSharedPreferences("weight", Context.MODE_PRIVATE);
        sharedPref = this.getSharedPreferences("result1", Context.MODE_PRIVATE);
        sharedPref = this.getSharedPreferences("category", Context.MODE_PRIVATE);

        float hei = sharedPref.getFloat("height", 0);
        float weg = sharedPref.getFloat("weight", 0);
        String bmi = sharedPref.getString("result1", "");
        String cat = sharedPref.getString("category", "");

        height.setText(String.valueOf(hei));
        weight.setText(String.valueOf(weg));
        result.setText(String.valueOf(bmi));
        bmi1.setText(cat);
        btn.setOnClickListener(new View.OnClickListener()
        {

         @Override
         public void onClick(View v){
         String weg = weight.getText().toString();
         String hei = height.getText().toString();

         if (weg.equals(""))
         {
             weight.setError("Please Enter Your Weight ");
             weight.requestFocus();
             return;
         }
         if (hei.equals("")) {
             height.setError("Please Enter Your Height ");
             height.requestFocus();
             return;
         }

         float w = Float.parseFloat(weg);
         float h = Float.parseFloat(hei);
         float hcm = h / 100;

         float bmiValue = BMICalculator(w, hcm);

         String bmi = df.format(bmiValue);
         bmi1.setText(interpreteBMI(bmiValue));
         result.setText(bmi);

         SharedPreferences.Editor editor = sharedPref.edit();
         editor.putFloat("weight", w);
         editor.putFloat("height", h);
         editor.putString("result1", bmi);
         editor.putString("category", interpreteBMI(bmiValue));
         editor.apply();
                                   }
                               });

        rst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                height.setText("");
                weight.setText("");
                result.setText("");
                }

        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.calculator)
        {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        else if (id == R.id.about)
        {
            Intent intent = new Intent(MainActivity.this, profile.class);
            startActivity(intent);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    public float BMICalculator (float weight, float height){
        return weight / (height * height);
    }
    public String interpreteBMI(float bmiValue){
        if (bmiValue <18.5){
            return "Underweight (Malnutrition Risk)";
        }
        else if (bmiValue >=18.5 && bmiValue <=24.9){
            return "Normal Weight (Low Risk)";
        }
        else if (bmiValue >=25 && bmiValue <=29.9){
            return "Overweight (Enhanced Risk)";
        }
        else if (bmiValue >=30 && bmiValue <=34.9){
            return "Moderately Obese (Medium Risk)";
        }
        else if (bmiValue >=35 && bmiValue <=39.9){
            return "Severely Obese (High Risk)";
        }
        else
            return "Very Severely Obese (Very High Risk)";
    }

}