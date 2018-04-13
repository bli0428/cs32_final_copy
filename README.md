# README

## Maps

### BREAKDOWN OF WORK
Gokul: Bacon and Autocorrect
Reid: Stars (and handling)

### BUGS
No known bugs.

### DESIGN DETAILS
We used Stars to determine the nearest point to a coordinate, since the command needed
for this project was essentially equivalent to "nearest 1 [x] [y]". We used Bacon to
find the shortest route from one node to the next by using Dijkstra's algorithm (updated 
with A* search) to find the shortest path. We used Autocorrect to autocorrect the names
of ways.

When the "map" command is run, we immediately populate both the tree and the trie. While
this takes a few extra seconds, it makes "nearest" and "suggest" commands nearly instantaneous
since there are no additional calls to the database.

### RUNTIME/SPACE OPTIMIZATIONS
We have a front end caching structure that stores pieces of the map as tiles. When the user
pans or scrolls over a tile that has already been loaded no calls are made to the backend.
When the user moves to a new area of the map that has not been reached, the new tiles are
loaded and added to the cache.

### HOW TO RUN TESTS
All of our system tests can be run using the command "python3 cs32-test 
./tests/student/maps/*"

All of our unit tests will be automatically run when "mvn package" is
entered from the command line.

### TESTS TRIED BY HAND
None.

### HOW TO RUN
The program can be built by running mvn package from the command-line.
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

While using the GUI, data must be input via the command-line using the
command "map <path>"

IMPORTANT: the command "loadwords" is used to add all of the names in the database into
AutoCorrect. If this command is not entered, the words will not be added and the GUI
inputs will not have autocorrect.

### BROWSER
Chrome