
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

package net.usikkert.kouchat.ui.console;

import net.usikkert.kouchat.event.FileTransferListener;
import net.usikkert.kouchat.net.FileTransfer;

/**
 * This is the console inplementation of a file transfer listener.
 * Does not do anything, but is needed to get file transfer support
 * in console mode.
 *
 * @author Christian Ihle
 */
public class TransferHandler implements FileTransferListener
{
	/**
	 * Constructor. Registers this class as a listener of
	 * file transfer events.
	 *
	 * @param fileTransfer The file transfer to listen to.
	 */
	public TransferHandler( final FileTransfer fileTransfer )
	{
		fileTransfer.registerListener( this );
	}

	/**
	 * Not implemented.
	 */
	@Override
	public void statusCompleted()
	{

	}

	/**
	 * Not implemented.
	 */
	@Override
	public void statusConnecting()
	{

	}

	/**
	 * Not implemented.
	 */
	@Override
	public void statusFailed()
	{

	}

	/**
	 * Not implemented.
	 */
	@Override
	public void statusTransferring()
	{

	}

	/**
	 * Not implemented.
	 */
	@Override
	public void statusWaiting()
	{

	}

	/**
	 * Not implemented.
	 */
	@Override
	public void transferUpdate()
	{

	}
}
