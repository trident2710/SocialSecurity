/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.attributeprovider.address;


/**
 *
 * @author adychka
 */
public class AddressProviderImpl implements AddressProvider{
   /** 
    * in format "Country|City|Street|StreetType|BuildingNo|Flat|Index"
    * Example: "Canada|Montreal|Kennedy|Str||||" if there are no building no, flat and index etc.
    */
    private String value;
    private AddressParser addressParser;
    
    public AddressProviderImpl(String value){
        this.value = value;
        this.addressParser = new AddressParser();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getParam(AddressField addressField) {
       return addressParser.parseField(addressField);
    }

   

    private final class AddressParser{
        private String parseField(AddressField addressField){
            String[] parts = getValue().split(",",-1);
            if(parts.length!=AddressField.class.getEnumConstants().length){
                throw new RuntimeException("malformed string, numer of parts is "+parts.length);
            }
            int i=0;
            for(AddressField f:AddressField.class.getEnumConstants()){
                if(f==addressField){
                    return parts[i];
                }
                i++;
            }
            return null;
        }
       
    }
}
