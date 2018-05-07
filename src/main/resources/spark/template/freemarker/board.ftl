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
  <div class="row" style="margin-top: 3%">
    <div class="col"></div>
    <div class="col-7">
      <table id="chessboard"></table>
    </div>
    <div class="col">
      <div class="row" id='alertBox' style="max-height: 30%">
      </div>
      <div class="row">
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
  <div class="row" style="margin-top: 3%">
    <table id="bank"></table>
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
