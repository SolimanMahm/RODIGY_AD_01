package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    TextInputLayout resultLayout;
    TextInputEditText display, result;
    CardView btn_clear, btn_delete, btn_mod, btn_division, btn_seven, btn_eight, btn_nine,
            btn_multiplication, btn_four, btn_five, btn_six, btn_minus, btn_one, btn_two,
            btn_three, btn_plus, btn_zero, btn_dot, btn_equal;

    String displayView = "0", resultView = "";

    NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);

    boolean start = false;
    boolean dot = false;
    boolean eq = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        display = findViewById(R.id.textInput);
        resultLayout = findViewById(R.id.resultLayout);
        result = findViewById(R.id.resultText);
        btn_clear = findViewById(R.id.clear);
        btn_delete = findViewById(R.id.delete);
        btn_mod = findViewById(R.id.mod);
        btn_division = findViewById(R.id.division);
        btn_multiplication = findViewById(R.id.multiplication);
        btn_minus = findViewById(R.id.minus);
        btn_plus = findViewById(R.id.plus);
        btn_equal = findViewById(R.id.equal);
        btn_zero = findViewById(R.id.zero);
        btn_one = findViewById(R.id.one);
        btn_two = findViewById(R.id.two);
        btn_three = findViewById(R.id.three);
        btn_four = findViewById(R.id.four);
        btn_five = findViewById(R.id.five);
        btn_six = findViewById(R.id.six);
        btn_seven = findViewById(R.id.seven);
        btn_eight = findViewById(R.id.eight);
        btn_nine = findViewById(R.id.nine);
        btn_dot = findViewById(R.id.dot);

        resultLayout.setVisibility(View.GONE);

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayView = "0";
                resultView = "";
                result.setText("");
                resultLayout.setVisibility(View.GONE);
                start = false;
                refactorDisplay();
                dot = false;
                display.setTextSize(48);
                display.setTextColor(getResources().getColor(R.color.black));
                result.setTextSize(24);
                result.setTextColor(getResources().getColor(R.color.gray));
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eq)return;
                if(displayView.equals("0.")){
                    displayView = "0";
                    resultView = "";
                    result.setText("");
                    resultLayout.setVisibility(View.GONE);
                    start = false;
                    refactorDisplay();
                    dot = false;
                }
                else if (start) {
                    if (displayView.length() == 1) {
                        displayView = "0";
                        resultView = "";
                        result.setText("");
                        resultLayout.setVisibility(View.GONE);
                        start = false;
                    } else {
                        if(displayView.charAt(displayView.length() - 1) == '.')dot = false;
                        displayView = displayView.substring(0, displayView.length() - 1);
                        char op = displayView.charAt(displayView.length() - 1);
                        if (!isOperator(op))
                            resultView = expressionEvaluation(infixToPostfix(displayView));
                        else
                            resultView = expressionEvaluation(infixToPostfix(displayView.substring(0, displayView.length() - 1)));
                        refactorResult();
                    }
                }
                refactorDisplay();
            }
        });

        btn_zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start) addNumber("0");
                refactorDisplay();
            }
        });

        btn_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dot) {
                    dot = true;
                    if(!start){
                        addNumber("0");
                    }
                    addNumber(".");
                }
            }
        });

        btn_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eq)start = false;
                eq = false;
                addNumber("1");
            }
        });

        btn_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eq)start = false;
                eq = false;
                addNumber("2");
            }
        });

        btn_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eq)start = false;
                eq = false;
                addNumber("3");
            }
        });

        btn_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eq)start = false;
                eq = false;
                addNumber("4");
            }
        });

        btn_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eq)start = false;
                eq = false;
                addNumber("5");
            }
        });

        btn_six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eq)start = false;
                eq = false;
                addNumber("6");
            }
        });

        btn_seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eq)start = false;
                eq = false;
                addNumber("7");
            }
        });

        btn_eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eq)start = false;
                eq = false;
                addNumber("8");
            }
        });

        btn_nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eq)start = false;
                eq = false;
                addNumber("9");
            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNumber("+");
                dot = false;
                eq = false;
            }
        });

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNumber("-");
                dot = false;
                eq = false;
            }
        });

        btn_multiplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNumber("*");
                dot = false;
                eq = false;
            }
        });

        btn_division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNumber("/");
                dot = false;
                eq = false;
            }
        });

        btn_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNumber("%");
                dot = false;
                eq = false;
            }
        });

        btn_equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                display.setTextSize(24);
                display.setTextColor(getResources().getColor(R.color.gray));
                result.setTextSize(48);
                result.setTextColor(getResources().getColor(R.color.black));
                displayView = resultView;
                eq = true;
            }
        });

    }

    private void refactorDisplay() {
        String number = "", newDisplayView = "";
        for (int i = 0; i < displayView.length(); i++) {
            if (isOperand(displayView.charAt(i))) number += displayView.charAt(i);
            else if (isOperator(displayView.charAt(i))) {
                newDisplayView += numberFormat.format(Double.parseDouble(number));
                newDisplayView += displayView.charAt(i);
                number = "";
            }
        }
        if (number != "") {
            newDisplayView += numberFormat.format((Double.parseDouble(number)));
            if (number.charAt(number.length() - 1) == '.') newDisplayView += '.';
        }
        display.setText(newDisplayView);
    }

    private void refactorResult() {
        result.setText("= " + numberFormat.format(Double.parseDouble(resultView)));
    }

    private void addNumber(String number) {
        if(!eq){
            display.setTextSize(48);
            display.setTextColor(getResources().getColor(R.color.black));
            result.setTextSize(24);
            result.setTextColor(getResources().getColor(R.color.gray));
        }
        if (!start) {
            displayView = number;
            start = true;
            resultLayout.setVisibility(View.VISIBLE);
        } else if (displayView.length() <= 11) {
            displayView += number;
        }
        if (!isOperator(number.charAt(0)))
            resultView = expressionEvaluation(infixToPostfix(displayView));
        else{
            display.setTextSize(48);
            display.setTextColor(getResources().getColor(R.color.black));
            result.setTextSize(24);
            result.setTextColor(getResources().getColor(R.color.gray));
        }
        refactorDisplay();
        refactorResult();
    }

    private String infixToPostfix(String expression) {
        Stack<Character> st = new Stack<>();
        String postfix = "", number = "";
        for (int i = 0; i < expression.length(); i++) {
            if (isOperand(expression.charAt(i))) {
                number += expression.charAt(i);
            } else if (isOperator(expression.charAt(i))) {
                postfix += number + " ";
                while (!st.empty() && hasHigherPrecedence(st.peek(), expression.charAt(i))) {
                    postfix += st.peek() + " ";
                    st.pop();
                }
                st.push(expression.charAt(i));
                number = "";
            }
        }
        postfix += number + " ";
        while (!st.empty()) {
            postfix += st.peek() + " ";
            st.pop();
        }
        return postfix;
    }

    private String expressionEvaluation(String expression) {
        Stack<Double> st = new Stack<>();
        String number = "";
        for (int i = 0; i < expression.length(); i++) {
            if (isOperand(expression.charAt(i))) {
                number += expression.charAt(i);
            } else if (expression.charAt(i) == ' ' && number != "") {
                st.push(Double.parseDouble(number));
                number = "";
            } else if (isOperator(expression.charAt(i))) {
                double num2 = st.peek();
                st.pop();
                double num1 = st.peek();
                st.pop();
                st.push(performOperation(num1, num2, expression.charAt(i)));
            }
        }
        return st.peek() + "";
    }

    private boolean isOperand(char c) {
        if ((c >= '0' && c <= '9') || c == '.') {
            return true;
        }
        return false;
    }

    private boolean isOperator(char c) {
        if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%') {
            return true;
        }
        return false;
    }

    private int getOperatorWeight(char op) {
        int weight = -1;
        switch (op) {
            case '+':
            case '-':
                weight = 1;
                break;
            case '*':
            case '/':
            case '%':
                weight = 2;
        }
        return weight;
    }

    private boolean hasHigherPrecedence(char op1, char op2) {
        return getOperatorWeight(op1) >= getOperatorWeight(op2);
    }

    private double performOperation(double num1, double num2, char op) {
        double ans = 0;
        switch (op) {
            case '+':
                ans = num1 + num2;
                break;
            case '-':
                ans = num1 - num2;
                break;
            case '*':
                ans = num1 * num2;
                break;
            case '/':
                ans = num1 / num2;
                break;
            case '%':
                ans = num1 % num2;
        }
        return ans;
    }

}