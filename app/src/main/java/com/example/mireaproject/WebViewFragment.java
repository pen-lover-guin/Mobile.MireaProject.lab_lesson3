package com.example.mireaproject;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Toolbar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView result;
    private EditText number, number2;
    private ToggleButton button0, button1, button2, button3;
    private Button btnRes;


    public WebViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WebViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WebViewFragment newInstance(String param1, String param2) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("CutPasteId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        result = view.findViewById(R.id.textView2);
        number = view.findViewById(R.id.editText);
        number2 = view.findViewById(R.id.editText2);
        button0 = view.findViewById(R.id.toggleButton);
        button1 = view.findViewById(R.id.toggleButton2);
        button2 = view.findViewById(R.id.toggleButton3);
        button3 = view.findViewById(R.id.toggleButton4);
        btnRes = view.findViewById(R.id.button);
        btnRes.setOnClickListener(this);
      return view;
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }

    public void onClick(View view) {
      calculate();

    }

    @SuppressLint("SetTextI18n")
    public void calculate() {

        int n1 = Integer.parseInt(number.getText().toString());
        int n2 = Integer.parseInt(number2.getText().toString());


        if (button0.isChecked()) {
            int res = n1 + n2;
            result.setText(Integer.toString(res));
        }
        if (button1.isChecked())
        {
            int res = n1 - n2;
            result.setText(Integer.toString(res));
        }
        if (button2.isChecked())
        {
            int res = n1 * n2;
            result.setText(Integer.toString(res));
        }
        if (button3.isChecked())
        {
            int res = n1 / n2;
            result.setText(Integer.toString(res));
        }
    }
}








