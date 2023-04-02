<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
    <#if SPRING_SECURITY_LAST_EXCEPTION??>
        <div class="col-sm-6">
            <div class="alert alert-danger" role="alert">
                ${SPRING_SECURITY_LAST_EXCEPTION.message}
            </div>
        </div>
    </#if>
    <@l.login "/login" false/>
</@c.page>
