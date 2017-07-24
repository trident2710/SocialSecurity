/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function resetDefaultAttributeDefinitions(url){
    if(confirm("This action will restore default attribute definitions and destroy existing. This will also affect harmtrees. Are you sure?")){
        sendRequest(url,'DELETE');
        alert("default attribute definitions restored");
    }
}

function sendRequest(url,type,success,error){
    $.ajax({
        success: success,
        error: error,
        type: type,
        url: url
    });
}