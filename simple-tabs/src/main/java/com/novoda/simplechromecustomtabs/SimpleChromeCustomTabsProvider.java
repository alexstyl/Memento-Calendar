package com.novoda.simplechromecustomtabs;

import com.novoda.simplechromecustomtabs.connection.Connection;
import com.novoda.simplechromecustomtabs.navigation.WebNavigator;
import com.novoda.simplechromecustomtabs.provider.AvailableAppProvider;

class SimpleChromeCustomTabsProvider {

    private final SimpleChromeCustomTabs simpleChromeCustomTabs;

    SimpleChromeCustomTabsProvider() {
        simpleChromeCustomTabs = new SimpleChromeCustomTabs();

        Connection connection = Connection.Creator.create(simpleChromeCustomTabs);
        WebNavigator webNavigator = WebNavigator.Creator.create(simpleChromeCustomTabs);
        AvailableAppProvider availableAppProvider = AvailableAppProvider.Creator.create();

        simpleChromeCustomTabs.injectModules(connection, webNavigator, availableAppProvider);
    }

    SimpleChromeCustomTabs getSimpleChromeCustomTabs() {
        return simpleChromeCustomTabs;
    }
}
