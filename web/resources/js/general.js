/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



function setTopicToFaq(xhr, status, args){
    document.getElementById("formFaqAbc:topicid").value=args.topic;
    document.getElementById("formFaqAbc:topicText").value=args.topicname;
}

function setFaqListEvent(xhr, status, args) {
    setUrlInMain("pages/faqlistform.xhtml", {topic: args.topic});
}

function setFaqDetail(faqid) {
    setUrlInMain("pages/faqviewform.xhtml",{faq: faqid});
}

function setSearchResult() {
    var str = document.getElementById("formfindtopic:textkey").value;
    setUrlInMain("pages/faqlistform.xhtml",{textkey: str});
}

function setNewFaq(){
    setUrlInMain("pages/faqabmform.xhtml",null);
}


function setUrlInMain(url, data) {
    $("#mainpanel").html("<center><img src='img/loading.gif' alt='cargando...'/></center>");
    //para evitar el cache de los navecadores
    var random = Math.random() * 100;
    $.ajax({
        url: url + "?" + random,
        data: data,
        dataType: "html"
    }).done(function(data) {
        insertHtml("mainpanel",data);
    });
}

function insertHtml(id, html)  
{ 
   $("#"+id).html(html);
//   var element = document.getElementById(id);  
//   element.innerHTML = html;  
//   var codes = element.getElementsByTagName("script");   
//   for(var i=0;i<codes.length;i++)  
//   {    
//       eval(codes[i].text);  
//   }  
}  

