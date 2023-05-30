<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>


<@c.page>
    <div class="card-columns">
        <#if  message??>
            <div class="card my-3">
                <#if message.filename??>
                    <img src="/img/${message.filename}" class="card-img-top">
                </#if>
                <div class="m-2">
                    <span>${message.text}</span>
                </div>
                <div class="card-footer text-muted">
                    <div class="row align-items-start gx-5">
                        <div class="col-3"><span>Author:  ${message.authorName}</span></div>
                        <div class="col-5"><i>${message.tag}</i></div>
                        <div class="col-2">Commnets: ${message.comments?size}</div>
                    </div>
                </div>
            </div>
        <#else>
            No message
        </#if>
    </div>


<#-- Comments Section -->
    <div class="container">
        <div class="row">
            <div class="col">
                <#if comments??>
                    <h3 class="mt-5">Comments:</h3>
                    <#list comments as comment>
                        <div class="card mt-3">
                            <div  class="card-header">
                                 Comment from ${comment.author.getUsername()}
                            </div>
                            <div class="card-body">
                                <p class="card-text">${comment.text}</p>
                            </div>
                        </div>
                    </#list>
                </#if>
            </div>
        </div>
    </div>

<#-- Add Comment Section (for authorized users only) -->
    <#if user??>
        <div class="container">
            <div class="row">
                <div class="col">
                    <h3 class="mt-5">Add a Comment</h3>
                    <form  method="post" action="/message/${message.getMessageId()}" >
                        <div class="mb-3">
                            <textarea  name="text"   class="form-control  ${(textError??)?string('is-invalid', '')}" id="commentText" rows="3" value="<#if comment??>${comment.text}</#if>"></textarea>
                            <#if textError??>
                                <div class="invalid-feedback">
                                    ${textError}
                                </div>
                            </#if>
                        </div>
                        <input type="hidden" name="_csrf" value="${_csrf.token}" />
                        <button type="submit" class="btn btn-primary">Submit</button>
                    </form>
                </div>
            </div>
        </div>
    </#if>

</@c.page>