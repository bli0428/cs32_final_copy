<#assign content>

<div class="container">

<div class="row">
    <div class="form-group col-xs-8 col-sm-8 col-md-8r">
		<form class="form-group" role="form" method="POST" action="/loginresults">
				<h1>Log In <small>${message}</small></h1>
				<hr class="colorgraph">
				<div class="form-group">
					<input type="text" name="username" placeholder="username" class="form-control input-lg"></input>
				</div>
				<div class="form-group">
					<input name="password" type="password" placeholder="password" class="form-control input-lg"></input>
				</div>
				<hr class="colorgraph">
				<div class="row">
					<div class="col-xs-6 col-sm-6 col-md-6">
                        <input type="submit" class="btn btn-lg btn-success btn-block" value="Sign In">
					</div>
					<div class="col-xs-6 col-sm-6 col-md-6">
						<a href="/newaccount" class="btn btn-lg btn-primary btn-block">Create Account</a>
					</div>
				</div>
				<a href="" class="btn btn-link pull-left">Forgot Password?</a>
		</form>
	</div>
</div>
</div>


</#assign>
<#include "main.ftl">
