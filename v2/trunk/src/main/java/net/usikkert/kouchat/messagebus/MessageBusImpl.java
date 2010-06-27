
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.usikkert.kouchat.reflect.GenericArgumentExtractor;
import net.usikkert.kouinject.annotation.Component;

import org.apache.commons.lang.Validate;

/**
 * TODO
 *
 * @author Christian Ihle
 */
@Component
public class MessageBusImpl implements MessageBus {

    private final List<MessageListenerHolder> messageListeners;

    @Inject
    private GenericArgumentExtractor genericArgumentExtractor;

    public MessageBusImpl() {
        messageListeners = new ArrayList<MessageListenerHolder>();
    }

    @Override
    public void registerMessageListener(final MessageListener<?> messageListener) {
        Validate.notNull(messageListener, "Message listener can not be null");

        final Class<?> classToListenFor = genericArgumentExtractor.getGenericArgument(messageListener, MessageListener.class);
        final MessageListenerHolder holder = new MessageListenerHolder(messageListener, classToListenFor);

        messageListeners.add(holder);
    }

    @Override
    public void sendMessage(final Message message) {
        Validate.notNull(message, "Message can not be null");

        for (final MessageListenerHolder holder : messageListeners) {
            if (holder.getClassToListenFor().equals(message.getClass())) {
                holder.getMessageListener().handleMessage(message);
            }
        }
    }

    public void setGenericArgumentExtractor(final GenericArgumentExtractor genericArgumentExtractor) {
        this.genericArgumentExtractor = genericArgumentExtractor;
    }
}
