<#assign content>

<div class="container">

<div class="row">
    <div class="form-group col-xs-8 col-sm-8 col-md-8r">
		<form role="form" method="POST" action="/newaccountresults">
			<h1>Create an Account</h2>
			<hr class="colorgraph">
<!-- 			<div class="row">
				<div class="col-xs-12 col-sm-6 col-md-6">
					<div class="form-group">
                        <input type="text" name="first_name" id="first_name" class="form-control input-lg" placeholder="First Name" tabindex="1">
					</div>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-6">
					<div class="form-group">
						<input type="text" name="last_name" id="last_name" class="form-control input-lg" placeholder="Last Name" tabindex="2">
					</div>
				</div>
			</div> -->
			<h1><small>${message}</small></h1>
			<div class="form-group">
				<input type="text" name="username" placeholder="username" class="form-control input-lg" tabindex="3">
			</div>
			<!-- <div class="form-group">
				<input type="email" name="email" id="email" class="form-control input-lg" placeholder="Email Address" tabindex="4">
			</div> -->
			<div class="row">
				<div class="col-xs-12 col-sm-6 col-md-6">
					<div class="form-group">
						<input type="password" name="password" id="password" class="form-control input-lg" placeholder="password" tabindex="5">
					</div>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-6">
					<div class="form-group">
						<input type="password" name="password2" placeholder="confirm password" class="form-control input-lg" tabindex="6">
					</div>
				</div>
			</div>
			
			<hr class="colorgraph">
			<div class="row">
				<div class="col-xs-12 col-md-6"><input id="submit" type="submit" value="Register" class="btn btn-primary btn-block btn-lg" tabindex="7"></div>
				<div class="col-xs-12 col-md-6"><a href="/login" class="btn btn-success btn-block btn-lg">Already have an account?</a></div>
			</div>
			
		</form>
	</div>
</div>


</#assign>
<#include "main.ftl">
