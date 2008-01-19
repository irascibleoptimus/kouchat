
/***************************************************************************
 *   Copyright 2006-2007 by Christian Ihle                                 *
 *   kontakt@usikkert.net                                                  *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/

package net.usikkert.kouchat.net;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.usikkert.kouchat.misc.Controller;
import net.usikkert.kouchat.misc.MessageController;
import net.usikkert.kouchat.misc.NickDTO;
import net.usikkert.kouchat.ui.UserInterface;

/**
 * This class responds to events from the message parser.
 *
 * @author Christian Ihle
 */
public class DefaultPrivateMessageResponder implements PrivateMessageResponder
{
	private static final Logger LOG = Logger.getLogger( DefaultPrivateMessageResponder.class.getName() );

	private final Controller controller;
	private final UserInterface ui;
	private final MessageController msgController;

	/**
	 * Constructor.
	 *
	 * @param controller The controller.
	 * @param ui The user interface.
	 */
	public DefaultPrivateMessageResponder( final Controller controller, final UserInterface ui )
	{
		this.controller = controller;
		this.ui = ui;

		msgController = ui.getMessageController();
	}

	/**
	 * Shows the message in the user's private chat window,
	 * and notifies the user interface that a new message
	 * has arrived.
	 */
	@Override
	public void messageArrived( final int userCode, final String msg, final int color )
	{
		if ( !controller.isNewUser( userCode ) )
		{
			NickDTO user = controller.getNick( userCode );

			if ( user.isAway() )
				LOG.log( Level.WARNING, "Got message from " + user.getNick() + " which is away: " + msg );

			else if ( user.getPrivateChatPort() == 0 )
				LOG.log( Level.WARNING, "Got message from " + user.getNick() + " which has no reply port: " + msg );

			else
			{
				msgController.showPrivateUserMessage( user, msg, color );
				ui.notifyMessageArrived();

				if ( !user.getPrivchat().isVisible() )
					controller.changeNewMessage( user.getCode(), true );
			}
		}

		else
		{
			LOG.log( Level.SEVERE, "Could not find user: " + userCode );
		}
	}
}
