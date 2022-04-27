<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>

<@c.page>
    <div class="mb-1"> <h5>Add new user</h5></div>
    <#--${message!""}-->
    ${message?if_exists}
    <@l.login "/registration" true/>
</@c.page>
