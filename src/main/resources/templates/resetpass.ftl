<#import "parts/common.ftl" as c>


<@c.page>
    <#if isRequestPosted>
        <div class="alert alert-success text-center" role="alert">
            A new password has been sent to the mailbox specified in the profile
        </div>
    <#else>
        <div class="container-fluid">
            <div class="row justify-content-center">
                <div class="col-lg-4 col-md-6 col-sm-8">
                    <div class="card my-5">
                        <div class="card-header bg-primary text-white">
                            <h4 class="mb-0">Reset password</h4>
                        </div>
                        <div class="card-body">
                            <form action="/resetpass" method="post">
                                <div class="mb-3">
                                    <label for="username" class="form-label">User Name</label>
                                    <input type="text" class="form-control ${(usernameError??)?string('is-invalid', '')}" id="username" name="username"  value="<#if user??>${user.username}</#if>" aria-describedby="usernameHelp" required />
                                    <div id="usernameHelp" class="form-text">A new password will be sent to your mailbox</div>
                                    <#if usernameError??>
                                        <div class="invalid-feedback">
                                            ${usernameError}
                                        </div>
                                    </#if>
                                </div>
                                <button type="submit" class="btn btn-primary btn-block">Send</button>
                                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </#if>



</@c.page>