package com.sia.client.ui.lineseeker;

import com.sia.client.config.Utils;

public abstract class AlertConfigValidator {

    public static String validate(AlertLayout alertLayout) {

        if ( alertLayout.getSelectedGameId() < 1) {
            return "Please select a game ";
        }
        String err = null;
        AlertSectionName [] assertSectionNames = AlertSectionName.values();
        boolean hasInput = false;
        for(AlertSectionName name: assertSectionNames) {
            SectionAttribute attribute = alertLayout.getSectionComponents(name).getSectionAtrribute();
            ColumnAttributes leftAttribute = attribute.getLeftColumn();
            ColumnAttributes rightAttribute = attribute.getRightColumn();
            if ( ! Utils.isEmpty(leftAttribute.getJuiceInput()) || ! Utils.isEmpty(leftAttribute.getLineInput())
               ||  ! Utils.isEmpty(rightAttribute.getJuiceInput()) || ! Utils.isEmpty(rightAttribute.getLineInput()) ){

                hasInput = true;
                break;
//            if ( null == leftAttribute.getJuiceInput() || "".equals(leftAttribute.getJuiceInput().trim()) || null == rightAttribute.getJuiceInput() || "".equals(rightAttribute.getJuiceInput().trim())) {
//                err =  "Please enter Juice for "+attribute.getSectionName().name();
//                break;
//            } else if ( ! Utils.isNumericString(leftAttribute.getJuiceInput()) ) {
//                err =  "Juice input \""+leftAttribute.getJuiceInput()+"\" is invalid for "+attribute.getSectionName().name();
//                break;
//            } else if ( ! Utils.isNumericString(rightAttribute.getJuiceInput() )) {
//                err =  "Juice input \""+rightAttribute.getJuiceInput()+"\" is invalid for "+attribute.getSectionName().name();
//                break;
//            }
//
//            if (  name.toShowLineInput() ) {
//                if (null == leftAttribute.getLineInput() || "".equals(leftAttribute.getLineInput().trim()) || null == rightAttribute.getLineInput() || "".equals(rightAttribute.getLineInput().trim())) {
//                    err = "Please enter Line for " + attribute.getSectionName().name();
//                    break;
//                } else if ( !Utils.isNumericString(leftAttribute.getLineInput()) ){
//                    err = "Line input \"" + leftAttribute.getLineInput() + "\" is invalid for " + attribute.getSectionName().name();
//                    break;
//                } else if ( !Utils.isNumericString(rightAttribute.getLineInput())) {
//                    err = "Line input \"" + rightAttribute.getLineInput() + "\" is invalide for " + attribute.getSectionName().name();
//                    break;
//                }
            }
        }
        if ( hasInput) {
            return null;
        } else {
            err = "Please enter at least one Line or Juice.";
            return err;
        }
    }
}
