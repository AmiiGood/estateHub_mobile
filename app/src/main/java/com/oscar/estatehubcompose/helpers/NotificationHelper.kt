package com.oscar.estatehubcompose.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.oscar.estatehubcompose.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationHelper @Inject constructor(@ApplicationContext private val context: Context) {

    //Funcion que muestra la notificacion
    fun showNotification(titulo: String, descripcion: String){
        //Obtiene el servicio de notificaciones del contexto(dispositivo)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val canal = NotificationChannel("canal_id",
                "Notificaciones generales",
                NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(canal);
        }

        val notificacion = NotificationCompat
            .Builder(context, "canal_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(titulo)
            .setContentText(descripcion)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notificacion);


    }


}