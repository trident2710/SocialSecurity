/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.test;

import inria.socialsecurity.attributeprovider.address.AddressField;
import inria.socialsecurity.attributeprovider.address.AddressProvider;
import inria.socialsecurity.attributeprovider.address.AddressProviderImpl;
import java.util.Random;
import org.junit.Assert;

/**
 *
 * @author adychka
 */
//@RunWith(JUnit4ClassRunner.class)
public class AddressProviderTestCase{

    
    private void testAddressProviderParsing(String address,
            AddressField field,
            String expectedValue,boolean  isMalformed){
        AddressProvider impl = new AddressProviderImpl(address);
        try {
            Assert.assertEquals(impl.getParam(field), expectedValue);
            if(isMalformed)
                Assert.fail("Should throw exception");
        } catch (RuntimeException e) {
            System.out.println(isMalformed);
            e.printStackTrace();
            if(!isMalformed){
                System.out.println(address);
                Assert.fail("Should not throw exception");
            }
        }
         
    }
    
    //@Test
    public void testAddressProvider(){
        Random random = new Random(System.currentTimeMillis());
        for(int i=0;i<100;i++){
            generateTestValue(random,(s,f,e,m)->{testAddressProviderParsing(s, f, e, m);});
        }
    }
    
    
    private interface GetTestValueCallback{
        void forTest(String address,AddressField field,String expectedValue,boolean isMalformed);
    }
    
    private void generateTestValue(Random random,GetTestValueCallback callback){
        String[] vals = new String[]{"one","two","UPPERCASE","lovercase","special symbol !@#$%^&*()_+{}/.,;:","123"};
        
        
        String test = vals[random.nextInt(vals.length)];
        int d = random.nextBoolean()?7:1+random.nextInt(7);
        int p = random.nextInt(d);
        String res = "";
        for(int i=0;i<d;i++){
            if(i==p){
                res+=test;
            } else{
                res+=random.nextBoolean()?vals[random.nextInt(vals.length)]:"";
            }
            if(i<d-1)res+="|";
        }
        callback.forTest(res, AddressField.class.getEnumConstants()[p], test,d!=7);
        
    }
}
