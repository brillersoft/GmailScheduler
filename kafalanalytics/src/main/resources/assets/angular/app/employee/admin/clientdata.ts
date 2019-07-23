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

var _templateURL =   'templates/employee/clientdata.html';

@Component({
    selector: 'app-root',
    templateUrl: _templateURL,
  })
  export class ClientData implements OnInit,OnDestroy,AfterViewInit {

    private searchPage:boolean = true;
    private resultPage:boolean = false;
    private toggleEdit:boolean = false;
    private toggleDelete:boolean = false;
    clForm: FormGroup;
    private ClientDetails:any = [];
    public response:Object = {};
    private executive:boolean = false;
    private errorMsg:string = "";
    private errorMsgType:boolean = false;
    private errorMsgState:boolean = false;
    private readonly notifier: NotifierService;
    public userId: any = "";
    public parmsData: any = {};

    constructor(private fb: FormBuilder,
        private sharedService:SharedService,
        private router:Router,
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

        this.clForm = this.fb.group({
            clientName: '',
            emailId: '',
            companyName: '', 
            executive: '', 
            designation: '',
            account: '',
            organization: '',
            location: '', 
            clSearch     : this.fb.array([
                this.initEmpSearch()
            ])
        });

    }

    ngAfterViewInit(){

    }

    initEmpSearch() : FormGroup
    {
       return this.fb.group({
        clientName: '',
        emailId: '',
        companyName: '', 
        executive: '', 
        designation: '',
        account: '',
        organization: '',
        location: ''
       });
    }

    changRoute(name)
  {
          // this.sharedService.setData({'tabName':tabName});
          this.router.navigate([name]);
  }

  backbutton(){
    this.searchPage = true;
}

    searchCl(){
        this.searchPage = false;
        // console.log(this.empForm.value.empSearch);
      var  requestObject = new BaseContainer();   
    //   if(this.empForm[0].value.empSearch.employeeName.indexOf("@") > -1){
        
    //     this.empForm[0].value.empSearch.emailId = this.empForm[0].value.empSearch.employeeName;
    //     this.empForm[0].value.empSearch.employeeName = '';
    //   }

        if(this.executive == true){
            this.clForm.value.clSearch[0].executive = 'yes';
        }
      
           requestObject = this.clForm.value.clSearch;
        //    console.log(this.empForm[0].value.empSearch);
           
           this.httpService.postRequest<OrganizationAdd>(requestObject,'app/auth/findCl').subscribe(
                 organizationadd=> {
                     console.log("Reply");
                     console.log(organizationadd.rsBody.msg);
                     console.log(organizationadd.rsBody.result);
                if(organizationadd.rsBody.result=='success'){
                    if(organizationadd.rsBody.msg != "success"){
                            this.response = organizationadd.rsBody.msg;
                            this.ClientDetails = this.response['clientDetails'];
                            // for(var i=0;i<this.ClientDetails.length;i++){
                            //     this.ClientDetails[i].clientNameToggle = false;
                            //     this.ClientDetails[i].executiveToggle = false;
                            //     this.ClientDetails[i].designationToggle = false;
                            //     this.ClientDetails[i].organizationToggle = false;
                            //     this.ClientDetails[i].locationToggle = false;
                            // }
                            
                    }
                 }

                 if(this.ClientDetails == null || this.ClientDetails == undefined){
                    this.ClientDetails = [];
                }
                    
                    });

                
    }

    checkdeletestatus(){
        for(var i=0;i< this.ClientDetails.length; i++){
            if(this.ClientDetails[i].checked == true){
                this.toggleDelete = true;
                break;
            }else{
                this.toggleDelete = false;
            }
        }
      }

    toggleDeleteButton(i){
        if(this.ClientDetails[i].checked == false){
            this.ClientDetails[i].checked = true;
            this.toggleDelete = true;
        }
        else{
            this.ClientDetails[i].checked = false;
            this.checkdeletestatus();
        }
    }

    DeleteCheckedClients(){

        var DelClientDetails:any = [];
        var j=0;

        if(confirm("Are you sure you want to delete selected records?")){
        for(var i = 0;i<this.ClientDetails.length;i++){
            if(this.ClientDetails[i].checked == true){
                DelClientDetails[j] = this.ClientDetails[i];
                j++;
            }
        }

        var  requestObject = new BaseContainer();
        requestObject = DelClientDetails;


        this.httpService.postRequest<OrganizationAdd>(requestObject,'app/auth/delCl').subscribe(
            organizationadd=> {
                console.log("Reply");
                console.log(organizationadd.rsBody.msg);
                console.log(organizationadd.rsBody.result);
           if(organizationadd.rsBody.result=='success'){
               if(organizationadd.rsBody.msg == "success"){
                // this.notifier.notify( 'success', "Records deleted Succesfully!" );
                       this.response = organizationadd.rsBody.msg;
                       this.errorMsg = "Records are deleted successfully";
                       this.errorMsgState = true;
                       this.errorMsgType = false;
                       
                      
               }
            }else{
                // this.notifier.notify( 'error', "Some Error Occured! Try Again!" );
                this.errorMsg = "Record not deleted. Please try again.";
                this.errorMsgState = true;
                this.errorMsgType = true;
            }

            this.searchCl();
               
               });
            }
    }

    toggleEditButton(i){

        if(this.ClientDetails[i].clientNameToggle == false && this.ClientDetails[i].executiveToggle ==false &&
            this.ClientDetails[i].designationToggle == false && this.ClientDetails[i].organizationToggle == false &&
            this.ClientDetails[i].locationToggle == false){

                this.ClientDetails[i].clientNameToggle = true;
                this.ClientDetails[i].executiveToggle = true;
                this.ClientDetails[i].designationToggle = true;
                this.ClientDetails[i].organizationToggle = true;
                this.ClientDetails[i].locationToggle = true;
                // this.toggleEdit = true;
                this.ClientDetails[i].editchecked = true;
            }
        else{
            this.ClientDetails[i].clientNameToggle = false;
            this.ClientDetails[i].executiveToggle = false;
            this.ClientDetails[i].designationToggle = false;
            this.ClientDetails[i].organizationToggle = false;
            this.ClientDetails[i].locationToggle = false;
            // this.toggleEdit = false;
            this.ClientDetails[i].editchecked = false;
        }

    }

    saveCl(i){

        var DelClientDetails:any = [];
        // var j=0;

        // for(var i = 0;i<this.ClientDetails.length;i++){
        //     if(this.ClientDetails[i].checked == true){
        //         DelClientDetails[j] = this.ClientDetails[i];
        //         j++;
        //     }
        // }

        if(this.ClientDetails[i].executive == true || this.ClientDetails[i].executive == "yes"){
            this.ClientDetails[i].executive = "yes";
        }else{
            this.ClientDetails[i].executive = ""; 
        }
        
        DelClientDetails[0] = this.ClientDetails[i];

        var  requestObject = new BaseContainer();
        requestObject = DelClientDetails;


        this.httpService.postRequest<OrganizationAdd>(requestObject,'app/auth/editCl').subscribe(
            organizationadd=> {
                // console.log("Reply");
                // console.log(organizationadd.rsBody.msg);
                // console.log(organizationadd.rsBody.result);
           if(organizationadd.rsBody.result=='success'){
               if(organizationadd.rsBody.msg == "success"){
                // this.notifier.notify( 'success', "Record updated Succesfully!" );
                this.errorMsg = "Records are saved successfully";
                this.errorMsgState = true;
                this.errorMsgType = false;
                       this.response = organizationadd.rsBody.msg;      
               }
               else{
                // this.notifier.notify( 'error', "Some Error Occured! Try Again!" );
                this.errorMsg = organizationadd.rsBody.msg;
                this.errorMsgState = true;
                this.errorMsgType = true;
               }
            }else{
                // this.notifier.notify( 'error', "Some Error Occured! Try Again!" );
                this.errorMsg = organizationadd.rsBody.msg;
                this.errorMsgState = true;
                this.errorMsgType = true;
            }

            this.searchCl();
            this.toggleEditButton(i);
               
               });

    }

    changeRouteAdmin(name){
        var userId = this.userId;
        this.router.navigate([name,userId]);
      }
      

    ngOnDestroy(){

    }
  }  

