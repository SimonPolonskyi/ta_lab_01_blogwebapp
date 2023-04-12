<#import "parts/common.ftl" as c>

<@c.page>
    <h5>${user.username!}</h5>


    <form method="post">
        <div class="form-group row mb-2">
            <label class="col-sm-2 col-form-label">First Name:</label>
            <div class="col-sm-6">
                <input type="text"  id="firstName" name="firstName" class="form-control" placeholder="First Name" value="${user.firstName!''}" />
            </div>
        </div>

        <div class="form-group row mb-2">
            <label class="col-sm-2 col-form-label">Last Name:</label>
            <div class="col-sm-6">
                <input type="text"  id="lastName"  name="lastName" class="form-control" placeholder="Last Name" value="${user.lastName!''}" />
            </div>
        </div>

        <div class="form-group row mb-1">
            <label class="col-sm-2 col-form-label">Password:</label>
            <div class="col-sm-6">
                <input type="password"  id="password"  name="password" class="form-control" placeholder="Password" />

            </div>
        </div>
        <div class="form-group row mb-2">
            <label class="col-sm-2 col-form-label">Password:</label>
            <div class="col-sm-6">
                <input type="password" id="password2"  name="password2" class="form-control ${(password2Error??)?string('is-invalid', '')}"
                       placeholder="Confirm password" />
                <#if password2Error??>
                    <div class="invalid-feedback">
                        ${password2Error}
                    </div>
                </#if>
            </div>
        </div>

        <div class="form-group row mb-2">
            <label class="col-sm-2 col-form-label">Email:</label>
            <div class="col-sm-6">
                <input type="email" id="email"  name="email" value="<#if user??>${user.email}<#else>${email!''}</#if>"
                       class="form-control ${(emailError??)?string('is-invalid', '')}"
                       placeholder="some@some.com" />
                <#if emailError??>
                    <div class="invalid-feedback">
                        ${emailError}
                    </div>
                </#if>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Save</button>
    </form>
</@c.page>
