<#assign content>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <a class="navbar-brand" href="/home">ChesS32</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo02" aria-controls="navbarTogglerDemo02" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>
  <div class="collapse navbar-collapse" id="navbarTogglerDemo02">
    <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
      <li class="nav-item">
        <a class="nav-link" href="/home">Home</a>
      </li>
    </ul>
    <form class="form-inline my-2 my-lg-0" method="POST" action="/logout">
      <input class="btn btn-outline-success my-2 my-sm-0" type="submit" value="Log Out">
    </form>
  </div>
</nav>

<div id="gameId" style="display:none">${gameId}</div>
<div id="gamePosition" style="display:none">${gamePosition}</div>

<div class="container">
  <div class='row' style="margin-top: 3%">
    <div class='col-sm'>
      <p id="message"></p>
    </div>
  </div>
  <div class="row justify-content-center" style="margin-top: 3%">
    <div class="col"></div>
    <div class="col-7" style="min-width: 120px">
      <table id="chessboard"></table>
    </div>
    <div class="col">
      <div class="container">
        <div class="row justify-content-center" style="min-height: 32%">
          <div class="col" id='alertBox'>
          </div>
        </div>
        <div class="row justify-content-center">
          <div class="col">
            <ul id='listRequest' class="list-group" style="display: none">
              <li class="list-group-item disabled">Request a Piece:</li>
              <li id='p' class="list-group-item list-group-item-action">Pawn</li>
              <li id='r' class="list-group-item list-group-item-action">Rook</li>
              <li id='k' class="list-group-item list-group-item-action">Knight</li>
              <li id='b' class="list-group-item list-group-item-action">Bishop</li>
              <li id='q' class="list-group-item list-group-item-action">Queen</li>
            </ul>
          </div>
        </div>
        
      </div>
    </div>
  </div>
  <div class="row" style="margin-top: 3%">
    <div class="col">
      <table id="bank"></table>
    </div>
  </div> 
</div>



<div class="modal fade" id="modal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-body">
        <h2 class="modal-title" style="margin-top: 0;padding:5px">Choose Promotion:</h2>
        <ul id="promotionMenu" class="list-group list-group-flush" style="margin-top: 0">
          <li id="rook" class="promoteOption list-group-item list-group-item-action">Rook</li>
          <li id="queen" class="promoteOption list-group-item list-group-item-action">Queen</li>
          <li id="knight" class="promoteOption list-group-item list-group-item-action">Knight</li>
          <li id="bishop" class="promoteOption list-group-item list-group-item-action">Bishop</li>
        </ul>
      </div>
    </div>
  </div>
</div>




<!-- <label class="switch">
	<input type="checkbox" id='moveToggle'>
	<span class="slider round"></span>
</label> -->

</#assign>
<#include "main.ftl">

<script>
  $(document).ready(function() {
    setup_live_moves();
  });
</script>
