
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

package net.usikkert.kouchat.reflect;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test of {@link GenericArgumentExtractorImpl}.
 *
 * @author Christian Ihle
 */
public class GenericArgumentExtractorImplTest {

    private GenericArgumentExtractor extractor;

    @Before
    public void createExtractor() {
        extractor = new GenericArgumentExtractorImpl();
    }

    @Test
    public void shouldFindGenericArgumentOfSpecifiedInterface() {
        final SingleGenericArgumentImpl singleGenericArgumentImpl = new SingleGenericArgumentImpl();

        final Class<?> genericArgument = extractor.getGenericArgument(singleGenericArgumentImpl, SingleGenericArgument.class);

        assertEquals(String.class, genericArgument);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowExceptionIfNoGenericArguments() {
        final NoGenericArgumentImpl noGenericArgumentImpl = new NoGenericArgumentImpl();

        extractor.getGenericArgument(noGenericArgumentImpl, NoGenericArgument.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfObjectDoesNotImplementInterface() {
        final SingleGenericArgumentImpl singleGenericArgumentImpl = new SingleGenericArgumentImpl();

        extractor.getGenericArgument(singleGenericArgumentImpl, NoGenericArgument.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowExceptionOfTooManyGenericArguments() {
        final DoubleGenericArgumentImpl doubleGenericArgumentImpl = new DoubleGenericArgumentImpl();

        extractor.getGenericArgument(doubleGenericArgumentImpl, DoubleGenericArgument.class);
    }
}
