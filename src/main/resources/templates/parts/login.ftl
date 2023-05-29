<#include "security.ftl">
<#macro login path isRegisterForm>
    <div class="container-fluid">
        <div class="row justify-content-center">
            <div class="col-lg-4 col-md-6 col-sm-8">
                <div class="card my-5">
                    <div class="card-header bg-primary text-white">
                        <#if isRegisterForm>
                            <h4 class="mb-0">Add new user</h4>
                        <#else>
                            <h4 class="mb-0">Login</h4>
                        </#if>
                    </div>
                    <div class="card-body">
                        <form action="${path}" method="post">
                            <div class="mb-3">
                                <label for="username" class="form-label">User Name</label>
                                <input type="text" class="form-control ${(usernameError??)?string('is-invalid', '')}" id="username" name="username"  value="<#if user??>${user.username}</#if>" required />
                                <#if usernameError??>
                                    <div class="invalid-feedback">
                                        ${usernameError}
                                    </div>
                                </#if>
                            </div>
                            <#if isRegisterForm>
                                <div class="mb-3">
                                    <label for="firstName" class="form-label">First Name</label>
                                    <input type="text" id="firstName"  name="firstName" class="form-control"  value="<#if user??>${user.firstName}</#if>" />
                                </div>
                                <div class="mb-3">
                                    <label for="lastName" class="form-label">Last Name</label>
                                    <input type="text" id="lastName"  name="lastName" class="form-control"  value="<#if user??>${user.lastName}</#if>" />
                                </div>
                            </#if>
                            <div class="mb-3">
                                <label for="password" class="form-label">Password</label>
                                <input type="password"  id="password" name="password" class="form-control ${(passwordError??)?string('is-invalid', '')}" />
                                <#if passwordError??>
                                    <div class="invalid-feedback">
                                        ${passwordError}
                                    </div>
                                </#if>
                            </div>
                            <#if isRegisterForm>
                                <div class="mb-2">
                                    <label for="password2" class="form-label">Confirm password</label>
                                    <input type="password"  id="password2" name="password2" class="form-control ${(password2Error??)?string('is-invalid', '')}" />
                                    <#if password2Error??>
                                        <div class="invalid-feedback">
                                            ${password2Error}
                                        </div>
                                    </#if>
                                </div>
                                <div class="mb-3">
                                    <label for="password" class="form-label">Email</label>
                                    <input type="email"  id="email"  name="email" value="<#if user??>${user.email}</#if>"
                                           class="form-control ${(emailError??)?string('is-invalid', '')}" />
                                </div>
                            </#if>
                            <input type="hidden" name="_csrf" value="${_csrf.token}" />
                            <button class="btn btn-primary" type="submit"><#if isRegisterForm>Create<#else>Sign In</#if></button>
                        </form>
                    </div>
                    <div class="card-footer text-center">
                        <a href="/resetpass">Forgot Password?</a>
                    </div>
                </div>
                <#if !isRegisterForm>
                    <div class="text-center">
                        <p>Don't have an account?</p>
                        <a href="/registration">Register</a>
                    </div>
                </#if>


            </div>
        </div>
    </div>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <#if _csrf??>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
        </#if>
        <button class="btn btn-primary" type="submit"><#if user??>Sign Out<#else>Sign In</#if></button>
    </form>
</#macro>
