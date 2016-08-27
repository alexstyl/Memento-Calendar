
package com.alexstyl.specialdates.debug;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.alexstyl.specialdates.BuildConfig;

import javax.security.auth.x500.X500Principal;
import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class Debugz {
    public static final boolean DISABLE_BIRTHDAYS = BuildConfig.DEBUG;
    public static final boolean isDebug = BuildConfig.DEBUG;

    private static final X500Principal DEBUG_DN = new X500Principal(
            "CN=Android Debug,O=Android,C=US");

    public static boolean isDebuggable(Context ctx) {
        boolean debuggable = false;

        try
        {
            PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            Signature signatures[] = pinfo.signatures;

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            for (int i = 0; i < signatures.length; i++)
            {
                ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable)
                    break;
            }
        } catch (NameNotFoundException e)
        {
            // debuggable variable will remain false
        } catch (CertificateException e)
        {
            // debuggable variable will remain false
        }
        return debuggable;
    }


    public static void printHashKeyToLogCat(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("com.alexstyl.specialdates", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("hash key", something);
            }
        } catch (NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }
}
