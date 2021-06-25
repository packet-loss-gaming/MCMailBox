/*
 * Copyright (c) 2021 Wyatt Childers.
 *
 * This file is part of MCMailBox.
 *
 * MCMailBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MCMailBox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with MCMailBox.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package gg.packetloss.mcmailbox;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MCMailBoxPlugin extends JavaPlugin {
    private final Map<String, String> keyValueStore = new HashMap<>();

    private Logger logger;
    private MCMailboxThread mailThread;

    @Override
    public void onEnable() {
        logger = getLogger();

        mailThread = new MCMailboxThread(this);
        mailThread.start();

        logger.info("Enabled!");
    }

    @Override
    public void onDisable() {
        destroyMailThreadIfRunning();
        logger.info("Disabled.");
    }

    private void destroyMailThreadIfRunning() {
        if (mailThread != null) {
            mailThread.destroy();
            mailThread = null;
        }
    }

    public String getValue(String key) {
        return keyValueStore.getOrDefault(key, "");
    }

    public String getValueOrDefault(String key, String defaultValue) {
        return keyValueStore.getOrDefault(key, defaultValue);
    }

    public void putValue(String key, String value) {
        new MailBoxUpdateEvent(key, getValue(key), value);
        keyValueStore.put(key, value);
    }
}
