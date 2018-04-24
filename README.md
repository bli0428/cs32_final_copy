# README

## Stars
### Design Questions
1. In order to add a command, I would have to make some changes to the processCommand and processCommandHelper methods in the Main class. I would parse the user input like in my current implementation and then match the new key command word to the command that the user typed in (by creating another "else if" statement). The alterations would allow for the user to enter the new command without getting an error. I would follow the same try/catch format that is currently in my code to make sure that any arguments following the new command are valid. If the arguments are not valid, I would throw a specific "ERROR: " message so that the user knows what went wrong.

2. Right now, my KDTree takes in Nodes that hold variables with doubles as coordinates. A double can only be so precise. If we are thinking about mapping the surface of the earth, the coordinate system may need to be changed. Additionally, we may want to look at earth in 2-D instead of 3-D, using longitude and latitude. My KDTree can support changes in dimension, but a few things would need to be altered. Also, runtime may be an issue as the number of points increase.

3. In order for Collection(Star) db = new KDTree(Star)() to compile and work properly, I would need to have KDTree implement Collection. Also, my implementation of KDTree does not take in a type T. I would have to have the class take in type T and change the methods in the class accordingly.

### Design Details
My KDTree class does not take in a type T. Instead, my Node class takes in a type T and the KDTree class has a root that holds Node.

### Running Tests
JUnit tests will run when "mvn package" is evoked. System tests can be run by "./cs32-test ./tests/student/stars/*".

### Building/Running Program
To build the program, evoke "mvn package". To run the GUI evoke "./run --gui". To run the program from the terminal evoke "./run" and proceed to type commands into the terminal.

### Comments
The GUI does not work very well. Be gentle. Further, I know that the style of the handlers is not ideal.

## Autocorrect
### Design Questions
1. To handle autocorrecting multiple input fields on the same page I would have to change the html to include another input field. I would assign it a different id value so I can correctly refer to and get the contents of the correct field. I would also probably put it in a different div with a different id so that I could refer to the list elements in a specific div to correctly assign the suggestion values to the list items corresponding to the user input. I would also have to add some javascript so that there is a function that sets a list item to the input field upon clicking it because right now it would set both input fields equal to whatever value that is clicked. Also, postParameters would have to take in more fields, or modified fields. I would probably make a variable to set the current word that is being autocorrected (based on which input box the user has clicked and is typing in). I would pass in this variable to postParameters so that the backend does not need to change. Overall, there would mostly be additions/changes to id references in order to make sure the right elements are being accessed and operated on. If I am autocorrecting on two pages I would not change the frontend. I would use the same frontend but create a new "/xxx" ending to be the target page. In terms of backend, I would create a new AutocorrectHandler but with a different name and a different corpus. I would create a variable in my GUI to store the new tree so that both trees are available when running the server. I would also create a new ValidateHandler that accesses the correct variables of the new tree and operates on those instead of the current ones. There is probably a way to combine this with what I have now, like passing in a certain tree into the validate handler so that I do not have to repeat code and that the handler will generate suggestions using the correct corpus.

2. The array of children will increase by one. Each "layer" of the trie will contain the a Node for each of the 26 letters of the English alphabet plus a node for trie. The new number of nodes will be proportional to the number of words in the trie. There will be one more node for each word in the trie as θ needs to be added to each word. However, that is the upper bound of nodes that will be added, since some words, such as "cat," can not be negated.

### Bugs
When the GUI is running, there are sometimes more than five options. I think this happens when the user types too fast. If there are more than five suggestions, it goes away upon typing a new letter.

### Design Details
I have a package that contains my implementation of a Trie. The package has a TrieNode that can be of any type T and contains an array of any size (have to change final SIZE variable) that represents the node's children. I have created a GUI class to separate handlers, gson, spark servers, etc. from the Main execution of the program. This was done for organization/clarity. I have created an autocorect package that contains classes specific to this project. SuggestionGeneration is an abstract/util class. However, it still needs an instance of Autocorrect to evoke the methods within. I made this design decision for oranization. Rank is also an abstract/util class. You do not need an instance of Autocorrect to evoke the methods within. Rank uses the WordComparator to rank an ArrayList. WordComparator implements Comparator and overrides the "compare" method to compare and rank words based on the algorithm given in the spec. CorporaReader is specific to Autocorrect in that it reads and parses the file in a way that makes it easy to build a Trie.

### Running Tests
JUnit tests will run when "mvn package" is evoked. System tests can be run by "./cs32-test ./tests/student/autocorrect/*".

### Building and Running Program
To build the program, evoke "mvn package". To run the GUI evoke "./run --gui". You can then visit "http://localhost:4567/autocorrect" on Google Chrome to interact with the GUI. To run the program from the terminal evoke "./run" and proceed to type commands into the terminal.

### Smart Suggestion
My implementation of smart suggestion is only applicable in the GUI. For smart suggestion I decided to rank words "higher" when they are what the user intended. For example, if the user inputs "hello", the five suggestions that are output are "hello", "helloed", "helloes", "helloing", "hellos". The user can interact with the GUI and click on the suggestion that they want. If they click "hellos" as what they meant to type, the next time that they input "hello", "hellos" will be the first word suggested after "hello". When the user clicks on the list item that contains the correctly autocorrected word/ the word that they meant, the word count in the wordFrequency map is updated to be +1 higher than it was before. Similarly, if the user typed multiple words, the correctly autocorrected phrase/ the phrase that they meant is used to update the bigramMap and increase the frequency of that pair by +1 or add the pair to the map if it wasn't there before. Just like when running the program from the command line, if there is more than one word that the user inputs, only the last two words are considered.

Smart rank doesn't work in the terminal because I was not sure how to implement it there without messing up testing. I suppose that if smart ranking was turned on (smart = true) then after suggestions are printed out, a line could be printed to the console asking which suggestion is the one that the user meant (referred to as 1-5 to make it easier for the user not to make mistakes). Then, the corresponding suggestion could be added to the maps like it is in the GUI.


## Bacon
### Design Questions
1. For other developers to easily add their own algorithms to my program, I would make some kind of 'algorithm' super class. For a developer to add on to my code, the would create their own algorithm class that extends mine. They would override the 'run()' method so that their algorithm can be called the same way as my Dijkstra algorithm. Their 'run' method would be their graph sorting algorithm.

2. To accept multiple file types, I would add an 'if' statement to my method that deals with reading in databases. I would check the file extension (i.e. '.txt' or '.sqlite3' or '.csv') and create the appropriate file reader to pass the file into. The file readers would all process the information in the same way (putting it into a HashMap or ArrayList or a database if it isn't already one) so that my program will not have to change. Creating different file readers for different types of files would allow me to evoke all of my code and seamlessly operate on the same data structures without knowing the work that went behind parsing the file.

3. If movies now have an associated year of release that needs to be considered when mapping actors to actors, I would add another constraint to my 'getNeighbors' function. The movie year constraint is very similar to the last-name-inital-first-name-initial constraint that is already imposed on the bacon project. I would add another field to my Edge class called 'year' which would be an int representing the movie's year of release. When adding Nodes to 'getNeighbors' I would check to see if the Edge year is less than or equal to (depending on descending or ascending years) the Edge year of the Edge that came before it.

### Bugs
My program is a bit slow. I think there might be some hashCode collisions. I had to override the hashCode and equals methods for the object classes that I made so I could easily cache things in a HashMap and compare equality. Some of my system tests time out after 5 seconds. When I ran the profiler on my project the method that took the most time was my 'equals' method for my Node and Actor classes. There might be an issue with how I overrode them. Also, for my GUI, I harcoded the small database in. This is not ideal but you can go into the GUI class and change it.

### Design Details
For this project I created a graph package that contains a generic implementation of a graph. While I do not actually have a graph class, the Edge and Node class work off of each other to create a graph. Nodes are connected by Edges, showing direction in the graph. Since Edge has a start and an end node, the graph is a digraph (directed graph) since it flows in one direction. Nodes and Edges are of generic type T. The Node holds a value of type T and the Edge holds Nodes that are of type T. The only thing specific to this project in my graph package is that the Edge class has a 'movie' variable. This is to keep track of the movies that connect to actors when the Nodes hold actors. The dijkstra implementation in my graph class is also of type T and can operate on any Node with type T.

I also have a bacon package that holds classes specific to this project. I created an Actor class and a Movie class for the Nodes to hold. I also have a Bacon class which is where the database is queried and where everything is run from. I also have a BaconDijkstra class. BaconDijkstra extends the Dijkstra class in graph and is identical to the Dijkstra class in graph except that type T is replaced by Actor and some specific things (like 'getNeighbors' instead of 'getEdges') are evoked due to not being able to read the entire database. Each method is overriden but there might have been a better way to do this?

### Building and Running Program
To build the program, evoke "mvn package". To run the GUI evoke "./run --gui". You can then visit "http://localhost:4567/bacon" on Google Chrome to interact with the GUI. In the GUI you can visit different pages and click on the actors in movies and the movies that actors are in. To run the program from the terminal evoke "./run" and proceed to type commands into the terminal.

### Running Tests
JUnit tests will run when "mvn package" is evoked. System tests can be run by "./cs32-test ./tests/student/bacon/*".
