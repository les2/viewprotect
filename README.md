N.B., This was an archived project on Google Code that I did many years ago when I knew nothing!
I have revived it from the [archive](https://code.google.com/archive/p/viewprotect/) and moved it to github!

Acegi View Protect is an extensible JSP custom tag for declaratively hiding or showing content based on the roles a user has.
It is intended for use with the Spring framework and with the Acegi Security Framework. It is designed to be used in lieu of
or in combination with the standard Acegi Security AuthorizationTag (affectionately known as "authz"), depending on your needs.

Example:

1) You must configure a ViewAuthorizationService bean in your Spring application context. A simple implementation that provides
functionality similar to the standard authz tag is provided in the com.upthescala.viewprotect.basic package.

```
TODO
```

The above bean definitions define a ComponentAttributeSource and a ViewAuthorizationService. Notice that the ComponentAttributeSource definition accepts a property named "propertiesAttributeMappings" of type java.util.Properties . This allows you to externalize the user roles required to access a particular resource from the JSP (this is the main point of using a custom tag like viewprotect).

2) You now need define the "component attribute mappings" in the file referenced by the ComponentAttributeSource bean definition. Add a file named myApp-view-authz.properties to the classpath and put the following in it: users.deleteUserButton.ifAllGranted=ROLE_ADMIN users.editUserButton.ifAllGranted=ROLE_ADMIN users.user.emailAddress.ifAnyGranted=ROLE_USER,ROLE_ADMIN users.user.emailAddress.ifNotGranted=ROLE_UNVERIFIED homePage.signUp.ifNotGranted=ROLE_USER

After that's configured, you can use the view protect tag in a JSP as follows:

```
<%@ taglib uri="http://upthescala.com/tags/viewprotect" prefix="viewprotect" %>

...

<viewprotect:protect componentId="users.deleteUserButton">
  <button name="deleteUser">Delete</button>
</viewprotect:protect>

...

<viewprotect:protect componentId="users.editUserButton">
  <button name="editUser">Edit</button>
</viewprotect:protect>

...

<viewprotect:protect componentId="users.user.emailAddress" var="canSeeEmail"> ${user.email} </viewprotect:protect>
<c:if test="${not canSeeEmail}">
You must be logged in to see this user's email.
</c:if>

...
<viewprotect:protect componentId="homePage.signUp"> Please sign up for my application: <form ...> ... </form> </viewprotect:protect>
```

To get the most benefit from the viewprotect tag, it is advisable to choose a logical component id nomenclature for your application. If you work on a medium to large project where the requirements are defined by specialists and given to developers in the form of "wire frames" or a "requirements traceability matrix", the access requirements for "view components" can be integrated into the wire frames and requirements so that everyone is on the same page and using the same language.

Note: We are currently working on simplifying the required configuration to something like:

```
users.deleteUserButton.ifAllGranted=ROLE_ADMIN
users.editUserButton.ifAllGranted=ROLE_ADMIN
users.user.emailAddress.ifAnyGranted=ROLE_USER,ROLE_ADMIN
...
```

... with the help of the Spring XSD namespace based configuration.

See http://static.springsource.org/spring/docs/2.0.x/reference/extensible-xml.html for more details on how to do this. (TODO: add this part to a Wiki Page).

Note 2: If you are using Spring Security, this project is not for you.
If you happen to be coding a legacy app that won't be upgraded, then you might be in luck.
My initial assessment on Spring Security 3 is that it has a much more powerful authz tag!
