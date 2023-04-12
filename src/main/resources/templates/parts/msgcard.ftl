    <div class="card-columns mb=5">
        <#list messages as message>
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
                        <div class="col-6"><i>${message.tag}</i></div>
                        <div class="col-2"> <a href="/message/${message.messageId}">Commnets: ${message.comments?size}</a></div>
                    </div>
                </div>
            </div>
        <#else>
            No message
        </#list>
    </div>
