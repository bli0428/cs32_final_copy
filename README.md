# README

## Maps

### BREAKDOWN OF WORK
Gokul: Bacon and Autocorrect
Reid: Stars (and Handling)

### BUGS
No known bugs.

### STYLE ERROR EXPLANATIONS
1. Static array initializer require a format that throws the style error "{ is followed by whitespace". We attempted to fix this spacing
in Eclipse, but the autoformatter recorrects it every time. This is the huge majority of style errors.
2. REPL.java line 136 gives a magic number style error, but this number is required so as to parse the input string 
based on the Street Names. It makes sense to hard code this magic number in because the input string will always
be formatted in the same way, and so there is no reason to use an iterative solution.
3. MapCommand.java Line 231 gives a line has too many characters error, but the constructor itself is longer
than 80 characters and so this is unavoidable.
4. MapsDatabaseHelper.java Line 288 & 289 give magic number errors, but these are required to setDoubles for our SQL query.
There is no cleaner iterative solution than to do this.

### DESIGN DETAILS(Please note that everything goes into more detail in the inline comments!)
We used the KDTree from Stars to determine the nearest point to a coordinate, since the command needed
for this project was essentially equivalent to "nearest 1 [x] [y]". Thus, the KDTree was populated with
every traversable node in the database, and then the static Nearest class was used to traverse the KDTree to find
the nearest Node for any given coordinates(More on this in comments).

We used Bacon to find the shortest route from one node to the next by using Dijkstra's algorithm (updated 
with A* search) to find the shortest path. The MapsGraphBuilder implements GraphBuilder interface that only has one method getNeighbors(GraphNode node). This method simply takes in any sourceNode in the graph and will return a Map from every neighbor 
GraphNode to the GraphEdge that connects them. This MapsGraphBuilder deals with Node and Way objects specifically, and uses the MapsDatabaseHelper to find the neighboring Nodes that Ways can be traversed to. Then, GraphNodes and GraphEdges are created that store these Nodes and Ways respectively as values that are mapped as mentioned before. This can then be given to the Graph, 
which allows the Graph to be built dynamically.

The Graph class is a completely generic structure that holds GraphNodes and GraphEdges which are each also generic and can hold values (like nodes, ways in this case). The Graph also takes in a
generic GraphBuilder, which allows it to know exactly which Nodes neighbor which what what Edges. Djikstra is called in this class where A* is also implemented where the A* heuristic is the haversine distance. Djiksta works with a priorityqueue in order to find the shortest distance between the two nodes based on the weight of the GraphEdge(Refer to inline comments for specifics of Djiikstra and A*).

We used Autocorrect to autocorrect the names of ways. Using the Generic Trie, any corpus of Strings as words can be inserted into it to autocorrect. In this case, these Strings were Way names.

Our codebases were able to be integrated easily due to how generic they already were. When the user connects to
a new database, all we needed to do was query all the necessary information to instantiate a TreeBuilder, which creates
the KDTree, a MapsGraphBuilder, which implements a generic GraphBuilder interface that is used to create the graph that can run dkikstra for route, and a Trie, which is used for autocorrect. Once this was done, the functionality for all of the commands 
was already there, and all that is needed is a MapsDatabaseHelper that either queried or checked cache for any more
information that was needed from the database. Thus, despite there being different codebases from different people, it was easy to tie everything in together.

GUI: Fully functional with smooth dragging and scrolling, click for nearest, either click or input intersection routing,
and autocorrect way completion.  

### RUNTIME/SPACE OPTIMIZATIONS
We have a front end caching structure that stores pieces of the map as tiles. When the user
pans or scrolls over a tile that has already been loaded no calls are made to the backend.
When the user moves to a new area of the map that has not been reached, the new tiles are
loaded and added to the cache. This improves speed and smoothness drastically. 

Our MapsDatabaseHelper also includes caches on every type of query that was needed for the various commands. 
Thus, on the backend side, our commands run very quickly as information is being stored efficiently.

### HOW TO RUN TESTS
All of our system tests can be run using the command "python3 cs32-test 
./tests/student/maps/*"

All of our unit tests will be automatically run when "mvn package" is
entered from the command line. 

In order to run both unit and system tests, we assume the smallMaps and maps db will be in the directory data/maps/<db>.sqlite3

### HOW TO RUN
The program can be built by running mvn package from the command-line
(note this may take some time because of unit tests).
The program can then be run using the command ./run
A few macros may be used:
--gui: runs a gui on a given port
--port <port #>: sets the port number

From within the program, the following commands may be used:
- map <db path>
- ways <lat1> <lon1> <lat2> <lon2>
- nearest <latitude> <longitude>
- route <lat1> <lon1> <lat2> <lon2>
- route "Street 1" "Cross-street 1" "Street 2" "Cross-street 2"
- suggest <input street name>

Before using the GUI, data must be input via the command-line using the
command "map <db path>"

### BROWSER
Chrome
