package com.byteshaft.callnote;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncomingCallListener extends PhoneStateListener {
    ArrayList<String> arrayList;
    DataBaseHelpers dbHelpers;
    Context mContext;

    public IncomingCallListener(Context context) {
        super();
        mContext = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        System.out.println(incomingNumber);
        dbHelpers = new DataBaseHelpers(mContext);
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                ArrayList<String> notes = getNotesForNumber(incomingNumber);
                if (notes != null) {
                    OverlayHelpers.showPopupNoteForContact(notes.get(0));
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                OverlayHelpers.removePopupNote();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                OverlayHelpers.removePopupNote();
                break;
        }
    }

    private ArrayList<String> getNotesForNumber(String number) {
        arrayList = dbHelpers.getAllNumbers();
        ArrayList<String> notesList = new ArrayList<>();
        for(String contact : arrayList) {
            if (PhoneNumberUtils.compare(contact, number)) {
                ArrayList<String> notes = dbHelpers.getNotefromNumber(number);
                for (String val: notes) {
                    notesList.add(val);
                }
                return notesList;
            }
        }

        return null;
    }
}
