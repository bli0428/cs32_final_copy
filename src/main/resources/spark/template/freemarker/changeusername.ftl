<#assign content>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
	<a class="navbar-brand" href="/home">Chess32</a>
	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo02" aria-controls="navbarTogglerDemo02" aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarTogglerDemo02">
		<ul class="navbar-nav mr-auto mt-2 mt-lg-0">
			<li class="nav-item">
				<a class="nav-link" href="/home">Home</a>
			</li>
			<li class="nav-item active">
				<a class="nav-link" href="/changeusername">Change Username<span class="sr-only">(current)</span></a>
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
	<div class="container">
		<div class="row">
			<div class="form-group col-lg-8 offset-lg-2" style="margin-top:2%">
				<form role="form" method="POST" action="/changeusernameresults">
					<h1>Change Username <small class="lead"> ${message}</small></h1>
					<hr class="colorgraph">
					<div class="row">
						<div class="col-xs-12 col-sm-6 col-md-6">
							<div class="form-group">
								<input type="text" name="currusername" placeholder="username" class="form-control input-lg" tabindex="5">
							</div>
						</div>
						<div class="col-xs-12 col-sm-6 col-md-6">
							<div class="form-group">
								<input name="password" type="password" placeholder="password" class="form-control input-lg" tabindex="6">
							</div>
						</div>
					</div>
					<div class="form-group">
						<input name="newusername" type="text" placeholder="new username" class="form-control input-lg" tabindex="3">
					</div>
					<div class="row">
						<div class="col-lg-12">
							<input id="submit" type="submit" value="Change Username" class="btn btn-success btn-block btn-lg">
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<canvas id="canvas"></canvas>

</#assign>
<#include "main.ftl">
