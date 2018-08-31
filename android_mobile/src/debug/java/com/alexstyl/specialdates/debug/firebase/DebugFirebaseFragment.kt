package com.alexstyl.specialdates.debug.firebase

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.preference.Preference
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.toast
import com.alexstyl.specialdates.ui.base.MementoPreferenceFragment
import com.google.firebase.iid.FirebaseInstanceId

class DebugFirebaseFragment : MementoPreferenceFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreate(paramBundle: Bundle?) {
        super.onCreate(paramBundle)

        addPreferencesFromResource(R.xml.preference_debug_firebase)

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { result ->
            val fcmToken = result.token
            findPreference<Preference>(R.string.key_debug_firebase_messaging_token)!!
                    .apply { summary = "Token $fcmToken" }
                    .setOnPreferenceClickListener {
                        val clipboardManager = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = android.content.ClipData.newPlainText("Firebase Token", fcmToken)
                        clipboardManager.primaryClip = clip
                        toast("Token copied to clipboard")
                        true
                    }
        }

    }

}
