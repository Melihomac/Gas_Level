package com.example.gas_level;

import static androidx.core.os.LocaleListCompat.create;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    String text;
    TextView textView;

    NotificationManagerCompat notificationManagerCompat;
    Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);

        RequestQueue queue = Volley.newRequestQueue(this);
        Thread Tiret = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://api.thingspeak.com/channels/1920200/feeds.json?results=2";
                while (true) {
                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jObject = new JSONObject(response);
                                        JSONArray jArray = jObject.getJSONArray("feeds");
                                        JSONObject result = jArray.getJSONObject(jArray.length() - 1);
                                        textView.setText(result.getString("field1"));
                                        String text = textView.getText().toString();

                                        String[] ar = text.split("[.]");
                                        int floatText = Integer.parseInt(ar[0]);

//                                        char first = text.charAt(0);
//                                        System.out.println(first);
                                        if (Float.parseFloat(String.valueOf(floatText)) > 50) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                            builder.setCancelable(true);
                                            builder.setTitle("GAS LEVEL ALARM");
                                            builder.setMessage(text);
                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            });
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            });
                                            builder.show();
                                        }
                                    } catch (JSONException e) {
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    textView.setText("That didn't work!");
                                }
                            });
                    queue.add(stringRequest);
                    System.out.println("MESAj");
                    SystemClock.sleep(1000);
                    //notificationManagerCompat.notify(1, notification);
                }
            }
        });
//        try {
//            if (text != null) {
//                try {
//                    if (Integer.parseInt(text) > 5) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            NotificationChannel channel = new NotificationChannel("myCh", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
//                            NotificationManager manager = getSystemService(NotificationManager.class);
//                            manager.createNotificationChannel(channel);
//                        }
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "myCh")
//                                .setSmallIcon(android.R.drawable.stat_notify_sync)
//                                .setContentTitle("GAS LEVEL ALARM")
//                                .setContentText(textView.getText());
//
//                        notification = builder.build();
//                        notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
//                    }
//                } catch (Exception e) {
//                    System.out.println("HATATATATAT");
//                    System.out.println(e);
//                }
//            }
//        } catch (Exception e) {
//
//        }

//        try {
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "mych")
//                    .setSmallIcon(android.R.drawable.stat_notify_sync)
//                    .setContentTitle("My notification")
//                    .setContentText((CharSequence) textView)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true);
//
//        } catch (Exception e) {
//            System.out.println("HATATATATAT");
//            System.out.println(e);
//        }


        Tiret.start();

    }

}



