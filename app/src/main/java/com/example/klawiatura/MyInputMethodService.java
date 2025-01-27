package com.example.klawiatura;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputMethodManager;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.content.Intent;
import android.widget.Toast;
import android.media.AudioManager;
import android.os.Vibrator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    KeyboardView keyboardView;
    Keyboard keyboard_1_page;
    Keyboard keyboard_2_page;
    Vibrator vibe;
    @Override
    public View onCreateInputView() {
        SharedPreferences pre = getSharedPreferences("keyboard", MODE_PRIVATE);
        int theme = pre.getInt("page", 1);
        this.keyboardView = (KeyboardView) this.getLayoutInflater().inflate(R.layout.keyboard_view, null);
        this.keyboard_1_page = new Keyboard(this, R.xml.keyboard_1_page);
        this.keyboard_2_page = new Keyboard(this, R.xml.keyboard_2_page);

        this.vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if(theme == 1)
        {
            this.keyboardView.setKeyboard(this.keyboard_1_page);
        }else
        {
            this.keyboardView.setKeyboard(this.keyboard_2_page);
        }
        this.keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
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
        CharSequence currentText;
        CharSequence beforCursorText;
        CharSequence afterCursorText;

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
                    currentText = inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text;
                    beforCursorText = inputConnection.getTextBeforeCursor(currentText.length(), 0);
                    afterCursorText = inputConnection.getTextAfterCursor(currentText.length(), 0);
                    inputConnection.deleteSurroundingText(beforCursorText.length(), afterCursorText.length());
                    inputConnection.setComposingText("Hello, I'm custom bluetooth keyboard", 1);
                    inputConnection.finishComposingText();

                    break;
                case KeyCodes.CAMERA:
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    break;
                case KeyCodes.DING:



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

                case KeyCodes.NFC_STATUS:
                    NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
                    NfcAdapter adapter = manager.getDefaultAdapter();
                    if (adapter == null){
                        Toast.makeText(this, "Brak modułu NFC", Toast.LENGTH_LONG).show();
                    } else if (adapter.isEnabled()) {
                        Toast.makeText(this, "NFC jest włączone", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "NFC jest wyłączone", Toast.LENGTH_LONG).show();
                    }

                    break;

                case KeyCodes.NFC_ON:
                    NfcManager m_manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
                    NfcAdapter m_adapter = m_manager.getDefaultAdapter();

                    if (m_adapter == null){
                        Toast.makeText(this, "Brak modułu NFC", Toast.LENGTH_LONG).show();
                    } else if (m_adapter.isEnabled()) {
                        Toast.makeText(this, "NFC jest włączone. Proszę o wyłączenie NFC, a następnie powrót do aplikacji", Toast.LENGTH_LONG).show();
                        Intent intentNFC = new Intent(Settings.ACTION_NFC_SETTINGS);
                        intentNFC.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentNFC);
                    } else {
                        Toast.makeText(this, "NFC jest wyłączone. Proszę o włączenie NFC, a następnie powrót do aplikacji", Toast.LENGTH_LONG).show();
                        Intent intentNFC = new Intent(Settings.ACTION_NFC_SETTINGS);
                        intentNFC.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentNFC);
                    }

                    break;

                case KeyCodes.WIFI:
                    WifiManager wifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(!wifiManager.isWifiEnabled());

                    break;

                case KeyCodes.CODE1:
                    currentText = inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text;
                    beforCursorText = inputConnection.getTextBeforeCursor(currentText.length(), 0);
                    afterCursorText = inputConnection.getTextAfterCursor(currentText.length(), 0);
                    inputConnection.deleteSurroundingText(beforCursorText.length(), afterCursorText.length());
                    inputConnection.setComposingText("616000010000123", 1);
                    inputConnection.finishComposingText();

                    break;

                case KeyCodes.CODE2:
                    currentText = inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text;
                    beforCursorText = inputConnection.getTextBeforeCursor(currentText.length(), 0);
                    afterCursorText = inputConnection.getTextAfterCursor(currentText.length(), 0);
                    inputConnection.deleteSurroundingText(beforCursorText.length(), afterCursorText.length());
                    inputConnection.setComposingText("616000010000124", 1);
                    inputConnection.finishComposingText();

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

                case KeyCodes.KEYB:
                    InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
                    imeManager.showInputMethodPicker();

                    break;

                default:
                    char code = (char) primaryCode;
                    inputConnection.commitText(String.valueOf(code), 1);
            }
            AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            switch (audio.getRingerMode())
            {
                case AudioManager.RINGER_MODE_NORMAL:
                    final MediaPlayer mp = MediaPlayer.create(this, R.raw.ding);
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                    mp.start();
                    break;
                case AudioManager.RINGER_MODE_SILENT:
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    vibe.vibrate(50);
                    break;
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
