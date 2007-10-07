
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

import net.usikkert.kouchat.event.ReceiverListener;
import net.usikkert.kouchat.misc.NickDTO;
import net.usikkert.kouchat.misc.Settings;

public class MessageParser implements ReceiverListener
{
	private static Logger log = Logger.getLogger( MessageParser.class.getName() );

	private MessageReceiver receiver;
	private MessageResponder responder;
	private Settings settings;
	private boolean loggedOn;

	public MessageParser( MessageResponder responder )
	{
		this.responder = responder;

		settings = Settings.getSettings();
		receiver = new MessageReceiver();
		receiver.registerReceiverListener( this );
	}
	
	public void start()
	{
		receiver.startReceiver();
	}

	public void stop()
	{
		receiver.stopReceiver();
	}

	public boolean restart()
	{
		return receiver.restartReceiver();
	}

	@Override
	public void messageArrived( String message, String ipAddress )
	{
		System.out.println( message ); // TODO

		try
		{
			int exclamation = message.indexOf( "!" );
			int hash = message.indexOf( "#" );
			int colon = message.indexOf( ":" );

			int msgCode = Integer.parseInt( message.substring( 0, exclamation ) );
			String type = message.substring( exclamation +1, hash );
			String msgNick = message.substring( hash +1, colon );
			String msg = message.substring( colon +1, message.length() );

			NickDTO tempme = settings.getMe();

			if ( msgCode != tempme.getCode() && loggedOn )
			{
				if ( type.equals( "MSG" ) )
				{
					int leftBracket = msg.indexOf( "[" );
					int rightBracket = msg.indexOf( "]" );
					int rgb = Integer.parseInt( msg.substring( leftBracket +1, rightBracket ) );

					responder.messageArrived( msgCode, msg.substring( rightBracket +1, msg.length() ), rgb );
				}

				else if ( type.equals( "LOGON" ) )
				{
					NickDTO newUser = new NickDTO( msgNick, msgCode );
					newUser.setIpAddress( ipAddress );
					newUser.setLastIdle( System.currentTimeMillis() );

					responder.userLogOn( newUser );
				}

				else if ( type.equals( "EXPOSING" ) )
				{
					NickDTO user = new NickDTO( msgNick, msgCode );
					user.setIpAddress( ipAddress );
					user.setAwayMsg( msg );

					if ( msg.length() > 0 )
						user.setAway( true );

					user.setLastIdle( System.currentTimeMillis() );
					responder.userExposing( user );
				}

				else if ( type.equals( "LOGOFF" ) )
				{
					responder.userLogOff( msgCode );
				}

				else if ( type.equals( "AWAY" ) )
				{
					responder.awayChanged( msgCode, true, msg );
				}

				else if ( type.equals( "BACK" ) )
				{
					responder.awayChanged( msgCode, false, "" );
				}

				else if ( type.equals( "EXPOSE" ) )
				{
					responder.exposeRequested();
				}

				else if ( type.equals( "NICKCRASH" ) )
				{
					if ( tempme.getNick().equals( msg ) )
					{
						responder.nickCrash();
					}
				}

				else if ( type.equals( "WRITING" ) )
				{
					responder.writingChanged( msgCode, true );
				}

				else if ( type.equals( "STOPPEDWRITING" ) )
				{
					responder.writingChanged( msgCode, false );
				}

				else if ( type.equals( "GETTOPIC" ) )
				{
					responder.topicRequested();
				}

				else if ( type.equals( "TOPIC" ) )
				{
					int leftBracket = msg.indexOf( "[" );
					int rightBracket = msg.indexOf( "]" );
					int leftPara = msg.indexOf( "(" );
					int rightPara = msg.indexOf( ")" );

					if ( rightBracket != -1 && leftBracket != -1 )
					{
						String theNick = msg.substring( leftPara +1, rightPara );
						long theTime = Long.parseLong( msg.substring( leftBracket +1, rightBracket ) );
						String theTopic = null;

						if ( msg.length() > rightBracket + 1 )
						{
							theTopic = msg.substring( rightBracket +1, msg.length() );
						}

						responder.topicChanged( msgCode, theTopic, theNick, theTime );
					}
				}

				else if ( type.equals( "NICK" ) )
				{
					responder.nickChanged( msgCode, msgNick );
				}

				else if ( type.equals( "IDLE" ) )
				{
					responder.userIdle( msgCode );
				}

				else if ( type.equals( "SENDFILEACCEPT" ) )
				{
					int leftPara = msg.indexOf( "(" );
					int rightPara = msg.indexOf( ")" );
					int fileCode = Integer.parseInt( msg.substring( leftPara +1, rightPara ) );

					if ( fileCode == tempme.getCode() )
					{
						int leftCurly = msg.indexOf( "{" );
						int rightCurly = msg.indexOf( "}" );
						int leftBracket = msg.indexOf( "[" );
						int rightBracket = msg.indexOf( "]" );
						int port = Integer.parseInt( msg.substring( leftBracket +1, rightBracket ) );
						int fileHash = Integer.parseInt( msg.substring( leftCurly +1, rightCurly ) );
						String fileName = msg.substring( rightCurly +1, msg.length() );

						responder.fileSendAccepted( msgCode, fileName, fileHash, port );
					}
				}

				else if ( type.equals( "SENDFILEABORT" ) )
				{
					int leftPara = msg.indexOf( "(" );
					int rightPara = msg.indexOf( ")" );
					int fileCode = Integer.parseInt( msg.substring( leftPara +1, rightPara ) );

					if ( fileCode == tempme.getCode() )
					{
						int leftCurly = msg.indexOf( "{" );
						int rightCurly = msg.indexOf( "}" );
						String fileName = msg.substring( rightCurly +1, msg.length() );
						int fileHash = Integer.parseInt( msg.substring( leftCurly +1, rightCurly ) );

						responder.fileSendAborted( msgCode, fileName, fileHash );
					}
				}

				else if ( type.equals( "SENDFILE" ) )
				{
					int leftPara = msg.indexOf( "(" );
					int rightPara = msg.indexOf( ")" );
					int fileCode = Integer.parseInt( msg.substring( leftPara +1, rightPara ) );

					if ( fileCode == tempme.getCode() )
					{
						int leftCurly = msg.indexOf( "{" );
						int rightCurly = msg.indexOf( "}" );
						int leftBracket = msg.indexOf( "[" );
						int rightBracket = msg.indexOf( "]" );
						long byteSize = Long.parseLong( msg.substring( leftBracket +1, rightBracket ) );
						String fileName = msg.substring( rightCurly +1, msg.length() );
						int fileHash = Integer.parseInt( msg.substring( leftCurly +1, rightCurly ) );

						responder.fileSend( msgCode, byteSize, fileName, msgNick, fileHash, fileCode );
					}
				}
			}

			else if ( type.equals( "LOGON" ) )
			{
				responder.meLogOn( ipAddress );
				loggedOn = true;
			}

			else if ( type.equals( "IDLE" ) )
			{
				responder.meIdle( ipAddress );
			}
		}

		catch ( StringIndexOutOfBoundsException e )
		{
			log.log( Level.SEVERE, e.getMessage(), e );
		}

		catch ( NumberFormatException e )
		{
			log.log( Level.SEVERE, e.getMessage(), e );
		}
	}
}
