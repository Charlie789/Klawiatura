package com.example.klawiatura;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.MediaPlayer;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.content.Intent;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    KeyboardView keyboardView;
    Keyboard keyboard_1_page;
    Keyboard keyboard_2_page;
    @Override
    public View onCreateInputView() {
        SharedPreferences pre = getSharedPreferences("keyboard", MODE_PRIVATE);
        int theme = pre.getInt("page", 1);
        this.keyboardView = (KeyboardView) this.getLayoutInflater().inflate(R.layout.keyboard_view, null);
        this.keyboard_1_page = new Keyboard(this, R.xml.keyboard_1_page);
        this.keyboard_2_page = new Keyboard(this, R.xml.keyboard_2_page);

        if(theme == 1)
        {
            this.keyboardView.setKeyboard(this.keyboard_1_page);
        }else
        {
            this.keyboardView.setKeyboard(this.keyboard_2_page);
        }
        this.keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
//        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
//        keyboard_1_page = new Keyboard(this, R.xml.keyboard_1_page);
//        keyboard_2_page = new Keyboard(this, R.xml.keyboard_2_page);
//        keyboardView.setKeyboard(keyboard_1_page);
//        keyboardView.setOnKeyboardActionListener(this);
//        return keyboardView;
    }

    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);

        setInputView(onCreateInputView());
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection inputConnection = getCurrentInputConnection();

        if (inputConnection != null) {
            switch (primaryCode) {
                case Keyboard.KEYCODE_DELETE :
                    CharSequence selectedText = inputConnection.getSelectedText(0);
                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0);
                    } else {
                        inputConnection.commitText("", 1);
                    }
                    break;
                case KeyCodes.TEXT:
                    inputConnection.setComposingText("hello", 1);
                    inputConnection.finishComposingText();

                    break;
                case KeyCodes.CAMERA:
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    break;
                case KeyCodes.DING:
                    final MediaPlayer mp = MediaPlayer.create(this, R.raw.ding);
                    mp.start();

                    break;

                case KeyCodes.SAVE:
                    try {
                        File root = new File(Environment.getExternalStorageDirectory(), "Klawiatura");
                        if (!root.exists()) {
                            root.mkdirs();
                        }
                        File gpxfile = new File(root, "klawiatura.txt");
                        FileWriter writer = new FileWriter(gpxfile);
                        writer.append("wiadomość z aplikacji");
                        writer.flush();
                        writer.close();
                        Toast.makeText(this, "Zapisano", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;

                case KeyCodes.TOAST:
                    Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show();

                    break;

                case KeyCodes.CFG:
                    SharedPreferences sharedPref = getSharedPreferences("keyboard", MODE_PRIVATE);
                    int theme = sharedPref.getInt("page", 1);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    if(theme == 1){
                        editor.putInt("page", 2);
                    } else {
                        editor.putInt("page", 1);
                    }
                    editor.apply();
                    setInputView(onCreateInputView());

                    break;

                default:
                    char code = (char) primaryCode;
                    inputConnection.commitText(String.valueOf(code), 1);
            }
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
