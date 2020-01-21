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

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.function.Supplier;

import static java.util.Spliterator.DISTINCT;
import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterator.SIZED;
import static java.util.Spliterator.SORTED;
import static java.util.Spliterator.SUBSIZED;

/**
 * @author Anantharaman "Anand" Ganesh
 * @since 2020-01-21
 */
public class SingletonSortedSetTest {
    private static final Random RANDOM = new Random();

    @Test
    public void testEquals() {
        SingletonSortedSet<Integer> s1, s2;

        s1 = new SingletonSortedSet<>(0);
        s2 = new SingletonSortedSet<>(0);
        Assert.assertEquals(s1, s2);
        Assert.assertEquals(s2, s1);

        s1 = new SingletonSortedSet<>(1);
        s2 = new SingletonSortedSet<>(1);
        Assert.assertEquals(s1, s2);
        Assert.assertEquals(s2, s1);

        int rand = RANDOM.nextInt();
        s1 = new SingletonSortedSet<>(rand);
        s2 = new SingletonSortedSet<>(rand);
        Assert.assertEquals(s1, s2);
        Assert.assertEquals(s2, s1);
    }

    @Test
    public void testNotEquals() {
        SingletonSortedSet<Integer> s1, s2;

        s1 = new SingletonSortedSet<>(0);
        s2 = new SingletonSortedSet<>(1);
        Assert.assertNotEquals(s1, s2);
        Assert.assertNotEquals(s2, s1);

        int rand = RANDOM.nextInt();
        s1 = new SingletonSortedSet<>(1);
        s2 = new SingletonSortedSet<>(rand);
        Assert.assertNotEquals(s1, s2);
        Assert.assertNotEquals(s2, s1);

        s1 = new SingletonSortedSet<>(rand);
        s2 = new SingletonSortedSet<>(0);
        Assert.assertNotEquals(s1, s2);
        Assert.assertNotEquals(s2, s1);
    }

    @Test
    public void testHashCode() {
        SingletonSortedSet<Integer> s1, s2;

        s1 = new SingletonSortedSet<>(0);
        s2 = new SingletonSortedSet<>(0);
        Assert.assertEquals(s1.hashCode(), s2.hashCode());
        Assert.assertEquals(s2.hashCode(), s1.hashCode());

        s1 = new SingletonSortedSet<>(1);
        s2 = new SingletonSortedSet<>(1);
        Assert.assertEquals(s1.hashCode(), s2.hashCode());
        Assert.assertEquals(s2.hashCode(), s1.hashCode());

        int rand = RANDOM.nextInt();
        s1 = new SingletonSortedSet<>(rand);
        s2 = new SingletonSortedSet<>(rand);
        Assert.assertEquals(s1.hashCode(), s2.hashCode());
        Assert.assertEquals(s2.hashCode(), s1.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNull() {
        new SingletonSortedSet<>(null);
    }

    /**
     * test the methods of {@link SingletonSortedSet}
     */
    @Test
    public void testSingletonSortedSetMethods() {
        Integer value = 1;
        final SingletonSortedSet<Integer> sss = new SingletonSortedSet<>(value);

        Assert.assertSame(value, sss.getElement());
        Assert.assertSame(value, sss.getElement(1));
        Assert.assertNull(sss.getElement(2));
    }

    /**
     * test the methods of {@link SortedSet}
     */
    @Test
    public void testSortedSetMethods() {
        final SingletonSortedSet<Integer> sss = new SingletonSortedSet<>(1);

        /**
         * Testing {@link SortedSet#comparator()}
         */
        Assert.assertNull(sss.comparator());

        /**
         * Testing {@link SortedSet#first()} and {@link SortedSet#last()}
         */
        Assert.assertEquals(1, (int) sss.first());
        Assert.assertEquals(1, (int) sss.last());

        /**
         * Testing {@link SortedSet#headSet(Object)}
         */
        SortedSet<Integer> sssHead = sss.headSet(1);
        Assert.assertTrue(sssHead.isEmpty());
        sssHead = sss.headSet(2);
        Assert.assertEquals(sss, sssHead);

        /**
         * Testing {@link SortedSet#tailSet(Object)}
         */
        SortedSet<Integer> sssTail = sss.tailSet(0);
        Assert.assertEquals(sss, sssTail);
        sssTail = sss.tailSet(1);
        Assert.assertTrue(sssTail.isEmpty());

        /**
         * Testing {@link SortedSet#subSet(Object, Object)}
         */
        SortedSet<Integer> sssSubset01 = sss.subSet(0, 1);
        Assert.assertTrue(sssSubset01.isEmpty());
        SortedSet<Integer> sssSubset11 = sss.subSet(1, 1);
        Assert.assertTrue(sssSubset11.isEmpty());
        SortedSet<Integer> sssSubset12 = sss.subSet(1, 2);
        Assert.assertEquals(sss, sssSubset12);
        SortedSet<Integer> sssSubset23 = sss.subSet(2, 3);
        Assert.assertTrue(sssSubset23.isEmpty());

        /**
         * Testing {@link SortedSet#spliterator()}
         */
        Spliterator<Integer> sssSpliterator = sss.spliterator();
        Assert.assertEquals(ORDERED | DISTINCT | SORTED | SIZED | NONNULL | IMMUTABLE | SUBSIZED,
                sssSpliterator.characteristics());
        Assert.assertEquals(1, sssSpliterator.estimateSize());
        Assert.assertEquals(sss.comparator(), sssSpliterator.getComparator());
        Assert.assertEquals(1, sssSpliterator.getExactSizeIfKnown());
        Assert.assertTrue(sssSpliterator
                .hasCharacteristics(ORDERED | DISTINCT | SORTED | SIZED | NONNULL | IMMUTABLE | SUBSIZED));
        /**
         * Testing {@link Spliterator#forEachRemaining(Consumer)}
         */
        sssSpliterator.forEachRemaining(val -> Assert.assertEquals(1, (int) val));
        /**
         * Testing {@link Spliterator#tryAdvance(Consumer)}
         */
        sssSpliterator = sss.spliterator();
        Assert.assertTrue(sssSpliterator.tryAdvance(val -> Assert.assertEquals(1, (int) val)));
        Assert.assertFalse(sssSpliterator.tryAdvance(val -> Assert.fail("should not be invoked")));
        /**
         * Testing {@link Spliterator#trySplit()}
         */
        sssSpliterator = sss.spliterator();
        Spliterator<Integer> sssSpliteratorSplit = sssSpliterator.trySplit();
        Assert.assertNull(sssSpliteratorSplit);
    }

    private void assertUnsupported(Supplier<?> func, String method) {
        try {
            func.get();
            Assert.fail(method + " must throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    private void testIterator(SingletonSortedSet<Integer> sss) {
        Iterator<Integer> iterator = sss.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, (int) iterator.next());
        Assert.assertFalse(iterator.hasNext());
        try {
            iterator.next();
            Assert.fail("Iterator::next() should have thrown NoSuchElementException here");
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    /**
     * test the methods of {@link java.util.AbstractSet}
     */
    @Test
    public void testAbstractSetMethods() {
    }

    /**
     * test the methods of {@link java.util.Set}
     */
    @Test
    public void testSetMethods() {
        final SingletonSortedSet<Integer> sss = new SingletonSortedSet<>(1);

        /**
         * Testing {@link java.util.Set#add(Object)}
         */
        Assert.assertFalse(sss.add(1));
        assertUnsupported(() -> sss.add(2), "SingletonSortedSet::add");

        /**
         * Testing {@link java.util.Set#addAll(Collection)}
         */
        Collection<Integer> sub;
        sub = new HashSet<>();
        Assert.assertFalse(sss.addAll(sub));
        sub = new HashSet<>();
        sub.add(1);
        Assert.assertFalse(sss.addAll(sub));
        final Collection<Integer> finalSub = new HashSet<>();
        finalSub.add(2);
        assertUnsupported(() -> sss.addAll(finalSub), "SingletonSortedSet::addAll");

        /**
         * Testing {@link Set#clear()}
         */
        assertUnsupported(() -> {
            sss.clear();
            return true;
        }, "SingletonSortedSet::clear");

        /**
         * Testing {@link Set#contains(Object)}
         */
        Assert.assertFalse(sss.contains(0));
        Assert.assertTrue(sss.contains(1));
        Assert.assertFalse(sss.contains(2));

        /**
         * Testing {@link Set#containsAll(Collection)}
         */
        sub = new HashSet<>();
        Assert.assertTrue(sss.containsAll(sub));
        sub = new HashSet<>();
        sub.add(1);
        Assert.assertTrue(sss.containsAll(sub));
        sub = new HashSet<>();
        sub.add(2);
        Assert.assertFalse(sss.containsAll(sub));
        sub = new HashSet<>();
        sub.add(1);
        sub.add(2);
        Assert.assertFalse(sss.containsAll(sub));
        sub = new HashSet<>();
        sub.add(2);
        sub.add(3);
        Assert.assertFalse(sss.containsAll(sub));

        /**
         * Testing {@link Set#isEmpty()}
         */
        Assert.assertFalse(sss.isEmpty());

        /**
         * Testing {@link Set#iterator()}
         */
        testIterator(sss);
        // run again to make sure no side-effects
        testIterator(sss);

        /**
         * Testing {@link Set#remove()}
         */
        assertUnsupported(() -> sss.remove(1), "Set::remove");
        assertUnsupported(() -> sss.remove(2), "Set::remove");
        assertUnsupported(() -> sss.remove(null), "Set::remove");

        /**
         * Testing {@link Set#removeAll(Collection)}
         */
        final Collection<Integer> subRemoveAll1 = new HashSet<>();
        assertUnsupported(() -> sss.removeAll(subRemoveAll1), "Set::removeAll");
        final Collection<Integer> subRemoveAll2 = new HashSet<>();
        subRemoveAll2.add(1);
        assertUnsupported(() -> sss.removeAll(subRemoveAll2), "Set::removeAll");
        final Collection<Integer> subRemoveAll3 = new HashSet<>();
        subRemoveAll3.add(2);
        assertUnsupported(() -> sss.removeAll(subRemoveAll3), "Set::removeAll");
        final Collection<Integer> subRemoveAll4 = new HashSet<>();
        subRemoveAll4.add(1);
        subRemoveAll4.add(2);
        assertUnsupported(() -> sss.removeAll(subRemoveAll4), "Set::removeAll");
        final Collection<Integer> subRemoveAll5 = null;
        assertUnsupported(() -> sss.removeAll(subRemoveAll5), "Set::removeAll");

        /**
         * Testing {@link Set#retainAll(Collection)}
         */
        final Collection<Integer> subRetainAll1 = new HashSet<>();
        assertUnsupported(() -> sss.retainAll(subRetainAll1), "Set::retainAll");
        final Collection<Integer> subRetainAll2 = new HashSet<>();
        subRetainAll2.add(1);
        assertUnsupported(() -> sss.retainAll(subRetainAll2), "Set::retainAll");
        final Collection<Integer> subRetainAll3 = new HashSet<>();
        subRetainAll3.add(2);
        assertUnsupported(() -> sss.retainAll(subRetainAll3), "Set::retainAll");
        final Collection<Integer> subRetainAll4 = new HashSet<>();
        subRetainAll4.add(1);
        subRetainAll4.add(2);
        assertUnsupported(() -> sss.retainAll(subRetainAll4), "Set::retainAll");
        final Collection<Integer> subRetainAll5 = null;
        assertUnsupported(() -> sss.retainAll(subRetainAll5), "Set::retainAll");

        /**
         * Testing {@link Set#size()}
         */
        Assert.assertEquals(1, sss.size());

        /**
         * Testing {@link Set#spliterator()}
         *
         * Covered by {@link SingletonSortedSetTest#testSortedSetMethods()}
         */
        // no test required

        /**
         * Testing {@link Set#toArray()}
         */
        Assert.assertArrayEquals(new Object[]{1}, sss.toArray());

        /**
         * Testing {@link Set#toArray(Object[])}
         */
        assertUnsupported(() -> sss.toArray(new Double[1]), "Set::toArray(T1[])");
    }
}
