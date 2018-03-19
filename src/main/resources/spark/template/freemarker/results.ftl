<#assign content>
<h1>Results</h1>
<p>${message}</p>
<#if 0 < stars?size >
<ul>
<#list stars as star>
   <li>${star.name} (${star.string}) is
    <b>${star.distance} light years away.</b>
    </li>
</#list>
</ul>
</#if>

<br />
<a href="/stars">Make Another Search!</a>

</#assign>
<#include "main.ftl">
