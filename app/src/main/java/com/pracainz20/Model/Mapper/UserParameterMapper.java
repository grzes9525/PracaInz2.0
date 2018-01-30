package com.pracainz20.Model.Mapper;

import com.pracainz20.Model.UserParameter;

/**
 * Created by Grzechu on 29.01.2018.
 */

public abstract class UserParameterMapper {

    public static String methodsGetMapper(Integer selectedItemId, UserParameter userParameter){

        String result = null;
        if(selectedItemId==0)
            result=userParameter.getWeight();

        else if(selectedItemId==1)
            result= userParameter.getNeckCircuit();

        else if(selectedItemId==2)
            result=userParameter.getHipCircuit();

        else if(selectedItemId==3)
            result=userParameter.getWaistCircuit();

        else if(selectedItemId==4)
            result=userParameter.getChestCircuit();

        else if(selectedItemId==5)
            result=userParameter.getLeftBicepsCircuit();

        else if(selectedItemId==6)
            result=userParameter.getRightBicepsCircuit();

        else if(selectedItemId==7)
            result=userParameter.getImageOfProfile();

        return result;
    }


    public static String mapper(Integer selectedItemId) {
        String columnName = null;
        if (selectedItemId == 0) {
            columnName = "weight";
        } else if (selectedItemId == 1) {
            columnName = "neckCircuit";
        } else if (selectedItemId == 2) {
            columnName = "hipCircuit";
        } else if (selectedItemId == 3) {
            columnName = "waistCircuit";
        } else if (selectedItemId == 4) {
            columnName = "chestCircuit";
        } else if (selectedItemId == 5) {
            columnName = "leftBicepsCircuit";
        } else if (selectedItemId == 6) {
            columnName = "rightBicepsCircuit";
        } else if (selectedItemId == 7) {
            columnName = "imageOfProfile";
        }
        return columnName;
    }

    public static void methodsMapper(Integer selectedItemId, String currentValue, UserParameter userParameter) {


        switch (selectedItemId) {
            case 0:
                userParameter.setWeight(currentValue);
                break;
            case 1:
                userParameter.setNeckCircuit(currentValue);
                break;
            case 2:
                userParameter.setHipCircuit(currentValue);
                break;
            case 3:
                userParameter.setWaistCircuit(currentValue);
                break;
            case 4:
                userParameter.setChestCircuit(currentValue);
                break;
            case 5:
                userParameter.setLeftBicepsCircuit(currentValue);
                break;
            case 6:
                userParameter.setRightBicepsCircuit(currentValue);
                break;
            case 7:
                userParameter.setImageOfProfile(currentValue);
                break;
        }
    }

}
