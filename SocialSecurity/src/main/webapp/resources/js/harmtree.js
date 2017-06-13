/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * delete the harmtree entity
 * @param {type} id
 * @returns {undefined}
 */
function deleteEntity(id) {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        window.location.replace("harmtrees-all");
    }
    if (this.status == 400){
        window.location.replace("error");
    }
  };
  xhttp.open("DELETE", "harmtrees-specific?id="+id, true);
  xhttp.send();
}
