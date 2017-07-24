/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var myStyle = [
    {
        "selector": ".FacebookProfile",
        "style": {
        "background-color": "blue"
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
            
        });
        $cy.on('mouseup','node',(evt)=>{
            sendUpdateProfileFriendGraphRequest($profileId);
        })
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

function sendUpdateProfileFriendGraphRequest(profileDataId){
    $.ajax({
        contentType: 'application/json',
        data: JSON.stringify($cy.json()),
        dataType: 'json',
        success: function(){
        },
        error: function(){
        },
        processData: false,
        type: 'POST',
        url: '/profiledata/friendgraph/'+profileDataId
    });
}