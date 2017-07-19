/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var myStyle = [
    {
        "selector": ".HarmTreeVertex",
        "style": {
        "background-color": "red"
        }
    },
    {
        "selector": ".HarmTreeLogicalNode",
        "style": {
        "background-color": "green"
        }
    },
    {
        "selector": ".HarmTreeLeaf",
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
var $harmTreeId;

function init(id){
    $harmTreeId = id;
    getData('/rest/harmtrees/cytoscape/'+id,(harmTree)=>{
        getData('/rest/attributes/all',(attributes)=>{
            getData('/rest/harmtrees/leaf/settings',(settings)=>{
                
                harmTree = JSON.parse(harmTree);
                $harmTree = harmTree;
                settings = JSON.parse(settings);
                
                let harmTreeLeafCreateData = {};
                harmTreeLeafCreateData['riskSource'] = settings['riskSources'];
                harmTreeLeafCreateData['threatType'] = settings['threatTypes'];
                harmTreeLeafCreateData['attributeDefinition'] = attributes.reduce((res,elem)=>{
                    let el = {};
                    el['value'] = elem['id'];
                    el['name'] = elem['displayName'];
                    res.push(el);
                    return res;
                },[]);
                
                $cy= cytoscape({
                    container: $('#cy'),
                    style: myStyle
                });
                
                const addController = new NodeCreateController((value)=>{
                    processChangeRequest(value);
                }, harmTreeLeafCreateData,[{'name':'and','value':-1},{'name':'or','value':1}]);
                
                let data = {};
                data['harmTreeLeafData'] = harmTreeLeafCreateData;
                data['harmTreeLogicData'] = [{'name':'and','value':-1},{'name':'or','value':1}];
                const updateController = new NodeUpdateController((value)=>{
                    processChangeRequest(value);
                },data);
                
                $cy.json(harmTree);
                $cy.on('click', 'node', function(evt){
                    addController.setNodeId(this.id());
                    if(getNodeData(this.id())['classes'].split(" ")[0]=='HarmTreeVertex'){
                        updateController.init(this.id());
                        addController.setState('parentClass','HarmTreeVertex');
                        addController.setState('stage','CLICKED');    
                    }
                    if(getNodeData(this.id())['classes'].split(" ")[0]=='HarmTreeLeaf'){
                        updateController.init(this.id());
                        addController.clear();
                    }
                    if(getNodeData(this.id())['classes'].split(" ")[0]=='HarmTreeLogicalNode'){
                        updateController.init(this.id());
                        addController.setState('parentClass','HarmTreeLogicalNode');
                        addController.setState('stage','CLICKED');
                    }
                });
                $cy.on('mouseup','node',(evt)=>{
                    sendUpdateHarmTreeRequest($harmTreeId);
                })
            });
        });
    });
}

function processChangeRequest(data){
    //window.alert(JSON.stringify(data));
    const callback = ()=>{
        init($harmTreeId);
    };
    switch(data['action']){
        case 'create':
            sendData('/rest/harmtrees/descendant/'+data['nodeId'],data,'POST',callback);
            break;
        case 'update':
            sendData('/rest/harmtrees/node/'+data['nodeId'],data,'PUT',callback);
            break;
        case 'delete':
            deleteData('/rest/harmtrees/node/'+data['nodeId'],callback)
            break;
    }
}

function getNodeData(nodeId,responseCallback){
    let data = $cy.json()['elements']['nodes'];
    for(i=0;i<data.length;i++){
        if(data[i]['data']['id'] == nodeId){
            return data[i];
        }
    }
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
function deleteData(path,callback){
    $.ajax({
        done:callback,
        success: callback,
        error: callback,
        processData: false,
        type: 'DELETE',
        url: path
    });
}

function sendData(path,data,type,callback){
    $.ajax({
        contentType: 'application/json',
        dataType: 'json',
        done: callback,
        error: callback,
        data:JSON.stringify(data),
        processData: false,
        type: type,
        url: path
    });
}

function sendUpdateHarmTreeRequest(id){
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
        url: '/rest/harmtrees/cytoscape/'+id
    });
}


function insertText(el,name){
    let cont = document.createElement('div');
    cont.appendChild(document.createElement('br'));
    let elem = document.createElement('label');
    elem.innerHTML=name;
    cont.appendChild(elem);
    el.appendChild(cont);
    return elem;
}

/**
 * @see HarmTreeElement
 * Creates the select to choose the harm tree type 
 * and calls @see onChange on select value chosen
 */
class NodeTypeInput {
  /**
   * @param {Array} options in format [{"name":"n","value":"v"}]
   * @param {Function} onChange
   */ 
  constructor(options, onChange) {
    this.options = options;
    this.onChange = onChange;
  }
  
  /**
   * return the chosen harm tree leaf type
   * @returns {Element|NodeTypeInput.render.container}
   */
  render() {
    let container = document.createElement('select');
    container.setAttribute('class','form-control');
    container.innerHTML = this.options.reduce((result, elem) => {
      result += 
        `<option value="${elem['value']}">${elem['name']}</option>`;
      return result;
    }, '');
   
    container.addEventListener('change', () => {
      this.onChange(container.value);
    });
    this.onChange(container.value);
    return container;
  }
}

function removeElementById(id){
    var elem = document.getElementById(id);
    if(elem!=null)
        elem.parentNode.removeChild(elem);
}

/**
 * @see HarmTreeLogicElement
 * for rendering the view which contains the elements needed to create the logical node
 * @type type
 */
class LogicNodeInput{
    /**
    * @param {Array} options
    * @param {Function} onChange
    */ 
    constructor(options,onChange,defaultValue) {
    this.options = options;
    this.onChange = onChange;
    this.state='default';
    this.defaultValue = defaultValue;
    }

    setState(state){
      this.state = state;
      this.render();
    }

    render() {
    if(document.getElementById('logic_input_type')==null){
        let button = document.createElement('button');
        button.setAttribute('id','logic_input_type');
        button.setAttribute('class','btn btn-default')
        button.innerHTML = 'Switch input mode';
        button.addEventListener('click',()=>{
            if(this.state=='custom')
                this.setState('default');
            else this.setState('custom');
        });

        let create = document.createElement('button');
        create.setAttribute('id','create');
        create.setAttribute('class','btn btn-primary')
        create.innerHTML = this.defaultValue!=null?'Update':'Create';
        create.addEventListener('click',()=>{
            if(this.state=='custom'){
                this.onChange(document.getElementById("logic_input").value);
            } else{
                this.onChange(document.getElementById("logic_select").value);
            }
        });

        let container = document.createElement('div');
        container.setAttribute('id','logic_container');
        insertText(container,"Logical requirement:");
        container.appendChild(button);
        container.appendChild(create);
        document.getElementById('menu').appendChild(container);
    }
    
    if(this.state=='custom'){
        removeElementById('logic_select');
        removeElementById('logic_input');

        let input  = document.createElement('input');
        input.setAttribute('type','number');
        input.setAttribute('min',1);
        input.setAttribute('max',10);
        input.setAttribute('id','logic_input');
        input.setAttribute('value','1');
        input.setAttribute('class','form-control logic');
        input.onkeypress=function(evt){
            evt.preventDefault();
        };
        if(this.defaultValue!=null){
            input.value = this.defaultValue['value'];
        }
        document.getElementById('logic_container').appendChild(input);
        
    }
    
    if(this.state=='default'){
        removeElementById('logic_select');
        removeElementById('logic_input');

        let select = document.createElement('select');
        select.setAttribute('class','form-control');
        select.setAttribute('id','logic_select');
        select.setAttribute('class','form-control logic');
        select.innerHTML = this.options.reduce((result, elem) => {
          result += 
            `<option value="${elem['value']}">${elem['name']}</option>`;
          return result;
        }, '');
        if(this.defaultValue!=null){
            select.value = this.defaultValue['value'];
        }
        document.getElementById('logic_container').appendChild(select);
    }


    let container  = document.getElementById('logic_container');
    return container;
    }
}
/**
 * @see HarmTreeLeaf
 * for rendering the view which contains the elements needed to create the leaf node
 * @type type
 */
class LeafNodeInput {
    /**
    * @param {Array} options in format: 
    * ["threatType":[1,2,3],"riskSource":[1,2],"attributeDefinition":[{"name":"n","value":"v"}]];
    * @param {Function} onChange
    */ 
    constructor(options, onChange,defaultValue) {
        this.options = options;
        this.onChange = onChange;
        this.defaultValue = defaultValue;
    
    }

    render() {
        let container = document.createElement('div');
        container.setAttribute('id','logic_container');
        let containerThreatType = document.createElement('select');
        containerThreatType.setAttribute('class','form-control');
        containerThreatType.innerHTML = this.options['threatType'].reduce((result, elem) => {
          result += 
            `<option value="${elem}">${elem}</option>`;
          return result;
        }, '');
        
        if(this.defaultValue!=null){
            containerThreatType.value = this.defaultValue['threatType'];
        }
        
        let containerRiskSource = document.createElement('select');
        containerRiskSource.setAttribute('class','form-control');
        containerRiskSource.innerHTML = this.options['riskSource'].reduce((result, elem) => {
          result += 
            `<option value="${elem}">${elem}</option>`;
          return result;
        }, '');
        if(this.defaultValue!=null){
            containerRiskSource.value = this.defaultValue['riskSource'];
        }
        
        let containerAttributeDefinition = document.createElement('select');
        containerAttributeDefinition.setAttribute('class','form-control');
        containerAttributeDefinition.innerHTML = this.options['attributeDefinition'].reduce((result, elem) => {
          result += 
            `<option value="${elem['value']}">${elem['name']}</option>`;
          return result;
        }, '');
        if(this.defaultValue!=null){
            containerAttributeDefinition.value = this.defaultValue['attributeDefinition'];
        }

        let buttonCreate = document.createElement('button');
        buttonCreate.setAttribute('id',"b_create");
        buttonCreate.setAttribute('class','btn btn-primary')
        buttonCreate.innerHTML = this.defaultValue!=null?'Update':'Create';
        buttonCreate.addEventListener('click',()=>{
           let values = {};
           values['threatType']=containerThreatType.value;
           values['riskSource']=containerRiskSource.value;
           values['attributeDefinition']= containerAttributeDefinition.value;
           this.onChange(values);
        });
        
        insertText(container,"Threat type:");
        container.appendChild(containerThreatType);
        insertText(container,"Risk source:");
        container.appendChild(containerRiskSource);
        insertText(container,"Attribute definition:");
        container.appendChild(containerAttributeDefinition);
        container.appendChild(buttonCreate);

        return container;
    }
}

class HarmTreeHeadInput{
    constructor(onChange,defaultData){
        this.onChange = onChange;
        this.defaultData = defaultData;
    }
    
    render(){
        let container = document.createElement('div');
        container.setAttribute('id','harm_tree_head_info');
        let name = document.createElement('input');
        name.setAttribute('id','harm_tree_name');
        name.setAttribute('class','form-control');
        
        if(this.defaultData!=null){
            name.setAttribute('value',this.defaultData['name']);
        }
            
        
        let description = document.createElement('input');
        description.setAttribute('id','harm_tree_description');
        description.setAttribute('class','form-control');
        
        if(this.defaultData!=null){
            description.setAttribute('value',this.defaultData['description']);
        }
        
        let update = document.createElement('button');
        update.innerHTML = 'Update';
        update.setAttribute('id','b_update');
        update.setAttribute('class','btn btn-primary')
        update.addEventListener('click',()=>{
            let res = {};
            res['name'] = name.value;
            res['description'] = description.value;
            this.onChange(res);
        });
        
        insertText(container,"Tree name:")
        container.appendChild(name);
        insertText(container,"Tree description:");
        container.appendChild(description);
        container.appendChild(update);
        
        return container;
    }
}

/**
 * For managing all elements needed to create the node
 */
class NodeCreateController {
    
    /**
     * 
     * @param {Fuction} onSubmit
     */
    constructor(onSubmit,harmTreeLeafCreateData,harmTreeLogicCreateData) {
        this.stageTypes = ['INACTIVE','CLICKED','SELECT_NODE_TYPE','ADD_LEAF_CHOSEN','ADD_LOGIC_CHOSEN'];
        this.state = {'stage':'INACTIVE','parentClass':''};
        this.onSubmit = onSubmit;
        this.harmTreeLeafCreateData = harmTreeLeafCreateData;
        this.harmTreeElementTypesVertex = [{'name':'Harm tree logical node','value':2}];
        this.harmTreeElementTypes = [{'name':'Harm tree leaf','value':1}, {'name':'Harm tree logical node','value':2}];
        this.harmTreeLogicCreateData = harmTreeLogicCreateData;
    }

    setNodeId(nodeId){
        this.nodeId = nodeId;
    }
    
    getNodeId(){
        return this.nodeId;
    }
    /**
     * 
     * @param prop 'stage' to change the stage
     * @param {String} value one of @see this.stageTypes
     */
    setState(prop, value) {
        this.state[prop] = value;
        this.render();
    }

    getCurrentStage(){
        return this.state['stage'];
    }
    
    createForm(){
        const formContainer = document.createElement('div');
        
        formContainer.setAttribute('id','create_container');
        formContainer.setAttribute('class','panel panel-default');
        formContainer.innerHTML = '<p class=s_title>Create options</p>';;
        document.getElementById('menu').appendChild(formContainer);
    }
    clear(){
        removeElementById('create_container');
    }

    render() {
        if(this.state['stage']=='INACTIVE'){
            removeElementById('create_container');
        }
        if(this.state['stage']=='CLICKED'){
            removeElementById('create_container');
            this.createForm();
            
            const formContainer = document.getElementById('create_container');
           
            const addChildButton = document.createElement('button');
            addChildButton.setAttribute('id','b_add');
            addChildButton.setAttribute('class','btn btn-success')
            addChildButton.innerHTML = 'Add child';
            addChildButton.setAttribute('id','node_add_child');
            addChildButton.addEventListener('click',()=>{
                this.setState('stage','SELECT_NODE_TYPE');
            });

            formContainer.appendChild(addChildButton);
    
        }
        if(this.state['stage']=='SELECT_NODE_TYPE'){
           
            const changeCallback = (selectedValue) => {
               if(selectedValue==1){
                   this.setState('stage','ADD_LEAF_CHOSEN');
               } else{
                   this.setState('stage','ADD_LOGIC_CHOSEN');
               }
            }
            const elem = new NodeTypeInput(this.state['parentClass']=='HarmTreeVertex'?this.harmTreeElementTypesVertex:this.harmTreeElementTypes, changeCallback);
            document.getElementById('create_container').appendChild(elem.render());


            
        }
        if(this.state['stage']=='ADD_LEAF_CHOSEN'){
            
            removeElementById('node_add_child');
            removeElementById('logic_container');
            
            const callback = (result)=>{
                let res = {"action":"create","type":1,"data":{}};
                res['data'] = result;
                res['nodeId'] = this.getNodeId();
                this.onSubmit(res);
                this.setState('CLICKED');
            }
            
            const leafInput = new LeafNodeInput(this.harmTreeLeafCreateData,callback,null);
            document.getElementById('create_container').appendChild(leafInput.render());
        }
        if(this.state['stage']=='ADD_LOGIC_CHOSEN'){
            
            removeElementById('node_add_child');
            removeElementById('logic_container');
            
            const callback = (result)=>{
                let res = {"action":"create","type":2,"data":{}};
                res['nodeId'] = this.getNodeId();
                res['data']['value'] = result;
                this.onSubmit(res);
                this.setState('CLICKED');
            };
            
            const logicInput = new LogicNodeInput(this.harmTreeLogicCreateData,callback,null);
            logicInput.setState('default');
            document.getElementById('create_container').appendChild(logicInput.render());
        }
    }
}

class NodeUpdateController{
    
    constructor(onSubmit,data){
        this.onSubmit = onSubmit;
        this.data = data;
        this.harmTreeLeafCreateData = data['harmTreeLeafData'];
        this.harmTreeElementTypes = [{'name':'Harm tree leaf','value':1}, {'name':'Harm tree logical node','value':2}];
        this.harmTreeLogicCreateData = data['harmTreeLogicData'];
    }
    
    init(id){
        this.nodeId = id;
        removeElementById('update_container');
        this.createContainer();
        
        
        
        
        getData('/rest/harmtrees/node/'+id,(node)=>{
            if(node['class']=='HarmTreeVertex'){ 
                const updateHead = new HarmTreeHeadInput((object)=>{
                    let res = {};
                    res['nodeId'] = this.getCurrentNodeId();
                    res['action'] = 'update';
                    res['class'] = node['class'];
                    res['data'] = object;
                    this.onSubmit(res);
                },node);
                document.getElementById('update_container').appendChild(updateHead.render());    
            } else{
                const deleteButton = document.createElement('button');
                deleteButton.innerHTML = 'Delete node';
                deleteButton.setAttribute('class','btn btn-danger')
                deleteButton.setAttribute('id','node_delete');
                deleteButton.addEventListener('click',()=>{
                    let res = {"action":"delete"};
                    res['nodeId'] = this.getCurrentNodeId();
                    this.onSubmit(res);
                });
                document.getElementById('update_container').appendChild(deleteButton);
            }
            if(node['class']=='HarmTreeLeaf'){
                const callback = (result)=>{
                let res = {};
                res['action'] = 'update';
                res['type'] = 1;
                res['data'] = result;
                res['nodeId'] = this.getCurrentNodeId();
                this.onSubmit(res);
                }
                let initialState = {'riskSource':node['riskSource'],'threatType':node['threatType'],'attributeDefinition':node['attributeDefinition']['id']};
                const leafInput = new LeafNodeInput(this.harmTreeLeafCreateData,callback,initialState);
                document.getElementById('update_container').appendChild(leafInput.render());
            }
            if(node['class']=='HarmTreeLogicalNode'){
                const callback = (result)=>{
                let res = {};
                res['action'] = 'update';
                res['type'] = 2;
                res['nodeId'] = this.getCurrentNodeId();
                res['data']={};
                res['data']['value'] = result;
                this.onSubmit(res);
                };

                const logicInput = new LogicNodeInput(this.harmTreeLogicCreateData,callback,{'value':node['logicalRequirement']});
                logicInput.setState((node['logicalRequirement']==1||node['logicalRequirement']==-1)?'default':'custom');
                document.getElementById('update_container').appendChild(logicInput.render());
            }
        });
        
        
    }
    clear(){
        removeElementById('update_container');
    }
    
    getCurrentNodeId(){
        return this.nodeId;
    }
    
    createContainer(){
        const formContainer = document.createElement('div');
        formContainer.setAttribute('id','update_container');
        formContainer.setAttribute('class','panel panel-default');
        formContainer.innerHTML = '<p class=s_title>Update options</p>';
                
        document.getElementById('menu').appendChild(formContainer);
    }
    
    
}


