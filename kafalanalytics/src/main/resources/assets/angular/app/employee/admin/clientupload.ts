import { Component ,OnInit,OnDestroy,AfterViewInit} from '@angular/core';
// import {HttpService} from '../../service/http.service';
import {HttpService} from '../../service/http.service';
import { OrganizationAdd } from './organizationadd';
import {BaseContainer} from '../../BaseContainer';
import { SharedService } from '../../service/shared-data-service';
import { ChangeDetectorRef } from '@angular/core';
import { NotifierService } from 'angular-notifier';
import { Router ,ActivatedRoute, Route} from '@angular/router';
// import {BaseContainer} from '../../BaseContainer';
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
// import { DataService } from "../data.service";
import { FormBuilder,FormControl, FormArray, FormGroup, Validators} from '@angular/forms';


import { JobsService } from '../../service/JobsService-service';


declare var $:any;



var path = require('path');

var _publicPath = path.resolve(__dirname, '../../../../../../webapp');

var _templateURL =   'templates/employee/clientupload.html';

@Component({
  selector: 'app-root',
  templateUrl: _templateURL,
 
})

export class ClientUpload implements OnInit,OnDestroy,AfterViewInit {
  myForm: FormGroup;
  fileToUpload: File = null;
   file: File;
   private readonly notifier: NotifierService;
   fileUploadMsg:String  = "";
   fileUploadMsgType:boolean = false;
   manualUploadMsg:String  = "";
   manualUploadMsgType:boolean = false;
   public userId: any = "";
   public parmsData: any = {};
 
    constructor(private fb: FormBuilder,
      private sharedService:SharedService,
      private router:Router,
     private httpService: HttpService,
     private jobsService: JobsService,
     private route: ActivatedRoute,
     notifierService: NotifierService,
     ) {
       this.notifier = notifierService;
     }; 
     
   
    private organizationadd:Object = {};
     ngOnInit() {

      this.parmsData = this.route.snapshot.params;
      this.userId = this.parmsData["userId"];

       this.myForm = this.fb.group({
         organizationName: '', 
     domainName: '', 
     country: '', 
     clientAdd     : this.fb.array([
       this.initClientAdd()
    ])
   });
     }

     initClientAdd() : FormGroup
     {
        return this.fb.group({
         organizationName: new FormControl('',[Validators.required,
        Validators.pattern("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")
      ]),
         domainName: new FormControl('', [
           Validators.required,
          //  Validators.pattern("^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}")
          Validators.pattern("^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9]\.[a-zA-Z]{2,}$")
         ]), 
         country: ''
        });
     }
     
     addMore() : void
     {
        const control = <FormArray>this.myForm.controls.clientAdd;
        control.push(this.initClientAdd());
     }
     removeMore(i : number) : void
     {
        const control = <FormArray>this.myForm.controls.clientAdd;
        control.removeAt(i);
     }
     
     saveClient(){
       console.log("kjjk---"+JSON.stringify(this.myForm.value.clientAdd,null,2));
       var  requestObject = new BaseContainer();   
       
       
            requestObject = this.myForm.value.clientAdd;
            
            
            this.httpService.postRequest<OrganizationAdd>(requestObject,'app/auth/saveorg').subscribe(
                  organizationadd=> {
                     //  console.log("Reply");
                     //  console.log(organizationadd.rsBody.msg);
                     //  console.log(organizationadd.rsBody.result);
       if(organizationadd.rsBody.result=='success'){
                     if(organizationadd.rsBody.msg == "101"){
                       console.log("Hello Sucess");
                       this.notifier.notify( 'success', "Data Saved Succesfully!" );
                     }else if(organizationadd.rsBody.msg == "500"){
                       this.notifier.notify( 'error', 'Error Occured!' );
                     }else{
                      //  this.notifier.notify( 'success', organizationadd.rsBody.msg );
                      this.manualUploadMsg = organizationadd.rsBody.msg;
                      if(this.manualUploadMsg.includes("unsuccessful")){
                        this.manualUploadMsgType = true;
                      }else{
                        this.manualUploadMsgType = false;
                      }
                     }
         
       }
       else{
         this.notifier.notify( 'error', "Some Error Occured! Try Again!" );
     }
                     
                     });
     }
     ngAfterViewInit(){
 
     }
     ngOnDestroy(){
       
     }
    changRoute(name)
   {
 
           // this.sharedService.setData({'tabName':tabName});
           this.router.navigate([name]);
   }

   getFileUploadMsgColor(){
    if(this.fileUploadMsgType == true){
      return 'red';
    }else{
      return '#439cc4';
    }
  }
  
  
  getManualUploadMsgColor(){
    if(this.manualUploadMsgType == true){
      return 'red';
    }else{
      return '#439cc4';
    }
  }

  changeRouteAdmin(name){
    var userId = this.userId;
    this.router.navigate([name,userId]);
  }
  
 
   
   runImportRecordsJob(files: FileList){
     
    var fileToUpload : File=files.item(0);
     console.log("hudhu 123-fileToUpload--"+fileToUpload);
     if (confirm("Would you like to upload the data?")){
        this.jobsService.runImportRecordsJob({file: fileToUpload}).subscribe(
         success => {
           console.log('Uploading file succefully.1..!' + JSON.stringify(success));
           if (success.rsBody.result == 'success') {
             if (success.rsBody.msg == "101") {
               console.log("Hello Sucess");
               this.notifier.notify('success', 'Data has been updated successfully!');
             } else if (success.rsBody.msg == "500") {
               this.notifier.notify('error', 'Error Occured!');
             } else {
              //  this.notifier.notify('success', success.rsBody.msg);
              this.fileUploadMsg = success.rsBody.msg;
              if(this.fileUploadMsg.includes("unsuccessful")){
                this.fileUploadMsgType = true;
              }else{
                this.fileUploadMsgType = false;
              }
               
             }
           }
           else {
             this.notifier.notify('error', "Some Error Occured! Try Again!");
           }
         },
         error => console.log(error),
       );
     }
   }
   
 

}

  
  
  
  
  
  
    
