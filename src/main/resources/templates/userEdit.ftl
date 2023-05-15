<#import "parts/common.ftl" as c>

<@c.page>

    <h3 class="mt-5">User editor</h3>
    <form action="/user" method="post">
        <input  class="md-3" type="text" name="username" value="${user.username}" />
        <#list roles as role>
            <div>
                <label><input  class="md-3" type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")} />${role}</label>
            </div>
        </#list>
        <input type="hidden" value="${user.id}" name="userId" />
        <input type="hidden" value="${_csrf.token}" name="_csrf" />
        <button class="btn btn-primary" type="submit">Save</button>
    </form>
</@c.page>
