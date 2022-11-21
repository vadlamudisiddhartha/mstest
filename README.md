1. Scheduler run() method will be triggered depending on schedule.
2. Optional data process.. If only replicated/activated pages, then LMD(Last Modified Date) < LRD(Last Replicated Date). If we need to include everything no condition.
3. A recursive function iterates through page by page and to the childs until we don't have any more child, read the tags, builds a hashmap 
4. All the tags pulled in the hashmap, then it will build that memory cache and thnen the content fragment path will be added to the CF Reference node.
5. In the recursive function , we know we are reading tags to build hashmap, then we get a key value pairs from the map (tagpath: page path)
6. Reading all the tags in the CF and fetching it from the hashmap which is already matched to generate CF Paths. Cf Path will be a new element.
7. We will build an arraylist of Content Fragment paths.
8. Activate only CF Ref Node which contains the arraylist of CF Paths because if you activate page some work in progress content get activated which should not happen.
9. For every servlet there should be a path to access the servlet.

Akamai Cache while fetching date from disclosure library
1. In a day based on the scheduler invalidating it , the first request goes to disclosure library and next requests will be served by the cache.
2. If we don't have an option to use Akamai cache, we can use webserver dispatcher to serve the need.

