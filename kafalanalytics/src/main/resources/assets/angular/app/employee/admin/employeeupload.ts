import { Component ,OnInit,OnDestroy,AfterViewInit} from '@angular/core';
// import {HttpService} from '../../service/http.service';
import {HttpService} from '../../service/http.service';
import { OrganizationAdd } from './organizationadd';
import {BaseContainer} from '../../BaseContainer';
import { SharedService } from '../../service/shared-data-service';
import { ChangeDetectorRef } from '@angular/core';

import { Router ,ActivatedRoute, Route} from '@angular/router';
// import {BaseContainer} from '../../BaseContainer';
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
// import { DataService } from "../data.service";
import { FormBuilder, FormArray, FormGroup} from '@angular/forms';
import { NotifierService } from 'angular-notifier';
// import { SharedService } from '../../service/shared-data-service';
import { JobsService } from '../../service/JobsService-service';
import { FormGroup,  FormBuilder,  Validators,FormControl } from '@angular/forms';


declare var $:any;

fileToUpload: File = null;

var path = require('path');

var _publicPath = path.resolve(__dirname, '../../../../../webapp');

var _templateURL =   'templates/employee/employeeupload.html';

@Component({
  selector: 'app-root',
  templateUrl: _templateURL,
})

export class Employeeupload implements OnInit,OnDestroy,AfterViewInit {
  myForm: FormGroup;
  
  employeeNamectrl: FormControl;
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
     private route: ActivatedRoute,
     private _sanitizer: DomSanitizer,
    private httpService: HttpService,
  private jobsService: JobsService,
  notifierService: NotifierService,) {
    this.notifier = notifierService;
  };

   private organizationadd:Object = {};

    ngOnInit() {

      this.parmsData = this.route.snapshot.params;
      this.userId = this.parmsData["userId"];

      this.myForm = this.fb.group({
        employeeName: '', 
    employeeId: '', 
    emailId: '',
    reportTo:'', 
    clientAdd     : this.fb.array([
      this.initClientAdd()
   ])
  });
  

 
    }
    initClientAdd() : FormGroup
    {
      
      return this.fb.group({
        employeeName: new FormControl('',[Validators.required,
        Validators.pattern("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")
      ]),
        employeeId: new FormControl('', [Validators.required]),
        emailId: new FormControl('', [
          Validators.required,
          Validators.pattern("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        ]),
        reportTo: '',
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

    onChange(value:any,i:number)
     {
          
      this.myForm.value.clientAdd[i].reportTo  = value.reportToId;
     }

     changeRouteAdmin(name){
      var userId = this.userId;
      this.router.navigate([name,userId]);
    }
  
   
    
    saveClient(){
      console.log("kjjk---"+JSON.stringify(this.myForm.value.clientAdd,null,2));
      var  requestObject = new BaseContainer();   
           requestObject = this.myForm.value.clientAdd;
           
           
           this.httpService.postRequest<OrganizationAdd>(requestObject,'app/auth/saveemp').subscribe(
                          organizationadd=> {
                     console.log("Reply");
                     console.log(organizationadd.rsBody.msg);
                     console.log(organizationadd.rsBody.result);
      if(organizationadd.rsBody.result=='success'){
          if(organizationadd.rsBody.msg == "101"){
            console.log("Hello Sucess");
            this.notifier.notify( 'success', 'Data has been updated successfully!' );
          }else if(organizationadd.rsBody.msg == "500"){
            this.notifier.notify( 'error', 'Error Occured!' );
          }else{
            // this.notifier.notify( 'success', organizationadd.rsBody.msg );
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

   // typehead
   autocompleListFormatter = (data: any): SafeHtml => {     
    //this.setObj = data;      
  let html = `<span style=" color: black; ">${data.reportToId}</span>`;
  return this._sanitizer.bypassSecurityTrustHtml(html);
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

   runImportRecordsJob(files: FileList){
    
   var fileToUpload : File=files.item(0);
    console.log("hudhu 123-fileToUpload--"+fileToUpload);
    if (confirm("Would you like to upload the data?")){
          this.jobsService.runImportRecordsJobforEmployee({file: fileToUpload}).subscribe(
        success => {
          console.log('Uploading file succefully.1..!' + JSON.stringify(success));
          if (success.rsBody.result == 'success') {
            if (success.rsBody.msg == "101") {
              console.log("Hello Sucess");
              this.notifier.notify('success', 'Data has been updated successfully!');
            } else if (success.rsBody.msg == "500") {
              this.notifier.notify('error', 'Error Occured!');
            } else {
              // this.notifier.notify('success', success.rsBody.msg);
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
  
