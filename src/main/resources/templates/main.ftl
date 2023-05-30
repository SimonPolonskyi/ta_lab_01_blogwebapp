<#import "parts/common.ftl" as c>

<@c.page>
    <div class="mb-5">
        <form method="get" action="/main" >
            <div class="row">
                <div class="col-md-11">
                    <input type="text" name="filter" class="form-control" value="${filter!}" placeholder="Search by tag" />
                </div>
                <div class="col-md-1">
                   <button type="submit" class="btn btn-primary ms-2">Search</button>
                </div>
            </div>
        </form>
    </div>
    <a class="btn btn-primary" data-bs-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
        Add new Message
    </a>


    <div class="collapse <#if message??>show</#if>" id="collapseExample">
        <div class="mt-3">
            <form method="post" enctype="multipart/form-data">
                <div class="mt-2">
                    <textarea type="textarea" class="form-control ${(textError??)?string('is-invalid', '')}"
                           value="<#if message??>${message.text}</#if>" name="text" placeholder="Type a message" ></textarea>
                    <#if textError??>
                        <div class="invalid-feedback">
                            ${textError}
                        </div>
                    </#if>
                </div>
                <div class="mt-2">
                    <input type="text" class="form-control"
                           value="<#if message??>${message.tag}</#if>" name="tag" placeholder="Tag" />
                    <#if tagError??>
                        <div class="invalid-feedback">
                            ${tagError}
                        </div>
                    </#if>
                </div>
                <div class="mt-2">
                    <div class="input-group">
                        <input type="file"  name="file" class="form-control" id="inputGroupFile04" aria-describedby="inputGroupFileAddon04" aria-label="Upload" />
                    </div>
                </div>

                <div class="mt-2">
                    <button type="submit" class="btn btn-primary">Add</button>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}" />
            </form>
        </div>
    </div>

    <#include "parts/msgcard.ftl">


</@c.page>
