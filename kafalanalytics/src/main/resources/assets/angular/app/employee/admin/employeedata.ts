import { Component ,OnInit,OnDestroy,AfterViewInit} from '@angular/core';
// import {HttpService} from '../../service/http.service';
import {HttpService} from '../../service/http.service';
import { OrganizationAdd } from './organizationadd';
import {BaseContainer} from '../../BaseContainer';
import { SharedService } from '../../service/shared-data-service';
import { ChangeDetectorRef } from '@angular/core';
import { NotifierService } from 'angular-notifier';
import { Router ,ActivatedRoute, Route} from '@angular/router';
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { FormBuilder, FormArray, FormGroup} from '@angular/forms';

declare var $:any;



var path = require('path');

var _publicPath = path.resolve(__dirname, '../../../../../webapp');

var _templateURL =   'templates/employee/employeedata.html';

@Component({
    selector: 'app-root',
    templateUrl: _templateURL,
  })
  export class EmployeeData implements OnInit,OnDestroy,AfterViewInit {

    private searchPage:boolean = true;
    private resultPage:boolean = false;
    private toggleEdit:boolean = false;
    private toggleDelete:boolean = false;
    empForm: FormGroup;
    private EmployeeDetails:any = [];
    public response:Object = {};
    private status:boolean = false;
    private errorMsg:string = "";
    private errorMsgType:boolean = false;
    private errorMsgState:boolean = false;
    public userId: any = "";
    public parmsData: any = {};
    private readonly notifier: NotifierService;

    constructor(private fb: FormBuilder,
        private sharedService:SharedService,
        private router:Router,
        private _sanitizer: DomSanitizer,
        private httpService: HttpService,
        private route: ActivatedRoute,
        notifierService: NotifierService,
    ){
        this.searchPage = true;
        this.resultPage = false;
        this.notifier = notifierService;
    }

    ngOnInit(){

        this.parmsData = this.route.snapshot.params;
        this.userId = this.parmsData["userId"];

        this.empForm = this.fb.group({
            employeeName: '',
            emailId: '',
            department: '', 
            area: '', 
            designation: '',
            region: '',
            reportToId: '',
            territory: '',
            fromDate: '',
            toDate: '',
            horizontal: '',
            vertical: '',
            status: '',
            channel: '',  
            empSearch     : this.fb.array([
                this.initEmpSearch()
            ])
        });

    }

    ngAfterViewInit(){

    }

    initEmpSearch() : FormGroup
    {
       return this.fb.group({
            employeeName: '',
            emailId: '',
            department: '', 
            area: '', 
            designation: '',
            region: '',
            reportToId: '',
            territory: '',
            fromDate: '',
            toDate: '',
            horizontal: '',
            vertical: '',
            status: '',
            channel: ''
       });
    }

    backbutton(){
        this.searchPage = true;
    }

    onChange(value:any,i:number)
     {
          
    //   this.myForm.value.clientAdd[i].reportTo  = value;
    this.EmployeeDetails[i].reportToId = value.reportToId;
     }

     changeRouteAdmin(name){
        var userId = this.userId;
        this.router.navigate([name,userId]);
      }

    searchEmp(){
        this.searchPage = false;
        // console.log(this.empForm.value.empSearch);
      var  requestObject = new BaseContainer();   
    //   if(this.empForm[0].value.empSearch.employeeName.indexOf("@") > -1){
        
    //     this.empForm[0].value.empSearch.emailId = this.empForm[0].value.empSearch.employeeName;
    //     this.empForm[0].value.empSearch.employeeName = '';
    //   }
        console.log("req");
        // console.log(this.empForm.value.empSearch);
        // // console.log( this.empForm[0].value.empSearch);
        // // console.log(this.empForm.value[0].empSearch);
        // console.log(this.empForm.value.empSearch[0]);
        
      
            if(this.status == true){
                this.empForm.value.empSearch[0].status = 'active';
            }
            // console.log(this.empForm.value.empSearch[0].status);
           requestObject = this.empForm.value.empSearch;
           
        //    console.log(this.empForm[0].value.empSearch);
           
           this.httpService.postRequest<OrganizationAdd>(requestObject,'app/auth/findEmp').subscribe(
                 organizationadd=> {
                    //  console.log("Reply");
                    //  console.log(organizationadd.rsBody.msg);
                    //  console.log(organizationadd.rsBody.result);
                if(organizationadd.rsBody.result=='success'){
                    if(organizationadd.rsBody.msg != "success"){
                            this.response = organizationadd.rsBody.msg;
                            this.EmployeeDetails = this.response['employeeData'];
                        //    for(var i=0;i<this.EmployeeDetails.length;i++){
                        //        if(this.EmployeeDetails[i].status == "active"){
                        //         this.EmployeeDetails[i].status = true;
                        //        }
                        //    }
                    }
                 }

                 if(this.EmployeeDetails == null || this.EmployeeDetails == undefined){
                    this.EmployeeDetails = [];
                }
                    
                    });

                

                
    }

    changRoute(name)
  {
          // this.sharedService.setData({'tabName':tabName});
          this.router.navigate([name]);
  }

  checkdeletestatus(){
    for(var i=0;i< this.EmployeeDetails.length; i++){
        if(this.EmployeeDetails[i].checked == true){
            this.toggleDelete = true;
            break;
        }else{
            this.toggleDelete = false;
        }
    }
  }

    toggleDeleteButton(i){
        if(this.EmployeeDetails[i].checked == false){
            this.EmployeeDetails[i].checked = true;
            this.toggleDelete = true;
        }
        else{
            this.EmployeeDetails[i].checked = false;
            this.checkdeletestatus();
        }
    }

    activeState(i){
        if(this.EmployeeDetails[i].status == "active"){
            this.EmployeeDetails[i].status = "inactive";
        }else if(this.EmployeeDetails[i].status == "inactive"){
            this.EmployeeDetails[i].status = "active";
        }
    }

    toggleState(i){
        if(this.EmployeeDetails[i].status == "active"){
            return true;
        }else{
            return false;
        }
    }

    onChangeStatus(){

        if(this.empForm.value.empSearch.status != 'active'){
            this.empForm.value.empSearch.status = 'active';
        }else{
            this.empForm.value.empSearch.status = '';
        }

    }

       // typehead
   autocompleListFormatter = (data: any) => {     
    //this.setObj = data;      
  let html = `<span style="width: 40%; line-height: 3em;  margin-top: 30px; color: black">${data.reportToId}</span>`;
  return this._sanitizer.bypassSecurityTrustHtml(html);
    }

    DeleteCheckedEmployees(){

        var DelEmployeeDetails:any = [];
        var j=0;

        for(var i = 0;i<this.EmployeeDetails.length;i++){
            if(this.EmployeeDetails[i].checked == true){
                DelEmployeeDetails[j] = this.EmployeeDetails[i];
                j++;
            }
        }
        if(confirm("Are you sure you want to delete selected records?")){
        var  requestObject = new BaseContainer();
        requestObject = DelEmployeeDetails;


        this.httpService.postRequest<OrganizationAdd>(requestObject,'app/auth/delEmp').subscribe(
            organizationadd=> {
                console.log("Reply");
                console.log(organizationadd.rsBody.msg);
                console.log(organizationadd.rsBody.result);
           if(organizationadd.rsBody.result=='success'){
            //    if(organizationadd.rsBody.msg == "success"){
            //     this.notifier.notify( 'success', "Records deleted Succesfully!" );
            //            this.response = organizationadd.rsBody.msg;
                       
                      
            //    }else if(organizationadd.rsBody.msg == "404"){
            //     this.notifier.notify( 'error', "Some Error Occured! Try Again!" );
            //    }
            //    else if(organizationadd.rsBody.msg != null){
            //     this.notifier.notify( 'error', organizationadd.rsBody.msg );
            //    }
            //    else{
            //     this.notifier.notify( 'error', "Some Error Occured! Try Again!" );
            //    }

            this.errorMsg = "Records are deleted successfully";
            this.errorMsgState = true;
            this.errorMsgType = false;

            }else if(organizationadd.rsBody.result=='error'){
                // this.notifier.notify( 'error', "Some Error Occured! Try Again!" );
                this.errorMsg = "Record not deleted. Please try again.";
                this.errorMsgState = true;
                this.errorMsgType = true;
            }

            

            this.searchEmp();
               
               });
            }
    }

    toggleEditButton(i){

        if(this.EmployeeDetails[i].areaToggle == false && this.EmployeeDetails[i].reportToIdToggle ==false &&
            this.EmployeeDetails[i].designationToggle == false && this.EmployeeDetails[i].statusToggle == false 
            ){

                this.EmployeeDetails[i].areaToggle = true;
                this.EmployeeDetails[i].reportToIdToggle = true;
                this.EmployeeDetails[i].designationToggle = true;
                this.EmployeeDetails[i].statusToggle = true;
                // this.toggleEdit = true;
                this.EmployeeDetails[i].editchecked = true;
            }
        else{
            this.EmployeeDetails[i].areaToggle = false;
            this.EmployeeDetails[i].reportToIdToggle = false;
            this.EmployeeDetails[i].designationToggle = false;
            this.EmployeeDetails[i].statusToggle = false;
            // this.toggleEdit = false;
            this.EmployeeDetails[i].editchecked = false;
        }

    }

    saveEmp(i){

        var DelEmployeeDetails:any = [];
        // var j=0;

        // for(var i = 0;i<this.EmployeeDetails.length;i++){
        //     if(this.EmployeeDetails[i].checked == true){
        //         DelEmployeeDetails[j] = this.EmployeeDetails[i];
        //         j++;
        //     }
        // }

        if(this.EmployeeDetails[i].status == true || this.EmployeeDetails[i].status == "active"){
            this.EmployeeDetails[i].status = "active";
        }else{
            this.EmployeeDetails[i].status = "";
        }
        
        DelEmployeeDetails[0] = this.EmployeeDetails[i];

        var  requestObject = new BaseContainer();
        requestObject = DelEmployeeDetails;


        this.httpService.postRequest<OrganizationAdd>(requestObject,'app/auth/editEmp').subscribe(
            organizationadd=> {
                console.log("Reply");
                console.log(organizationadd.rsBody.msg);
                console.log(organizationadd.rsBody.result);
           if(organizationadd.rsBody.result=='success'){
            //    if(organizationadd.rsBody.msg == "success"){
            //     this.notifier.notify( 'success', "Record updated Succesfully!" );
            //            this.response = organizationadd.rsBody.msg;      
            //    }else if(organizationadd.rsBody.msg == "404"){
            //     this.notifier.notify( 'error', "Some Error Occured! Try Again!" );
            //    }
            //    else if(organizationadd.rsBody.msg != null){
            //     this.notifier.notify( 'error', organizationadd.rsBody.msg );
            //    }
            //    else{
            //     this.notifier.notify( 'error', "Some Error Occured! Try Again!" );
            //    }

            this.errorMsg = "Records are saved successfully";
            this.errorMsgState = true;
            this.errorMsgType = false;
            }else{
                // this.notifier.notify( 'error', "Some Error Occured! Try Again!" );
                this.errorMsg = organizationadd.rsBody.msg;
            this.errorMsgState = true;
            this.errorMsgType = true;
            }

            this.searchEmp();
            this.toggleEditButton(i);
               
               });

    }


    ngOnDestroy(){

    }
  }  

