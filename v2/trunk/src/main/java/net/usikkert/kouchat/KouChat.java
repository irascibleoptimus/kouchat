
/***************************************************************************
 *   Copyright 2006-2010 by Christian Ihle                                 *
 *   kontakt@usikkert.net                                                  *
 *                                                                         *
 *   This file is part of KouChat.                                         *
 *                                                                         *
 *   KouInject is free software; you can redistribute it and/or modify     *
 *   it under the terms of the GNU Lesser General Public License as        *
 *   published by the Free Software Foundation, either version 3 of        *
 *   the License, or (at your option) any later version.                   *
 *                                                                         *
 *   KouInject is distributed in the hope that it will be useful,          *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU      *
 *   Lesser General Public License for more details.                       *
 *                                                                         *
 *   You should have received a copy of the GNU Lesser General Public      *
 *   License along with KouInject.                                         *
 *   If not, see <http://www.gnu.org/licenses/>.                           *
 ***************************************************************************/

package net.usikkert.kouchat;

import net.usikkert.kouchat.messagebus.MessageBus;
import net.usikkert.kouinject.DefaultInjector;
import net.usikkert.kouinject.Injector;

/**
 * TODO
 *
 * @author Christian Ihle
 */
public class KouChat {

    public static void main(final String[] args) {
        final Injector injector = new DefaultInjector("net.usikkert.kouchat");
        final MessageBus messageBus = injector.getBean(MessageBus.class);

        System.out.println(messageBus);
    }
}
