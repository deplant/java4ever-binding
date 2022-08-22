'use strict';

const api = require('../resources/api.json')
const fs = require('fs');

const packageName = 'tech.deplant.java4ever.binding';
const PATH = 'src/gen/java/tech/deplant/java4ever/binding/';

const reserved = {
    public: 'publicKey',
    secret: 'secretKey',
    switch: 'switchTo'
};

var genFiles = require('../../gen/nodejs/gen-files.json');

let types = {};
let appInterfaces = {};
let currMod;

for(var f of genFiles) {
    if (fs.existsSync(PATH + f))
        fs.unlinkSync(PATH + f);
}

genFiles = [];

function isEmpty(strValue) {
    return (!strValue || strValue.trim() === "" || (strValue.trim()).length === 0);
}

function dereserve(iden) {
    //return (reserved.includes(iden)?'_':'') + iden;
    return reserved[iden]||iden;
}

const flatMap = (f,xs) =>
  xs.reduce((acc,x) =>
    acc.concat(f(x)), []);

function toCapitalCase(s) {
    return s.charAt(0).toUpperCase() + s.slice(1);
}

function toCamelCase(s) {
    var arr = s.split('_');
    return dereserve(arr.shift() + arr.map(toCapitalCase).join(''));
}

function toHTML(s) {
    return s&&s.trim().replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/\n\s*\n/g, '<p>').replace(/\n/g,'').replace(/\[(.+)\]/g, (match,p1) => `<a target="_blank" href="${p1}">${p1}</a>`)||'';
}

function toJavadoc(summary,description) {
    if (!isEmpty(summary) || !isEmpty(description)) {
        return toHTML((!isEmpty(summary)?summary+' ':'') + (!isEmpty(description)?description:''));
    }
    else {
        return '';
    }
}

function toJavadocBlock(summary,description) {
    if (!isEmpty(summary) || !isEmpty(description)) {
        return `/**
        * ${toHTML((!isEmpty(summary)?summary+' ':'') + (!isEmpty(description)?description:''))}
        */`
    }
    else {
        return ``;
    }
}

function isDeprecated(summary,description) {
        return (!isEmpty(summary) && summary.toLowerCase().includes('deprecated')) ||
        (!isEmpty(description) && description.toLowerCase().includes('deprecated'));
}

function isFlattable(typeName) {
    let type = types[typeName];
    return type && type.isStruct && (typeName.indexOf('.ParamsOf') > 0 || typeName.indexOf('.ResultOf') > 0);
}

function trimClassName(cName, mName) {
    //console.log('==',cName,currMod.name);
    let arr = cName.split('.');
    if (arr.length == 2 && arr[0] == arr[1].toLowerCase())
        cName = arr[0] + '.' + arr[1].toUpperCase();
    return currMod&&cName.startsWith(currMod.name+'.')?cName.split('.').pop():toCapitalCase(cName);
}

function setTypeExported(field) {
    let type = types[field.type];
    if (type&&!type.isExported) {
        type.isExported = true;
        if (type.fields)
            type.fields.forEach(f => setTypeExported(f));
        //console.log(field.type);
    }
    return field;
}

function stringifyFields(fields, type) {
    let totLen = fields.length + (type?1:0);
    let stream = totLen > 1?'Stream.of(':'';
    return `"{${totLen?`"+${stream}${(type?[`"\\"type\\":\\"${type}\\""`]:[]).concat(fields.map(f=> {
        let qu = f.type == 'String'?'\\"':'';
        const flat = isFlattable(f.type);
        if (flat)
            return `"\\"${f.name}\\":"+${stringifyFields(types[f.type].fields)}`;
        return `(${toCamelCase(f.name)}==null?${stream?'null':'""'}:("\\"${f.name}\\":${qu}"+${(n=>f.isArray?`Arrays.toString(${n})`:n)(toCamelCase(f.name))}${qu?`+"${qu}"`:''}))`;
    }))}${stream&&`).filter(_f -> _f != null).collect(Collectors.joining(","))`}+"`:''}}"`
}

function fieldMapper(f) {
    let field = {name: f.name||'value', desc: f.description,
        summary: f.summary,
        deprecated: f.summary != null && f.summary.includes("Deprecated"),
        getType:(mName) => {
            let result;
            if (field.type in types && types[field.type].isSubclass)
                result = types[field.type].parent;
            else
                result = (!field.isRef)||(field.type in types&&types[field.type].isExported)?trimClassName(field.type, mName):'Map<String,Object>';
            if (field.isArray)
                result += '[]';
            //console.log(field.type,'=>',result);
            return result;
        }
    };
    while (true) {
        if (f.type == 'Optional') {
            f = f.optional_inner;
            field.isOptional = true;
        } else if (f.type == 'Array') {
            f = f.array_item;
            field.isArray = true;
        } else {
            if (f.type == 'Ref') {
                if (f.ref_name == 'Value') {
                    field.type = 'Map<String,Object>';
                }
                else {
                    field.type = f.ref_name;
                }
                field.isRef = true;
            } else
                field.type = f.type == 'BigInt'?'Long':f.type;
            break;
        }
    }
    return field;
}

function getAppObjectHandler(obj) {
    return `(params,type) -> {
                Map data = (Map)(type==3?((Map)params).get("request_data"):params);
                switch ((String)data.remove("type")) {
${Object.entries(obj.methods).map(([n,o]) => {
    
    return `
                    case "${n}":
                        try {${o.params.length?`
                            ParamsOf${obj.type}.${n} p = new ObjectMapper().convertValue(data, ParamsOf${obj.type}.${n}.class);`:''}
                            appObject.${dereserve(n.charAt(0).toLowerCase()+n.substring(1))}(${o.params.map(p=>`p.${toCamelCase('get_'+p.name)}()`)})${o.result?`.whenComplete((res,ex) -> {
                                new Client(context).resolveAppRequest(
                                    (Integer)((Map)params).get("app_request_id"),
                                    ex==null?
                                        new Client.AppRequestResult.Ok(new ResultOf${obj.type}.${n}(${o.result.length?'res':''})):
                                        new Client.AppRequestResult.Error(ex.getMessage())
                                );
                            })`:''};
                        } catch (Exception e) {
                            e.printStackTrace(System.out);
                        }
                        break;
`}).join('')
                }
                }
            }`;
}

api.modules.forEach(m => m.types.forEach(t => {
    let type = {};
    switch (t.type) {
        case 'Struct':
            type.fields = t.struct_fields.map(fieldMapper);
            type.isStruct = true;
            break;
        case 'EnumOfConsts':
            type.fields = t.enum_consts.map(f => ({name:f.name, desc: f.description, summary: f.summary/*, getType:(mName)=>{console.log('==',mName,t.name);return toCapitalCase(t.name)}*/}));
            type.isEnum = true;
            break;
        case 'EnumOfTypes':
            type.variants = t.enum_types.map(v => ({
                name:v.name,
                desc:v.description,
                summary:v.summary,
                get fields() {
                    if (v.ref_name)
                        return types[v.ref_name].fields;
                    return v.struct_fields.map(fieldMapper)
                }
            }));
            type.isEnumOfTypes = true;
            break;
        case 'Number':
            type.parent = 'Integer';
            type.isSubclass = true;
            break;
        default:
            console.log('!!! ', t);
    }
    types[m.name + '.' + t.name] = type;
}));

setTypeExported({type:'client.ClientConfig'});

api.modules.forEach(mod => {
    currMod = mod;
    let imports = {'com.fasterxml.jackson.annotation.JsonProperty':true,'com.fasterxml.jackson.core.JsonProcessingException':true,'java.util.Map':true,'java.util.Optional':true,'lombok.*':true,'java.util.stream.*':true,'java.util.Arrays':true};
    let body = '';

    mod.functions.forEach(f => {
        let rName = f.result.generic_args[0].ref_name;
        let rType = types[rName];
        let origName = f.name
        let rField = {getType:()=>'void'};
        let rParamName = '';
        if (rType&&rType.fields.length > 0) { // edited >1 to >0 to export real results
            setTypeExported({type:rName})
            rField = {getType:(mName) => trimClassName(rName, mName)}
        } else if (rType) {
            const f = rType.fields[0];
            rField = f;
        }

        let event = false;
        let appObject = false;
        let fields = [];
        f.params.forEach(p => {
            //let pName = p.ref_name;
            switch (p.name) {
                case 'context':
                case '_context':
                    break;
                case 'params':
                    /*
                    var fl = types[p.ref_name]
                    if (isFlattable(p.ref_name) || fl.fields.length == 1)
                        fields = fl.fields;
                    else {
                        fl.getType = () => '=======================';
                        fl.name = '!!!!!!!!!!!!';
                        fields = [fl];
                    }*/
                    fields = types[p.ref_name].fields;
                    rParamName = trimClassName(p.ref_name, mod.name);
                    setTypeExported({type:p.ref_name});
                    break;
                case 'callback':
                case 'request':
                    event = toCamelCase('_'+f.name+'_event');
                    imports['java.util.function.Consumer'] = true;
                    break;
                case 'app_object':
                    let ref = p.generic_args[0].ref_name;
                    let name = ref.substring(ref.indexOf('.') + 9);
                    console.log(name);
                    if (!(name in appInterfaces)) {
                        let methods = {};
                        p.generic_args.forEach((t,i) => {
                            for (let m of types[t.ref_name].variants) {
                                let mName = m.name; //.charAt(0).toLowerCase() + m.name.slice(1);
                                if (i==0)
                                    methods[mName] = {params: m.fields};
                                else
                                    methods[mName].result = m.fields;
                                setTypeExported({type:t.ref_name});
                            }
                        });
                        appInterfaces[name] = {methods};
                    }
                    appObject = {type:name, methods:appInterfaces[name].methods};
                    imports['java.util.Map'] = true;
                    //imports['com.fasterxml.jackson.databind.ObjectMapper'] = true;
                    break;
                default:
                    console.log(p.name);
            }
        });

        /*event = f.params.length > 2?toCamelCase('_'+f.name+'_event'):false;
        if (event)
            imports['java.util.function.Consumer'] = true;
        fields = f.params.length > 1?types[f.params[1].ref_name].fields:[];*/

        let params = flatMap(f=> isFlattable(f.type)?types[f.type].fields:[f], fields)
            .map(p=> ({type:setTypeExported(p).getType(), name:toCamelCase(p.name), desc:p.desc,
            summary:p.summary, deprecated:p.deprecated, optional: (p.isOptional?'':'@NonNull') }));

        //let paramsInner = fields.map(p=> ({type:setTypeExported(p).getType(), name:toCamelCase(p.name), desc:p.desc,
        //    summary:p.summary, deprecated:p.deprecated, optional: (p.isOptional?'':'@NonNull') }));

        if (appObject)
            params.push({type:appObject.type, name:'appObject', optional:''});

        // TODO temporary removed app objects
        body += `    /**\n`;
        body += `    * <strong>${mod.name}.${f.name}</strong>\n`;
        body += `    * ${toJavadoc(f.summary,f.description)}\n`;
        body += params.map(p => `    * @param ${p.name} ${toHTML(p.summary)} ${toHTML(p.desc)}\n`).join('');
        // let rDesc = rField.desc || (((type)=>{return type&&type.desc})(types[rField.getType()]));
        // if (rDesc)
        if (rField.getType(mod.name)!='void')
            body += `    * @return {@link ${packageName}.${toCapitalCase(mod.name)}.${rField.getType(mod.name)}}\n`;
        body += `    */\n`;
        body += `    ${isDeprecated(f.summary,f.description)?'@Deprecated ':''}public static ${rField.getType(mod.name).replace('Map<String,Object>','Map')} ${toCamelCase(f.name)}(Context ctx${params.map(p=>', '+p.optional+' '+p.type+' '+p.name).join('')}${event?`, Consumer<${event}> consumer`:''})  throws JsonProcessingException {\n`
        body += `        ${rField.getType(mod.name)=='void'?'':'return '} ctx.call${rField.getType(mod.name)=='void'?'Void':''}${appObject?'AppObject':''}${event?'Event':''}("${mod.name}.${f.name}", ${rParamName?'new ' + rParamName + '(':''}${(params.filter(p=>p.name!='appObject').length > 0)?params.filter(p=>p.name!='appObject').map(p=>p.name).join(', '):'null'}${rParamName?')':''}${event?`, consumer`:''}${appObject?`, appObject`:''}${rField.getType(mod.name)=='void'?')':', '+rField.getType(mod.name).replace('Map<String,Object>','Map')+'.class)'};\n`;
        body += `    }\n\n`;
    });

    const className = toCapitalCase(mod.name);
    genFiles.push(className + '.java');

    fs.writeFileSync(PATH + className + '.java', `package ${packageName};

${Object.keys(imports).map(i=>`import ${i};`).join('\n')}

/**
 *  <strong>${mod.name}</strong>
 *  Contains methods of "${mod.name}" module.\n
 *  ${toJavadoc(mod.summary,mod.description)}
 *  @version EVER-SDK ${api.version}
 */
public class ${className} {
${Object.entries(types).filter(([n,t])=>t.isExported&&n.startsWith(mod.name+'.')).map(([cName,t]) => {
    let genFunc;
    if (t.isEnum)
        genFunc = getEnumSource;
    else if(t.isEnumOfTypes)
        genFunc = getEnumOfTypesSource;
    //else if(t.isSubclass)
    //    genFunc = (cName,t) => `    public static class ${cName} extends ${t.parent} {}\n`;
    else if(t.isStruct)
        genFunc = getStructSource;
    if (genFunc)
        return genFunc(cName.split('.').pop(),t);
}).join('')}
${body}}
`);

});

function getStructSource(cName, t, sClass) {
    //console.log(cName,t.fields, sClass);
    var args = t.fields.filter(f=>f.name);
    var constr = '';

return `\n
    /**
    * ${toJavadoc(t.summary,t.desc)}
${t.fields.filter(f=>f.name).map(f=>
    {
    return `    * @param ${toCamelCase(f.name)} ${toJavadoc(f.summary,f.desc)}`
    }).join('\n')}
    */
    public record ${cName}(${t.fields.filter(f=>f.name).map(f=> {
                           return `${f.isOptional && isDeprecated(f.summary,f.desc)?`@Deprecated `:``}${!f.isOptional?``:``}${reserved.hasOwnProperty(f.name)?`@JsonProperty("${f.name}") `:``}${f.getType()} ${toCamelCase(f.name)}`
                           }).join(', ')}) ${sClass?`implements ${sClass} `:``}${sClass?`{
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }`:`{}`}`


}

function getEnumSource(cName, t) {
    return `${toJavadocBlock(t.summary,t.desc)}
    public enum ${cName} {
        ${t.fields.filter(f=>f.name).map(f=> `
        ${toJavadocBlock(f.summary,f.desc)}
        ${f.name}`).join(',\n')}
    }`
}

function getEnumOfTypesSource(cName, t) {
    if (toCapitalCase(currMod.name) == cName)
        cName = cName.toUpperCase();
    return `
    public interface ${cName} {
${t.variants.map(v => {

    return (v.fields.length?'':`
        public static final ${v.name} ${v.name.toUpperCase()} = new ${v.name}();
`) + getStructSource(v.name,v,cName);
}).join('\n')}
}`;
}

currMod = null;

for(let className in appInterfaces) {
    genFiles.push(className + '.java');
    let iface = appInterfaces[className];
    fs.writeFileSync(PATH + className + '.java', `package ${packageName};

import java.util.Map;

public interface ${className} {
${Object.entries(iface.methods).map(([n,o]) => {
    let name = n.charAt(0).toLowerCase() + n.slice(1);
    let res = o.result && o.result.length ? o.result[0].getType() : 'void';
    return `    ${o.result?`${res}`:'void'} ${dereserve(name)}(${o.params.map(p=>p.getType()+' '+toCamelCase(p.name))});`;
}).join('\n')}
}
`);
}

fs.writeFileSync('src/gen/nodejs/gen-files.json', JSON.stringify(genFiles));

//Object.entries(types).filter(([n,t])=>true/*t.isExported*/).forEach(([n,t])=>console.log(n,t.isExported));
