package com.nomadlab.class_android_v3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private Button addBtn;
    private Button initBtn;
    private Button runBtn;
    private NumberPicker numberPicker;
    private boolean didRun = false;
    private final Set<Integer> pickerNumberSet = new HashSet<>();
    private final ArrayList<TextView> numberTextViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        addEventListener();
    }

    private void getRandom1() {
        Random random = new Random();
        int number = random.nextInt(45) + 1;
        int[] lottoNumbers = new int[6];
        Log.d("TAG", "number : " + number);
        for(int i = 0; i < lottoNumbers.length; i ++) {
            int selectedNumber = random.nextInt(45) + 1;
            lottoNumbers[i] = selectedNumber;
            // 중복된 값이 있는지 확인
            for (int j = 0; j < i; j++) {
                if (lottoNumbers[i] == lottoNumbers[j]) {
                    i = i - 1;
                    break;
                }
            }
        }
        Arrays.sort(lottoNumbers);
        for(int i = 0; i < lottoNumbers.length; i++) {
            Log.d("TAG", "lottoNumber : " + lottoNumbers[i]);
        }
    }

    private void getRandom2() {
        Random random = new Random();
        ArrayList<Integer> list = new ArrayList<>();
        while (list.size() < 6) {
            int number = random.nextInt(45) + 1;
            if (list.contains(number)) {
                continue;
            }
            list.add(number);
        }
        Collections.sort(list);
        Log.d("TAG", "list : " + list.toString());
    }


    private void initData() {
        addBtn = findViewById(R.id.addButton);
        initBtn = findViewById(R.id.initButton);
        runBtn = findViewById(R.id.runButton);
        // 범위가 정해져 있지 않아 정상 동작이 되지 않는다.
        numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(45);

        numberTextViewList.add(findViewById(R.id.textView1));
        numberTextViewList.add(findViewById(R.id.textView2));
        numberTextViewList.add(findViewById(R.id.textView3));
        numberTextViewList.add(findViewById(R.id.textView4));
        numberTextViewList.add(findViewById(R.id.textView5));
        numberTextViewList.add(findViewById(R.id.textView6));
    }

    private Drawable setTextViewBackground(int number) {

        Drawable drawable;
        // Drawable Resource 가져 오는 방법
//        Drawable drawableId = ContextCompat.getDrawable(this, R.drawable.round_background_1);
        if(number <= 20) {
            drawable = ContextCompat.getDrawable(this, R.drawable.round_background_1);
        } else {
            drawable = ContextCompat.getDrawable(this, R.drawable.round_background_2);
        }
        return drawable;
    }

    private void addEventListener() {
        runBtn.setOnClickListener(view -> {
            List<Integer> list = getRandomNumber();
            // numberPickerSet 에 저장된 값 더하기
            list.addAll(pickerNumberSet);
            Collections.sort(list);
            Log.d("TAG", list.toString());
            didRun = true;

            // xml 에 출력
            for(int i = 0; i < list.size(); i++) {
                // setText -> int 셋팅시 오류 확인
                numberTextViewList.get(i).setText(String.valueOf(list.get(i)));
                numberTextViewList.get(i).setVisibility(View.VISIBLE);
                numberTextViewList.get(i).setBackground(setTextViewBackground(list.get(i)));
            }

        });
        addBtn.setOnClickListener(view -> {
            int selectedNumber = numberPicker.getValue();
            Log.d("TAG", selectedNumber + "");
            // 예외처리
            if(didRun) {
                Toast.makeText(this, "초기화 후에 시도해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            // 번호는 다섯개 까지만 선택 가능하다.
            if(pickerNumberSet.size() >= 5) {
                Toast.makeText(this, "번호는 5개까지만 선택 가능합니다", Toast.LENGTH_SHORT).show();
                return;
            }
            // 포함 여부 확인
            if(pickerNumberSet.contains(selectedNumber)) {
                Toast.makeText(this, "이미 선택한 번호 입니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // xml 에 tv 를 가지고 온다.
            // 주의! 초기 0 부터 시작 : pickerNumberSet.size()
            TextView textView = numberTextViewList.get(pickerNumberSet.size());
            textView.setVisibility(View.VISIBLE);
            textView.setText(String.valueOf(selectedNumber));
            textView.setBackground(setTextViewBackground(selectedNumber));

            pickerNumberSet.add(selectedNumber);

        });

        initBtn.setOnClickListener(view -> {
            didRun = false;
            pickerNumberSet.clear();
            for (TextView tv: numberTextViewList) {
                tv.setVisibility(View.GONE);
            }
        });
    }



    private List<Integer> getRandomNumber() {
        ArrayList<Integer> numberList = new ArrayList<>();
        for (int i = 1; i < 46; i++) {
            // 추후
            if(pickerNumberSet.contains(i)) {
                continue;
            }
            numberList.add(i);
        }
        Collections.shuffle(numberList);
//        Log.d("TAG", numberList.toString());
//        Log.d("TAG", numberList.subList(0, 6).toString());
//        return numberList.subList(0, 6);
        return numberList.subList(0, 6 - pickerNumberSet.size());
    }

}