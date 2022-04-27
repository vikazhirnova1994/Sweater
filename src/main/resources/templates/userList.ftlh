<#import "parts/common.ftlh" as c>
<@c.page>
    <div class="mb-3">
        <h5>List of User</h5>
    </div>
    <div class="table-hover">
        <table class="table">
            <thead class="thead-grey">
            <tr>
                <th>Name</th>
                <th>Role</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <#list users as user>
                <tr>
                    <td>${user.username}</td>
                    <td><#list user.roles as role>${role}<#sep>, </#list></td>
                    <td><a href="/user/${user.id}">edit</a></td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</@c.page>
