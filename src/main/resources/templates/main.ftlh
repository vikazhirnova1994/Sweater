<#import "parts/common.ftlh" as c>
<!--#import "parts/login.ftlh" as l>-->

<@c.page>
    <!-- @l.logout />--> <!--span><a href="/user">User List</a> </span>-->
    <!-- Форма поиска-->
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <input type="text" name="filter" class="form-control" value="${filter?ifExists}"
                       placeholder="Search by tag">
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>
    <#include "parts/messageEdit.ftlh"/>
    <#include "parts/messageList.ftlh"/>
</@c.page>


<!-- Форма поиска c PostMapping-->
<!--
      <form method="post" action="filter">
        <input type="text" name="filter">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Найти</button>
    </form>
    -->


