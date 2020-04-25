/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.codesnippets.graph;

import eu.discoveri.codesnippets.graphs.GraphEntity;


/**
 *
 * @author Chris Powell, Discoveri OU
 */
interface Service<T extends GraphEntity>
{
    public Iterable<T> findAll();

    public T find(Long id);

    public void delete(Long id);

    public T createOrUpdate(T object);
}
