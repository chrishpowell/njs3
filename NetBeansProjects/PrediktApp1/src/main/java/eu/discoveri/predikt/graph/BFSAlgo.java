/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.predikt.graph;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Note T must implement equals/hash.
 * 
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class BFSAlgo
{
    /**
     * Search rooted tree (not graph).
     * 
     * @param <T>
     * @param find
     * @param root
     * @return 
     */
    public static <T> Optional<Tree<T>> search(Tree<T> find, Tree<T> root)
    {
        Queue<Tree<T>> queue = new ArrayDeque<>();
        queue.add(root);

        Tree<T> currentNode;
        while( !queue.isEmpty() )
        {
            currentNode = queue.remove();
            System.out.println("(BFSAlgo::tree search) Visited node: " +currentNode.getValue());

            if( currentNode.equals(find) )
                { return Optional.of(currentNode); }
            else
                { queue.addAll(currentNode.getChildren()); }
        }

        return Optional.empty();
    }

    /**
     * Search graph structure (rather than tree).
     * 
     * @param <T>
     * @param find
     * @param start
     * @return 
     */
    public static <T> Optional<Vertex> search(Vertex find, Vertex start)
    {
        System.out.println("Find vtx: " +find+ ", Start vtx: " +start);
        Queue<Vertex> queue = new ArrayDeque<>();
        queue.add(start);

        Vertex currentNode;
        Set<Vertex> alreadyVisited = new HashSet<>();

        while( !queue.isEmpty() )
        {
            currentNode = queue.remove();
            System.out.println("(BFSAlgo::graph search) Visited node: " +currentNode);

            if (currentNode.equals(find)) {
                return Optional.of(currentNode);
            } else {
                alreadyVisited.add(currentNode);
                System.out.println(" :Adjacencies");
                currentNode.getAdjacencies().forEach(adj -> System.out.print("  "+adj.getENode()));
                System.out.println("");
                
                // Get remote nodes
                queue.addAll(currentNode.getAdjacencies().stream().map(n -> n.getENode()).collect(Collectors.toList()));
                System.out.println(" :Queue");
                queue.forEach(q -> System.out.println("  "+q.toString()));
                queue.removeAll(alreadyVisited);
            }
        }

        return Optional.empty();
    }
}
