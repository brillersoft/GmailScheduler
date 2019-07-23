declare var $: any
export class ValidationHandler {

    validateDOM (tarfind:any) {
        if (null == tarfind) {
            return this.validateMandatoryFields("DynamicUserViewPanel");
        } else {
            return this.validateMandatoryFields(tarfind);
        }
    }

    validateMandatoryFields(rootElem:any) {

        /*
         * first check if need to do index based mandatory checks based on the
         * tarfind element that is coming in tarfind element will be
         */

        let foundError:boolean = false;
        let mandatoryElemList = $('#' + rootElem).find(
            '[checkMandatory="true"]');

        for (let i = 0; i < mandatoryElemList.length; i++) {
            let mandatoryElem = $(mandatoryElemList[i]);

            /*
             * first check if the element is of the type radio group
             */
            if (mandatoryElem.attr("elementType")
                && mandatoryElem.attr("elementType") == "radiogroup") {

                if (mandatoryElem.find("input[type='radio']:checked").length == 0) {
                    if (mandatoryElem.siblings('#' + mandatoryElem.attr("id")
                            + "_error").length > 0) {
                        mandatoryElem.siblings(
                            '#' + mandatoryElem.attr("id") + "_error")
                            .removeClass("ng-hide").text("select value");
                    } else {
                        mandatoryElem.parent().siblings(
                            '#' + mandatoryElem.attr("id") + "_error")
                            .removeClass("ng-hide").text("select value");
                    }

                    foundError = true;
                }
            }

            else if (mandatoryElem.attr("elementType")
                && mandatoryElem.attr("elementType") == "checkboxgroup") {

                if (mandatoryElem.find("input[type='checkbox']:checked").length == 0) {
                    if (mandatoryElem.siblings('#' + mandatoryElem.attr("id")
                            + "_error").length > 0) {
                        mandatoryElem.siblings(
                            '#' + mandatoryElem.attr("id") + "_error")
                            .removeClass("ng-hide").text("select value");
                    } else {
                        mandatoryElem.parent().siblings(
                            '#' + mandatoryElem.attr("id") + "_error")
                            .removeClass("ng-hide").text("select value");
                    }

                    foundError = true;
                }

            } else {

                /*
                 * then check if the attribute is of the type checkbox group
                 */

                if (mandatoryElem.val() == '' || mandatoryElem.val() == null
                    || mandatoryElem.val() == undefined) {
                    if (mandatoryElem.siblings('#' + mandatoryElem.attr("id")
                            + "_error").length > 0) {
                        mandatoryElem.siblings(
                            '#' + mandatoryElem.attr("id") + "_error")
                            .removeClass("ng-hide").text("required");
                        mandatoryElem.parent(
                            '#' + mandatoryElem.attr("id"))
                            .addClass("errorIntput");

                    } else {
                        mandatoryElem.parent().siblings(
                            '#' + mandatoryElem.attr("id") + "_error")
                            .removeClass("ng-hide").text("required");
                        mandatoryElem.parent(
                            '#' + mandatoryElem.attr("id"))
                            .addClass("errorIntput").text("required");

                    }

                    foundError = true;
                }
            }
        }
        return foundError;
    }

    displayErrors(data:any,errorElemTarfind:any,businessFlowName:any) {
        if (data) {

            /*
             * display the exceptions for each key
             */



            $.each(data, function(key:any, val:any) {

console.log(key);
console.log(val);

                if(errorElemTarfind !=undefined && null != errorElemTarfind && errorElemTarfind != "" && val!=null)
                {

                    $('#' + errorElemTarfind).find('#' + key + "_error").removeClass("ng-hide");
                    $('#' + errorElemTarfind).find('#' + key).addClass("errorIntput");

                    $('#' + errorElemTarfind).find('#' + key + "_error").text(val);
                }
               /* else
                {

                    $('#' + key + "_error").removeClass("ng-hide");
                    $('#' + key).addClass("errorIntput");

                    $('#' + key + "_error").text(val);
                }*/


            });
        } else if (data.msg.businessException) {
            alert(data.msg.businessException);
        } else {
            alert(data.msg.technicalException);
        }
    }

    hideErrors(mandatoryCheckTarfind:any) {

        if (mandatoryCheckTarfind == undefined || mandatoryCheckTarfind == null
            || mandatoryCheckTarfind == '') {
            $("#"+ mandatoryCheckTarfind).find(".displayError").addClass("ng-hide");
            //$("#DynamicUserViewPanel").find(".form-error").removeClass("errorIntput");
        } else {
            $("#" + mandatoryCheckTarfind).find(".displayError").addClass(
                "ng-hide");
           // $("#" + mandatoryCheckTarfind).find(".form-error").removeClass(
             //   "errorIntput");
        }
    }
}