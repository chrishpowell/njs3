/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.codesnippets.graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @param <T>
 * @email info@astrology.ninja
 */
public class Tree<T>
{
    private final T             value;
    private final List<Tree<T>> children = new ArrayList<>();

    /**
     * Constructor
     * @param value 
     */
    private Tree(T value)
    {
        this.value = value;
    }

    public static <T> Tree<T> of(T value) { return new Tree<>(value); }

    public T getValue() { return value; }

    public List<Tree<T>> getChildren() { return Collections.unmodifiableList(children); }

    public Tree<T> addChild(T value)
    {
        Tree<T> newChild = new Tree<>(value);
        children.add(newChild);
        return newChild;
    }
}
