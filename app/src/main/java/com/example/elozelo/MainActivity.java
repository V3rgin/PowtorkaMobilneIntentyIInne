package com.example.elozelo;

import static com.example.elozelo.NotificationHelper.sendNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button wyslijPow, otworzStr, wybKont, pokazDialog;
    private TextView text;
    private static final String CHANNEL_ID = "my_channel_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        NotificationHelper.createNotificationChannels(this);
        wyslijPow = findViewById(R.id.wyslijPow);
        wyslijPow.setOnClickListener(v -> sendNotification());

        otworzStr = findViewById(R.id.otworzStr);
        otworzStr.setOnClickListener(v->openWebpage("https://www.google.com"));

        wybKont = findViewById(R.id.wybKont);
        wybKont.setOnClickListener(v->dialPhoneNumber("506248020"));

        pokazDialog = findViewById(R.id.pokazDialog);
        pokazDialog.setOnClickListener(v->showAlertDialogue());

        text = findViewById(R.id.text);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "elozelo";
            String description = "elozelo";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
                return;
            }
        }
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Nowe Powiadomienie")
                .setContentText("To jest przykładowe powiedomienie")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
    private void openWebpage(String url){
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        try {
            startActivity(intent);
        } catch (Exception e){
            Log.e("ERROR", "Brak aplikacji do obslugi intencji", e);
        }
    }
    private void dialPhoneNumber(String phoneNumber){
        text.setText(phoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData((Uri.parse("tel:" + phoneNumber)));
        try {
            startActivity(intent);
        } catch (Exception e){
            Log.e("ERROR", "Brak aplikacji do obslugi intencji", e);
        }
    }
    private void showAlertDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elo żelo");
        builder.setMessage("Priviet ziele");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
                Toast.makeText(MainActivity.this,
                        "Kliknieto przycisk", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this,
                        "Kliknieto anuluj", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

}