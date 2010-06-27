
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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Singleton;

import net.usikkert.kouinject.annotation.Component;

import org.apache.commons.lang.Validate;

/**
 * TODO
 *
 * @author Christian Ihle
 */
@Component
@Singleton
public class GenericArgumentExtractorImpl implements GenericArgumentExtractor {

    public Class<?> getGenericArgument(final Object objectWithGenericArgument, final Class<?> genericType) {
        Validate.notNull(objectWithGenericArgument, "Object can not be null");
        Validate.notNull(genericType, "Generic Type can not be null");

        final Type[] genericInterfaces = objectWithGenericArgument.getClass().getGenericInterfaces();

        for (final Type type : genericInterfaces) {
            if (type.equals(genericType)) {
                throw new UnsupportedOperationException("A generic argument must be present on " + genericType);
            }

            if (type instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType) type;

                if (parameterizedType.getRawType().equals(genericType)) {
                    final Type[] typeArguments = parameterizedType.getActualTypeArguments();

                    if (typeArguments.length != 1) {
                        throw new UnsupportedOperationException("Only one generic argument can be present on " + genericType);
                    }

                    return (Class<?>) typeArguments[0];
                }
            }
        }

        throw new IllegalArgumentException("The generic type " + genericType + " was not present on " + objectWithGenericArgument.getClass());
    }
}
