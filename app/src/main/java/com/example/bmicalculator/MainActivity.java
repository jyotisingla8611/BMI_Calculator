package com.example.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

    private String TOAST_MESSAGE;

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
        heightSpinner(heightSpinner);
        weightSpinner(weightSpinner);

        TOAST_MESSAGE = getString(R.string.invalid_bmi_message);

        fetchAndUpdateFromSavedState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.HEIGHT_VALUE, heightValue.getText().toString());
        outState.putString(Constants.HEIGHT_UNIT, heightUnit.getText().toString());
        outState.putString(Constants.WEIGHT_VALUE, weightValue.getText().toString());
        outState.putString(Constants.WEIGHT_UNIT, weightUnit.getText().toString());
        outState.putInt(Constants.HEIGHT_COLOR, heightValue.getCurrentTextColor());
        outState.putInt(Constants.WEIGHT_COLOR, weightValue.getCurrentTextColor());
        outState.putBoolean(Constants.RESULT_VISIBILITY, false);
        if (result.getVisibility() == View.VISIBLE) {
            outState.putBoolean(Constants.RESULT_VISIBILITY, true);
            outState.putString(Constants.RESULT_VALUE, bmiValue.getText().toString());
            outState.putString(Constants.RESULT_TYPE, bmiResult.getText().toString());
            outState.putInt(Constants.RESULT_TYPE_COLOR, bmiResult.getCurrentTextColor());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
                int bmi = CalculateBmi();
                if (bmi == 1) {
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
                    weightValue.setText(getString(R.string.zero));
                } else if (heightValue.getCurrentTextColor() == getResources().getColor(R.color.orange)) {
                    heightValue.setText(getString(R.string.zero));
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
                            weightValue.setText(getString(R.string.zero));
                    }
                } else if (heightValue.getCurrentTextColor() == getResources().getColor(R.color.orange)) {
                    String heightInputValue = heightValue.getText().toString();
                    if (heightInputValue.length() != 0) {
                        heightInputValue = heightInputValue.substring(0, heightInputValue.length() - 1);
                        if (heightInputValue.length() != 0)
                            heightValue.setText(heightInputValue);
                        else
                            heightValue.setText(getString(R.string.zero));
                    }
                }
                break;
            }
        }
    }

    private int CalculateBmi() {
        double weight = WeightConverter(weightUnit.getText().toString(), weightValue.getText().toString());
        double height = HeightConverter(heightUnit.getText().toString(), heightValue.getText().toString());
        if (weight == 0 && height == 0 || height == 0 || weight == 0) {
            Toast.makeText(this, TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
            return -1;
        } else {
            double bmi = weight / (height * height);
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            bmiValue.setText(decimalFormat.format(bmi));
            String type = getType(decimalFormat.format(bmi));
            double parseDouble = Double.parseDouble(decimalFormat.format(bmi));
            if (parseDouble > 40.0 || parseDouble < 16.0) {
                Toast.makeText(this, TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
                return -1;
            }
            bmiResult.setText(type);
            if (type.equals(Constants.UNDERWEIGHT)) {
                bmiResult.setTextColor(getResources().getColor(R.color.blue));
            } else if (type.equals(Constants.NORMAL)) {
                bmiResult.setTextColor(getResources().getColor(R.color.green));
            } else
                bmiResult.setTextColor(getResources().getColor(R.color.orange));
        }
        return 1;
    }

    private String getType(String format) {
        double value = Double.parseDouble(format);
        if (value >= 16.0 && value < 18.5 || value < 16.0)
            return Constants.UNDERWEIGHT;
        else if (value >= 18.5 && value < 25.0)
            return Constants.NORMAL;
        else
            return Constants.OVERWEIGHT;
    }

    private void fetchAndUpdateFromSavedState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            heightValue.setText(savedInstanceState.getString(Constants.HEIGHT_VALUE));
            heightUnit.setText(savedInstanceState.getString(Constants.HEIGHT_UNIT));
            weightValue.setText(savedInstanceState.getString(Constants.WEIGHT_VALUE));
            weightUnit.setText(savedInstanceState.getString(Constants.WEIGHT_UNIT));
            heightValue.setTextColor(savedInstanceState.getInt(Constants.HEIGHT_COLOR));
            weightValue.setTextColor(savedInstanceState.getInt(Constants.WEIGHT_COLOR));
            if (savedInstanceState.getBoolean(Constants.RESULT_VISIBILITY)) {
                result.setVisibility(View.VISIBLE);
                keypad.setVisibility(View.INVISIBLE);
                bmiValue.setText(savedInstanceState.getString(Constants.RESULT_VALUE));
                bmiResult.setText(savedInstanceState.getString(Constants.RESULT_TYPE));
                bmiResult.setTextColor(savedInstanceState.getInt(Constants.RESULT_TYPE_COLOR));
            } else {
                keypad.setVisibility(View.VISIBLE);
                result.setVisibility(View.INVISIBLE);
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
            else if (str.length() < 3 || s.equals(Constants.DOT)) {
                textView.append(s);
            }
        } else {
            if (str.equals("0")) {
                textView.setText("0.");
            }
            if (str.length() <= 6 && !s.equals(Constants.DOT)) {
                int ind = textView.getText().toString().indexOf(Constants.DOT);
                if (textView.getText().toString().length() <= (ind + 2))
                    textView.append(s);
            }
        }
    }

    private void weightSpinner(Spinner spinner) {
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

    private void heightSpinner(Spinner spinner) {

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

    private double WeightConverter(String weight, String value) {
        double weightValue = Double.parseDouble(value);
        switch (weight) {
            case Constants.KILOGRAM:
                return weightValue;
            case Constants.POUND:
                return (weightValue * 0.453592);
            default:
                return -1;
        }
    }

    private double HeightConverter(String height, String value) {
        double heightValue = Double.parseDouble(value);
        switch (height) {
            case Constants.CENTIMETER:
                return (heightValue * 0.01);
            case Constants.METER:
                return heightValue;
            case Constants.FEET:
                return (heightValue * 0.3048);
            case Constants.INCH:
                return (heightValue * 0.0254);
            default:
                return -1;
        }
    }
}
