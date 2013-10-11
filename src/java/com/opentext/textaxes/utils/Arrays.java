package com.opentext.textaxes.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterators;

public final class Arrays
{
	public static <T> boolean any(T[] array, Predicate<? super T> predicate) {
		return Iterators.any(Iterators.forArray(array), predicate);
	}

	public static <T> boolean all(T[] array, Predicate<? super T> predicate) {
		return Iterators.all(Iterators.forArray(array), predicate);
	}

	public static <T> T find(T[] array, Predicate<? super T> predicate) {
		return Iterators.find(Iterators.forArray(array), predicate);
	}

	public static <T> Iterable<T> filter(final T[] unfiltered, final Predicate<? super T> predicate) {
		checkNotNull(unfiltered);
		checkNotNull(predicate);
		return new FluentIterable<T>()
		{
			@Override
			public Iterator<T> iterator() {
				return Iterators.filter(Iterators.forArray(unfiltered), predicate);
			}
		};
	}
}
