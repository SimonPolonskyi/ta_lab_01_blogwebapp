<#import "parts/common.ftl" as c>


<@c.page>
    <#if SPRING_SECURITY_LAST_EXCEPTION??>
        <div class="col-sm-6">
            <div class="alert alert-danger" role="alert">
                ${SPRING_SECURITY_LAST_EXCEPTION.message}
            </div>
        </div>
    </#if>
    <#if message??>
        <div class="alert alert-${messageType}" role="alert">
            ${message}
        </div>
    </#if>

</@c.page>
