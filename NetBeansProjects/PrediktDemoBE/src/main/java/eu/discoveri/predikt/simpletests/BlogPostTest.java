/*
 */
package eu.discoveri.predikt.simpletests;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class BlogPostTest
{
    BlogPostType    type;
    String          author;
    
    public static void main(String[] args)
    {
        List<BlogPost> posts = Arrays.asList(   new BlogPost("Title-1", "Author-1", BlogPostType.GUIDE, 2),
                                                new BlogPost("Title-2", "Author-2", BlogPostType.GUIDE, 3),
                                                new BlogPost("Title-3", "Author-3", BlogPostType.NEWS, 1),
                                                new BlogPost("Title-4", "Author-4", BlogPostType.NEWS, 0),
                                                new BlogPost("Title-5", "Author-5", BlogPostType.NEWS, 2),
                                                new BlogPost("Title-6", "Author-6", BlogPostType.NEWS, 3),
                                                new BlogPost("Title-7", "Author-7", BlogPostType.REVIEW, 4)
        );
        
        // Construct type of: Map<BlogPostType, List<BlogPost>> (via posts.stream().collect(Collectors.groupingBy(BlogPost::getType));)
        // .values().forEach(System.out::println) gives:
        //   [Author-1 Title-1 [GUIDE] (2), Author-2 Title-2 [GUIDE] (3)
        //   [Author-3 Title-3 [NEWS] (1), Author-4 Title-4 [NEWS] (0), Author-5 Title-5 [NEWS] (2), Author-6 Title-6 [NEWS] (3)]
        //   [Author-7 Title-7 [REVIEW] (4)]
        //
        // Following gives (correct):
        //   GUIDE: {Author-1, Title-1 (2)} {Author-2, Title-2 (3)}
        //   NEWS: {Author-3, Title-3 (1)} {Author-4, Title-4 (0)} {Author-5, Title-5 (2)} {Author-6, Title-6 (3)}
        //   REVIEW: {Author-7, Title-7 (4)}
//        posts.stream()
//                .sorted(Comparator.comparing(BlogPost::getType))                // Sort on the BlogPost type
//                .collect(Collectors.groupingBy(BlogPost::getType))              // Now group by BlogPost type
//                .forEach((k,v) -> {                                             // This is Map<BlogPostType, List<BlogPost>>
//                    System.out.print(k.name()+":");
//                    v.forEach(bp->System.out.print(" {"+bp.getAuthor()+", "+bp.getTitle()+ " ("+bp.getLikes()+")}"));
//                    System.out.println("");
//                });
        //
        //...  Version for sorting Group via LinkedhashMap
        // Following gives:
        //   GUIDE: {Author-1, Title-1 (2)} {Author-2, Title-2 (3)}
        //   NEWS: {Author-3, Title-3 (1)} {Author-4, Title-4 (0)} {Author-5, Title-5 (2)} {Author-6, Title-6 (3)}
        //   REVIEW: {Author-7, Title-7 (4)}
        posts.stream()
                .collect(Collectors.groupingBy(BlogPost::getType, LinkedHashMap::new, Collectors.toList())) // Now group and sort by BlogPost type
                .forEach((k,v) -> {                                             // This is Map<BlogPostType, List<BlogPost>>
                    System.out.print(k.name()+":");
                    v.forEach(bp->System.out.print(" {"+bp.getAuthor()+", "+bp.getTitle()+ " ("+bp.getLikes()+")}"));
                    System.out.println("");
                });

//... Simple dump
//                .forEach((k,v) -> {                                             // This is Map<BlogPostType, List<BlogPost>>
//                    System.out.print(k.name()+":");
//                    v.forEach(bp->System.out.print(" {"+bp.getAuthor()+", "+bp.getTitle()+ " ("+bp.getLikes()+")}"));
//                    System.out.println("");
//                });
    }
}
