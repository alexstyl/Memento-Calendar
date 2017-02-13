package com.alexstyl.specialdates.about;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alexstyl.specialdates.ExternalNavigator;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.AnalyticsProvider;
import com.alexstyl.specialdates.theming.AttributeExtractor;
import com.alexstyl.specialdates.ui.CheatsSheat;
import com.alexstyl.specialdates.ui.activity.MainActivity;
import com.alexstyl.specialdates.ui.base.ThemedActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.alexstyl.specialdates.util.Utils;
import com.novoda.notils.caster.Views;
import com.novoda.simplechromecustomtabs.SimpleChromeCustomTabs;
import com.novoda.simplechromecustomtabs.navigation.IntentCustomizer;
import com.novoda.simplechromecustomtabs.navigation.NavigationFallback;
import com.novoda.simplechromecustomtabs.navigation.SimpleChromeCustomTabsIntentBuilder;

import de.psdev.licensesdialog.LicensesDialog;

public class AboutActivity extends ThemedActivity {

    private ExternalNavigator externalNavigator;

    private View rateView;
    private View emailView;
    private View communityView;
    private View shareView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        externalNavigator = new ExternalNavigator(this, AnalyticsProvider.getAnalytics(this));
        MementoToolbar toolbar = Views.findById(this, R.id.memento_toolbar);

        setSupportActionBar(toolbar);
        toolbar.displayAsUp();

        rateView = Views.findById(this, R.id.about_rate);
        emailView = Views.findById(this, R.id.about_email);
        communityView = Views.findById(this, R.id.about_plus);
        shareView = Views.findById(this, R.id.about_share);

        setupButtons();
    }

    private void setupButtons() {
        setupRateView();
        setupEmailView();
        setupCommunityView();
        setupShareView();
    }

    private void setupRateView() {
        if (externalNavigator.canGoToPlayStore()) {
            CheatsSheat.setup(rateView, R.string.rate_app);
            rateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    externalNavigator.toPlayStore();
                }
            });
        } else {
            rateView.setVisibility(View.GONE);
        }
    }

    private void setupEmailView() {
        if (externalNavigator.canGoToEmailSupport()) {
            CheatsSheat.setup(emailView, R.string.contact_dev);
            emailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    externalNavigator.toEmailSupport();
                }
            });
        } else {
            emailView.setVisibility(View.GONE);
        }
    }

    private void setupCommunityView() {
        CheatsSheat.setup(communityView, R.string.about_plus_community_cheatsheat);
        communityView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (hasPlusInstalled()) {
                    externalNavigator.toGooglePlusCommunityApp();
                } else {
                    SimpleChromeCustomTabs.getInstance().withFallback(navigateWithBrowserFallback)
                            .withIntentCustomizer(intentCustomizer)
                            .navigateTo(ExternalNavigator.GOOGLE_PLUS_COMMUNITY, AboutActivity.this);

                }
            }

            private boolean hasPlusInstalled() {
                return isPackageInstalled("com.google.android.apps.plus", AboutActivity.this);
            }

            private boolean isPackageInstalled(String packagename, Context context) {
                PackageManager pm = context.getPackageManager();
                try {
                    pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
                    return true;
                } catch (PackageManager.NameNotFoundException e) {
                    return false;
                }
            }

        });
    }

    private final NavigationFallback navigateWithBrowserFallback = new NavigationFallback() {
        @Override
        public void onFallbackNavigateTo(Uri url) {
            externalNavigator.toGooglePlusCommunityBrowser();
        }
    };

    private final IntentCustomizer intentCustomizer = new IntentCustomizer() {
        @Override
        public SimpleChromeCustomTabsIntentBuilder onCustomiseIntent(SimpleChromeCustomTabsIntentBuilder simpleChromeCustomTabsIntentBuilder) {
            int toolbarColor = new AttributeExtractor().extractPrimaryColorFrom(AboutActivity.this);
            return simpleChromeCustomTabsIntentBuilder.withToolbarColor(toolbarColor);
        }
    };

    private void setupShareView() {
        CheatsSheat.setup(shareView, R.string.share);
        shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.shareApp(v.getContext());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        SimpleChromeCustomTabs.getInstance().connectTo(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        SimpleChromeCustomTabs.getInstance().disconnectFrom(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(AboutActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                return true;
            case R.id.action_licences:
                showLicencesDialog();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLicencesDialog() {
        new LicensesDialog(this, R.raw.licences, false, true).show();
    }

}
