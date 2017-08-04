/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test;

import com.google.gson.JsonParser;
import inria.socialsecurity.converter.transformer.FacebookTrueVisibilityToAttributeMatrixTransformer;
import inria.socialsecurity.converter.transformer.MapToJsonConverter;
import inria.socialsecurity.test.config.TestConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author adychka
 */
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ConversionTestCase {
    
    @Autowired
    FacebookTrueVisibilityToAttributeMatrixTransformer ftvtamt;
    
    @Autowired
    MapToJsonConverter mtjc;
    
    String tst = "{\"id\":{\"value\":\"100001470650611\",\"visibility\":\"PUBLIC\"},\"first_name\":{\"value\":\"Andrii\",\"visibility\":\"PUBLIC\"},\"last_name\":{\"value\":\"Dychka\",\"visibility\":\"PUBLIC\"},\"birthday\":{\"value\":\"null27 October 1994\",\"visibility\":\"FRIEND\"},\"gender\":{\"value\":\"Male\",\"visibility\":\"PUBLIC\"},\"gender_interests\":{\"value\":\"Women\",\"visibility\":\"PUBLIC\"},\"political_view\":{\"value\":\"ukrainian nationalism\",\"visibility\":\"FRIEND_OF_FRIEND\"},\"religious_view\":{\"value\":\"Humanism\",\"visibility\":\"FRIEND_OF_FRIEND\"},\"languages\":{\"value\":[\"Russian\",\"Ukrainian\",\"English\"],\"visibility\":\"PUBLIC\"},\"phones\":{\"value\":[\"+380 63 487 5802\",\"07 91 85 91 11\"],\"visibility\":\"FRIEND\"},\"address\":{\"value\":[\"grt 34\",\"Kyiv, Ukraine\",\"01230\"],\"visibility\":\"FRIEND_OF_FRIEND\"},\"email_address\":{\"value\":[\"andy1994dic@gmail.co\"],\"visibility\":\"PUBLIC\"},\"work_ids\":{\"value\":[\"100001470650611\",\"381859175220687\"],\"visibility\":\"PUBLIC\"},\"education_ids\":{\"value\":[\"100001470650611\",\"156498661085979\",\"216166991779508\"],\"visibility\":\"PUBLIC\"}}";
    
    @Test
    public void testRealVisibilityDataConversion(){
        mtjc.convertFrom(ftvtamt.parseFromSource(new JsonParser().parse(tst).getAsJsonObject())).toString();
        Assert.assertEquals(true,true);
    }
}
