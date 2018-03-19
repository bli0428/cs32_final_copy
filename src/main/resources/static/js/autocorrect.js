$(document).ready(() => {

    const suggestionList = $("#mainlist");
    const input = $("#input");
    const def = $("#default");
    const prefix = $("#prefix");
    const whitespace = $("#whitespace");
    const led = $("#led");
    const smart = $("#smart");

    const dict = $("#dict");
    let dictin = 0;
    let dictval = "off";

    const ge = $("#ge");
    let gein = 0;
    let geval = "off";

    def.click(function(){
        prefix.val("on");
        whitespace.val("on");
        led.val("2");
        smart.val("off");
    });

        dict.click(function(){

        if(dictin == 0){
    dictval = "on";
    dictin = dictin + 1;
    $.post('/validate', {'dict': dictval}, responseJSON => {
})
}else{
    dictval = "off";
}

    });

            ge.click(function(){

        if(gein == 0){
    geval = "on";
    gein = gein + 1;
             $.post('/validate', {'ge': geval}, responseJSON => {
})
}else{
    geval = "off";
}

    });

    input.keyup(event => {

        suggestionList.empty();
        let currInput = input.val();


        $.post('/validate', {'input': input.val(), 'prefix': prefix.val(), 'whitespace': whitespace.val(), 'led': led.val(), 'smart': smart.val()}, responseJSON => {

            const responseObject = JSON.parse(responseJSON);
                for(let s of responseObject.suggestions){
                   suggestionList.append('<li>' + s + '</li>');
               }

               let li = document.getElementsByTagName("li");

               for(let l of li){
               l.addEventListener("click", clickSuggestion);
               }
               if(suggestionList.length > 5){
               suggestionList.remove();
               }

function clickSuggestion(e){

const lastIndex = currInput.lastIndexOf(" ");

currInput = currInput.substring(0, lastIndex);
input.val(currInput + " " + e.target.innerHTML);

}

        })

               });



               });
