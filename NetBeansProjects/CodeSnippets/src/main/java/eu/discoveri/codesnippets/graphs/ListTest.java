/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.codesnippets.graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class ListTest
{
    public static void main(String[] args)
    {
        List<Vtx> vx = new ArrayList<>();
        
        Vtx v1 = new V("n1",1.1d); vx.add(v1);
        Vtx v2 = new V("n2",1.2d); vx.add(v2);
        Vtx v3 = new V("n3",1.3d); vx.add(v3);
        Vtx v4 = new V("n4",1.4d); vx.add(v4);
        Vtx v5 = new V("n5",1.5d); vx.add(v5);
        Vtx v6 = new V("n6",1.6d); vx.add(v6);
        Vtx v7 = new V("n7",1.7d); vx.add(v7);
        
        Collection<Vtx> coll = vx;
        List<Vtx> d = coll.stream().collect(Collectors.toList());
        List<Double> ld = vx.stream().map(w -> w.getWeight()).collect(Collectors.toList());
        ld.forEach(w -> System.out.println("Weight: "+w));
    }
}

class V implements Vtx
{
    private String name;
    private double w;
    
    public V( String name, double w )
    {
        this.name = name;
        this.w = w;
    }
    
    @Override
    public double getWeight() { return w; }
    
    @Override
    public String getName() { return name; }
}

interface Vtx
{
    public String getName();
    public double getWeight();
}