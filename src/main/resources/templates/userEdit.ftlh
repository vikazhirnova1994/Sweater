<#import "parts/common.ftlh" as c>

<@c.page>
    <div class="mb-3">
        <h5>User editor</h5>
    </div>
    <div class="form-group">
        <form action="/user" method="post">
            <div class="form-group">
                <input type="text" name="username" value="${user.username}" class="form-control">
            </div>
            <#list roles as role>
                <div class="form-group mt-2">
                    <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}</label>
                </div>
            </#list>
            <input type="hidden" value="${user.id}" name="userId">
            <input type="hidden" value="${_csrf.token}" name="_csrf">
            <button type="submit" class="btn btn-primary">Save</button>
        </form>
    </div>
</@c.page>
