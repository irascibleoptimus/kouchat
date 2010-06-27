
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

package net.usikkert.kouchat.messagebus;

import static org.junit.Assert.*;
import net.usikkert.kouchat.reflect.GenericArgumentExtractor;
import net.usikkert.kouchat.reflect.GenericArgumentExtractorImpl;

import org.junit.Before;
import org.junit.Test;

/**
 * Test of {@link MessageBusImpl}.
 *
 * @author Christian Ihle
 */
public class MessageBusImplTest {

    private MessageBusImpl messageBus;

    @Before
    public void createMessageBus() {
        messageBus = new MessageBusImpl();

        final GenericArgumentExtractor genericArgumentExtractor = new GenericArgumentExtractorImpl();
        messageBus.setGenericArgumentExtractor(genericArgumentExtractor);
    }

    @Test
    public void test() {
        final LongMessageListener longMessageListener = new LongMessageListener();
        final ShortMessageListener shortMessageListener = new ShortMessageListener();

        messageBus.registerMessageListener(shortMessageListener);
        messageBus.registerMessageListener(longMessageListener);

        final ShortMessage shortMessage = new ShortMessage();
        messageBus.sendMessage(shortMessage);

        assertSame(shortMessage, shortMessageListener.getMessage());
        assertNull(longMessageListener.getMessage());
    }

    @Test
    public void test2() {
        final LongMessageListener longMessageListener = new LongMessageListener();
        final ShortMessageListener shortMessageListener = new ShortMessageListener();

        messageBus.registerMessageListener(shortMessageListener);
        messageBus.registerMessageListener(longMessageListener);

        final LongMessage longMessage = new LongMessage();
        messageBus.sendMessage(longMessage);

        assertSame(longMessage, longMessageListener.getMessage());
        assertNull(shortMessageListener.getMessage());
    }
}
