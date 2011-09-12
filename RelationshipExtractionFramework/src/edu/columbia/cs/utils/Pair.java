package edu.columbia.cs.utils;

import java.io.Serializable;

public class Pair<A, B> implements Serializable{
	private final A mA;
	private final B mB;

	public Pair(A a, B b) {
		this.mA = a;
		this.mB = b;
	}

	public A a() {
		return this.mA;
	}

	public B b() {
		return this.mB;
	}

	public String toString() {
		return "(" + a() + "," + b() + ")";
	}

	public boolean equals(Object that) {
		if (!(that instanceof Pair))
			return false;
		Pair thatPair = (Pair)that;
		return ((this.mA.equals(thatPair.mA)) && (this.mB.equals(thatPair.mB)));
	}

	public int hashCode()
	{
		return (31 * this.mA.hashCode() + this.mB.hashCode());
	}
}
