package com.poc.dialogflow.api.ai;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.poc.dialogflow.api.ai.utils.Config;
import com.poc.dialogflow.api.ai.utils.PermissionUtils;
import com.poc.dialogflow.api.ai.utils.TTS;
import ai.api.android.AIConfiguration;
import ai.api.android.GsonFactory;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;
import ai.api.ui.AIButton;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AIButton.AIButtonListener {

    private final int REQUEST_AUDIO = 100;
    private Gson gson;
    @BindView(R.id.micButton)
    AIButton micButton;
    @BindView(R.id.tvTextSample)
    TextView tvTextSample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void onResult(ai.api.model.AIResponse response) {

        // this is example how to get different parts of result object
        final Status status = response.getStatus();
        final Result result = response.getResult();
        final String speech = result.getFulfillment().getSpeech();
        tvTextSample.setText(speech);
        TTS.speak(speech);
        final Metadata metadata = result.getMetadata();
    }

    @Override
    public void onError(ai.api.model.AIError error) {
        tvTextSample.setText(error.getMessage());
    }

    @Override
    public void onCancelled() {
        tvTextSample.setText("");
    }

    private void init() {
        TTS.init(this);
        gson = GsonFactory.getGson();
        if (PermissionUtils.hasPermission(Manifest.permission.RECORD_AUDIO, this)) {
            initDialogFlow();
        } else {
            PermissionUtils.requestPermission(Manifest.permission.RECORD_AUDIO, REQUEST_AUDIO, this);
        }
    }


    private void initDialogFlow() {

        final AIConfiguration config = new AIConfiguration(Config.WEATHER_APP_ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        config.setRecognizerStartSound(getResources().openRawResourceFd(R.raw.test_start));
        config.setRecognizerStopSound(getResources().openRawResourceFd(R.raw.test_stop));
        config.setRecognizerCancelSound(getResources().openRawResourceFd(R.raw.test_cancel));


        micButton.initialize(config);
        micButton.setResultsListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_AUDIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initDialogFlow();
            } else {
                Toast.makeText(this, "Audio permission is denied", Toast.LENGTH_LONG);
            }
        }
    }
}
