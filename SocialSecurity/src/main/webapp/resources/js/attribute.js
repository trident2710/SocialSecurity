/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * delete the complex attribute entity
 * @param {type} id
 * @returns {undefined}
 */
function deleteEntity(id) {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        window.location.replace("attributes-all");
    }
    if (this.status == 400){
        window.location.replace("error");
    }
  };
  xhttp.open("DELETE", "attributes-specific?id="+id, true);
  xhttp.send();
}

var $x = 1; 


$(document).ready(function() {
    var max_fields      = 10; //maximum input boxes allowed
    var wrapper         = $(".input_fields_wrap"); //Fields wrapper
    var add_button      = $(".add_field_button"); //Add button ID
    
    $(add_button).click(function(e){ //on add input button click
        e.preventDefault();
        if($x < max_fields){ //max input box allowed
            $x++; //text box increment
            var $div  = $("<div class='removable_attr'></div>");
            var $sel = ($("#selectPrimitive").clone());
            $sel.attr("name","primitiveAttribut"+$x);
            $div.append($sel);
            $div.append($("<a href='#' class='remove_field'>Remove</a>"));
            wrapper.append($div);
        }
    });
    
    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
        e.preventDefault(); $(this).parent('div').remove(); $x--;
    })
});

