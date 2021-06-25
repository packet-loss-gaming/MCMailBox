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

import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.*;

public class MCMailboxThread extends Thread {
    private static final int BUFFER_SIZE = 1024;

    private final MCMailBoxPlugin plugin;
    private final byte[] messageBuffer = new byte[BUFFER_SIZE];

    private boolean isAlive = true;

    public MCMailboxThread(MCMailBoxPlugin plugin) {
        super("MCMailBox");
        this.plugin = plugin;
    }

    public void destroy() {
        this.isAlive = false;
    }


    @Override
    public void run() {
        try (DatagramSocket datagramSocket = new DatagramSocket(62895);) {
            plugin.getLogger().info("Now listening");

            // Set a timeout so we don't wait forever and can actually shutdown this thread
            datagramSocket.setSoTimeout(500);
            while (isAlive) {
                try {
                    DatagramPacket dp = new DatagramPacket(messageBuffer, BUFFER_SIZE);
                    datagramSocket.receive(dp);

                    String rawString = new String(dp.getData(), 0, dp.getLength());
                    String[] parts = rawString.split("=");

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        plugin.getLogger().info("Received: \"" + key + "\" = \"" + value + '"');
                        plugin.putValue(key, value);
                    });
                } catch (SocketTimeoutException ignored) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

