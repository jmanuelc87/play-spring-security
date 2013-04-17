package org.oaksoft.app.controllers;


import org.oaksoft.security.web.access.Secured;
import org.oaksoft.security.web.authentication.Login;
import org.oaksoft.security.web.authentication.logout.Logout;
import play.mvc.Controller;
import play.mvc.Result;

@org.springframework.stereotype.Controller
public class Application extends Controller
{

    public Result index()
    {
        return ok("Hola Mundo!");
    }

    public Result login()
    {
        return ok(org.oaksoft.app.views.html.login.index.render());
    }

    @Logout
    public Result logout()
    {
        return ok();
    }

    @Login(successRedirect = "/home", failureRedirect = "/")
    public Result loginSubmit()
    {
        return ok("Login to Application");
    }

    @Secured(expression = "ROLE_USER")
    public Result home()
    {
        return ok("Home Application");
    }

    public Result list()
    {
        return ok("Hola List!");
    }
}
