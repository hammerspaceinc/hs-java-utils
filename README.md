# <a name="readme"/>A library of utility classes from Hammerspace, Inc.

## Packages / Contents
+ [Java](#java)
    + [Data Structures](#data-structures)
        + [SingletonSortedSet](#sss)

# <a name="java"/>Java
## <a name="data-structures"/>Data Structures
### <a name="sss"/>SingletonSortedSet
A [SortedSet](https://docs.oracle.com/javase/8/docs/api/java/util/SortedSet.html) implementation that holds a single
element. This implementation also extends [AbstractSet](https://docs.oracle.com/javase/8/docs/api/java/util/AbstractSet.html)
because ... why not? So, it can be instantiated under an
[AbstractCollection](https://docs.oracle.com/javase/8/docs/api/java/util/AbstractCollection.html)
or [Collection](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html) as well.

The use-case for this is to have a memory-efficient data-structure in situations where you are using a `SortedSet`, say
as a member variable in a class, but know that in many occasions you would hold only a single element in the set. Its
behavior/properties are "documented" by the included unit-tests that validate/illustrate the behavior of every API of
the parent classes (`SortedSet`, `AbstractSet`, `Set`).

The implementation assumes that the element stored in the `SingletonSortedSet` implements
[Comparable](https://docs.oracle.com/javase/8/docs/api/java/lang/Comparable.html). For this reason it also implements
 `getElement()` and `getElement(T element)`.

There is some good related information in
[this post on StackOverflow](https://stackoverflow.com/questions/44399058/why-doesnt-singletonset-implement-sortedset)

 ---
 This library is licensed under *Eclipse Public License v2.0*