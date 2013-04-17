play-spring-security
====================

This intend to be a spring security and play framework 2.1 integration

you can download the project and start it with play run

as an example there are 5 method actions in the Application Controller

	/				-> NOT SECURED
	/list			-> NOT SECURED
	/home			-> SECURED WITH ROLE_USER
	/login			-> NOT SECURED (It will display a login form)
	/submit/login	-> (receive a post request to perform a user login)