/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var contest1 = {
    "name":"Win a Free VisualStudio '05 Copy!",
    "steps":[{
        "intro":"Texte Intro Etape 1",
        "fields":[{
            "type":"EKO",
            "subtype":"prenom",
            "label":"Prénom",
            "validation":"None"
        },{
            "type":"EKO",
            "subtype":"nom",
            "label":"Nom",
            "validation":"None"
        },{
            "type":"TAG",
            "subtype":"shorttext",
            "label":"Occupation",
            "validation":"None"
        }]
    },{
        "intro":"Texte Intro Etape 2",
        "fields":[{
            "type":"EKO",
            "subtype":"prenom",
            "label":"Prénom",
            "validation":"None"
        },{
            "type":"EKO",
            "subtype":"nom",
            "label":"Nom",
            "validation":"None"
        },{
            "type":"TAG",
            "subtype":"shorttext",
            "label":"Occupation",
            "validation":"None"
        }]
    }]
};

// namespace
var eko={}; eko.contest={};
eko.contest.FieldTypes = { // and subtypes
    EKO : {
        prenom: "Prenom",
        nom:"Nom",
        couriel:"Couriel"
    },
    TAG : {
        shorttext:"Short Text",
        longtext:"Long Text"
    },
    CHOICE : {
        dropdown:"Drop Down (1)",
        radio:"Radio Buttons",
        check:"CheckBoxes (mult)"
    }
};
eko.groups = [
{
    name:"gr-french",
    label:"Francais"
},
{
    name:"gr-english",
    label:"Anglais"
},
{
    name:"gr-child",
    label:"Enfant"
},
{
    name:"gr-teen",
    label:"Adolescent"
},
{
    name:"gr-adult",
    label:"Adulte"
},
{
    name:"gr-green",
    label:"Vert"
},
{
    name:"gr-techsavy",
    label:"Techno"
},
{
    name:"gr-bio",
    label:"Bio"
},
];


function renderJSON(contest,divselector){
    $(divselector).html('JSON: <textarea cols="80" rows="25" name="myname">'+$.toJSON(contest)+'</textarea>');
}

function renderEditor(contest,divselector){
    var rCtx = $(divselector);

    var contestheader = $('<h3><i>Contest: </i></h3>');
    contestheader.append($('<span></span>').text(contest.name));

    var stepsElt = $('<div class="steps"></div>');
    stepsElt.data('parentarray',contest.steps);
    for (var s=0;s<contest.steps.length;s++ ){
        var step = contest.steps[s];
        var stepElt = $('<div class="step brother"/>');
        // step title in a h4
        stepElt.append($('<h4 />').append('<a href="#" >Step '+(s+1)+'</a>'));
        //html += '<h4><a href="#">Step '+(s+1)+'</a></h4>';

        var upArrowElt=EkoActionIcon('ui-icon-arrowthick-1-n');
        var downArrowElt=EkoActionIcon('ui-icon-arrowthick-1-s');
        var deleteRowElt=EkoActionIcon('ui-icon-closethick');

        var ctrlElt = $('<span class=accordioncontrol></span>');
        ctrlElt.append(upArrowElt);
        ctrlElt.append(downArrowElt);
        ctrlElt.append(deleteRowElt);
        upArrowElt.click(function(){
            EkoMoveInDOM($(this),-1);
            return false;
        });
        downArrowElt.click(function(){
            EkoMoveInDOM($(this),1);
            return false;
        });
        deleteRowElt.click(function(){
            EkoRemoveInDOM($(this));
            return false;
        });

        var contentElt = $('<div/>')
        .append(ctrlElt)
        .append($('<span>Step Intro Text: </span><br>'))
        .append($('<textarea cols="40" rows="5" name="myname" />').text(step.intro))
        for (var f=0;f<step.fields.length;f++ ){
            var field = step.fields[f];
            contentElt.append($('<div/>')
                .text('Field '+(s+1)+'.'+(f+1)+': '+field.type+' : '+field.subtype+' : '+field.label+' : '+field.validation)
                );
        }
        var choicegrpelt = EkoMakeGSLGroup();
        contentElt.append(choicegrpelt);

        stepElt.append(contentElt);
        stepsElt.append(stepElt);
    }
    //var addStepBtn = $('<a href="#" class="eko-btn ui-state-default ui-corner-all"><span class="ui-icon ui-icon-plus"></span>Step</a>');
    var addStepBtn = EkoButton('Step');
    addStepBtn.click(function(){
        //alert('Would add step');
        contest.steps.push({
            "intro":"Texte Intro Etape "+(contest.steps.length+1),
            "fields":[{
                "type":"EKO",
                "subtype":"prenom",
                "label":"Prénom",
                "validation":"None"
            }]
        });
        renderEditor(contest,divselector);
        return false;
    });

    rCtx.empty();
    rCtx.append(contestheader);
    rCtx.append(stepsElt);
    rCtx.append($('<p />').append(addStepBtn));

    // call accordion affter adding, fixes content heights...
    stepsElt.accordion({ 
        /*fillSpace:true,*/
        header: "h4"
    });

}
function EkoMakeGSLGroup(){
    var boundarray = [
    {
        "name":"gr-french",
        "label":""
    },

    {
        "name":"gr-english",
        "label":"English"
    },

    {
        "name":"gr-child",
        "label":""
    },

    {
        "name":"gr-adult",
        "label":""
    },

    {
        "name":"gr-green",
        "label":"Enviro"
    },

    {
        "name":"gr-techsavy",
        "label":"Techie"
    },
    ];
    var choiceditorgroupelt = $('<div class="choiceeditorgroup"></div')
    choiceditorgroupelt.data('parentarray', boundarray);
    var addGSLBtn = EkoButton('Add GSL').click(function() {
        var nuentry = {
            "name":"gr-new",
            "label":"Nu Label"
        };
        boundarray.push(nuentry);
        choiceditorgroupelt.append(EkoGroupSelectAndLabel(nuentry));
        return false;
    });
    choiceditorgroupelt.append($('<p />').append(addGSLBtn));
    var parentarray = choiceditorgroupelt.data('parentarray');
    for (var z=0;z<parentarray.length;z++){
        choiceditorgroupelt.append(EkoGroupSelectAndLabel(parentarray[z]));
    }
    return choiceditorgroupelt;
}
function EkoGroupSelect(selname){
    //eko.groups = [
    // {name:"gr-french", label:"Francais"},
    var selElt = $('<select></select>');
    for (var g=0; g<eko.groups.length; g++) {
        var gr = eko.groups[g];
        var optElt = $('<option value="'+gr.name+'">'+gr.name+'</option>');
        if (gr.name==selname){
            optElt.attr('selected', 'selected');
        }
        selElt.append(optElt);
    }
    return selElt;
}

function EkoGroupDefaultLabel(name){
    for (var g=0; g<eko.groups.length; g++) {
        var gr = eko.groups[g];
        if (name==gr.name){
            return gr.label;
        }
    }
    return "Label";
}
function EkoGroupSelectAndLabel(bounddata){
    bounddata = bounddata || {
        name:'gr-french',
        label:''
    };
    var grSelElt = EkoGroupSelect(bounddata.name);
    var grLabelElt=$('<input type="text" value=""></input>');
    grLabelElt.attr("value",bounddata.label);
    grLabelElt.css('margin-left','10px');
    var adjustDefaultLabel = function(){
        var defname=EkoGroupDefaultLabel(bounddata.name);
        if (bounddata.label==''){
            grLabelElt.attr("value",defname);
            grLabelElt.css('color','gray');
        }
    }

    var upArrowElt=EkoActionIcon('ui-icon-arrowthick-1-n');
    var downArrowElt=EkoActionIcon('ui-icon-arrowthick-1-s');
    var deleteRowElt=EkoActionIcon('ui-icon-closethick');

    var jsonElt=$('<pre style="display:inline; margin-left:20px;"></pre>');
    jsonElt.text($.toJSON(bounddata));
    grSelElt.change(function(){
        //alert($.toJSON($("option:selected", this)));
        var name = $("option:selected", this).val();
        bounddata.name = name;
        adjustDefaultLabel();
        jsonElt.text($.toJSON(bounddata));
    });
    grLabelElt.change(function(){
        var label = $(this).attr("value");
        // save
        bounddata.label=label;
        grLabelElt.css('color','');
        grSelElt.change();
    //alert('name changed to: '+label)
    });
    upArrowElt.click(function(){
        EkoMoveInDOM($(this),-1);
        return false;
    });
    downArrowElt.click(function(){
        EkoMoveInDOM($(this),1);
        return false;
    });
    deleteRowElt.click(function(){
        EkoRemoveInDOM($(this),1);
        return false;
    });
    
    

    var combined=$('<div></div>');
    combined.append(grSelElt);
    combined.append(grLabelElt);
    combined.append(upArrowElt);
    combined.append(downArrowElt);
    combined.append(deleteRowElt);
    combined.append(jsonElt);
    adjustDefaultLabel();
    combined.addClass('brother');
    return combined;
}

function EkoRemoveInDOM(element) {
    if (element==undefined) return
    msg='delete: ';
    msg+=' | '+$(element).nodeName;
    msg+='\n';
    // climb the searching for the sibling which is us.
    var measbrother;
    var ll = element.parents();
    for (i=0;i<ll.length;i++){
        msg+=' | '+ll[i].nodeName+ '-'+$(ll[i]).hasClass('brother');
        if ($(ll[i]).hasClass('brother')) {
            measbrother = ll[i];
            break;
        }
    }

    // find my position in the list of siblings - for affecting the array
    var position=0;
    for (p = measbrother; $(p).prev('.brother').length>0; p = $(p).prev('.brother')) {
        position+=1;
    }
    msg = 'position: '+position + ' '+ msg

    // find the parent holding the bound parentarray.
    // traverse same ll = element.parents as above...
    msg+='\n';
    for (i=0;i<ll.length;i++){
        if ($(ll[i]).data('parentarray')){
            var bounddata = $(ll[i]).data('parentarray');
            EkoRemoveInArray(bounddata,position);
            msg+=' arrayJSON:  [ '+$.toJSON(bounddata)+']';
            break;
        }
    }

    // make sure not las entry: has one sibling, before or after...
    nextsibling = ($(measbrother).next('.brother'))[0];
    prevsibling = ($(measbrother).prev('.brother'))[0];
    if (nextsibling || prevsibling){
        $(measbrother).remove();
    }
    $('#status').html(msg);

}
function EkoMoveInDOM(element,direction) {
    if (element==undefined) return
    direction = direction || 1;
    msg='move direction: '+direction;
    msg+=' | '+$(element).nodeName;
    msg+='\n';

    // climb the searching for the sibling which is us.
    var measbrother;
    var ll = element.parents();
    for (i=0;i<ll.length;i++){
        msg+=' | '+ll[i].nodeName+ '-'+$(ll[i]).hasClass('brother');
        if ($(ll[i]).hasClass('brother')) {
            measbrother = ll[i];
            break;
        }
    }

    // find my position in the list of siblings - for affecting the array
    var position=0;
    for (p = measbrother; $(p).prev('.brother').length>0; p = $(p).prev('.brother')) {
        position+=1;
    }
    msg = 'position: '+position + ' '+ msg

    // find the parent holding the bound parentarray.
    // traverse same ll = element.parents as above...
    msg+='\n';
    for (i=0;i<ll.length;i++){
        if ($(ll[i]).data('parentarray')){
            var bounddata = $(ll[i]).data('parentarray');
            EkoMoveInArray(bounddata,direction,position);
            msg+=' arrayJSON:  [ '+$.toJSON(bounddata)+']';
            break;
        }
    }

    if (direction>0){
        nextsibling = ($(measbrother).next('.brother'))[0];
        if (nextsibling) $(nextsibling).after(measbrother);
    } else if (direction<0){
        prevsibling = ($(measbrother).prev('.brother'))[0];
        if (prevsibling) $(prevsibling).before(measbrother);
    }
    $('#status').html(msg);
}

function EkoRemoveInArray(anarray,position){
    anarray.splice(position,1);
}

function EkoMoveInArray(anarray,direction,position){
    //alert('dir:'+direction+' position:'+position);
    if (direction>0){
        if (position<anarray.length-1) {
            var tmp1 = anarray[position+1];
            anarray[position+1] = anarray[position];
            anarray[position] = tmp1;
        }
    } else if (direction<0){
        if (position>0) {
            var tmp2 = anarray[position-1];
            anarray[position-1] = anarray[position];
            anarray[position] = tmp2;
        }
    }
}

function EkoActionIcon(iconClass) {
    // calls EkoIconWrapper...
    return EkoIconWrapper('',iconClass,'eko-action-icon');
}

function EkoButton(label,iconClass) {
    // calls EkoIconWrapper...
    label = label || "Add";
    return EkoIconWrapper(label,iconClass,'eko-btn');
}
function EkoIconWrapper(label,iconClass,cssClass) {
    /* return a new element for insertion into DOM
     * icon class : e.g. ui-icon-plus, ui-icon-arrowthick-1-n, etc
     * cssClass: eko-btn, eko-action-icon (arrown,..)
     */
    iconClass = iconClass || "ui-icon-plus";
    cssClass = cssClass || "eko-btn";
    var btnElt = $('<a href="#" class="'+cssClass+' ui-state-default ui-corner-all"><span class="ui-icon '+iconClass+'"></span>'+label+'</a>');
    btnElt.hover(function() {
        $(this).addClass('ui-state-hover');
    },function() {
        $(this).removeClass('ui-state-hover');
    })
    return btnElt;
}