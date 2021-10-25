package com.example.uppgift6;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 *  Denna klass får in sms genom en Broadcast Reciver
 *
 * @author Oliver Gallo, olga7031
 */

public class SmsReciver extends BroadcastReceiver {
    public static final String pdu_type = "pdus";

    @TargetApi(Build.VERSION_CODES.M) // kollar api leven
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();//hämtar bundle
        SmsMessage[] msgs; //array för sms
        String strMessage = "";
        String format = null;
        if (bundle != null) {
            format = bundle.getString("format");
        }

        Object[] pdus = new Object[0];//skapa array med objekt från bundle
        if (bundle != null) {
            pdus = (Object[]) bundle.get(pdu_type);
        }
        if(pdus != null){//kontrollerar att arrayen inte är tom
            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);//kollar om api leveln är minst M

            msgs = new SmsMessage[pdus.length];//lägg till ny sms
            for(int i = 0; i < msgs.length; i++){//loop
                if(isVersionM){//Tittar på api leveln
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i],format);
                }else{ //om api leveln är mindre än lollipop
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                strMessage +="SMS from" + msgs[i].getOriginatingAddress();//bygg sms
                strMessage += " :" + msgs[i].getMessageBody() + "/n";
                Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show(); //visa texten genom en toast
            }
        }
    }
}
