
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

package net.usikkert.kouchat.misc;

import net.usikkert.kouchat.event.MessageListener;
import net.usikkert.kouchat.net.MessageParser;
import net.usikkert.kouchat.net.Messages;

public class Controller
{
	private ChatState chatState;
	private NickController nickController;
	private Messages msgSender;
	private MessageParser msgParser;
	private IdleThread idleThread;
	
	public Controller()
	{
		nickController = new NickController();
		chatState = new ChatState();
		msgParser = new MessageParser();
		msgSender = new Messages();
		idleThread = new IdleThread( this );
	}
	
	public Topic getTopic()
	{
		return chatState.getTopic();
	}
	
	public NickList getNickList()
	{
		return nickController.getNickList();
	}
	
	public boolean isWrote()
	{
		return chatState.isWrote();
	}
	
	public void updateLastIdle( int code, long lastIdle )
	{
		nickController.updateLastIdle( code, lastIdle );
	}
	
	public void changeWriting( int code, boolean writing )
	{
		nickController.changeWriting( code, writing );
		Nick me = Settings.getSettings().getNick();
		
		if ( code == me.getCode() )
		{
			chatState.setWrote( writing );
			
			if ( writing )
				msgSender.sendWritingMessage();
			else
				msgSender.sendStoppedWritingMessage();
		}
	}
	
	public boolean checkIfValidNick( String tmp, boolean quiet )
	{
		return nickController.checkIfValidNick( tmp, quiet );
	}
	
	public boolean checkIfNewUser( int code )
	{
		return nickController.checkIfNewUser( code );
	}
	
	public void changeNick( int code, String nick )
	{
		nickController.changeNick( code, nick );
		Nick me = Settings.getSettings().getNick();
		
		if ( code == me.getCode() )
		{
			msgSender.sendNickMessage();
		}
	}
	
	public Nick getNick( int code )
	{
		return nickController.getNick( code );
	}
	
	public void addMessageListener( MessageListener listener )
	{
		msgParser.addMessageListener( listener );
	}
	
	public void logOn()
	{
		msgSender.sendLogonMessage();
		msgSender.sendExposeMessage();
		msgSender.sendGetTopicMessage();
		idleThread.start();
	}
	
	public void logOff()
	{
		idleThread.stopThread();
		msgParser.stop();
		msgSender.sendLogoffMessage();
		msgSender.stop();
	}
	
	public void sendExposeMessage()
	{
		msgSender.sendExposeMessage();
	}
	
	public void sendExposingMessage()
	{
		msgSender.sendExposingMessage();
	}
	
	public void sendGetTopicMessage()
	{
		msgSender.sendGetTopicMessage();
	}
	
	public void sendIdleMessage()
	{
		msgSender.sendIdleMessage();
	}
	
	public void sendChatMessage( String msg )
	{
		msgSender.sendChatMessage( msg );
	}
	
	public void sendTopicMessage( Topic topic )
	{
		msgSender.sendTopicMessage( topic );
	}
	
	public void sendAwayMessage()
	{
		msgSender.sendAwayMessage();
	}
	
	public void sendBackMessage()
	{
		msgSender.sendBackMessage();
	}
	
	public void sendNickCrashMessage( String nick )
	{
		msgSender.sendNickCrashMessage( nick );
	}
	
	public void changeAwayStatus( int code, boolean away, String awaymsg )
	{
		nickController.changeAwayStatus( code, away, awaymsg );
	}
}
