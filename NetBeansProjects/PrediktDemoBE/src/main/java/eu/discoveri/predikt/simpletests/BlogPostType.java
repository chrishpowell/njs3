/*
 */
package eu.discoveri.predikt.simpletests;

/**
 *
 * @author Chris Powell, Discoveri OU
 */
public enum BlogPostType
{
    // Deliberately this way round (enum has built in compareTo [random order? default here is: GUIDE, NEWS, REVIEW !])
    NEWS("News",2),
    GUIDE("Guide",1),
    REVIEW("Review",3);
    
    private final String  name;
    private final int     order;

    BlogPostType( String name, int order )
    {
        this.name = name;
        this.order = order;
    }
}
