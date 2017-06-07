/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.aspect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import inria.socialsecurity.aspect.exception.JsonPropertyCheckException;
import inria.socialsecurity.entity.FacebookAccount;
import inria.socialsecurity.entity.JsonStoringEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 *
 * @author adychka
 */
@Aspect
public class JsonStoringEntityAspect {
    @Before("execution(* inria.socialsecurity.entity.JsonStoringEntity.*())")
    public void logData(JoinPoint joinPoint){
        System.out.println("facebookAccount used");
    }
    
    @Before("execution(* inria.socialsecurity.entity.JsonStoringEntity.get*())")
    public void checkGet(JoinPoint joinPoint){
        assureGsonInitialized((JsonStoringEntity)joinPoint.getThis());
    }
    
    
    @Before("execution(* inria.socialsecurity.entity.JsonStoringEntity.getJsonElementByName(..))")
    public void checkParamExistInJson(JoinPoint jp) throws JsonPropertyCheckException{
        JsonElement jsonElement = (JsonElement)jp.getArgs()[0];
        String propertyName = (String)jp.getArgs()[1];
        if(!jsonElement.isJsonObject()){
            throw new JsonPropertyCheckException("json element is not a json object");
        }
        if(!jsonElement.getAsJsonObject().has(propertyName)){
            throw new JsonPropertyCheckException("json object does not contain the property with the name: "+propertyName);
        }

    }
    
    private void assureGsonInitialized(JsonStoringEntity fa){
        if(fa.getInfo()==null){
            JsonParser jp = new JsonParser();
            fa.setInfo((JsonObject)jp.parse(fa.getJsonString()));
        }
    }
}
