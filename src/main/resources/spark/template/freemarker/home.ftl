<#assign content>


<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
	<a class="navbar-brand" href="/home">ChesS32</a>
	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo02" aria-controls="navbarTogglerDemo02" aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarTogglerDemo02">
		<ul class="navbar-nav mr-auto mt-2 mt-lg-0">
			<li class="nav-item active">
				<a class="nav-link" href="/home">Home<span class="sr-only">(current)</span></a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="/changeusername">Change Username</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="/changepassword">Change Password</a>
			</li>
		</ul>
		<form class="form-inline my-2 my-lg-0" method="POST" action="/logout">
			<input class="btn btn-outline-success my-2 my-sm-0" type="submit" value="Log Out">
		</form>
	</div>
</nav>

<div class="container">
	<div class="row">
		<div class="col-lg-12 text-center" style="margin-top: 4%">
			<p class="display-4" style="margin-bottom: 0px;margin-top: 10px">Welcome ${user}!</p>
			<p class="lead" style="margin-top: 0px">${message}</p>
		</div>
	</div>
	<div class="row justify-content-center" style="margin-top: 4%">
		<div class="col-md-3 text-center">
			<button class="btn btn-success btn-lg" type="button" onclick="addGame('chess')">Create Chess Game</button>
		</div>
		<div class="col-md-3 text-center">
			<button class="btn btn-success btn-lg" type="button" onclick="addGame('bughouse')">Create Bughouse Game</button>
		</div>
	</div>
	<div id='menu' class="row align-items-center" style="margin-top: 4%">
		${content}
	</div>
</div>






</#assign>
<#include "main.ftl">
