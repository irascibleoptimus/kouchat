
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

package net.usikkert.kouchat.gui;

import net.usikkert.kouchat.Constants;
import net.usikkert.kouchat.event.DayListener;
import net.usikkert.kouchat.util.DayTimer;
import net.usikkert.kouchat.util.Tools;

import java.awt.BorderLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public class KouChatGUI extends JFrame
{
	private MenuBar menuBar;
	private MainPanel mainP;
	private ListenerMediator listener;
	
	public KouChatGUI()
	{
		listener = new ListenerMediator( this );
		mainP = new MainPanel( listener );
		menuBar = new MenuBar( listener );
		new SysTray( listener );
		new SettingsFrame( listener );
		
		setJMenuBar( menuBar );
		getContentPane().add( mainP, BorderLayout.CENTER );
		setTitle( Constants.APP_NAME + " v" + Constants.APP_VERSION + " - (Not connected)" );
		setIconImage( new ImageIcon( getClass().getResource( "/icons/kou_normal.png" ) ).getImage() );
		setSize( 650, 480 );
		setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		setVisible( true );
		
		// Catch focus for the textfield
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher( new KeyEventDispatcher()
		{
			public boolean dispatchKeyEvent( KeyEvent e )
			{
				if( e.getID() == KeyEvent.KEY_TYPED && isFocused() )
				{
					KeyboardFocusManager.getCurrentKeyboardFocusManager().redispatchEvent( mainP.getMsgTF(), e );
					mainP.getMsgTF().requestFocus();
					
					return true;
				}
				
				else
					return false;
			}
		} );
		
		// Minimize with Escape key
		KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0, false );
		
		Action escapeAction = new AbstractAction()
		{
			public void actionPerformed( ActionEvent e )
			{
				setVisible( false );
			}
		};
		 
		getRootPane().getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( escapeKeyStroke, "ESCAPE" );
		getRootPane().getActionMap().put( "ESCAPE", escapeAction );
		
		// Shut down the right way
		addWindowListener( new WindowAdapter()
		{
			public void windowClosing( WindowEvent arg0 )
			{
				listener.quit();
			}
		} );
		
		mainP.appendSystemMessage( "*** Welcome to " + Constants.APP_NAME + " v" + Constants.APP_VERSION+ "!" );
		mainP.getMsgTF().requestFocus();
		listener.start();
		
		// TODO testing
		DayTimer dt = new DayTimer();
		dt.addDayListener( new DayListener()
		{
			@Override
			public void dayChanged( Date date )
			{
				mainP.appendSystemMessage( "*** Day changed to " + Tools.dateToString( null, "EEEE, d MMMM yyyy" ) );
			}
		} );
	}
}
