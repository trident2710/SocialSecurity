/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var myStyle = [
    {
        "selector": ".Male",
        "style": {
        "background-color": "blue"
        }
    },
    {
        "selector": ".Female",
        "style": {
        "background-color": "purple"
        }
    },
    {
        "selector": ".target",
        "style": {
        "background-color": "green"
        }
    },
    {
        "selector": "node",
        "style": {
          "label": "data(label)", 
          "text-wrap": "wrap",
          "text-max-width": 180
        }
    },
    {
        "selector": ":selected",
        "style": {
          "background-color":"black"
        }
    }
];

var $cy;
var $profileId;
var $friendGraph;

function init(id){
    $profileId = id;
    getData('/rest/profiledata/friendgraph/'+id,(friendgraph)=>{

        console.log(friendgraph);
        $friendGraph = JSON.parse(friendgraph);

        $cy= cytoscape({
            container: $('#cy'),
            style: myStyle
        });

        $cy.json($friendGraph);
        $cy.on('click', 'node', function(evt){
            window.open("/profiledata/"+this.id(), '_blank');
            //window.location="/profiledata/"+this.id();
        });
        $cy.on('mouseup','node',function(evt){
            sendNodePosition($profileId, this.id());
        });
    });
}

function getData(path,callback){
    $.ajax({
        success: callback,
        error: callback,
        processData: false,
        type: 'GET',
        url: path
    });
}



function sendUpdateProfileFriendGraphRequest(profileDataId,data){
    $.ajax({
        contentType: 'application/json',
        data: JSON.stringify(data),
        dataType: 'json',
        processData: false,
        type: 'POST',
        url: '/rest/profiledata/friendgraph/'+profileDataId
    });
}

function sendNodePosition(profileDataId,nodeId){
    $cy.json().elements.nodes.forEach((node)=>{
        console.log(node.data.id);
        console.log("looking "+nodeId);
        if(JSON.stringify(node.data.id)===JSON.stringify(nodeId)){
            sendUpdateProfileFriendGraphRequest(profileDataId,{id:nodeId,position:node['position']});
        }
    });
   
}