package com.example.pract2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private EditText display;
    private Button prev;
    TextView textViewType;
    int index=0,index2=2;
    String op="", str_1, expression;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_ui);

        display = findViewById(R.id.dis);
        textViewType = findViewById(R.id.textViewType);
        display.setShowSoftInputOnFocus(false);
        prev = findViewById(R.id.prev);

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("Enter".equals(display.getText().toString())){
                    display.setText("");
                    display.setSelection(0);
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences shrd2=getSharedPreferences("expressions",MODE_PRIVATE);
                if(index2<0){
                    Toast.makeText(MainActivity.this, "Only 3 histories are stored", Toast.LENGTH_SHORT).show();
                    textViewType.setText("0");
                    expression="";
                    index2=2;
                }else {
                    String expr = shrd2.getString("expr" + String.valueOf(index2), "nothing");
                    index2 = index2 - 1;
                    textViewType.setText(expr);
                    expression=expr;
                }
            }
        });
    }


    private void text(String str){
        str_1 = display.getText().toString();
        int cursor = display.getSelectionStart();
        String en = "Enter";
        if(en.equals(display.getText().toString())){
            display.setText(str);
            display.setSelection(0);
        }
        else {
            display.setText(String.format("%s%s", str_1, str));
            display.setSelection(cursor + 1);
        }
    }

    public void zerobtn(View view){
        text("0");

    }

    public void btn_one(View view){
        text("1");

    }

    public void btn_two(View view){
        text("2");

    }

    public void btn_three(View view){
        text("3");

    }

    public void btn_four(View view){
        text("4");

    }

    public void btn_five(View view){
        text("5");

    }

    public void btn_six(View view){
        text("6");

    }

    public void btn_seven(View view){
        text("7");

    }

    public void btn_eight(View view){
        text("8");

    }

    public void btn_nine(View view){
        text("9");

    }

    public void btn_add(View view){
        text("+");

    }

    public void btn_sub(View view){
        text("-");

    }

    public void btn_mul(View view){
        text("x");

    }

    public void btn_div(View view){
        text("÷");

    }

    public void btn_equals(View view){
        expression = display.getText().toString();
        expression = expression.replaceAll("÷", "/");
        expression = expression.replaceAll("x", "*");

        SharedPreferences shrd=getSharedPreferences("expressions",MODE_PRIVATE);
        SharedPreferences.Editor editor=shrd.edit();


        char[] tokens = expression.toCharArray();

        Stack<Float> values = new
                Stack<Float>();
        Stack<Character> ops = new
                Stack<Character>();

        for (int i = 0; i < tokens.length; i++)
        {

            if (tokens[i] == ' ')
                continue;

            if (tokens[i] >= '0' &&
                    tokens[i] <= '9')
            {
                StringBuffer sbuf = new
                        StringBuffer();

                while (i < tokens.length &&
                        tokens[i] >= '0' &&
                        tokens[i] <= '9')
                    sbuf.append(tokens[i++]);
                values.push((float)Integer.parseInt(sbuf.
                        toString()));


                i--;
            }


            else if (tokens[i] == '(')
                ops.push(tokens[i]);

            else if (tokens[i] == ')')
            {
                while (ops.peek() != '(')
                    values.push(applyOp(ops.pop(),
                            values.pop(),
                            values.pop()));
                ops.pop();
            }

            else if (tokens[i] == '+' ||
                    tokens[i] == '-' ||
                    tokens[i] == '*' ||
                    tokens[i] == '/')
            {

                while (!ops.empty() &&
                        hasPrecedence(tokens[i],
                                ops.peek()))
                    values.push(applyOp(ops.pop(),
                            values.pop(),
                            values.pop()));

                ops.push(tokens[i]);
            }
        }

        while (!ops.empty())
            values.push(applyOp(ops.pop(),
                    values.pop(),
                    values.pop()));

        op = values.pop().toString();
        if(op.contains(".0") && op.indexOf(".")==op.length()-2)
        {
            int idx=op.indexOf(".");
            op=op.substring(0, idx);
        }
        display.setText(op);
        editor.putString("expr" + String.valueOf(index), expression);
        editor.apply();
        index = (index + 1) % 3;
//        String s = values.pop().toString();
        display.setSelection(display.getText().length());

    }

    public static boolean hasPrecedence(
            char op1, char op2)
    {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') &&
                (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }
    public static float applyOp(char op,
                              float b, float a)
    {
        switch (op)
        {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if(b!=0)
                    return a / b;
        }
        return 0;
    }


    public void btn_dot(View view){
        text(".");

    }

    public void btn_open_para(View view){
        text("(");

    }

    public void btn_closed_para(View view){
        text(")");

    }

    public void btn_del(View view){
        int cursor = display.getSelectionStart();
        int textLen = display.getText().length();
        if(cursor!=0 && textLen!=0){
            SpannableStringBuilder selection = (SpannableStringBuilder)display.getText();
            selection.replace(cursor-1, cursor, "");
            display.setText(selection);
            display.setSelection(cursor-1);
        }

    }

    public void btn_clr(View view){

        display.setText("");

    }



    public void showHistory(View view) {
        SharedPreferences shrd2=getSharedPreferences("expressions",MODE_PRIVATE);
        if(index2<1){
            Toast.makeText(this, "Only 3 histories are stored", Toast.LENGTH_SHORT).show();
            textViewType.setText("0");
            op="";
            index2=2;
        }else {
            String expr = shrd2.getString("expr" + String.valueOf(index2), "nothing");
            index2 = index2 - 1;
            textViewType.setText(expr);
            op=expr;
        }
    }

}