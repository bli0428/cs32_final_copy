$(document).ready(() => {

	const mainlist = $("#finalpath");
    const actor1 = $("#actor1");
    const actor2 = $("#actor2");
    
    const ac1 = $("#ac1");
    const ac2 = $("#ac2");
    
    const submit = $("#submit");
    
    const smalldb = $("#smalldb");
    const db = $("#db");
    
    const message = $("#message");
    
    smalldb.click(function(){
    		
    	const command = "mdb data/bacon/smallBacon.sqlite3";
    		
    	$.post('/baconac', {'command': command}, function(responseJSON){
    		
    		
    		
    	});
    	
    });

    db.click(function(){
		
    	const command = "mdb data/bacon/bacon.sqlite3";
    		
    	$.post('/baconac', {'command': command}, function(responseJSON){
    		
    		
    		
    	});
    	
    });
    
    actor1.keyup(event => {
    	
    	let currInput = actor1.val();

    	$.post('/baconac', {'ac': actor1.val()}, function(responseJSON){
    		
    		const responseObject = JSON.parse(responseJSON);
    		
        	ac1.empty();
    		const len = responseObject.suggestions;
    		
            for(let s of responseObject.suggestions){
                ac1.append('<li>' + s + '</li>');
               
            }
            
            if(ac1.length > 5){
                ac1.remove();
                }
            
            let li = document.getElementById("ac1").getElementsByTagName("li");
            
            for(let l of li){
            l.addEventListener("click", clickSuggestion1);
            }


function clickSuggestion1(e){

const lastIndex = currInput.lastIndexOf(" ");

currInput = currInput.substring(0, lastIndex);

actor1.val(e.target.innerHTML);

ac1.empty();
}
    		
    		
    	});

    	
    });
    
    
    actor2.keyup(event => {
    	
    	let currInput = actor2.val();

    	$.post('/baconac', {'ac': actor2.val()}, function(responseJSON){
    		
    		const responseObject = JSON.parse(responseJSON);
    		
        	ac2.empty();
    		const len = responseObject.suggestions;
    		
            for(let s of responseObject.suggestions){
                ac2.append('<li>' + s + '</li>');
               
            }
            
            if(ac2.length > 5){
                ac2.remove();
                }
            
            let li = document.getElementById("ac2").getElementsByTagName("li");
            
            for(let l of li){
            l.addEventListener("click", clickSuggestion1);
            }


function clickSuggestion1(e){

const lastIndex = currInput.lastIndexOf(" ");

currInput = currInput.substring(0, lastIndex);

actor2.val(e.target.innerHTML);

ac2.empty();
}
    		
    		
    	});

    	
    });

    
    submit.click(function(){
    	
    	
    	$.post('/bacon', {'actor1': actor1.val(), 'actor2': actor2.val()}, responseJSON => {
    		const responseObject = JSON.parse(responseJSON);
    		
        	mainlist.empty();
    		
    		const actorNames = responseObject.actors;
    		const movieNames = responseObject.movies;
    		const actorIds = responseObject.actorids;
    		const movieIds = responseObject.movieids;
    		
    		const len = movieNames.length;
		      $("#message").text(responseObject.message);
    		
    		for(let i = 0; i < len; i++){
    			const actor1 = actorNames[i];
    			const actor2 = actorNames[i+1];
    			const id1 = actorIds[i];
    			const id2 = actorIds[i+1];
    			const movie = movieNames[i];
    			const mid = movieIds[i];

    			const link1 = "<a href=\"/bacon/actor/" + id1 + "\">" + actor1 +"</a>";

    		      const link2 = "<a href=\"/bacon/actor/" + id2 + "\">" + actor2 +"</a>";
    		      const link3 = "<a href=\"/bacon/movie/" + mid + "\">" + movie + "</a>";
    		      mainlist.append("<li>" + "&emsp;" + link1 +
    		     " &emsp; -> &emsp;" + link2 + "&emsp; : &emsp;" + link3 + "&emsp;" + "</li>");
    		      

    		      
    			
    			
    		}
    		

    		
    	})
    	
    });

});
