package com.example.mireaproject;


import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MediaRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MediaRecordFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_PERMISSION = 100;
    private Button startRecordButton;
    private Button stopRecordButton;
    private MediaRecorder mediaRecorder;
    private File audioFile;
    private String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };
    private boolean isWork;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MediaRecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MediaRecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MediaRecordFragment newInstance(String param1, String param2) {
        MediaRecordFragment fragment = new MediaRecordFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_record, container, false);
        startRecordButton = view.findViewById(R.id.btnStart);
        stopRecordButton = view.findViewById(R.id.btnStop);
        startRecordButton.setOnClickListener(this);
        stopRecordButton.setOnClickListener(this);
// инициализация объекта MediaRecorder
        mediaRecorder = new MediaRecorder();
// проверка наличия разрешений на выполнение аудиозаписи и сохранения на карту памяти данных.
        isWork = hasPermissions(getActivity(), PERMISSIONS);
        if (!isWork) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS,
                    REQUEST_CODE_PERMISSION);
        }
        // Inflate the layout for this fragment
        return view;
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
            int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
// permission granted
            isWork = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnStart){
            try {
                Log.d("Media", "start");
                startRecordButton.setEnabled(false);
                stopRecordButton.setEnabled(true);
                stopRecordButton.requestFocus();
                startRecording();
            } catch (Exception e) {
                Log.e(TAG, "Caught io exception " + e.getMessage());
            }
        } else {
            Log.d("Media", "stop");

            startRecordButton.setEnabled(true);
            stopRecordButton.setEnabled(false);
            startRecordButton.requestFocus();
            stopRecording();
            processAudioFile();
        }

    }
    private void startRecording() throws IOException {
// проверка доступности sd - карты
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d(TAG, "sd-card success");
// выбор источника звука
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
// выбор формата данных
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
// выбор кодека
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            if (audioFile == null) {
// создание файла
                audioFile = new File(getActivity().getExternalFilesDir(
                        Environment.DIRECTORY_MUSIC), "mirea.3gp");
            }
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(getActivity(), "Recording started!", Toast.LENGTH_SHORT).show();
        }
    }
    private void stopRecording() {
        if (mediaRecorder != null) {
            Log.d(TAG, "stopRecording");
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            Toast.makeText(getActivity(), "You are not recording right now!", Toast.LENGTH_SHORT).show();
        }
    }
    private void processAudioFile() {
        Log.d(TAG, "processAudioFile");
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
// установка meta данных созданному файлу
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audioFile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audioFile.getAbsolutePath());
        ContentResolver contentResolver = getActivity().getContentResolver();
        Log.d(TAG, "audioFile: " + audioFile.canRead());
        Uri baseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(baseUri, values);
// оповещение системы о новом файле
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
    }
}
