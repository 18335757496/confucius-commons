/**
 * Project   : commons-utils File      : FilterOperator.java Date      : 2011-12-31 Time      : ����10:15:26 Copyright :
 * taobao.com Ltd.
 */
package org.confucius.commons.lang.filter;

import java.lang.reflect.Array;

/**
 * {@link Filter} Operator enumeration , which contains {@link #AND}��{@link #OR}��{@link #XOR}
 *
 * @author <a href="mailto:taogu.mxx@taobao.com">Mercy</a>
 * @version 1.0.0
 * @see Filter
 * @see #AND
 * @see #OR
 * @see #XOR
 * @since 1.0.0
 */
public enum FilterOperator {

    /**
     * &
     */
    AND {
        @Override
        public <T> boolean accept(T filteredObject, Filter<T>... filters) {
            int length = Array.getLength(filters);
            if (length == 0)
                return true;
            boolean success = true;
            for (Filter<T> filter : filters) {
                success &= filter.accept(filteredObject);
            }
            return success;
        }
    },
    /**
     * |
     */
    OR {
        @Override
        public <T> boolean accept(T filteredObject, Filter<T>... filters) {
            int length = Array.getLength(filters);
            if (length == 0)
                return true;
            boolean success = false;
            for (Filter<T> filter : filters) {
                success |= filter.accept(filteredObject);
            }
            return success;
        }
    },
    /**
     * XOR
     */
    XOR {
        @Override
        public <T> boolean accept(T filteredObject, Filter<T>... filters) {
            int length = Array.getLength(filters);
            if (length == 0)
                return true;
            boolean success = true;
            for (Filter<T> filter : filters) {
                success ^= filter.accept(filteredObject);
            }
            return success;
        }
    };

    /**
     * multiple {@link Filter} accept
     *
     * @param <T>
     *         Filtered object type
     * @param filteredObject
     *         Filtered object
     * @param filters
     *         multiple {@link Filter}
     * @return If accepted ��return <code>true</code>
     * @version 1.0.0
     * @since 1.0.0
     */
    abstract public <T> boolean accept(T filteredObject, Filter<T>... filters);
}
