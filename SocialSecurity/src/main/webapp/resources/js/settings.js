/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function resetDefaultAttributeDefinitions(url){
    if(confirm("This action will restore default attribute definitions and destroy existing. This will also affect harmtrees. Are you sure?")){
        sendRequest(url,'DELETE');
        alert("default attribute definitions will be restored");
    }
}

function repairDefaultAttributeDefinitions(url){
    if(confirm("This action will repair default attribute definitions (check the presence and correctness of synonims).")){
        sendRequest(url,'GET');
        alert("default attribute definitions will be repaired");
    }
}

function resetDefaultHarmTrees(url){
    if(confirm("This action will restore default harm trees and destroy existing. Are you sure?")){
        sendRequest(url,'DELETE');
        alert("default harm trees will be restored");
    }
}

function deleteData(url){
    if(confirm("This action will destroy EVERYTHING in database and restore default parameters. Are you sure?")){
        sendRequest(url,'DELETE');
        alert("delete all data will be performed");
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