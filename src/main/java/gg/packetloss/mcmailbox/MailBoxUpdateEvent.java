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

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MailBoxUpdateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final String key;
    private final String oldValue;
    private final String newValue;

    public MailBoxUpdateEvent(String key, String oldValue, String newValue) {
        this.key = key;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getKey() {
        return key;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public boolean isNowFalse() {
        return newValue.isBlank() || newValue.equals("0");
    }

    public boolean isNowTrue() {
        return !isNowFalse();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
