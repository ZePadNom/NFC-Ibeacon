package com.example.maxime.mynfsapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {
    IntentFilter[] filters;
    String[][] techs;
    PendingIntent pendingIntent;
    NfcAdapter adapter;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pendingIntent = PendingIntent.getActivity(this,0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter mifare = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        filters = new IntentFilter[]{mifare};
        techs = new String[][] { new String[] { NfcA.class.getName() } };
        adapter = NfcAdapter.getDefaultAdapter(this);

        imageView = (ImageView) findViewById(R.id.imageview);

        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);

                imageView.startAnimation(rotation);
            }
        });
    }

    public void onResume(){
        super.onResume();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);

        imageView.startAnimation(animation);

        adapter.enableForegroundDispatch(this, pendingIntent, filters, techs);
    }

    public void onPause(){
        super.onPause();
        adapter.disableForegroundDispatch(this);
    }

    public void onNewIntent(Intent intent){
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte[] id = tag.getId();
        ByteBuffer wrapped = ByteBuffer.wrap(id);
        int signedInt = wrapped.getInt();
        long number = signedInt & 0xffffffffl;
        Evt(number);
    }

    public void Evt(long number){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);

        imageView.startAnimation(animation);

        if(number == 69673562 || number == 72885082){
            Toast.makeText(MainActivity.this, "Identification acceptée", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, NextActivity.class);
            startActivity(i);
        }else {
            Toast.makeText(MainActivity.this, "Votre carte ne permet pas de vous identifier", Toast.LENGTH_SHORT).show();
        }
    }

}
