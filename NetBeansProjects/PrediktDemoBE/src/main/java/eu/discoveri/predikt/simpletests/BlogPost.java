/*
 */
package eu.discoveri.predikt.simpletests;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class BlogPost
{
    String title;
    String author;
    BlogPostType type;
    int likes;

    /**
     * Constructor.
     * 
     * @param title
     * @param author
     * @param type
     * @param likes 
     */
    public BlogPost(String title, String author, BlogPostType type, int likes)
    {
        this.title = title;
        this.author = author;
        this.type = type;
        this.likes = likes;
    }

    /*
     * Getters
     */
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getLikes() { return likes; }
    public BlogPostType getType() { return type; }
    
    /**
     * Dump.
     * @return 
     */
    @Override
    public String toString()
    {
        return author+" "+title+" ["+type+"] ("+likes+")";
    }
}
