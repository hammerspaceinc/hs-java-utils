// Copyright (c) 2020 Hammerspace, Inc.
// 	  www.hammer.space
//
// Licensed under the Eclipse Public License - v 2.0 ("the License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.txt
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.hammerspace.open.datastructures;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Anantharaman "Anand" Ganesh
 * @since 2020-01-21
 */
public class SingletonSortedSet<T extends Comparable<? super T>> extends AbstractSet<T> implements SortedSet<T> {

    private final T element;

    public SingletonSortedSet(T element) {
        if (element == null) {
            // I don't have any fundamental reasons for doing this; just trying to keep the cases to a minimum and
            // provide determinism to methods like getElement(Comparable).
            //
            // Relax this restriction with care.
            throw new IllegalArgumentException("cannot have null elements");
        }
        this.element = element;
    }

    /**
     * Two methods provided by this class, not provided by parent classes.
     *
     * I believe that {@link SortedSet}s should provide {@link SingletonSortedSet#getElement(Comparable)}
     * because it would be as efficient as {@link SortedSet#contains(Object)} but allows you to look into other
     * fields of the existing object that may not participate in the comparison but may need to be updated.
     */
    @Nonnull
    public T getElement() {
        return element;
    }

    @Nullable
    public T getElement(T element) {
        if (this.element.compareTo(element) == 0) {
            return this.element;
        }
        return null;
    }

    @Override
    public Comparator<? super T> comparator() {
        return null; // we use the natural ordering of elements
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        // fromElement inclusive, toElement exclusive
        if (fromElement.compareTo(element) <= 0 && toElement.compareTo(element) > 0) {
            return this;
        }
        // TODO(aganesh): violating the "subset is backed by the original set" contract here; fix when needed
        return Collections.emptySortedSet();
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        // return all elements strictly less than toElement
        if (toElement.compareTo(element) > 0) {
            return this;
        }
        // TODO(aganesh): violating the "subset is backed by the original set" contract here; fix when needed
        return Collections.emptySortedSet();
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        // return all elements strictly greater than fromElement
        if (fromElement.compareTo(element) < 0) {
            return this;
        }
        // TODO(aganesh): violating the "subset is backed by the original set" contract here; fix when needed
        return Collections.emptySortedSet();
    }

    @Override
    public T first() {
        return element;
    }

    @Override
    public T last() {
        return element;
    }

    @Override
    public Spliterator<T> spliterator() {
        return new SingletonSortedSetSpliterator(this);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return element.equals(o);
    }

    @Override
    public Iterator<T> iterator() {
        return new SingletonSoredSetIterator(element);
    }

    @Override
    public Object[] toArray() {
        return new Object[]{element};
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        if (t.equals(element)) {
            return false;
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        // a singleton set is precisely that - singleton. it cannot become an empty set
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c.isEmpty()) {
            return true;
        }
        if (c.size() > 1) {
            return false;
        }
        return element.equals(c.iterator().next());
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c.isEmpty()) {
            return false;
        }
        if (c.size() == 1 && element.equals(c.iterator().next())) {
            return false;
        }
        // we cannot grow
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "[" + element + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        SingletonSortedSet rhs = (SingletonSortedSet) o;
        return element.equals(rhs.element);
    }

    @Override
    public int hashCode() {
        return element.hashCode();
    }

    private class SingletonSoredSetIterator implements Iterator<T> {
        private T onlyElem;

        private SingletonSoredSetIterator(T onlyElem) {
            this.onlyElem = onlyElem;
        }

        @Override
        public boolean hasNext() {
            return onlyElem != null;
        }

        @Override
        public T next() {
            if (onlyElem == null) {
                throw new NoSuchElementException();
            }
            T elem = onlyElem;
            onlyElem = null; // done iterating
            return elem;
        }
    }

    private class SingletonSortedSetSpliterator implements Spliterator<T> {
        private static final int characteristics = ORDERED | DISTINCT | SORTED | SIZED | NONNULL | IMMUTABLE | SUBSIZED;
        private T onlyElem;

        private SingletonSortedSetSpliterator(SingletonSortedSet<T> theSet) {
            this.onlyElem = theSet.first();
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (onlyElem != null) {
                action.accept(onlyElem);
                onlyElem = null; // terminate
                return true;
            }
            return false;
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            tryAdvance(action);
        }

        @Override
        public Spliterator<T> trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            return getExactSizeIfKnown();
        }

        @Override
        public long getExactSizeIfKnown() {
            if (onlyElem != null) {
                return 1;
            }
            return 0;
        }

        @Override
        public int characteristics() {
            return characteristics;
        }

        @Override
        public boolean hasCharacteristics(int characteristics) {
            return (SingletonSortedSetSpliterator.characteristics & characteristics) == characteristics;
        }

        @Override
        public Comparator<? super T> getComparator() {
            return null; // same as SingletonSortedSet
        }
    }
}
