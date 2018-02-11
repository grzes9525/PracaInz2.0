package com.pracainz20.Model.Mapper;

/**
 * Created by Grzechu on 09.02.2018.
 */

public abstract class UserMapper {

    public static String mapper(Integer selectedItemId) {
        String columnName = null;
        if (selectedItemId == 0) {
            columnName = "age";
        } else if (selectedItemId == 1) {
            columnName = "firstName";
        } else if (selectedItemId == 2) {
            columnName = "height";
        } else if (selectedItemId == 3) {
            columnName = "lastName";
        } else if (selectedItemId == 4) {
            columnName = "phoneNumber";
        } else if (selectedItemId == 5) {
            columnName = "profileImage";
        } else if (selectedItemId == 6) {
            columnName = "userId";
        }
        return columnName;
    }
}
