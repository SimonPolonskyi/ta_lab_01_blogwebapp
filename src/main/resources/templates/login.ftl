<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
    <#if SPRING_SECURITY_LAST_EXCEPTION??>
        <div class="container-fluid">
            <div class="row justify-content-center">
                <div class="col-lg-4 col-md-6 col-sm-8">
                    <div class="alert alert-danger text-center" role="alert">
                        ${SPRING_SECURITY_LAST_EXCEPTION.message}
                    </div>
                </div>
            </div>
        </div>
    </#if>
    <#if message??>
        <div class="container-fluid">
            <div class="row justify-content-center">
                <div class="col-lg-4 col-md-6 col-sm-8">
                    <div class="alert alert-${messageType}" role="alert">
                        ${message}
                    </div>
                </div>
            </div>
        </div>
    </#if>
    <@l.login "/login" false/>
</@c.page>
