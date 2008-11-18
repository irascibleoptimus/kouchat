
/***************************************************************************
 *   Copyright 2006-2008 by Christian Ihle                                 *
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

import java.io.File;

import net.usikkert.kouchat.event.FileTransferListener;
import net.usikkert.kouchat.misc.User;

/**
 * This is the interface for both sending and receiving file transfers
 * between users.
 *
 * <p>Useful for the user interface, as it doesn't need to know what kind of
 * file transfer it is showing progress information about.</p>
 *
 * @author Christian Ihle
 */
public interface FileTransfer
{
	public enum Direction
	{
		SEND,
		RECEIVE
	};

	Direction getDirection();
	User getNick();
	int getPercent();
	long getTransferred();
	File getFile();
	long getFileSize();
	long getSpeed();
	void cancel();
	boolean isCanceled();
	boolean isTransferred();
	void registerListener( FileTransferListener listener );
}
