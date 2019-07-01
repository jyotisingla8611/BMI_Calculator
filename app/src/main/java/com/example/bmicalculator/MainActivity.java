package com.example.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TOAST_MESSAGE = "Invalid BMI! Please Check your Input";
    private static final String UNDERWEIGHT = "Underweight";
    private static final String NORMAL = "Normal";
    private static final String OVERWEIGHT = "Over Weight";
    private static final String DOT = ".";
    private static final String KILOGRAM = "Kilogram";
    private static final String POUND = "Pound";
    private static final String CENTIMETER = "Centimeter";
    private static final String METER = "Meter";
    private static final String FEET = "Feet";
    private static final String INCH = "Inch";
    private static final String HEIGHT_VALUE = "height_Value";
    private static final String HEIGHT_UNIT = "height_Unit";
    private static final String WEIGHT_VALUE = "weight_Value";
    private static final String WEIGHT_UNIT = "weight_Unit";
    private static final String HEIGHT_COLOR = "height_color";
    private static final String WEIGHT_COLOR = "weight_color";
    private static final String RESULT_VISIBILITY = "result_visibility";
    private static final String RESULT_VALUE = "result_value";
    private static final String RESULT_TYPE = "result_type";
    private static final String RESULT_TYPE_COLOR = "result_type_color";

    private boolean heightBoolean = false;
    private boolean weightBoolean = true;
    private LinearLayout keypad;
    private ConstraintLayout result;
    private TextView weightValue;
    private TextView heightValue;
    private Spinner weightSpinner;
    private Spinner heightSpinner;
    private TextView bmiValue;
    private TextView bmiResult;
    private TextView heightUnit;
    private TextView weightUnit;

    void weightSpinner(Spinner spinner) {
        ArrayAdapter<String> weight_adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.weight_units));
        weight_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(weight_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                weightUnit = findViewById(R.id.weight_unit);
                weightUnit.setText(selected);
                heightValue.setTextColor(getResources().getColor(R.color.black));
                weightValue.setTextColor(getResources().getColor(R.color.orange));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    void heightSpinner(Spinner spinner) {

        ArrayAdapter<String> height_adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.height_units));
        height_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(height_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                heightUnit = findViewById(R.id.height_unit);
                heightUnit.setText(selected);
                heightValue.setTextColor(getResources().getColor(R.color.orange));
                weightValue.setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    double WeightConverter(String weight, String value) {
        double weightValue = Double.parseDouble(value);
        switch (weight) {
            case KILOGRAM:
                return weightValue;
            case POUND:
                return (weightValue * 0.453592);
            default:
                return -1;
        }
    }

    double HeightConverter(String height, String value) {
        double heightValue = Double.parseDouble(value);
        switch (height) {
            case CENTIMETER:
                return (heightValue * 0.01);
            case METER:
                return heightValue;
            case FEET:
                return (heightValue * 0.3048);
            case INCH:
                return (heightValue * 0.0254);
            default:
                return -1;
        }
    }

    int CalculateBmi() {
        double weight = WeightConverter(weightUnit.getText().toString(), weightValue.getText().toString());
        double height = HeightConverter(heightUnit.getText().toString(), heightValue.getText().toString());
        if (weight == 0 && height == 0 || height == 0 || weight == 0) {
            Toast.makeText(getApplicationContext(), TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
            return -1;
        } else {
            double bmi = weight / (height * height);
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            bmiValue.setText(decimalFormat.format(bmi));
            String type = getType(decimalFormat.format(bmi));
            double parseDouble = Double.parseDouble(decimalFormat.format(bmi));
            if (parseDouble > 40.0 || parseDouble < 16.0) {
                Toast.makeText(getApplicationContext(), TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
                return -1;
            }
            bmiResult.setText(type);
            if (type.equals(UNDERWEIGHT)) {
                bmiResult.setTextColor(getResources().getColor(R.color.blue));
            } else if (type.equals(NORMAL)) {
                bmiResult.setTextColor(getResources().getColor(R.color.green));
            } else
                bmiResult.setTextColor(getResources().getColor(R.color.orange));
        }
        return 1;
    }

    private String getType(String format) {
        double value = Double.parseDouble(format);
        if (value >= 16.0 && value < 18.5 || value < 16.0)
            return UNDERWEIGHT;
        else if (value >= 18.5 && value < 25.0)
            return NORMAL;
        else
            return OVERWEIGHT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keypad = findViewById(R.id.keypad);
        result = findViewById(R.id.output_layout);
        weightValue = findViewById(R.id.weight_value);
        heightValue = findViewById(R.id.height_value);
        weightSpinner = findViewById(R.id.weight_spinner);
        heightSpinner = findViewById(R.id.height_spinner);
       heightUnit = findViewById(R.id.height_unit);
        weightUnit = findViewById(R.id.weight_unit);
        bmiValue = findViewById(R.id.bmi_calculated_value);
        bmiResult = findViewById(R.id.bmi_type);
        weightSpinner(weightSpinner);
        heightSpinner(heightSpinner);

        if (savedInstanceState != null) {
            heightValue.setText(savedInstanceState.getString(HEIGHT_VALUE));
            heightUnit.setText(savedInstanceState.getString(HEIGHT_UNIT));
            weightValue.setText(savedInstanceState.getString(WEIGHT_VALUE));
            weightUnit.setText(savedInstanceState.getString(WEIGHT_UNIT));
            heightValue.setTextColor(savedInstanceState.getInt(HEIGHT_COLOR));
            weightValue.setTextColor(savedInstanceState.getInt(WEIGHT_COLOR));
            if (savedInstanceState.getBoolean(RESULT_VISIBILITY)) {
                result.setVisibility(View.VISIBLE);
                keypad.setVisibility(View.INVISIBLE);
                bmiValue.setText(savedInstanceState.getString(RESULT_VALUE));
                bmiResult.setText(savedInstanceState.getString(RESULT_TYPE));
                bmiResult.setTextColor(savedInstanceState.getInt(RESULT_TYPE_COLOR));
            } else {
                keypad.setVisibility(View.VISIBLE);
                result.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(HEIGHT_VALUE, heightValue.getText().toString());
        outState.putString(HEIGHT_UNIT, heightUnit.getText().toString());
        outState.putString(WEIGHT_VALUE, weightValue.getText().toString());
        outState.putString(WEIGHT_UNIT, weightUnit.getText().toString());
        outState.putInt(HEIGHT_COLOR, heightValue.getCurrentTextColor());
        outState.putInt(WEIGHT_COLOR, weightValue.getCurrentTextColor());
        outState.putBoolean(RESULT_VISIBILITY, false);
        if (result.getVisibility() == View.VISIBLE) {
            outState.putBoolean(RESULT_VISIBILITY, true);
            outState.putString(RESULT_VALUE, bmiValue.getText().toString());
            outState.putString(RESULT_TYPE, bmiResult.getText().toString());
            outState.putInt(RESULT_TYPE_COLOR, bmiResult.getCurrentTextColor());
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button_five:
            case R.id.button_six:
            case R.id.button_seven:
            case R.id.button_eight:
            case R.id.button_nine:
            case R.id.button_zero:
            case R.id.button_one:
            case R.id.button_two:
            case R.id.button_three:
            case R.id.button_four:
            case R.id.button_dot: {
                if (weightValue.getCurrentTextColor() == getResources().getColor(R.color.orange)) {
                    String weight = ((TextView) view).getText().toString();
                    updateValues(weightValue, weight);
                    break;
                } else {
                    String height = ((TextView) view).getText().toString();
                    updateValues(heightValue, height);
                    break;
                }
            }
            case R.id.button_go: {
                int calculatedBmi = CalculateBmi();
                if (calculatedBmi == 1) {
                    keypad.setVisibility(View.INVISIBLE);
                    result.setVisibility(View.VISIBLE);
                }
                break;
            }
            case R.id.weightValues: {
                if (weightValue.getCurrentTextColor() != getResources().getColor(R.color.orange)) {
                    heightBoolean = false;
                    weightBoolean = true;
                }
                heightValue.setTextColor(getResources().getColor(R.color.black));
                weightValue.setTextColor(getResources().getColor(R.color.orange));
                if (keypad.getVisibility() == View.INVISIBLE) {
                    keypad.setVisibility(View.VISIBLE);
                    result.setVisibility(View.INVISIBLE);
                }
                break;
            }
            case R.id.heightValues: {
                if (heightValue.getCurrentTextColor() != getResources().getColor(R.color.orange)) {
                    heightBoolean = true;
                    weightBoolean = false;
                }
                weightValue.setTextColor(getResources().getColor(R.color.black));
                heightValue.setTextColor(getResources().getColor(R.color.orange));
                if (keypad.getVisibility() == View.INVISIBLE) {
                    keypad.setVisibility(View.VISIBLE);
                    result.setVisibility(View.INVISIBLE);
                }
                break;

            }
            case R.id.weightLayout: {
                weightSpinner.performClick();
                break;
            }
            case R.id.heightLayout: {
                heightSpinner.performClick();
                break;
            }
            case R.id.button_clear_all: {
                if (weightValue.getCurrentTextColor() == getResources().getColor(R.color.orange)) {
                    weightValue.setText("0");
                } else if (heightValue.getCurrentTextColor() == getResources().getColor(R.color.orange)) {
                    heightValue.setText("0");
                }
                break;
            }
            case R.id.button_erase: {
                if (weightValue.getCurrentTextColor() == getResources().getColor(R.color.orange)) {
                    String weightInputValue = weightValue.getText().toString();
                    if (weightInputValue.length() != 0) {
                        weightInputValue = weightInputValue.substring(0, weightInputValue.length() - 1);
                        if (weightInputValue.length() != 0)
                            weightValue.setText(weightInputValue);
                        else
                            weightValue.setText("0");
                    }
                } else if (heightValue.getCurrentTextColor() == getResources().getColor(R.color.orange)) {
                    String heightInputValue = heightValue.getText().toString();
                    if (heightInputValue.length() != 0) {
                        heightInputValue = heightInputValue.substring(0, heightInputValue.length() - 1);
                        if (heightInputValue.length() != 0)
                            heightValue.setText(heightInputValue);
                        else
                            heightValue.setText("0");
                    }
                }
                break;
            }
        }
    }

    private void updateValues(TextView textView, String s) {

        if (heightBoolean) {
            textView.setText("0");
            heightBoolean = false;
        } else if (weightBoolean) {
            textView.setText("0");
            weightBoolean = false;
        }

        String str = textView.getText().toString();
        if (!str.contains(".")) {
            if (str.equals("0")) {
                textView.setText("");
            }
            if (s.equals(".") && (str.equals("") || str.equals("0")))
                textView.setText("0.");
            else if (str.length() < 3 || s.equals(DOT)) {
                textView.append(s);
            }
        } else {
            if (str.equals("0")) {
                textView.setText("0.");
            }
            if (str.length() <= 6 && !s.equals(DOT)) {
                int ind = textView.getText().toString().indexOf(DOT);
                if (textView.getText().toString().length() <= (ind + 2))
                    textView.append(s);
            }
        }
    }
}
